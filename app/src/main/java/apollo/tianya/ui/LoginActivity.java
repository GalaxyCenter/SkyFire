package apollo.tianya.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import apollo.tianya.AppConfig;
import apollo.tianya.AppContext;
import apollo.tianya.R;
import apollo.tianya.api.remote.TianyaApi;
import apollo.tianya.api.ApiHttpClient;
import apollo.tianya.util.TLog;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.client.protocol.ClientContext;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.protocol.HttpContext;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static String TAG = "LoginActivity";

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mCaptchaView;
    private ImageView mCaptchaImg;

    private View mRootLayout;
    private View mCaptchaLayout;
    private View mProgressView;
    private View mLoginFormView;

    private Header mHeaderCookie = null;

    private final AsyncHttpResponseHandler mLoginHandle = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            showProgress(false);
            // 读取cookie
            AsyncHttpClient client = ApiHttpClient.getHttpClient();
            HttpContext httpContext = client.getHttpContext();
            CookieStore cookies = (CookieStore) httpContext.getAttribute(ClientContext.COOKIE_STORE);
            if (cookies != null) {
                String cookies_str = "";

                for (Cookie c : cookies.getCookies()) {
                    cookies_str += (c.getName() + "=" + c.getValue()) + "; ";
                }
                TLog.log(TAG, "Cookies:" + cookies_str);
                AppContext.getInstance().setProperty(AppConfig.CONF_COOKIE,
                        cookies_str);
                ApiHttpClient.setCookie(ApiHttpClient.getCookie(AppContext
                        .getInstance()));
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            String body = null;
            Pattern pattern = null;
            Matcher matcher = null;
            String err_msg = null;

            body = new String(responseBody);

            pattern = Pattern.compile("(?s)<i class=\"icon icon-error\"><\\/i>(.*?)<\\/div>");
            matcher = pattern.matcher(body);
            if(matcher.find()) {
                err_msg = matcher.group(1);
            }
            Log.i(TAG, "login fail:" + err_msg);

            showProgress(false);

            if ("验证码错误".equals(err_msg) || "请输入验证码".equals(err_msg)) {
                mCaptchaView.setError(err_msg);
                mCaptchaView.requestFocus();
                showCaptcha();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }
    };

    private final AsyncHttpResponseHandler mCaptchaHandle = new AsyncHttpResponseHandler(){

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            BitmapFactory.Options option = new BitmapFactory.Options();
            Bitmap bitmap = null;

            bitmap = BitmapFactory.decodeByteArray(responseBody, 0,
                    responseBody.length, option);
            mCaptchaImg.setImageBitmap(bitmap);
            bitmap = null;

            // 读取验证码中的cookie
            AsyncHttpClient client = ApiHttpClient.getHttpClient();
            HttpContext httpContext = client.getHttpContext();
            CookieStore cookies = (CookieStore) httpContext.getAttribute(ClientContext.COOKIE_STORE);
            if (cookies != null) {
                String cookies_str = "";
                for (Cookie c : cookies.getCookies()) {
                    cookies_str += (c.getName() + "=" + c.getValue()) + "; ";
                }
                mHeaderCookie = new ApiHttpClient.HttpHeader("Cookie", cookies_str);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Snackbar.make(mRootLayout, R.string.error_captcha_load_fail, Snackbar.LENGTH_SHORT)
                    .show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mCaptchaImg = (ImageView) findViewById(R.id.captcha_img);
        mCaptchaImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showCaptcha();
            }
        });
        mCaptchaView = (EditText) findViewById(R.id.captcha);

        mRootLayout = findViewById(R.id.root_layout);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mCaptchaLayout = findViewById(R.id.captcha_layout);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            TianyaApi.login(mEmailView.getText().toString(), mPasswordView.getText().toString(),
                    mCaptchaView.getText().toString(), mHeaderCookie, mLoginHandle);
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showCaptcha() {
        mCaptchaLayout.setVisibility(View.VISIBLE);
        TianyaApi.getCaptcha(mCaptchaHandle);
    }

}

