package apollo.tianya.fragment.bar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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

    class CloseKeyboardOnOutsideContainer extends FrameLayout {
        /**
         * @param context context
         */
        public CloseKeyboardOnOutsideContainer(Context context) {
            this(context, null);
        }

        /**
         * @param context context
         * @param attrs attrs
         */
        public CloseKeyboardOnOutsideContainer(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        /**
         * @param context context
         * @param attrs attrs
         * @param defStyle defStyle
         */
        public CloseKeyboardOnOutsideContainer(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            if ((mKeyBoardShowed || isEmojiPanelShowing()) && event.getAction() == MotionEvent.ACTION_DOWN) {
                int touchY = (int) (event.getY());
                if (isTouchKeyboardOutside(touchY)) {
                    if (mKeyBoardShowed) {
                        hideSoftKeyboard();
                    }
                    if (isEmojiPanelShowing()) {
                        hideEmojiKeyBoard();
                    }
                }
            }
            return super.onTouchEvent(event);
        }

        private boolean isTouchKeyboardOutside(int touchY) {
            int[] l = new int[2];
            int confine = 0;

            mEditor.getLocationOnScreen(l);
            confine = l[1] - CompatibleUtil.getStatusBarHeight(getActivity());
            return touchY < confine;
        }
    }

    private Runnable mHideEmotionPanelTask = new Runnable() {
        @Override
        public void run() {
            hideEmojiKeyBoard();
        }
    };

    private OnActionClickListener mActionListener;
    private AppCompatEditText mEditor;
    private GridView mGridView;
    private EmotionAdapter mEmoAdapter;
    private boolean mKeyBoardShowed = false;
    private int mKeyBoardHeight = 0;

    protected int getLayoutId() {
        return R.layout.fragment_detail_input_bar;
    }

    @Override
    public void initView(View view) {
        mKeyBoardHeight = getContext().getResources().getDimensionPixelSize(R.dimen.keyboard_height);

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
        mEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mKeyBoardShowed = true;
                if (isEmojiPanelShowing()) {
                    mGridView.postDelayed(mHideEmotionPanelTask, 500);
                }
            }
        });

        view.findViewById(R.id.btn_change).setOnClickListener(this);
        ((CheckBox)view.findViewById(R.id.btn_face_change)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isEmojiPanelShowing()) {
                    toggleSoftInput(getActivity().getCurrentFocus());
                    mGridView.postDelayed(mHideEmotionPanelTask, 500);
                } else {
                    showEmojiKeyBoard();
                }
            }
        });

        CloseKeyboardOnOutsideContainer frameLayout = new CloseKeyboardOnOutsideContainer(getActivity());
        getActivity().addContentView(frameLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
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

    public boolean isEmojiPanelShowing() {
        return mGridView.getVisibility() == View.VISIBLE;
    }

    /**
     * 隐藏Emoji并显示软键盘
     */
    public void hideEmojiKeyBoard() {
        mGridView.setVisibility(View.GONE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mKeyBoardShowed = true;
    }

    /**
     * 显示Emoji并隐藏软键盘
     */
    public void showEmojiKeyBoard() {
        LinearLayout.LayoutParams params = null;
        int height = 0;

        mGridView.removeCallbacks(mHideEmotionPanelTask);
        height = CompatibleUtil.getSoftInputHeight(getActivity());

        if (height != 0) {
            mKeyBoardHeight = height;
        }
        params = (LinearLayout.LayoutParams) mGridView.getLayoutParams();
        params.height = mKeyBoardHeight;

        mGridView.setVisibility(View.VISIBLE);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        hideSoftKeyboard();
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard() {
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
        ((InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE)).showSoftInput(mEditor,
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
        mKeyBoardShowed = true;
    }

    public void toggleSoftInput(View currentFocusView) {
        InputMethodManager imm = (InputMethodManager) currentFocusView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(currentFocusView, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
