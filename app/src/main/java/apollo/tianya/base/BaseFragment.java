package apollo.tianya.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;


/**
 * 碎片基类
 *
 * Created by Texel on 2016/6/1.
 */
public abstract class BaseFragment extends Fragment {

    protected abstract int getLayoutId();

    public void initView(View view) {};
    protected void sendRequestData() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;

        view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        initView(view);


        return view;
    }
}
