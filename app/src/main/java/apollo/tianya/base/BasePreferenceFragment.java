package apollo.tianya.base;

import android.os.Bundle;
import android.view.View;

import com.github.machinarius.preferencefragment.PreferenceFragment;


/**
 * Created by Texel on 2016/7/18.
 */
public abstract class BasePreferenceFragment extends PreferenceFragment {

    protected abstract int getLayoutId();

    public void initView(View view) {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(getLayoutId());
    }
}
