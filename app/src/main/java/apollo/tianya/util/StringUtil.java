package apollo.tianya.util;

/**
 * Created by kuibo on 2016/6/21.
 */
public class StringUtil {

    public static String replace(String str, int start, int end,
                                 String replacement) {
        String new_str = null;
        char[] raw_str = null;
        char[] tar_raw_str = null;
        char[] new_raw_str = null;

        raw_str = str.toCharArray();
        tar_raw_str = replacement.toCharArray();
        new_raw_str = new char[start + tar_raw_str.length + raw_str.length
                - end - 1];

        System.arraycopy(raw_str, 0, new_raw_str, 0, start);
        System.arraycopy(tar_raw_str, 0, new_raw_str, start, tar_raw_str.length);
        System.arraycopy(raw_str, end + 1, new_raw_str, start
                + tar_raw_str.length, str.length() - end - 1);

        new_str = new String(new_raw_str);
        return new_str;
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }
}
