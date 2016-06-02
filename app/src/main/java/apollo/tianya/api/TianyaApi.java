package apollo.tianya.api;


import android.preference.PreferenceActivity;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import apollo.tianya.api.remote.ApiHttpClient;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HeaderElement;
import cz.msebera.android.httpclient.ParseException;
import cz.msebera.android.httpclient.client.HttpResponseException;

import static apollo.tianya.api.remote.ApiHttpClient.HttpHeader;

/**
 * Created by Texel on 2016/5/27.
 */
public class TianyaApi {

    private static String TAG = "TianyaApi";

    /**
     * 登录
     * @param username
     * @param password
     * @param handler
     */
    public static void login(String username, String password, String captcha, Header cookie_header,
                             final AsyncHttpResponseHandler handler) {
        RequestParams params = null;
        String loginurl = null;
        AsyncHttpResponseHandler _hld = null;
        Header[] headers = null;
        Header header_referer = null;

        params = new RequestParams();
        params.put("vwriter", username);
        params.put("vpassword", password);
        params.put("vc", captcha);
        params.put("rmflag", 1);
        params.put("__sid", "1#4#1.0#a0b0eb92-404d-4ea1-a45c-ad5bdbc439bf");
        params.put("action", "b2.1.1102|7341eb362f75554bf2e0a56029769c43|7b774effe4a349c6dd82ad4f4f21d34c|Mozilla/5.0 (Windows NT 10.0; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0|0|3|v2.2");

        header_referer = new ApiHttpClient.HttpHeader("Referer", "http://www.tianya.cn");
        if (cookie_header == null)
            headers = new Header[] {header_referer};
        else
            headers = new Header[] {header_referer, cookie_header};

        loginurl = "https://passport.tianya.cn/login?from=index&_goto=login&returnURL=http://www.tianya.cn";

        _hld = new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] responseHeaders, byte[] responseBody) {
                String body = null;
                String querys = null;
                String url = null;
                Pattern pattern = null;
                Matcher matcher = null;
                Header[] headers = null;

                body = new String(responseBody);
                pattern = Pattern.compile("&t=(.*)");
                matcher = pattern.matcher(body);
                if(matcher.find()) {
                    querys = matcher.group();
                    querys = querys.substring(1, querys.length() - 2);
                } else {
                    handler.sendFailureMessage(statusCode, responseHeaders, responseBody, null);
                    return;
                }

                headers = new Header[1];
                headers[0] = new ApiHttpClient.HttpHeader("Referer", "http://passport.tianya.cn/online/loginSuccess.jsp?fowardurl=http%3A%2F%2Fwww.tianya.cn%2F1749397&userthird=index&regOrlogin=%E7%99%BB%E5%BD%95%E4%B8%AD......=" + querys);

                url = "http://passport.tianya.cn/online/domain.jsp?" + querys + "&domain=tianya.cn";
                ApiHttpClient.get(url, headers, null, handler);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendFailureMessage(statusCode, headers, responseBody, error);
                return;
            }
        };
        ApiHttpClient.post(loginurl, headers, params, _hld);
    }

    public static void getCaptcha(AsyncHttpResponseHandler handler) {
        Header[] headers = null;
        String url = "https://passport.tianya.cn/staticHttps/validateImgProxy.jsp";

        headers = new Header[1];
        headers[0] = new ApiHttpClient.HttpHeader("Referer", "https://passport.tianya.cn/login");

        ApiHttpClient.post(url, headers, handler);
    }

    public static void getChannel(String a, int page) {

    }
}
