package apollo.tianya.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import apollo.tianya.R;
import apollo.tianya.base.BaseFragment;

/**
 * Created by Texel on 2016/6/1.
 */
public class MainFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;

        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_main, container,
                false);

        return view;
    }

}
