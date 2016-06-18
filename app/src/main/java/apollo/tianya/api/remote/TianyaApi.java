package apollo.tianya.api.remote;


import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import apollo.tianya.api.ApiHttpClient;
import cz.msebera.android.httpclient.Header;

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
            }
        };
        ApiHttpClient.post(loginurl, headers, params, _hld);
    }

    /**
     * 获取验证码
     * @param handler
     */
    public static void getCaptcha(AsyncHttpResponseHandler handler) {
        Header[] headers = null;
        String url = "https://passport.tianya.cn/staticHttps/validateImgProxy.jsp";

        headers = new Header[1];
        headers[0] = new ApiHttpClient.HttpHeader("Referer", "https://passport.tianya.cn/login");

        ApiHttpClient.post(url, headers, handler);
    }

    /**
     * 获取用户id
     * @param name
     * @param handler
     */
    public static void getUserId(String name, final AsyncHttpResponseHandler handler) {
        String transf_url = "http://my.tianya.cn/info/" + name;

        ApiHttpClient.get(transf_url, handler);
    }

    /**
     * 获取用户头像
     * @param userId
     * @param handler
     */
    public static void getAvatar(int userId, AsyncHttpResponseHandler handler) {
        String url = "http://tx.tianyaui.com/logo/" + userId;

        getImage(url, handler);
    }

    /**
     * 获取相册内容
     * @param url
     * @param handler
     */
    public static void getImage(String url, AsyncHttpResponseHandler handler) {
        Header[] headers = null;

        headers = new Header[1];
        headers[0] = new ApiHttpClient.HttpHeader("Referer", "https://www.tianya.cn");

        ApiHttpClient.get(url, headers, handler);
    }

    /**
     * 获取推荐内容
     * @param handler
     */
    public static void getRecommendThread(AsyncHttpResponseHandler handler) {
        String url = "http://www.tianya.cn/m/find/index.shtml";

        ApiHttpClient.get(url, handler);
    }

    /**
     * 获取热贴内容
     * @param handler
     */
    public static void getHotThread(int pageIndex, AsyncHttpResponseHandler handler) {
        String url = "http://bbs.tianya.cn/m/hotArticle.jsp?pageNum=" + pageIndex;

        ApiHttpClient.get(url, handler);
    }

    /**
     * 获取一个主题的所有帖子
     * @param sectionId 板块Id
     * @param threadId 主题Id
     * @param pageIndex 页码
     * @param handler
     */
    public static void getPosts(String sectionId, String threadId, int pageIndex, AsyncHttpResponseHandler handler) {
        String post_url = "http://bbs.tianya.cn/m/post-" + sectionId + "-" + threadId + "-" + pageIndex + ".shtml";

        getPosts(post_url, handler);
    }

    /**
     * 获取一个主题的所有帖子
     * @param post_url 主题的URL
     * @param handler
     */
    public static void getPosts(String post_url, AsyncHttpResponseHandler handler) {
        ApiHttpClient.get(post_url, handler);
    }
}
