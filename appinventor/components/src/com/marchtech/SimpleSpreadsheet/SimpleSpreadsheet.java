package com.marchtech.SimpleSpreadsheet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;

import org.json.JSONArray;
import org.json.JSONException;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.collect.Lists;
import com.google.appinventor.components.runtime.collect.Maps;
import com.google.appinventor.components.runtime.errors.DispatchableError;
import com.google.appinventor.components.runtime.errors.PermissionException;
import com.google.appinventor.components.runtime.errors.RequestTimeoutException;
import com.google.appinventor.components.runtime.util.AsynchUtil;
import com.google.appinventor.components.runtime.util.ChartDataSourceUtil;
import com.google.appinventor.components.runtime.util.CsvUtil;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.GingerbreadUtil;
import com.google.appinventor.components.runtime.util.JsonUtil;
import com.google.appinventor.components.runtime.util.SdkLevel;
import com.google.appinventor.components.runtime.util.YailDictionary;
import com.google.appinventor.components.runtime.util.YailList;

import com.marchtech.Icon;
import com.marchtech.SimpleSpreadsheet.helpers.As;

import android.app.Activity;
import android.util.Log;

@DesignerComponent(version = 1, description = "Extension to using spreadsheat", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = Icon.ICON)
@SimpleObject(external = true)
public class SimpleSpreadsheet extends AndroidNonvisibleComponent {
    private static final String LOG_TAG = "SimpleSpreadsheet";

    private final Activity activity;
    private final CookieHandler cookieHandler;

    private String url = "";
    private String scriptUrl = "";
    private String id = "";
    private String sheet = "Sheet1";
    private String sheetId = "0";

    private int counterMultiple = 0;
    private int lenMultiple = 1;

    private YailList requestHeaders = new YailList();
    private YailList columns = new YailList();
    private FutureTask<Void> lastTask = null;

    private List<String> tag = new ArrayList<>();

    public SimpleSpreadsheet(ComponentContainer container) {
        super(container.$form());

        activity = container.$context();
        cookieHandler = (SdkLevel.getLevel() >= SdkLevel.LEVEL_GINGERBREAD) ? GingerbreadUtil.newCookieManager() : null;
    }

    protected SimpleSpreadsheet() {
        super(null);
        activity = null;
        cookieHandler = null;
    }

    @SimpleEvent(description = "On got value with specific tag.")
    public void GotValue(As as, Object tag, Object value) {
        EventDispatcher.dispatchEvent(this, "GotValue", as, tag, value);
    }

    @SimpleEvent(description = "On got all value.")
    public void GotAll(As as, Object value) {
        EventDispatcher.dispatchEvent(this, "GotAll", as, value);
    }

    @SimpleEvent(description = "On got all value with specific tag.")
    public void GotAllValue(Object key, Object value) {
        EventDispatcher.dispatchEvent(this, "GotAllValue", key, value);
    }

    @SimpleEvent(description = "On added new value.")
    public void Added(Object value) {
        EventDispatcher.dispatchEvent(this, "Added", value);
    }

    @SimpleEvent(description = "On edited value.")
    public void Edited(Object tag, Object value) {
        EventDispatcher.dispatchEvent(this, "Edited", tag, value);
    }

    @SimpleEvent(description = "On edited multiple value.")
    public void MultipleEdited(Object tags, Object values) {
        EventDispatcher.dispatchEvent(this, "MultipleEdited", tags, values);
    }

    @SimpleEvent(description = "On edited all value.")
    public void AllEdited(Object tag, Object value) {
        EventDispatcher.dispatchEvent(this, "AllEdited", tag, value);
    }

    @SimpleEvent(description = "On deleted tag.")
    public void Deleted(Object tag) {
        EventDispatcher.dispatchEvent(this, "Deleted", tag);
    }

    @SimpleEvent(description = "On all deleted.")
    public void AllDeleted() {
        EventDispatcher.dispatchEvent(this, "AllDeleted");
    }

    @SimpleEvent(description = "Occurrs when failed add, edit or delete")
    public void OnFailed(String functionName, String messages) {
        EventDispatcher.dispatchEvent(this, "OnFailed", functionName, messages);
    }

    @SimpleFunction(description = "To initialize extension.")
    public void Initialize(YailList keys) {
        tag = new ArrayList<>();
        for (Object t : keys.toArray()) {
            tag.add(t.toString());
        }
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "")
    @SimpleProperty(description = "The id of spreadsheet.")
    public void ID(String id) {
        this.id = id;
    }

