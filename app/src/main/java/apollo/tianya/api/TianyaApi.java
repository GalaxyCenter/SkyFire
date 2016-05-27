package apollo.tianya.api;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import apollo.tianya.api.remote.ApiHttpClient;

/**
 * Created by Texel on 2016/5/27.
 */
public class TianyaApi {

    /**
     * 登录
     * @param username
     * @param password
     * @param handler
     */
    public static void login(String username, String password,
                             AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("username", username);
        params.put("pwd", password);
        params.put("keep_login", 1);
        String loginurl = "";
        ApiHttpClient.post(loginurl, params, handler);
    }
}
