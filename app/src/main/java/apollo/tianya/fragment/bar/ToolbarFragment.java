package apollo.tianya.fragment.bar;

import android.view.View;

import apollo.tianya.R;

/**
 * Created by Texel on 2016/6/23.
 */
public class ToolbarFragment extends BarBaseFragment implements View.OnClickListener {

    private OnActionClickListener mActionListener;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail_tool_bar;
    }

    @Override
    public void initView(View view) {
        view.findViewById(R.id.btn_change).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Action action = null;
        if (id == R.id.btn_change)
            action = Action.ACTION_CHANGE;

        if (action != null && mActionListener != null) {
            mActionListener.onActionClick(action);
        }
    }

    public void setOnActionClickListener(OnActionClickListener lis) {
        mActionListener = lis;
    }
}