    @SimpleProperty
    public String ID() {
        return id;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "0")
    @SimpleProperty(description = "The sheet id of spreedsheet.")
    public void SheetID(String sheetId) {
        this.sheetId = sheetId;
    }

    @SimpleProperty
    public String SheetID() {
        return sheetId;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "")
    @SimpleProperty(description = "The URL of google scripts.")
    public void URL(String scriptUrl) {
        this.scriptUrl = scriptUrl;
    }

    @SimpleProperty
    public String URL() {
        return scriptUrl;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "Sheet1")
    @SimpleProperty(description = "To set active sheet.")
    public void SheetName(String sheet) {
        this.sheet = sheet;
    }

    @SimpleProperty
    public String SheetName() {
        return sheet;
    }

    @SimpleFunction(description = "To get all keys after initialized.")
    public YailList GetAllKeys() {
        return YailList.makeList(tag);
    }

    @SimpleFunction(description = "To get value with specific tag.")
    public void Get(As as, String tag, Object valueIfTagNotThere) {
        GET("Get", tag, valueIfTagNotThere, as);
    }

    @SimpleFunction(description = "To get all value.")
    public void GetAll(As as) {
        GET("GetAll", null, null, as);
    }

    @SimpleFunction(description = "To get all value with specific tag.")
    public void GetAllValue(String key) {
        GET("GetAllValue", key, null, null);
    }

    private void GET(final String mode, final Object data, final Object dataNot, final As as) {
        url = "https://spreadsheet.google.com/tq?tqx=out:csv&key=" + id + "&gid=" + sheetId + "&tq";
        final String METHOD = "Get";
        final CapturedProperties webProps = capturePropertyValues(METHOD);
        if (webProps == null)
            return;

        lastTask = new FutureTask<Void>(new Runnable() {
            @Override
            public void run() {
                performRequest(mode, data, dataNot, 1, as, webProps, null, "GET", METHOD);
            }
        }, null);

        AsynchUtil.runAsynchronously(lastTask);
    }

    @SimpleFunction(description = "To add new value with list or dictionary.")
    public void Add(Object value) {
        url = scriptUrl;
        List<Object> list = new ArrayList<>();
        YailDictionary dictionary = new YailDictionary();
        if (value instanceof YailList)
            list = convert(value);
        else if (value instanceof YailDictionary)
            dictionary = (YailDictionary) value;
        else
            return;

        String data = "sheet=" + sheet + "&action=add";
        for (int i = 0; i < tag.size(); i++) {
            if (!list.isEmpty()) {
                if (list.get(i) instanceof YailList)
                    data += "&add_" + tag.get(i) + "=" + convertToYailList(list.get(i)).toJSONString();
                else
                    data += "&add_" + tag.get(i) + "=" + list.get(i).toString();
            } else {
                if (dictionary.get(tag.get(i)) instanceof YailList)
                    data += "&add_" + tag.get(i) + "=" + convertToYailList(dictionary.get(tag.get(i))).toJSONString();
                else
                    data += "&add_" + tag.get(i) + "=" + dictionary.get(tag.get(i)).toString();
            }
        }

        requestTextImpl("Add", null, value, 1, data, "UTF-8", "Add", "POST");
    }

    private List<Object> convert(Object object) {
        List<Object> list = new ArrayList<>();
        if (object.getClass().isArray())
            list = Arrays.asList((Object[]) object);
        else if (object instanceof Collection)
            list = new ArrayList<>((Collection<?>) object);

        return list;
    }

    private YailList convertToYailList(Object object) {
        List<Object> result = convert(object);
        return YailList.makeList(result);
    }

    @SimpleFunction(description = "To edit value with specific tag.")
    public void Edit(String tag, Object value) {
        url = scriptUrl;
        String[] mTag = tag.split("/");
        int index = this.tag.indexOf(mTag[1]) + 1;
        String data = "sheet=" + sheet + "&action=specificEdit&edit_id=" + mTag[0] + "&edit_index="
                + String.valueOf(index) + "&edit_value="
                + value;

        requestTextImpl("Edit", tag, value, 1, data, "UTF-8", "Edit", "POST");
    }

