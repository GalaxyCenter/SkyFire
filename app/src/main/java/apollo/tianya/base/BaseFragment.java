package apollo.tianya.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 碎片基类
 *
 * Created by Texel on 2016/6/1.
 */
public abstract class BaseFragment extends Fragment {

    protected abstract int getLayoutId();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), null);
    }
}
