package apollo.tianya.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import apollo.tianya.R;
import apollo.tianya.api.TianyaParser;
import apollo.tianya.base.BaseActivity;
import apollo.tianya.bean.Entity;
import apollo.tianya.bean.Section;
import apollo.tianya.bean.Thread;
import apollo.tianya.util.UIHelper;

/**
 * Created by Texel on 2016/8/22.
 */
public class FacadeActivity extends BaseActivity {
    private static String TAG = "FacadeActivity";

    @Override
    protected void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

        Log.i(TAG, "URI:" + data);
        if (data == null) {
            super.finish();
            return;
        }

        dispatchUri(data.toString());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple;
    }

    @Override
    protected void initView() {

    }

    private void dispatchUri(String url) {
        Entity e = null;

        e = TianyaParser.parseThreadUrl(url);
        if (e != null) {
            UIHelper.showPostDetail(this, (Thread) e);
            finish();
            return;
        }

        e = TianyaParser.parseThreadUrl(url);
        if (e != null) {
            UIHelper.showThreadsActivity(this, ((Section) e).getGuid());
            finish();
            return;
        }

        UIHelper.showMainActivity(this);
        finish();
    }

}