    @SimpleFunction(description = "To edit multiple value with specific tags as list.")
    public void EditMultiple(YailList tags, YailList values) {
        url = scriptUrl;
        List<Object> mTags = YailListToList(tags);
        List<Object> mValues = YailListToList(values);
        counterMultiple = 0;
        lenMultiple = mTags.size();
        for (int i = 0; i < mTags.size(); i++) {
            String[] mTag = String.valueOf(mTags.get(i)).split("/");
            int index = tag.indexOf(mTag[1]) + 1;
            String data = "sheet=" + sheet + "&action=specificEdit&edit_id=" + mTag[0] + "&edit_index="
                    + String.valueOf(index) + "&edit_value="
                    + mValues.get(i).toString();

            requestTextImpl("EditMultiple", tags, values, i, data, "UTF-8", "Edit", "POST");
        }
    }

    private List<Object> YailListToList(YailList list) {
        List<Object> result = new ArrayList<>();
        for (Object obj : list.toArray()) {
            result.add(obj);
        }

        return result;
    }

    @SimpleFunction(description = "To edit all value with specific tag.")
    public void EditAll(Object value) {
        url = scriptUrl;
        List<Object> list = new ArrayList<>();
        YailDictionary dictionary = new YailDictionary();
        if (value instanceof YailList)
            list = convert(value);
        else if (value instanceof YailDictionary)
            dictionary = (YailDictionary) value;
        else
            return;

        String data = "sheet=" + sheet + "&action=edit";
        for (int i = 0; i < tag.size(); i++) {
            if (!list.isEmpty()) {
                if (list.get(i) instanceof YailList)
                    data += "&edit_" + tag.get(i) + "=" + convertToYailList(list.get(i)).toJSONString();
                else
                    data += "&edit_" + tag.get(i) + "=" + list.get(i).toString();
            } else {
                if (dictionary.get(tag.get(i)) instanceof YailList)
                    data += "&edit_" + tag.get(i) + "="
                            + convertToYailList(dictionary.get(tag.get(i))).toJSONString();
                else
                    data += "&edit_" + tag.get(i) + "=" + dictionary.get(tag.get(i)).toString();
            }
        }

        requestTextImpl("EditAll", dictionary.get(tag.get(0)).toString(), value, 1, data, "UTF-8", "EditAll", "POST");
    }

    @SimpleFunction(description = "To delete specific tag.")
    public void Delete(String tag) {
        url = scriptUrl;
        String data = "sheet=" + sheet + "&action=delete&delete_id=" + tag;

        requestTextImpl("Delete", tag, null, 1, data, "UTF-8", "Delete", "POST");
    }

    @SimpleFunction(description = "To delete all.")
    public void DeleteAll() {
        url = scriptUrl;
        String data = "sheet=" + sheet + "&action=deleteAll";

        requestTextImpl("DeleteAll", null, null, 1, data, "UTF-8", "DeleteAll", "POST");
    }

    private static class InvalidRequestHeadersException extends Exception {
        final int errorNumber;
        final int index;

        InvalidRequestHeadersException(int errorNumber, int index) {
            super();
            this.errorNumber = errorNumber;
            this.index = index;
        }
    }

    private static class CapturedProperties {
        final String urlString;
        final URL url;
        final boolean allowCookies;
        final int timeout;
        final Map<String, List<String>> requestHeaders;
        final Map<String, List<String>> cookies;

        CapturedProperties(SimpleSpreadsheet web) throws MalformedURLException, InvalidRequestHeadersException {
            urlString = web.url;
            url = new URL(urlString);
            allowCookies = false;
            timeout = 0;
            requestHeaders = processRequestHeaders(web.requestHeaders);

            Map<String, List<String>> cookiesTemp = null;
            if (allowCookies && web.cookieHandler != null) {
                try {
                    cookiesTemp = web.cookieHandler.get(url.toURI(), requestHeaders);
                } catch (URISyntaxException e) {

                } catch (IOException e) {

                }
            }

            cookies = cookiesTemp;
        }
    }

    private static Map<String, List<String>> processRequestHeaders(YailList list)
            throws InvalidRequestHeadersException {
        Map<String, List<String>> requestHeadersMap = Maps.newHashMap();
        for (int i = 0; i < list.size(); i++) {
            Object item = list.getObject(i);
            if (item instanceof YailList) {
                YailList sublist = (YailList) item;
                if (sublist.size() == 2) {
                    String fieldName = sublist.getObject(0).toString();
                    Object fieldValues = sublist.getObject(1);
                    String key = fieldName;
                    List<String> values = Lists.newArrayList();
                    if (fieldValues instanceof YailList) {
                        YailList multipleFieldsValues = (YailList) fieldValues;
                        for (int j = 0; j < multipleFieldsValues.size(); j++) {
                            Object value = multipleFieldsValues.getObject(j);
                            values.add(value.toString());
                        }
                    } else {
                        Object singleFieldValue = fieldValues;
                        values.add(singleFieldValue.toString());
                    }

                    requestHeadersMap.put(key, values);
                } else
                    throw new InvalidRequestHeadersException(ErrorMessages.ERROR_WEB_REQUEST_HEADER_NOT_TWO_ELEMENTS,
                            i + 1);
            } else
                throw new InvalidRequestHeadersException(ErrorMessages.ERROR_WEB_REQUEST_HEADER_NOT_LIST, i + 1);
        }

        return requestHeadersMap;
    }

