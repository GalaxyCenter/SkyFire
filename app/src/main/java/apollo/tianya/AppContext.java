package apollo.tianya;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import apollo.tianya.api.ApiHttpClient;
import apollo.tianya.base.BaseApplication;
import apollo.tianya.bean.Constants;
import apollo.tianya.bean.User;
import apollo.tianya.cache.DataCleanManager;
import apollo.tianya.util.MethodsCompat;
import apollo.tianya.util.StringUtil;

/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 *
 * Created by Texel on 2016/5/30.
 */
public class AppContext extends BaseApplication {
    private static String PREF_NAME = "creativelocker.pref";

    public static final int PAGE_SIZE = 20;// 默认分页大小
    private static AppContext instance;

    private boolean mLogin;
    private int mLoginUserId;
    private static boolean sIsAtLeastGB;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            sIsAtLeastGB = true;
        }
    }

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

        ImageLoaderConfiguration ilconfig = null;

        ilconfig = new ImageLoaderConfiguration.Builder(this.getApplicationContext())
                .imageDownloader(new BaseImageDownloader(this.getApplicationContext()){
                    @Override
                    protected HttpURLConnection createConnection(String url, Object extra) throws IOException {
                        HttpURLConnection conn = super.createConnection(url, extra);
                        Map<String, String> headers = (Map<String, String>) extra;
                        if (headers != null) {
                            for (Map.Entry<String, String> header : headers.entrySet()) {
                                conn.setRequestProperty(header.getKey(), header.getValue());
                            }
                        }
                        conn.setRequestProperty("Referer", "https://www.tianya.cn");
                        return conn;
                    }
                })
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(100 * 1024 * 1024)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(ilconfig);
    }

    public String getProperty(String key) {
        String res = AppConfig.getAppConfig(this).get(key);
        return res;
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    public void setProperties(Properties ps) {
        AppConfig.getAppConfig(this).set(ps);
    }

    public Properties getProperties() {
        return AppConfig.getAppConfig(this).get();
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
        int userId = 0;

        userId = StringUtil.toInt(getProperty("user.uid"), 0);
        if (userId > 0) {
            mLogin = true;
            mLoginUserId = userId;
        } else {
            this.cleanLoginInfo();
        }
    }

    public void logOut() {
        cleanLoginInfo();
        ApiHttpClient.cleanCookie();
        this.cleanCookie();
        this.mLogin = false;
        this.mLoginUserId = 0;

        Intent intent = new Intent(Constants.INTENT_ACTION_LOGOUT);
        sendBroadcast(intent);
    }

    /**
     * 清除保存的缓存
     */
    public void cleanCookie() {
        removeProperty(AppConfig.CONF_COOKIE);
    }

    public int getLoginUserId() {
        return mLoginUserId;
    }

    public User getLoginUser() {
        User user = null;

        if (mLoginUserId != 0) {
            user = new User();
            user.setId(StringUtil.toInt(getProperty("user.uid"), 0));
            user.setName(getProperty("user.name"));
        }
        return user;
    }

    public void saveUserInfo(final User user) {
        mLogin = true;
        mLoginUserId = user.getId();
        setProperties(new Properties() {
            {
                setProperty("user.uid", String.valueOf(user.getId()));
                setProperty("user.name", user.getName());
            }
        });
    }

    /**
     * 清除登录信息
     */
    public void cleanLoginInfo() {
        mLogin = false;
        removeProperty("user.uid", "user.name", "user.face", "user.location",
                "user.followers", "user.fans", "user.score",
                "user.isRememberMe", "user.gender", "user.favoritecount");
    }

    /************ Settings *************/
    public static void setFontSize(int size) {
        set(Constants.Settings.KEY_FONT_SIZE, size);
    }

    public static int getFontSize() {
        return getInt(Constants.Settings.KEY_FONT_SIZE, AppConfig.FONT_SIZE);
    }

    public static void setShowImage(boolean v) {
        set(Constants.Settings.KEY_SHOW_IMG, v);
    }

    public static boolean isShowImage() {
        return getBool(Constants.Settings.KEY_SHOW_IMG, true);
    }

    public static void setShowHeadImage(boolean v) {
        set(Constants.Settings.KEY_SHOW_HEAD_IMG, v);
    }

    public static boolean isShowHeadImage() {
        return getBool(Constants.Settings.KEY_SHOW_HEAD_IMG, true);
    }

    public static void set(String key, int value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(key, value);
        apply(editor);
    }

    public static void set(String key, boolean value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(key, value);
        apply(editor);
    }

    public static void set(String key, String value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(key, value);
        apply(editor);
    }

    public static int getInt(String key) {
        return getInt(key, -1);
    }

    public static int getInt(String key, int defVal) {
        return getPreferences().getInt(key, defVal);
    }

    public static boolean getBool(String key) {
        return getBool(key, false);
    }

    public static boolean getBool(String key, boolean defVal) {
        return getPreferences().getBoolean(key, defVal);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static SharedPreferences getPreferences() {
        SharedPreferences pre = context().getSharedPreferences(PREF_NAME,
                Context.MODE_MULTI_PROCESS);
        return pre;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void apply(SharedPreferences.Editor editor) {
        if (sIsAtLeastGB) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    public void clearAppCache() {
        DataCleanManager.cleanDatabases(this);
        // 清除数据缓存
        DataCleanManager.cleanInternalCache(this);
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            DataCleanManager.cleanCustomCache(MethodsCompat
                    .getExternalCacheDir(this));
        }
        // 清除编辑器保存的临时内容
        Properties props = getProperties();
        for (Object key : props.keySet()) {
            String _key = key.toString();
            if (_key.startsWith("temp"))
                removeProperty(_key);
        }
    }
}
