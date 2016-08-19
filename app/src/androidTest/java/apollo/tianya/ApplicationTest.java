package apollo.tianya;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.concurrent.CountDownLatch;

import apollo.tianya.api.ApiHttpClient;
import apollo.tianya.api.remote.TianyaApi;
import cz.msebera.android.httpclient.Header;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    private static String TAG = "ApplicationTest";

    public ApplicationTest() {
        super(Application.class);
    }

    public void testLogin() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        AsyncHttpClient client = new AsyncHttpClient();

        client.setProxy("192.168.33.55", 8888);
        client.addHeader("Accept-Encoding", "gzip");
        client.addHeader("Connection", "keep-alive");
        //client.addHeader("Content-Length", "0");
        //client.addHeader("Referer", "http://bbs.tianya.cn/post-1169-85-1.shtml");
        client.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.2; rv:18.0) Gecko/20100101 Firefox/18.0");

        //client.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        //client.addHeader("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");


        //client.addHeader("Accept-Encoding", "gzip, deflate");
        //client.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        //client.addHeader("X-Requested-With", "XMLHttpRequest");
        //client.addHeader("Pragma", "no-cache");
        //client.addHeader("Cache-Control", "no-cache");

        ApiHttpClient.setHttpClient(client);
        TianyaApi.searchThreads("sega", 1, new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String body = new String(responseBody);

                System.out.print(body);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String body = new String(responseBody);

                Log.i(TAG, body);
            }
        });

        latch.await();

        Log.i(TAG, "OK");
    }
}