    private static HttpURLConnection openConnection(CapturedProperties webProps, String httpVerb)
            throws IOException, ClassCastException, ProtocolException {
        HttpURLConnection connection = (HttpURLConnection) webProps.url.openConnection();
        connection.setConnectTimeout(webProps.timeout);
        connection.setReadTimeout(webProps.timeout);

        for (Map.Entry<String, List<String>> header : webProps.requestHeaders.entrySet()) {
            String name = header.getKey();
            for (String value : header.getValue()) {
                connection.addRequestProperty(name, value);
            }
        }

        if (webProps.cookies != null) {
            for (Map.Entry<String, List<String>> cookie : webProps.cookies.entrySet()) {
                String name = cookie.getKey();
                for (String value : cookie.getValue()) {
                    connection.addRequestProperty(name, value);
                }
            }
        }

        return connection;
    }

    private static void writeRequestData(HttpURLConnection connection, byte[] postData) throws IOException {
        connection.setDoOutput(true);
        connection.setFixedLengthStreamingMode(postData.length);
        BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream());
        try {
            out.write(postData, 0, postData.length);
            out.flush();
        } finally {
            out.close();
        }
    }

    private static String getResponseType(HttpURLConnection connection) {
        String responseType = connection.getContentType();
        return (responseType != null) ? responseType : "";
    }

    private void processResponseCookies(HttpURLConnection connection) {
        if (cookieHandler != null) {
            try {
                Map<String, List<String>> headerFields = connection.getHeaderFields();
                cookieHandler.put(connection.getURL().toURI(), headerFields);
            } catch (URISyntaxException e) {

            } catch (IOException e) {

            }
        }
    }

    private static InputStream getConnectionStream(HttpURLConnection connection) throws SocketTimeoutException {
        try {
            return connection.getInputStream();
        } catch (SocketTimeoutException e) {
            throw e;
        } catch (IOException el) {
            return connection.getErrorStream();
        }
    }

    private static String getResponseContent(HttpURLConnection connection) throws IOException {
        String encoding = connection.getContentEncoding();
        if (encoding == null)
            encoding = "UTF-8";

        InputStreamReader reader = new InputStreamReader(getConnectionStream(connection), encoding);
        try {
            int contentLength = connection.getContentLength();
            StringBuilder sb = (contentLength != -1) ? new StringBuilder(contentLength) : new StringBuilder();
            char[] buf = new char[1024];
            int read;
            while ((read = reader.read(buf)) != -1)
                sb.append(buf, 0, read);

            return sb.toString();
        } finally {
            reader.close();
        }
    }

    private void updateColumns(final String responseContent, final String responseType) {
        if (responseType.contains("json")) {
            try {
                columns = JsonUtil.getColumnsFromJson(responseContent);
            } catch (JSONException e) {

            }
        } else if (responseType.contains("csv") || responseType.startsWith("text/")) {
            try {
                columns = CsvUtil.fromCsvTable(responseContent);
                columns = ChartDataSourceUtil.getTranspose(columns);
            } catch (Exception e) {
                columns = new YailList();
            }
        }
    }

    private Object get(final String tag, final Object ifNot, final As as, final String responseContent) {
        String mTag = "";
        int index = 0;
        boolean all = false;
        if (tag.contains("/")) {
            String[] TAG = tag.split("/");
            mTag = TAG[0];
            if (this.tag.contains(TAG[1]))
                index = this.tag.indexOf(TAG[1]);
            else
                return ifNot;
        } else {
            mTag = tag;
            all = true;
        }

        List<String> values = new ArrayList<>();
        String[] data = responseContent.split("\n");
        for (int i = 1; i < data.length; i++) {
            values.add(data[i]);
        }

        if (all) {
            for (String v : values) {
                List<Object> value = convertJSONToList(v);
                if (value.contains(mTag)) {
                    if (as == As.Dictionary) {
                        Map<Object, Object> result = new HashMap<>();
                        for (int i = 0; i < this.tag.size(); i++) {
                            result.put(this.tag.get(i), value.get(i));
                        }

                        return YailDictionary.makeDictionary(result);
                    } else if (as == As.List)
                        return YailList.makeList(value);
                    else if (as == As.Text)
                        return v;
                    else if (as == As.Automatic)
                        return "Please select another As!";
                    else
                        return ifNot;
                }
            }

            return ifNot;
        }

        for (String v : values) {
            List<Object> value = convertJSONToList(v);
            if (value.contains(mTag)) {
                Object mResult = index >= value.size() ? null : value.get(index);
                if (as == As.Dictionary) {
                    Map<Object, Object> result = new HashMap<>();
                    result.put(this.tag.get(index), mResult);
                    return YailDictionary.makeDictionary(result);
                } else if (as == As.List) {
                    List<Object> result = new ArrayList<>();
                    result.add(mResult);
                    return YailList.makeList(result);
                } else if (as == As.Text)
                    return mResult.toString();
                else if (as == As.Automatic)
                    return mResult;
                else
                    return ifNot;
            }
        }

        return ifNot;
    }

    private Object getAll(final As as, final String responseContent) {
        List<String> values = new ArrayList<>();
        String[] data = responseContent.split("\n");
        for (int i = 1; i < data.length; i++) {
            values.add(data[i]);
        }

        if (as == As.Dictionary) {
            Map<Object, Object> result = new HashMap<>();
            for (int i = 0; i < values.size(); i++) {
                Map<Object, Object> mResult = new HashMap<>();
                List<Object> mData = convertJSONToList(values.get(i));
                for (int j = 1; j < tag.size(); j++) {
                    Object val = j >= mData.size() ? null : mData.get(j);
                    if (j >= mData.size())
                        break;
                    mResult.put(tag.get(j), val);
                }

                result.put(mData.get(0), YailDictionary.makeDictionary(mResult));
            }

            return YailDictionary.makeDictionary(result);
        } else if (as == As.List) {
            List<Object> result = new ArrayList<>();
            for (String string : values) {
                result.add(YailList.makeList(convertJSONToList(string)));
            }

            return YailList.makeList(result);
        } else if (as == As.Text)
            return responseContent;
        else if (as == As.Automatic)
            return "Please select another As!";
        else
            return null;
    }

    private Object getAllValue(final String key, final String responseContent) {
        int index = tag.indexOf(key);

        List<String> values = new ArrayList<>();
        String[] data = responseContent.split("\n");
        for (int i = 1; i < data.length; i++) {
            values.add(data[i]);
        }

        List<Object> result = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            List<Object> value = convertJSONToList(values.get(i));
            if (index >= value.size())
                result.add(null);
            else
                result.add(value.get(index));
        }

        return YailList.makeList(result);
    }

    private List<Object> convertJSONToList(String string) {
        string = "[" + string + "]";
        StringBuilder sb = new StringBuilder(string);
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '"') {
                if (sb.charAt(i + 1) == '"')
                    sb.replace(i, i + 1, "");
            }
        }

        for (int i = 0; i < sb.length(); i++) {
            if (i != 0 && i != sb.length() - 1) {
                if (sb.charAt(i) == '[') {
                    if (sb.charAt(i - 1) == '"' && sb.charAt(i + 1) == '"')
                        sb.replace(i - 1, i, "");
                } else if (sb.charAt(i) == ']') {
                    if (sb.charAt(i - 1) == '"' && sb.charAt(i + 1) == '"')
                        sb.replace(i + 1, i + 2, "");
                }
            }
        }

        string = sb.toString();
        List<Object> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(string);
            for (int i = 0; i < array.length(); i++) {
                if (isJSON(array.getString(i))) {
                    List<Object> mValue = new ArrayList<>();
                    JSONArray mArray = new JSONArray(array.getString(i));
                    for (int j = 0; j < mArray.length(); j++) {
                        mValue.add(mArray.get(j));
                    }

                    list.add(YailList.makeList(mValue));
                } else
                    list.add(array.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    private boolean isJSON(String json) {
        try {
            new JSONArray(json);
        } catch (JSONException e) {
            return false;
        }

        return true;
    }

    private void performRequest(final String mode, final Object data, final Object value, final int count, final As as,
            final CapturedProperties webProps, final byte[] postData, final String httpVerb, final String method) {
        try {
            HttpURLConnection connection = openConnection(webProps, httpVerb);
            if (connection != null) {
                try {
                    if (postData != null) {
                        writeRequestData(connection, postData);
                    }

                    final int responseCode = connection.getResponseCode();
                    final String responseType = getResponseType(connection);
                    processResponseCookies(connection);

                    final String responseContent = getResponseContent(connection);

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mode.equals("Get"))
                                GotValue(as, data, get(data.toString(), value, as, responseContent));
                            else if (mode.equals("GetAll"))
                                GotAll(as, getAll(as, responseContent));
                            else if (mode.equals("GetAllValue"))
                                GotAllValue(data, getAllValue(data.toString(), responseContent));
                            else {
                                if (responseCode == 200) {
                                    if (mode.equals("Add"))
                                        Added(value);
                                    else if (mode.equals("Edit"))
                                        Edited(data, value);
                                    else if (mode.equals("editMultiple")) {
                                        counterMultiple++;
                                        if (counterMultiple >= lenMultiple)
                                            MultipleEdited(data, value);
                                    } else if (mode.equals("EditAll"))
                                        AllEdited(data, value);
                                    else if (mode.equals("Delete"))
                                        Deleted(data);
                                    else if (mode.equals("DeleteAll"))
                                        AllDeleted();
                                } else
                                    OnFailed(mode, "Please check your's arguments!");
                            }
                        }
                    });

                    updateColumns(responseContent, responseType);
                } catch (SocketTimeoutException e) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            OnFailed(mode, "Connection Error, please check your network!");
                        }
                    });

                    throw new RequestTimeoutException();
                } finally {
                    connection.disconnect();
                }
            }
        } catch (PermissionException e) {
            form.dispatchPermissionDeniedEvent(SimpleSpreadsheet.this, method, e);
        } catch (DispatchableError e) {
            form.dispatchErrorOccurredEvent(SimpleSpreadsheet.this, method, e.getErrorCode(), e.getArguments());
        } catch (RequestTimeoutException e) {
            form.dispatchErrorOccurredEvent(SimpleSpreadsheet.this, method, ErrorMessages.ERROR_WEB_REQUEST_TIMED_OUT,
                    webProps.urlString);
        } catch (Exception e) {
            int message;
            String[] args;
            if (method.equals("Get")) {
                message = ErrorMessages.ERROR_WEB_UNABLE_TO_GET;
                args = new String[] { webProps.urlString };
            } else {
                message = ErrorMessages.ERROR_WEB_UNABLE_TO_MODIFY_RESOURCE;
                String content = "";
                try {
                    if (postData != null)
                        content = new String(postData, "UTF-8");
                } catch (UnsupportedEncodingException el) {
                    Log.e(LOG_TAG, "UTF-8 is the default charset for Android but not available???");
                }

                args = new String[] { content, webProps.urlString };
            }

            form.dispatchErrorOccurredEvent(SimpleSpreadsheet.this, method, message, (Object[]) args);
        }
    }

    private CapturedProperties capturePropertyValues(String functionName) {
        try {
            return new CapturedProperties(this);
        } catch (MalformedURLException e) {
            form.dispatchErrorOccurredEvent(this, functionName, ErrorMessages.ERROR_WEB_MALFORMED_URL, url);
        } catch (InvalidRequestHeadersException e) {
            form.dispatchErrorOccurredEvent(this, functionName, e.errorNumber, e.index);
        }

        return null;
    }

    private void requestTextImpl(final String mode, final Object data, final Object value, final int count,
            final String text,
            final String encoding, final String functionName, final String httpVerb) {
        final CapturedProperties webProps = capturePropertyValues(functionName);
        if (webProps == null)
            return;

        lastTask = new FutureTask<Void>(new Runnable() {
            @Override
            public void run() {
                byte[] requestData;
                try {
                    if (encoding == null || encoding.length() == 0)
                        requestData = text.getBytes("UTF-8");
                    else
                        requestData = text.getBytes(encoding);
                } catch (UnsupportedEncodingException e) {
                    form.dispatchErrorOccurredEvent(SimpleSpreadsheet.this, functionName,
                            ErrorMessages.ERROR_WEB_UNSUPPORTED_ENCODING, encoding, null);
                    return;
                }

                performRequest(mode, data, value, count, null, webProps, requestData, httpVerb, functionName);
            }
        }, null);

        AsynchUtil.runAsynchronously(lastTask);
    }
}
