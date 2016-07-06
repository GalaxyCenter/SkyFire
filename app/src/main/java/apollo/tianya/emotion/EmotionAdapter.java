package apollo.tianya.emotion;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import apollo.tianya.util.ImageUtil;

/**
 * Created by Texel on 2016/6/28.
 */
public class EmotionAdapter extends BaseAdapter {

    private Context mContext = null;
    private HashMap<String, Integer> mEmo = EmotionUtil.getWriteEmotion();
    private ArrayList<Integer> mEmoList = EmotionUtil.getWriteEmotionList();
    private HashMap<Integer, SoftReference<Bitmap>> mSoftBitmap = null;

    public EmotionAdapter(Context context) {
        mEmo = EmotionUtil.getWriteEmotion();
        mEmoList = EmotionUtil.getWriteEmotionList();
        mSoftBitmap = new HashMap<Integer, SoftReference<Bitmap>>();
        mContext = null;
        mContext = context;
    }

    @Override
    public int getCount() {
        return this.mEmoList.size();
    }

    @Override
    public Object getItem(int position) {
        Bitmap bmp = null;
        SoftReference<Bitmap> ref = null;
        Integer resid = null;

        resid = mEmoList.get(position);
        ref = mSoftBitmap.get(resid);
        if (ref != null) {
            bmp = ref.get();
        } else {
            bmp = ImageUtil.getResBitmap(mContext, resid);
            ref = new SoftReference<Bitmap>(bmp);
            mSoftBitmap.put(resid, ref);
        }
        return bmp;
    }

    public String getName(int position) {
        String name = null;
        Iterator<Map.Entry<String,Integer>> iter;
        Map.Entry<String, Integer> entry;
        int resid = 0;

        resid = mEmoList.get(position).intValue();
        iter = mEmo.entrySet().iterator();
        while(iter.hasNext()) {
            entry = (Map.Entry<String, Integer>)iter.next();
            if (entry.getValue().intValue() == resid){
                name = entry.getKey();
                break;
            }
        }
        return name;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView view = null;

        if (convertView == null) {
            view = new ImageView(mContext);
            view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            view.setLayoutParams(new AbsListView.LayoutParams(100, 100));
        } else {
            view = (ImageView) convertView;
        }
        view.setFocusable(false);
        view.setImageBitmap((Bitmap) getItem(position));
        return view;
    }
}
