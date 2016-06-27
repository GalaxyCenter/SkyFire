package apollo.tianya.fragment.bar;

import apollo.tianya.base.BaseFragment;

/**
 * Created by kuibo on 2016/6/27.
 */
public abstract class BarBaseFragment extends BaseFragment {

    public interface OnActionClickListener {
        public void onActionClick(Action action);
    }

    public enum Action {
        ACTION_CHANGE, ACTION_WRITE_COMMENT, ACTION_VIEW_COMMENT, ACTION_FAVORITE, ACTION_SHARE, ACTION_REPORT
    }
}
