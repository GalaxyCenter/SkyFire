package apollo.tianya.emotion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import apollo.tianya.R;
import apollo.tianya.util.ImageUtil;

/**
 * Created by Texel on 2016/6/28.
 */
public class EmotionUtil {

    private static final HashMap<String, Integer> emos;
    private static final HashMap<String, String> file2str;
    private static final HashMap<String, Integer> write_emos;
    private static final ArrayList<Integer> write_emos_list;

    static {
        emos = new HashMap<String, Integer>();
        emos.put("image_emoticon", R.drawable.image_emoticon);
        emos.put("image_emoticon2", R.drawable.image_emoticon2);
        emos.put("image_emoticon3", R.drawable.image_emoticon3);
        emos.put("image_emoticon4", R.drawable.image_emoticon4);
        emos.put("image_emoticon5", R.drawable.image_emoticon5);
        emos.put("image_emoticon6", R.drawable.image_emoticon6);
        emos.put("image_emoticon7", R.drawable.image_emoticon7);
        emos.put("image_emoticon8", R.drawable.image_emoticon8);
        emos.put("image_emoticon9", R.drawable.image_emoticon9);
        emos.put("image_emoticon10", R.drawable.image_emoticon10);
        emos.put("image_emoticon11", R.drawable.image_emoticon11);
        emos.put("image_emoticon12", R.drawable.image_emoticon12);
        emos.put("image_emoticon13", R.drawable.image_emoticon13);
        emos.put("image_emoticon14", R.drawable.image_emoticon14);
        emos.put("image_emoticon15", R.drawable.image_emoticon15);
        emos.put("image_emoticon16", R.drawable.image_emoticon16);
        emos.put("image_emoticon17", R.drawable.image_emoticon17);
        emos.put("image_emoticon18", R.drawable.image_emoticon18);
        emos.put("image_emoticon19", R.drawable.image_emoticon19);
        emos.put("image_emoticon20", R.drawable.image_emoticon20);
        emos.put("image_emoticon21", R.drawable.image_emoticon21);
        emos.put("image_emoticon22", R.drawable.image_emoticon22);
        emos.put("image_emoticon23", R.drawable.image_emoticon23);
        emos.put("image_emoticon24", R.drawable.image_emoticon24);
        emos.put("image_emoticon25", R.drawable.image_emoticon25);
        emos.put("image_emoticon26", R.drawable.image_emoticon26);
        emos.put("image_emoticon27", R.drawable.image_emoticon27);
        emos.put("image_emoticon28", R.drawable.image_emoticon28);
        emos.put("image_emoticon29", R.drawable.image_emoticon29);
        emos.put("image_emoticon30", R.drawable.image_emoticon30);
        emos.put("image_emoticon31", R.drawable.image_emoticon31);
        emos.put("image_emoticon32", R.drawable.image_emoticon32);
        emos.put("image_emoticon33", R.drawable.image_emoticon33);
        emos.put("image_emoticon34", R.drawable.image_emoticon34);
        emos.put("image_emoticon35", R.drawable.image_emoticon35);
        emos.put("image_emoticon36", R.drawable.image_emoticon36);
        emos.put("image_emoticon37", R.drawable.image_emoticon37);
        emos.put("image_emoticon38", R.drawable.image_emoticon38);
        emos.put("image_emoticon39", R.drawable.image_emoticon39);
        emos.put("image_emoticon40", R.drawable.image_emoticon40);
        emos.put("image_emoticon41", R.drawable.image_emoticon41);
        emos.put("image_emoticon42", R.drawable.image_emoticon42);
        emos.put("image_emoticon43", R.drawable.image_emoticon43);
        emos.put("image_emoticon44", R.drawable.image_emoticon44);
        emos.put("image_emoticon45", R.drawable.image_emoticon45);
        emos.put("image_emoticon46", R.drawable.image_emoticon46);
        emos.put("image_emoticon47", R.drawable.image_emoticon47);
        emos.put("image_emoticon48", R.drawable.image_emoticon48);
        emos.put("image_emoticon49", R.drawable.image_emoticon49);
        emos.put("image_emoticon50", R.drawable.image_emoticon50);

        write_emos_list = new ArrayList<Integer>();
        write_emos_list.add(R.drawable.image_emoticon);
        write_emos_list.add(R.drawable.image_emoticon2);
        write_emos_list.add(R.drawable.image_emoticon3);
        write_emos_list.add(R.drawable.image_emoticon4);
        write_emos_list.add(R.drawable.image_emoticon5);
        write_emos_list.add(R.drawable.image_emoticon6);
        write_emos_list.add(R.drawable.image_emoticon7);
        write_emos_list.add(R.drawable.image_emoticon8);
        write_emos_list.add(R.drawable.image_emoticon9);
        write_emos_list.add(R.drawable.image_emoticon10);
        write_emos_list.add(R.drawable.image_emoticon11);
        write_emos_list.add(R.drawable.image_emoticon12);
        write_emos_list.add(R.drawable.image_emoticon13);
        write_emos_list.add(R.drawable.image_emoticon14);
        write_emos_list.add(R.drawable.image_emoticon15);
        write_emos_list.add(R.drawable.image_emoticon16);
        write_emos_list.add(R.drawable.image_emoticon17);
        write_emos_list.add(R.drawable.image_emoticon18);
        write_emos_list.add(R.drawable.image_emoticon19);
        write_emos_list.add(R.drawable.image_emoticon20);
        write_emos_list.add(R.drawable.image_emoticon21);
        write_emos_list.add(R.drawable.image_emoticon22);
        write_emos_list.add(R.drawable.image_emoticon23);
        write_emos_list.add(R.drawable.image_emoticon24);
        write_emos_list.add(R.drawable.image_emoticon25);
        write_emos_list.add(R.drawable.image_emoticon26);
        write_emos_list.add(R.drawable.image_emoticon27);
        write_emos_list.add(R.drawable.image_emoticon28);
        write_emos_list.add(R.drawable.image_emoticon29);
        write_emos_list.add(R.drawable.image_emoticon30);
        write_emos_list.add(R.drawable.image_emoticon31);
        write_emos_list.add(R.drawable.image_emoticon32);
        write_emos_list.add(R.drawable.image_emoticon33);
        write_emos_list.add(R.drawable.image_emoticon34);
        write_emos_list.add(R.drawable.image_emoticon35);
        write_emos_list.add(R.drawable.image_emoticon36);
        write_emos_list.add(R.drawable.image_emoticon37);
        write_emos_list.add(R.drawable.image_emoticon38);
        write_emos_list.add(R.drawable.image_emoticon39);
        write_emos_list.add(R.drawable.image_emoticon40);
        write_emos_list.add(R.drawable.image_emoticon41);
        write_emos_list.add(R.drawable.image_emoticon42);
        write_emos_list.add(R.drawable.image_emoticon43);
        write_emos_list.add(R.drawable.image_emoticon44);
        write_emos_list.add(R.drawable.image_emoticon45);
        write_emos_list.add(R.drawable.image_emoticon46);
        write_emos_list.add(R.drawable.image_emoticon47);
        write_emos_list.add(R.drawable.image_emoticon48);
        write_emos_list.add(R.drawable.image_emoticon49);
        write_emos_list.add(R.drawable.image_emoticon50);

        write_emos = new HashMap<String, Integer>();
        write_emos.put("[呵呵]", write_emos_list.get(0));
        write_emos.put("[哈哈]", write_emos_list.get(1));
        write_emos.put("[吐舌]", write_emos_list.get(2));
        write_emos.put("[啊]", write_emos_list.get(3));
        write_emos.put("[酷]", write_emos_list.get(4));
        write_emos.put("[怒]", write_emos_list.get(5));
        write_emos.put("[开心]", write_emos_list.get(6));
        write_emos.put("[汗]", write_emos_list.get(7));
        write_emos.put("[泪]", write_emos_list.get(8));
        write_emos.put("[黑线]", write_emos_list.get(9));
        write_emos.put("[鄙视]", write_emos_list.get(10));
        write_emos.put("[不高兴]", write_emos_list.get(11));
        write_emos.put("[真棒]", write_emos_list.get(12));
        write_emos.put("[钱]", write_emos_list.get(13));
        write_emos.put("[疑问]", write_emos_list.get(14));
        write_emos.put("[阴险]", write_emos_list.get(15));
        write_emos.put("[吐]", write_emos_list.get(16));
        write_emos.put("[咦]", write_emos_list.get(17));
        write_emos.put("[委屈]", write_emos_list.get(18));
        write_emos.put("[花心]", write_emos_list.get(19));
        write_emos.put("[呼~]", write_emos_list.get(20));
        write_emos.put("[笑眼]", write_emos_list.get(21));
        write_emos.put("[冷]", write_emos_list.get(22));
        write_emos.put("[太开心]", write_emos_list.get(23));
        write_emos.put("[滑稽]", write_emos_list.get(24));
        write_emos.put("[勉强]", write_emos_list.get(25));
        write_emos.put("[狂汗]", write_emos_list.get(26));
        write_emos.put("[乖]", write_emos_list.get(27));
        write_emos.put("[睡觉]", write_emos_list.get(28));
        write_emos.put("[惊哭]", write_emos_list.get(29));
        write_emos.put("[升起]", write_emos_list.get(30));
        write_emos.put("[惊讶]", write_emos_list.get(31));
        write_emos.put("[喷]", write_emos_list.get(32));
        write_emos.put("[爱心]", write_emos_list.get(33));
        write_emos.put("[心碎]", write_emos_list.get(34));
        write_emos.put("[玫瑰]", write_emos_list.get(35));
        write_emos.put("[礼物]", write_emos_list.get(36));
        write_emos.put("[彩虹]", write_emos_list.get(37));
        write_emos.put("[星星月亮]", write_emos_list.get(38));
        write_emos.put("[太阳]", write_emos_list.get(39));
        write_emos.put("[钱币]", write_emos_list.get(40));
        write_emos.put("[灯泡]", write_emos_list.get(41));
        write_emos.put("[茶杯]", write_emos_list.get(42));
        write_emos.put("[蛋糕]", write_emos_list.get(43));
        write_emos.put("[音乐]", write_emos_list.get(44));
        write_emos.put("[haha]", write_emos_list.get(45));
        write_emos.put("[胜利]", write_emos_list.get(46));
        write_emos.put("[大拇指]", write_emos_list.get(47));
        write_emos.put("[弱]", write_emos_list.get(48));
        write_emos.put("[OK]", write_emos_list.get(49));


        file2str = new HashMap<String, String>();
        file2str.put("image_emoticon", "呵呵");
        file2str.put("image_emoticon2", "哈哈");
        file2str.put("image_emoticon3", "吐舌");
        file2str.put("image_emoticon4", "啊?");
        file2str.put("image_emoticon5", "酷");
        file2str.put("image_emoticon6", "怒");
        file2str.put("image_emoticon7", "开心");
        file2str.put("image_emoticon8", "汗");
        file2str.put("image_emoticon9", "泪");
        file2str.put("image_emoticon10", "黑线");
        file2str.put("image_emoticon11", "鄙视");
        file2str.put("image_emoticon12", "不高兴");
        file2str.put("image_emoticon13", "真棒");
        file2str.put("image_emoticon14", "钱");
        file2str.put("image_emoticon15", "疑问");
        file2str.put("image_emoticon16", "阴险");
        file2str.put("image_emoticon17", "吐");
        file2str.put("image_emoticon18", "咦?");
        file2str.put("image_emoticon19", "委屈");
        file2str.put("image_emoticon20", "花心");
        file2str.put("image_emoticon21", "呼~");
        file2str.put("image_emoticon22", "笑眼");
        file2str.put("image_emoticon23", "冷");
        file2str.put("image_emoticon24", "太开心");
        file2str.put("image_emoticon25", "滑稽");
        file2str.put("image_emoticon26", "勉强");
        file2str.put("image_emoticon27", "狂汗");
        file2str.put("image_emoticon28", "乖");
        file2str.put("image_emoticon29", "睡觉");
        file2str.put("image_emoticon30", "惊哭");
        file2str.put("image_emoticon31", "升起");
        file2str.put("image_emoticon32", "惊讶");
        file2str.put("image_emoticon33", "喷");
        file2str.put("image_emoticon34", "爱心");
        file2str.put("image_emoticon35", "心碎");
        file2str.put("image_emoticon36", "玫瑰");
        file2str.put("image_emoticon37", "礼物");
        file2str.put("image_emoticon38", "彩虹");
        file2str.put("image_emoticon39", "星星月亮");
        file2str.put("image_emoticon40", "太阳");
        file2str.put("image_emoticon41", "钱币");
        file2str.put("image_emoticon42", "灯泡");
        file2str.put("image_emoticon43", "茶杯");
        file2str.put("image_emoticon44", "蛋糕");
        file2str.put("image_emoticon45", "音乐");
        file2str.put("image_emoticon46", "haha");
        file2str.put("image_emoticon47", "胜利");
        file2str.put("image_emoticon48", "大拇指");
        file2str.put("image_emoticon49", "弱");
        file2str.put("image_emoticon50", "OK");
    }

