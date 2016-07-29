package apollo.tianya.fragment.bar;

import java.util.ArrayList;
import java.util.List;

import apollo.tianya.base.BaseFragment;

/**
 * Created by kuibo on 2016/6/27.
 */
public abstract class BarBaseFragment extends BaseFragment {

    protected List<OnActionClickListener> mActionListeners;

    public interface OnActionClickListener {
        public void onActionClick(Action action);
    }

    public enum Action {
        ACTION_CHANGE, ACTION_FLIGHT, ACTION_VIEW_COMMENT, ACTION_FAVORITE, ACTION_SHARE, ACTION_REPORT
    }

    public void addOnActionClickListener(OnActionClickListener lis) {
        if (mActionListeners == null) {
            mActionListeners = new ArrayList<OnActionClickListener>();
        }
        mActionListeners.add(lis);
    }
}
