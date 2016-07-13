package apollo.tianya;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

import apollo.tianya.api.ApiHttpClient;
import apollo.tianya.base.BaseApplication;
import apollo.tianya.bean.User;
import apollo.tianya.util.StringUtil;

/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 *
 * Created by Texel on 2016/5/30.
 */
public class AppContext extends BaseApplication {

    public static final int PAGE_SIZE = 20;// 默认分页大小
    private static AppContext instance;

    private boolean mLogin;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        init();
        initLogin();
    }

    private void init() {
        // 初始化网络请求
        AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore pcs = new PersistentCookieStore(this);

        client.setCookieStore(pcs);
        ApiHttpClient.setHttpClient(client);
        ApiHttpClient.setCookie(ApiHttpClient.getCookie(this));

    }

    public String getProperty(String key) {
        String res = AppConfig.getAppConfig(this).get(key);
        return res;
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    public void removeProperty(String... key) {
        AppConfig.getAppConfig(this).remove(key);
    }

    /**
     * 获得当前app运行的AppContext
     *
     * @return
     */
    public static AppContext getInstance() {
        return instance;
    }

    public boolean isLogin() {
        return mLogin;
    }

    private void initLogin() {
        User user = getLoginUser();
        if (null != user && user.getId() > 0) {
            mLogin = true;
        } else {
            this.cleanLoginInfo();
        }
    }

    public User getLoginUser() {
        User user = new User();
        user.setId(StringUtil.toInt(getProperty("user.uid"), 0));
        user.setName(getProperty("user.name"));
        return user;
    }

    /**
     * 清除登录信息
     */
    public void cleanLoginInfo() {
        this.mLogin = false;
        removeProperty("user.uid", "user.name", "user.face", "user.location",
                "user.followers", "user.fans", "user.score",
                "user.isRememberMe", "user.gender", "user.favoritecount");
    }
}