    public static Bitmap getBitmap(Context context, String s) {
        Integer integer = (Integer) emos.get(s);
        Bitmap bitmap;
        if (integer != null)
            bitmap = ImageUtil.getResBitmap(context, integer.intValue());
        else
            bitmap = null;
        return bitmap;
    }

    public static String getFaceStrByFile(String s) {
        return (String) file2str.get(s);
    }

    public static HashMap<String, Integer> getWriteEmotion() {
        return write_emos;
    }

    public static ArrayList<Integer> getWriteEmotionList() {
        return write_emos_list;
    }

    public static void parserEmotion(Context context, SpannableString spannable, String source) {
        Matcher matcher = Pattern.compile("\\[(\\w?)+\\]").matcher(source);
        while (matcher.find()) {
            String s1 = matcher.group();
            Integer integer = (Integer) write_emos.get(s1);
            if (integer != null) {
                Bitmap bitmap = ImageUtil.getResBitmap(context, integer.intValue());
                if (bitmap != null) {
                    int i = s1.length();
                    int j = matcher.start();
                    BitmapDrawable bitmapdrawable = new BitmapDrawable(
                            bitmap);
                    bitmapdrawable.setBounds(0, 0, bitmap.getWidth(),
                            bitmap.getHeight());
                    spannable.setSpan(
                            new ImageSpan(bitmapdrawable, 0), j, j + i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
    }
}
