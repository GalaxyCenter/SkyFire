package apollo.tianya.api;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.Locale;

import apollo.tianya.AppContext;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HeaderElement;
import cz.msebera.android.httpclient.ParseException;
import cz.msebera.android.httpclient.client.params.ClientPNames;

/**
 * Created by Texel on 2016/5/27.
 */
public class ApiHttpClient {

    public static class HttpHeader implements Header {

        private String name;
        private String value;

        public HttpHeader(String n, String v) {
            this.name = n;
            this.value = v;
        }

        public void setName(String n) {
            this.name = n;
        }
        @Override
        public String getName() {
            return name;
        }

        public void setValue(String v) {
            this.value = v;
        }
        @Override
        public String getValue() {
            return value;
        }

        @Override
        public HeaderElement[] getElements() throws ParseException {
            return new HeaderElement[0];
        }
    }

    public static AsyncHttpClient client;
    public final static String HOST = "www.tianya.cn";
    private static String appCookie;
    private static String API_URL = "http://www.tianya.cn/%s";

    public ApiHttpClient() {
    }

    public static AsyncHttpClient getHttpClient() {
        return client;
    }

    public static void log(String log) {
        Log.d("BaseApi", log);
    }

    public static String getAbsoluteApiUrl(String partUrl) {
        String url = partUrl;
        if (!partUrl.startsWith("http:") && !partUrl.startsWith("https:")) {
            url = String.format(API_URL, partUrl);
        }
        Log.d("BASE_CLIENT", "request:" + url);
        return url;
    }

    public static void setCookie(String cookie) {
        //client.addHeader("Cookie", cookie);
    }

    public static String getCookie(AppContext appContext) {
        if (appCookie == null || appCookie == "") {
            appCookie = appContext.getProperty("cookie");
        }
        return appCookie;
    }

    public static void cleanCookie() {
        appCookie = "";
    }

    public static void setHttpClient(AsyncHttpClient c) {
        client = c;
        //client.setProxy("192.168.33.55", 8888);

        client.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.2; rv:18.0) Gecko/20100101 Firefox/18.0");
        client.addHeader("Accept-Language", Locale.getDefault().toString());
        client.getHttpClient().getParams()
                .setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
    }

    public static void setUserAgent(String userAgent) {
        client.setUserAgent(userAgent);
    }

    public static void post(String partUrl, AsyncHttpResponseHandler handler) {
        post(partUrl, null, null, handler);
    }

    public static void post(String partUrl, Header[] headers, AsyncHttpResponseHandler handler) {
        post(partUrl, headers, null, handler);
    }

    public static void post(String partUrl, Header[] headers, RequestParams params,
                            AsyncHttpResponseHandler handler) {
        client.post(null, partUrl, headers, params, null, handler);
        log(new StringBuilder("POST ").append(partUrl).append("&")
                .append(params).toString());
    }

    public static void get(String partUrl, AsyncHttpResponseHandler handler) {
        get(partUrl, null, null, handler);
    }

    public static void get(String partUrl, Header[] headers, AsyncHttpResponseHandler handler) {
        get(partUrl, headers, null, handler);
    }

    public static void get(String partUrl, Header[] headers, RequestParams params,
                           AsyncHttpResponseHandler handler) {
        //client.get(getAbsoluteApiUrl(partUrl), params, handler);
        client.get(null, partUrl, headers, params, handler);
        log(new StringBuilder("GET ").append(partUrl).append("&")
                .append(params).toString());
    }
}
