package apollo.tianya.util;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Texel on 2016/6/3.
 */
public class Transforms {

    private static String[] stripHtmlTags = {"font", "b", "center", "h", "h1", "h2", "h3", "h4", "table", "tr", "td", "tbody", "th", "p", "div", "strong"};
    private static String[] stripHtmlContentTags = {"style"};

    public static String formatPost(String rawPostBody) {
        return formatPost(rawPostBody, true);
    }

    public static String formatPost(String rawPostBody, boolean enableUBB) {
        String formattedPost = rawPostBody;
        String replacement = null;

        formattedPost = formattedPost.replaceAll("(?i)<[ |/]*br[ |/]*>", "\r\n");
        formattedPost = formattedPost.replaceAll("(?i)&nbsp;", " ");
        formattedPost = formattedPost.replaceAll("\t", "");

        for (int idx = 0; idx < stripHtmlTags.length; idx++) {
            formattedPost = Transforms.stripHtmlTag(formattedPost, stripHtmlTags[idx]);
        }
        for (int idx = 0; idx < stripHtmlContentTags.length; idx++) {
            formattedPost = Transforms.stripTagContent(formattedPost, stripHtmlContentTags[idx]);
        }

        // trans iframe to link
        if (enableUBB)
            replacement = "[link=$1]$1[/link]";
        else
            replacement = "$1";
        formattedPost = formattedPost.replaceAll("(?i)(?s)<iframe.*?href=\"(.*?)\".*?>(.*?)</iframe>", replacement);

        // pickup link
        if (enableUBB)
            replacement = "[url=$1]$2[/url]";
        else
            replacement = "$1 $2";
        formattedPost = formattedPost.replaceAll("(?i)(?s)<a.*?href=\"(.*?)\".*?>(.*?)</a>", replacement);

        // pickup img
        if (enableUBB)
            replacement = "[img]$1[/img]";
        else
            replacement = "$1";
        formattedPost = formattedPost.replaceAll("(?s)<img.*?original=\"(.*?)\".*?[/]?>", replacement);

        return formattedPost;
    }

    public static String formatImage(String raw) {
        return raw.replaceAll("(?s)<img.*?original=\"(.*?)\".*?[/]?>", "<img src=\"$1\"/>");
    }

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
