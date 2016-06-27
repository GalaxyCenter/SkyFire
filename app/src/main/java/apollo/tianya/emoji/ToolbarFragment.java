package apollo.tianya.emoji;

import android.view.View;

import apollo.tianya.R;
import apollo.tianya.base.BaseFragment;
import butterknife.BindView;

/**
 * Created by Texel on 2016/6/23.
 */
public class ToolbarFragment extends BaseFragment {

    public enum ToolAction {
        ACTION_CHANGE, ACTION_WRITE_COMMENT, ACTION_VIEW_COMMENT, ACTION_FAVORITE, ACTION_SHARE, ACTION_REPORT
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail_tool_bar;
    }
}
