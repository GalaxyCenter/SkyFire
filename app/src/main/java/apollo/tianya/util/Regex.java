package apollo.tianya.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Texel on 2016/6/3.
 */
public class Regex {

    private static final Pattern GRAB_SP_CHARS = Pattern.compile("([\\\\*+\\[\\](){}\\$.?\\^|])");

    public static String escape(String str) {
        Matcher match = GRAB_SP_CHARS.matcher(str);
        return match.replaceAll("\\\\$1");
    }

    public static String escRegEx(String str) {
        return str.replaceAll("([\\\\*+\\[\\](){}\\$.?\\^|])", "\\\\$1");
    }

    public static String replace(String content, String pattern, String replacement) {
        return content.replaceAll(pattern, replacement);
    }

    public static List<Map<String,Object>> getStartAndEndIndex(String source, Pattern pattern){
        List<Map<String,Object>> list = null;
        Map<String,Object> map = null;
        Matcher matcher = null;
        String match = null;
        boolean isFind = false;

        list = new ArrayList<Map<String,Object>>();
        matcher = pattern.matcher(source);
        isFind = matcher.find();
        while (isFind) {
            map = new HashMap<String, Object>();
            match = matcher.group().substring(0, matcher.group().length());
            map.put("startIndex",matcher.start());
            map.put("endIndex",matcher.end() - 1);
            map.put("match", match);
            list.add(map);
            for (int idx=1; idx<=matcher.groupCount(); idx++) {
                map.put("str" + idx, matcher.group(idx));
            }
            isFind = matcher.find((Integer)map.get("endIndex") + 1);
        }
        return list;
    }

}
