package apollo.tianya.fragment.bar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;

import apollo.tianya.R;
import apollo.tianya.emotion.EmotionAdapter;
import apollo.tianya.util.CompatibleUtil;

/**
 * Created by kuibo on 2016/6/27.
 */
public class InputFragment extends BarBaseFragment implements View.OnClickListener {

    private OnActionClickListener mActionListener;
    private AppCompatEditText mEditor;
    private GridView mGridView;
    private EmotionAdapter mEmoAdapter;
    private LinearLayout mInputLayout;
    private boolean mKeyBoardShowed = false;

    protected int getLayoutId() {
        return R.layout.fragment_detail_input_bar;
    }

    @Override
    public void initView(View view) {
        mInputLayout = (LinearLayout) view.findViewById(R.id.input_layout);
        mEmoAdapter = new EmotionAdapter(super.getContext());
        mGridView = (GridView) view.findViewById(R.id.face_view);
        mGridView.setAdapter(mEmoAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String emo = null;

                emo = mEmoAdapter.getName(position);
                if (emo != null) {
                    int start = 0;
                    SpannableStringBuilder spannable = null;
                    Bitmap bmp = null;

                    start = mEditor.getSelectionStart();
                    spannable = new SpannableStringBuilder(emo);
                    bmp = (Bitmap) mEmoAdapter.getItem(position);
                    if (bmp != null) {
                        BitmapDrawable draw = null;

                        draw = new BitmapDrawable(bmp);
                        draw.setBounds(0, 0, 1 + bmp.getWidth(), bmp.getHeight());
                        draw.setGravity(3);
                        spannable.setSpan(new ImageSpan(draw, 0), 0, spannable.length(), Spannable.SPAN_POINT_MARK);
                        mEditor.getText().insert(start, spannable);
                    }
                }
            }
        });

        mEditor = (AppCompatEditText) view.findViewById(R.id.editor);
        view.findViewById(R.id.btn_change).setOnClickListener(this);
        ((CheckBox)view.findViewById(R.id.btn_face_change)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    showEmojiKeyBoard();
                } else {
                    hideEmojiKeyBoard();
                }
            }
        });
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

    /**
     * 隐藏Emoji并显示软键盘
     */
    public void hideEmojiKeyBoard() {
        if (!mKeyBoardShowed) {
            LinearLayout.LayoutParams params = null;

            //params = (LinearLayout.LayoutParams) mInputLayout.getLayoutParams();
            //params.height = mGridView.getTop();
            //params.height = 520;
            //params.weight = 0.0f;
        }

        mGridView.setVisibility(View.GONE);
        showSoftKeyboard();
    }

    /**
     * 显示Emoji并隐藏软键盘
     */
    public void showEmojiKeyBoard() {
        if (mKeyBoardShowed) {
            LinearLayout.LayoutParams params = null;

            params = (LinearLayout.LayoutParams) mGridView.getLayoutParams();
            params.height = CompatibleUtil.getSoftInputHeight(super.getActivity());
        }
        mGridView.setVisibility(View.VISIBLE);
        hideSoftKeyboard();
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        ((InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mEditor.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        mKeyBoardShowed = false;
    }

    /**
     * 显示软键盘
     */
    public void showSoftKeyboard() {
        mEditor.requestFocus();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ((InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE)).showSoftInput(mEditor,
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
        mKeyBoardShowed = true;
    }
}
