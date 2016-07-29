package apollo.tianya.fragment.bar;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import apollo.tianya.R;

/**
 * Created by Texel on 2016/6/23.
 */
public class ToolbarFragment extends BarBaseFragment implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail_tool_bar;
    }

    @Override
    public void initView(View view) {
        view.findViewById(R.id.btn_change).setOnClickListener(this);
        view.findViewById(R.id.action_flight).setOnClickListener(this);
        view.findViewById(R.id.action_bookmark).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Action action = null;
        if (id == R.id.btn_change)
            action = Action.ACTION_CHANGE;
        else if (id == R.id.action_flight)
            action = Action.ACTION_FLIGHT;
        else if (id == R.id.action_bookmark)
            action = Action.ACTION_BOOKMARK;

        if (mActionListeners != null) {
            for (int i = mActionListeners.size() - 1; i >= 0; i--) {
                mActionListeners.get(i).onActionClick(action);
            }
        }
    }
}
