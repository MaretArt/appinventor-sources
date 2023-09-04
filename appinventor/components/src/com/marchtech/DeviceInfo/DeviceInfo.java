package com.marchtech.DeviceInfo;

import android.app.Activity;
import android.util.Log;

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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.FutureTask;
import java.util.List;
import java.util.Map;

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
import com.google.appinventor.components.runtime.util.Dates;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.GingerbreadUtil;
import com.google.appinventor.components.runtime.util.JsonUtil;
import com.google.appinventor.components.runtime.util.SdkLevel;
import com.google.appinventor.components.runtime.util.YailList;

import com.marchtech.Icon;
import com.marchtech.DeviceInfo.helpers.Data;
import com.marchtech.OTPSender.OTPSender;

@DesignerComponent( version = 1,
                    description = "Extension to get device info",
                    category = ComponentCategory.EXTENSION,
                    nonVisible = true,
                    iconName = Icon.ICON)
@SimpleObject(external = true)
public class DeviceInfo extends AndroidNonvisibleComponent {
    private static final String LOG_TAG = "DeviceInfo";

    private final Activity activity;
    private final CookieHandler cookieHandler;

    private YailList requestHeaders = new YailList();
    private YailList columns = new YailList();
    private FutureTask<Void> lastTask = null;
    private HashMap<String, String> data = new HashMap<String, String>();

    public DeviceInfo(ComponentContainer container) {
        super(container.$form());
        activity = container.$context();

        cookieHandler = (SdkLevel.getLevel() >= SdkLevel.LEVEL_GINGERBREAD) ? GingerbreadUtil.newCookieManager() : null;
    }

    protected DeviceInfo() {
        super(null);
        activity = null;
        cookieHandler = null;
    }

    @SimpleEvent(description = "Occurs when obtained data.")
    public void GotData() {
        EventDispatcher.dispatchEvent(this, "GotData");
    }

    @SimpleEvent(description = "Occurs when failed obtaining data.")
    public void Failed() {
        EventDispatcher.dispatchEvent(this, "Failed");
    }

    @SimpleFunction(description = "To start getting info of device.")
    public void Start() {
        final String METHOD = "Get";
        final CapturedProperties webProps = capturePropertyValues(METHOD);
        if (webProps == null) return;

        lastTask = new FutureTask<Void>(new Runnable() {
            @Override
            public void run() {
                performRequest(webProps, "GET", METHOD);
            }
        }, null);

        AsynchUtil.runAsynchronously(lastTask);
    }

    @SimpleFunction(description = "To get IP address.")
    public @Options(Data.class) String Get(@Options(Data.class) String data) {
        return this.data.get(data);
    }

    private void Getted(String content) {
        if (content == null) {
            Failed();
            return;
        }

        String cont = content.substring(1, content.length() - 1);
        String[] array = cont.split(",");
        for (int i = 0; i < array.length; i++) {
            String[] keyValue = array[i].split(":");
            String key = keyValue[0].replaceAll("\"", "");
            String value = (keyValue[1] != null || keyValue[1] != "") ? keyValue[1].replaceAll("\"", "") : "";
            data.put(key, value);
        }

        GotData();
    }

    private void performRequest(final CapturedProperties webProps, final String httpVerb, final String method) {
        try {
            HttpURLConnection connection = openConnection(webProps, httpVerb);
            if (connection != null) {
                try {
                    final String responseType = getResponseType(connection);
                    final String responseContent = getResponseContent(connection);

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Getted(responseContent);
                        }
                    });

                    updateColumns(responseContent, responseType);
                } catch (SocketTimeoutException e) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Failed();
                        }
                    });

                    throw new RequestTimeoutException();
                } finally {
                    connection.disconnect();
                }
            }
        } catch (PermissionException e) {
            form.dispatchPermissionDeniedEvent(DeviceInfo.this, method, e);
        } catch (DispatchableError e) {
            form.dispatchErrorOccurredEvent(DeviceInfo.this, method, e.getErrorCode(), e.getArguments());
        } catch (RequestTimeoutException e) {
            form.dispatchErrorOccurredEvent(DeviceInfo.this, method, ErrorMessages.ERROR_WEB_REQUEST_TIMED_OUT, webProps.urlString);
        } catch (Exception e) {
            int message = ErrorMessages.ERROR_WEB_UNABLE_TO_GET;
            String[] args = new String[] { webProps.urlString };
            form.dispatchErrorOccurredEvent(DeviceInfo.this, method, message, (Object[]) args);
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
        if (encoding == null) encoding = "UTF-8";

        InputStreamReader reader = new InputStreamReader(getConnectionStream(connection), encoding);
        try {
            int contentLength = connection.getContentLength();
            StringBuilder sb = (contentLength != -1) ? new StringBuilder(contentLength) : new StringBuilder();
            char[] buf = new char[1024];
            int read;
            while ((read = reader.read(buf)) != -1) sb.append(buf, 0, read);

            return sb.toString();
        } finally {
            reader.close();
        }
    }

    private static String getResponseType(HttpURLConnection connection) {
        String responseType = connection.getContentType();
        return (responseType != null) ? responseType : "";
    }

    private static HttpURLConnection openConnection(CapturedProperties webProps, String httpVerb) throws IOException, ClassCastException, ProtocolException {
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

    private static class CapturedProperties {
        final String urlString;
        final URL url;
        final boolean allowCookies;
        final boolean saveResponse;
        final String responseFineName;
        final int timeout;
        final Map<String, List<String>> requestHeaders;
        final Map<String, List<String>> cookies;

        CapturedProperties(DeviceInfo web) throws MalformedURLException, InvalidRequestHeadersException {
            urlString = "http://ip-api.com/json";
            url = new URL(urlString);
            allowCookies = false;
            saveResponse = false;
            responseFineName = "";
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

    private static Map<String, List<String>> processRequestHeaders(YailList list) throws InvalidRequestHeadersException {
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
                } else throw new InvalidRequestHeadersException(ErrorMessages.ERROR_WEB_REQUEST_HEADER_NOT_TWO_ELEMENTS, i + 1);
            } else throw new InvalidRequestHeadersException(ErrorMessages.ERROR_WEB_REQUEST_HEADER_NOT_LIST, i + 1);
        }

        return requestHeadersMap;
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

    private CapturedProperties capturePropertyValues(String functionName) {
        try {
            return new CapturedProperties(this);
        } catch (MalformedURLException e) {
            form.dispatchErrorOccurredEvent(this, functionName, ErrorMessages.ERROR_WEB_MALFORMED_URL, "http://ip-api.com/json");
        } catch (InvalidRequestHeadersException e) {
            form.dispatchErrorOccurredEvent(this, functionName, e.errorNumber, e.index);
        }

        return null;
    }
}
