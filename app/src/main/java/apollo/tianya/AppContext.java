package apollo.tianya;

import com.loopj.android.http.AsyncHttpClient;

import apollo.tianya.api.remote.ApiHttpClient;
import apollo.tianya.base.BaseApplication;

/**
 * Created by Texel on 2016/5/30.
 */
public class AppContext extends BaseApplication {

    private static AppContext instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        init();
    }

    private void init() {
        AsyncHttpClient client = new AsyncHttpClient();

        ApiHttpClient.setHttpClient(client);
    }
}
