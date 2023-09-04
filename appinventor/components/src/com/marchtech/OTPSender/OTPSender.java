package com.marchtech.OTPSender;

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

@DesignerComponent(version = 1, description = "Extension to using pedometer", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = Icon.ICON)
@SimpleObject(external = true)
public class OTPSender extends AndroidNonvisibleComponent {
    private static final String LOG_TAG = "OTPSender";

    private final Activity activity;
    private final CookieHandler cookieHandler;

    private String url = "";

    private YailList requestHeaders = new YailList();
    private YailList columns = new YailList();
    private FutureTask<Void> lastTask = null;

    public OTPSender(ComponentContainer container) {
        super(container.$form());
        activity = container.$context();

        cookieHandler = (SdkLevel.getLevel() >= SdkLevel.LEVEL_GINGERBREAD) ? GingerbreadUtil.newCookieManager() : null;
    }

    protected OTPSender() {
        super(null);
        activity = null;
        cookieHandler = null;
    }

    @SimpleEvent(description = "Occurs when otp has been sended.")
    public void onSuccess() {
        EventDispatcher.dispatchEvent(this, "onSuccess");
    }

    @SimpleEvent(description = "Occurs when otp has not sended.")
    public void onFailed() {
        EventDispatcher.dispatchEvent(this, "onFailed");
    }

    @SimpleEvent(description = "")
    public void onTimeout() {
        EventDispatcher.dispatchEvent(this, "onTimeout");
    }

    @SimpleFunction(description = "For sending OTP to email.")
    public void Send(String to, String subject, String otp, String msg) {
        String day = LocalDate.now().getDayOfWeek().name();
        String dateTime = Clock.FormatDateTime(Dates.Now(), "dd/MM/yyyy hh:mm:ss a zZ");
        String data = "time=" + day + ", " + dateTime + "&recipient=" + to + "&subject=" + subject + "&otp=" + otp
                + "&msg=" + msg;

        requestTextImpl(data, "UTF-8", "Send", "POST");
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "")
    @SimpleProperty(description = "The URL for the web request.")
    public void URL(String url) {
        this.url = url;
    }

    @SimpleProperty
    public String URL() {
        return url;
    }

    private void performRequest(final CapturedProperties webProps, final byte[] postData, final String httpVerb,
            final String method) {
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
                            if (responseCode == 200)
                                onSuccess();
                            else
                                onFailed();
                        }
                    });

                    updateColumns(responseContent, responseType);
                } catch (SocketTimeoutException e) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onTimeout();
                        }
                    });

                    throw new RequestTimeoutException();
                } finally {
                    connection.disconnect();
                }
            }
        } catch (PermissionException e) {
            form.dispatchPermissionDeniedEvent(OTPSender.this, method, e);
        } catch (DispatchableError e) {
            form.dispatchErrorOccurredEvent(OTPSender.this, method, e.getErrorCode(), e.getArguments());
        } catch (RequestTimeoutException e) {
            form.dispatchErrorOccurredEvent(OTPSender.this, method, ErrorMessages.ERROR_WEB_REQUEST_TIMED_OUT,
                    webProps.urlString);
        } catch (Exception e) {
            int message = ErrorMessages.ERROR_WEB_UNABLE_TO_MODIFY_RESOURCE;
            String[] args;
            String content = "";
            try {
                if (postData != null)
                    content = new String(postData, "UTF-8");
            } catch (UnsupportedEncodingException el) {
                Log.e(LOG_TAG, "UTF-8 is the default charset for Android but not available???");
            }

            args = new String[] { content, webProps.urlString };
            form.dispatchErrorOccurredEvent(OTPSender.this, method, message, (Object[]) args);
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

    private static InputStream getConnectionStream(HttpURLConnection connection) throws SocketTimeoutException {
        try {
            return connection.getInputStream();
        } catch (SocketTimeoutException e) {
            throw e;
        } catch (IOException el) {
            return connection.getErrorStream();
        }
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

    private static String getResponseType(HttpURLConnection connection) {
        String responseType = connection.getContentType();
        return (responseType != null) ? responseType : "";
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

    private void requestTextImpl(final String text, final String encoding, final String functionName,
            final String httpVerb) {
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
                    form.dispatchErrorOccurredEvent(OTPSender.this, functionName,
                            ErrorMessages.ERROR_WEB_UNSUPPORTED_ENCODING, encoding, null);
                    return;
                }

                performRequest(webProps, requestData, httpVerb, functionName);
            }
        }, null);

        AsynchUtil.runAsynchronously(lastTask);
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

        CapturedProperties(OTPSender web) throws MalformedURLException, InvalidRequestHeadersException {
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
}
