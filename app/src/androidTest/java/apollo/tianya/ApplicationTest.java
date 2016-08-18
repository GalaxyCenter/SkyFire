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