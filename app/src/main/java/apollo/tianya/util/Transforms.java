package apollo.tianya.util;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Texel on 2016/6/3.
 */
public class Transforms {

    public static String stripHtmlTag(String content, String tag) {
        //content = Regex.replace(content, "<\\s*" + tag + "\\s+([^>]*)\\s*>", "");
        content = Regex.replace(content, "(?s)(?i)<\\/{0,1}" + tag + "[^<>]*>", "");
        return content;
    }

    public static String extractTagAttribute(String content, String tag, String attName) {
        String reg = MessageFormat.format("<{0} {1}=\"(\\S*)\"([^>]*)>", tag, attName);
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(content);
        String result = "";

        if (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

    public static String stripHtmlXmlTags(String content) {
        return Regex.replace(content, "<[^>]+>", "");
    }

    public static String ascii(String content) {
        return Regex.replace(content, "[\\x00-\\xff]+", "");
    }

    public static String stripTagContent(String content, String tag) {
        return Regex.replace(content, "(?s)(?i)<" + tag + "((.|\n)*?)</" + tag + ">", "");
    }

    public static String stripScriptTags(String content) {
        content = Regex.replace(content, "(?s)(?i)<script((.|\n)*?)</script>", "");
        content = Regex.replace(content, "(?s)(?i)\"javascript:", "");
        return content;
    }

}
