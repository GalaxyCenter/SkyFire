package apollo.tianya.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import apollo.tianya.AppContext;
import apollo.tianya.R;
import apollo.tianya.base.BaseFragment;
import apollo.tianya.util.DialogHelp;
import apollo.tianya.util.ImageUtil;
import apollo.tianya.util.SpannableUtil;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Texel on 2016/10/25.
 */

public class ActivityPubFragment extends BaseFragment {

    private static final int MAX_TEXT_LENGTH = 2400;
    public static final int ACTION_TYPE_ALBUM = 0;
    public static final int ACTION_TYPE_PHOTO = 1;

    private MenuItem mMenuSend;
    private String mSelectedImgPatch;
    private DisplayImageOptions mOptions;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1 && msg.obj != null) {
                SpannableString ss = null;
                String body = null;

                body = "[img]file://" + mSelectedImgPatch + "[/img]";
                ss = new SpannableString(body);
                SpannableUtil.drawImage(ss, body, SpannableUtil.DRAWABLE_LOADING, mOptions,
                        new SpannableUtil.ImageLoadedHandle() {
                            @Override
                            public void onImageLoaded(SpannableString spannable, String url, Bitmap bmp) {
                                ImageSpan[] image_spans = spannable.getSpans(0, spannable.length(), ImageSpan.class);
                                for (ImageSpan span : image_spans) {
                                    if (span.getDrawable() == SpannableUtil.DRAWABLE_LOADING
                                            && span.getSource().equals(url)) {
                                        int start = spannable.getSpanStart(span);
                                        int end = spannable.getSpanEnd(span);
                                        spannable.removeSpan(span);

                                        BitmapDrawable draw = new BitmapDrawable(AppContext.getInstance().getResources(), bmp);
                                        draw.setBounds(0, 0, draw.getIntrinsicWidth(), draw.getIntrinsicHeight());
                                        span = new ImageSpan(draw, url, ImageSpan.ALIGN_BOTTOM);
                                        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                        int select_start = mEtInput.getSelectionStart();
                                        mEtInput.getText().insert(select_start, spannable);
                                        break;
                                    }
                                }
                            }
                        }, null);
            }
        }
    };

    @BindView(R.id.et_content)
    EditText mEtInput;

    @BindView(R.id.tv_clear)
    TextView mTvClear;

    @BindView(R.id.iv_img)
    ImageView mIvImage;

    @BindView(R.id.rl_img)
    View mLyImage;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_activity_pub;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateSendMenu();
                mTvClear.setText((MAX_TEXT_LENGTH - s.length()) + "");
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_pub_menu, menu);
        mMenuSend = menu.findItem(R.id.menu_pub);

        updateSendMenu();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @OnClick({R.id.ib_picture})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_picture:
                handleSelectPicture();
                break;

        }
    }

    private void handleSelectPicture() {
        DialogHelp.getSelectDialog(this.getContext(), getResources().getStringArray(R.array.choose_picture), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case ACTION_TYPE_ALBUM:
                        Intent intent;
                        if (Build.VERSION.SDK_INT < 19) {
                            intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                                    ImageUtil.REQUEST_CODE_GETIMAGE_BYSDCARD);
                        } else {
                            intent = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                                    ImageUtil.REQUEST_CODE_GETIMAGE_BYSDCARD);
                        }
                        break;
                    case ACTION_TYPE_PHOTO:
                        // 判断是否挂载了SD卡
                        String savePath = "";
                        String storageState = Environment.getExternalStorageState();
                        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                            savePath = Environment.getExternalStorageDirectory()
                                    .getAbsolutePath() + "/apollo/Camera/";
                            File savedir = new File(savePath);
                            if (!savedir.exists()) {
                                savedir.mkdirs();
                            }
                        }

                        // 没有挂载SD卡，无法保存文件
                        if (TextUtils.isEmpty(savePath)) {
                            //AppContext.showToastShort("无法保存照片，请检查SD卡是否挂载");
                            Snackbar.make(null, R.string.error_no_sd, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            return;
                        }

                        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                                .format(new Date());
                        String fileName = "osc_" + timeStamp + ".jpg";// 照片命名
                        File out = new File(savePath, fileName);
                        Uri uri = Uri.fromFile(out);

                        mSelectedImgPatch = savePath + fileName;// 该照片的绝对路径

                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(intent,
                                ImageUtil.REQUEST_CODE_GETIMAGE_BYCAMERA);
                        break;
                    default:
                        break;
                }
            }
        }).show();
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK)
            return;

        new Thread() {
            @Override
            public void run() {
                Bitmap bmp = null;

                if (requestCode == ImageUtil.REQUEST_CODE_GETIMAGE_BYSDCARD) {
                    if (data == null)
                        return;

                    Uri selected_img_uri = data.getData();
                    if (selected_img_uri != null)
                        mSelectedImgPatch = ImageUtil.getImagePath(selected_img_uri, getActivity());
                }

                Message msg = new Message();
                msg.what = 1;
                msg.obj = mSelectedImgPatch;
                handler.sendMessage(msg);
            }

        }.run();
    }

    private void updateSendMenu() {
        if (mEtInput.getText().length() == 0) {
            mMenuSend.setEnabled(false);
            mMenuSend.setIcon(R.drawable.ic_send_gray_24dp);
        } else {
            mMenuSend.setEnabled(true);
            mMenuSend.setIcon(R.drawable.ic_send_while_24dp);
        }
    }

    private void insertImage(SpannableStringBuilder spannable, Bitmap bmp) {
        BitmapDrawable draw = null;

        draw = new BitmapDrawable(bmp);
        draw.setBounds(0, 0, draw.getIntrinsicWidth(), draw.getIntrinsicHeight());
        spannable.setSpan(new ImageSpan(draw, 0), 0, spannable.length(), Spannable.SPAN_POINT_MARK);
        int start = mEtInput.getSelectionStart();
        mEtInput.getText().insert(start, spannable);
    }
}
