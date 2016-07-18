package apollo.tianya.api.remote;


import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import apollo.tianya.AppConfig;
import apollo.tianya.AppContext;
import apollo.tianya.R;
import apollo.tianya.api.ApiHttpClient;
import apollo.tianya.util.DateTime;
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
                headers[0] = new ApiHttpClient.HttpHeader("Referer", "http://passport.tianya.cn/online/loginSuccess.jsp?fowardurl=http%3A%2F%2Fwww.tianya.cn%2F1749397&userthird=index&regOrlogin=%E7%99%BB%E5%BD%95%E4%B8%AD......&" + querys);

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
     * 获取栏目内容 
     * @param channel
     * @param handler
     */
    public static void getChannel(String channel, AsyncHttpResponseHandler handler) {
        String url = "http://www.tianya.cn/m/find/" + channel + "/index.shtml";

        ApiHttpClient.get(url, handler);
    }

    /**
     * 获取收藏
     * @param pageIndex
     * @param handler
     */
    public static void getBookMarks(int pageIndex, AsyncHttpResponseHandler handler) {
        String url = "http://www.tianya.cn/api/tw?method=bbsArticleMark.select&params.pageSize=20&params.pageNo=" + pageIndex;
        Header[] headers = null;

        headers = new Header[1];
        headers[0] = new ApiHttpClient.HttpHeader("Cookie", AppContext.getInstance().getProperty(AppConfig.CONF_COOKIE));

        ApiHttpClient.get(url, headers, handler);
    }

    /**
     * 获取用户发表的帖子
     * @param userId
     * @param publicNextId
     * @param techNextId
     * @param cityNextId
     * @param handler
     */
    public static void getUserThreads(int userId, int publicNextId, int techNextId, int cityNextId, AsyncHttpResponseHandler handler) {
        String url = "http://www.tianya.cn/api/tw?method=userinfo.ice.getUserTotalArticleList&params.userId="
                + userId + "&params.pageSize=20&params.bMore=true&params.publicNextId="
                + publicNextId + "&params.techNextId="
                + techNextId + "&params.cityNextId="
                + cityNextId;

        ApiHttpClient.get(url, handler);
    }

    /**
     * 获取用户回复的帖子
     * @param userId
     * @param publicNextId
     * @param techNextId
     * @param cityNextId
     * @param handler
     */
    public static void getUserPosts(int userId, int publicNextId, int techNextId, int cityNextId, AsyncHttpResponseHandler handler) {
        String url = "http://www.tianya.cn/api/tw?method=userinfo.ice.getUserTotalReplyList&params.userId="
                + userId + "&params.pageSize=20&params.bMore=true&params.publicNextId="
                + publicNextId + "&params.techNextId="
                + techNextId + "&params.cityNextId="
                + cityNextId;
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

    public static void createPost(String sectionId, String threadId, String title, String content, AsyncHttpResponseHandler handler) {
        String url = null;
        String referer = null;
        RequestParams params = new RequestParams();
        Header[] headers = null;

        if (TextUtils.isEmpty(threadId)) {
            url = "http://bbs.tianya.cn/api?method=bbs.ice.compose";
            referer = "http://bbs.tianya.cn/post-" + sectionId + "-" + threadId + "-1.shtml";
        } else {
            url = "http://bbs.tianya.cn/api?method=bbs.ice.reply";
            referer = "http://bbs.tianya.cn/post-" + sectionId + "-" + threadId + "-1.shtml";
        }
        headers = new Header[11];
        headers[0] = new ApiHttpClient.HttpHeader("Referer", referer);
        headers[1] = new ApiHttpClient.HttpHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.2; rv:18.0) Gecko/20100101 Firefox/18.0");
        headers[2] = new ApiHttpClient.HttpHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        headers[3] = new ApiHttpClient.HttpHeader("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
        headers[4] = new ApiHttpClient.HttpHeader("Accept-Encoding", "gzip, deflate");
        headers[5] = new ApiHttpClient.HttpHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headers[6] = new ApiHttpClient.HttpHeader("X-Requested-With", "XMLHttpRequest");
        headers[7] = new ApiHttpClient.HttpHeader("Connection", "keep-alive");
        headers[8] = new ApiHttpClient.HttpHeader("Pragma", "no-cache");
        headers[9] = new ApiHttpClient.HttpHeader("Cache-Control", "no-cache");
        headers[10] = new ApiHttpClient.HttpHeader("Cookie", AppContext.getInstance().getProperty(AppConfig.CONF_COOKIE));

        content += AppContext.getInstance().getResources().getString(R.string.post_content_tail);
        params.put("params.action", "");
        params.put("params.appBlock", sectionId);
        params.put("params.appId", "bbs");
        params.put("params.artId", threadId);
        params.put("params.bScore", "true");
        params.put("params.bWeibo", "false");
        params.put("params.content", content);
        params.put("params.item", sectionId);
        params.put("params.postId", threadId);
        params.put("params.prePostTime", Long.toString(DateTime.now().getTime()));
        params.put("params.preTitle", title);
        params.put("params.preUrl", "");
        params.put("params.preUserId", "");
        params.put("params.preUserName", "");
        params.put("params.sourceName", "iTianya");
        params.put("params.title", title);
        params.put("params.appId", "3");

        ApiHttpClient.post(url, headers, params, handler);
    }
}
