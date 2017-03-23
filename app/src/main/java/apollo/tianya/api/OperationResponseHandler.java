package apollo.tianya.api;

import android.os.Looper;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.ByteArrayInputStream;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Texel on 2016/10/27.
 */

public class OperationResponseHandler extends AsyncHttpResponseHandler {

    private Object[] mArgs;

    public OperationResponseHandler(Looper looper, Object... args) {
        super(looper);

        this.mArgs = args;
    }

    public OperationResponseHandler(Object... args) {
        this.mArgs = args;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        try {
            onSuccess(statusCode, new String(responseBody), mArgs);
        } catch(Exception e) {
            e.printStackTrace();
            onFailure(statusCode, e.getMessage(), mArgs);
        }
    }

    public void onSuccess(int code, String responseBody, Object[] args)
            throws Exception {
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        onFailure(statusCode, error.getMessage(), mArgs);
    }

    public void onFailure(int code, String errorMessage, Object[] args) {
    }
}
