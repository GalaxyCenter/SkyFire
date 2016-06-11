package apollo.tianya.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Texel on 2016/6/1.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected abstract void init(Bundle savedInstanceState);
    protected abstract int getLayoutId();
    protected abstract void initView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());
        init(savedInstanceState);
        initView();
    }
}
