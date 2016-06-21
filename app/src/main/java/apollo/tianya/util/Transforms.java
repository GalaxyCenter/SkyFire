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
        List<Map<String, Object>> list = null;
        Map<String, Object> map = null;
        String str = null;
        String regex_str = null;
        String img_src_regex = null;

        formattedPost = formattedPost.replaceAll("(?i)<[ |/]*br[ |/]*>", "\r\n");
        formattedPost = formattedPost.replaceAll("(?i)&nbsp;", " ");
        for (int idx = 0; idx < stripHtmlTags.length; idx++) {
            formattedPost = Transforms.stripHtmlTag(formattedPost, stripHtmlTags[idx]);
        }
        for (int idx = 0; idx < stripHtmlContentTags.length; idx++) {
            formattedPost = Transforms.stripTagContent(formattedPost, stripHtmlContentTags[idx]);
        }
        // trans iframe to link
        regex_str = "(?i)<iframe[\\s][^>]*src\\s*=\\s*[\\\"\\']?([^\\\"\\'>\\s]*)[\\\"\\']?[^>]*></iframe>";
        list = Regex.getStartAndEndIndex(formattedPost, Pattern.compile(regex_str));

        for (int idx = list.size() - 1; idx >= 0; idx--) {
            map = list.get(idx);
            str = (String) map.get("str1");
            //str = "<a href=\"" + str + "\">" + ApolloApplication.app().getString(R.string.web_url) + "</a>";

            if (enableUBB)
                str = "[link]" + str + "[/link]";

            formattedPost = StringUtil.replace(formattedPost,
                    (Integer) map.get("startIndex"),
                    (Integer) map.get("endIndex"), str);
        }

        regex_str = "(?i)<a[\\s][^>]*href\\s*=\\s*[\\\"\\']?([^\\\"\\'>\\s]*)[\\\"\\']?[^>]*>(.*?)</a>";
        list = Regex.getStartAndEndIndex(formattedPost, Pattern.compile(regex_str, Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
        String link_content = null;
        String image = null;
        for (int idx = list.size() - 1; idx >= 0; idx--) {
            map = list.get(idx);
            str = (String) map.get("str1");
            link_content = (String) map.get("str2");

            link_content = Regex.replace(link_content, "\n", "");
            image = Transforms.formatImage(link_content);
            link_content = Transforms.stripHtmlXmlTags(link_content);
            image = image.replace(link_content, "");

            if (link_content == "") {
                link_content = "#link#";
            }
            //str = "<a href=\"" + str + "\">" + link_content + "</a>\r\n" + image;
            if (enableUBB)
                str = "[link]" + str + "[/link]" + image;

            formattedPost = StringUtil.replace(formattedPost,
                    (Integer) map.get("startIndex"),
                    (Integer) map.get("endIndex"), str);
        }

        // pickup img
        Pattern pattern = null;
        Matcher matcher = null;
        String img_src = null;
        String img_orig = null;
        String img_match = null;

        regex_str = "(?s)<[img|IMG].*?>";
        img_src_regex = "(?s)(src|original)=[\'|\"](.*?)[\'|\"]";

        list = Regex.getStartAndEndIndex(formattedPost, Pattern.compile(regex_str, Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
        for (int idx = list.size() - 1; idx >= 0; idx--) {
            map = list.get(idx);
            img_match = (String)map.get("match");

            pattern = Pattern.compile(img_src_regex);
            matcher = pattern.matcher(img_match);
            while(matcher.find())
                map.put(matcher.group(1), matcher.group(2));

            img_src = (String)map.get("src");
            img_orig = (String)map.get("original");

            if (img_orig != null)
                img_src = img_orig;
            if (enableUBB)
                img_src = "[img]" + img_src + "[/img]";

            formattedPost = StringUtil.replace(formattedPost,
                    (Integer) map.get("startIndex"),
                    (Integer) map.get("endIndex"), img_src);
        }
        formattedPost = formattedPost.replaceAll("\t", "");
        return formattedPost;
    }

    public static String formatImage(String raw) {
        Pattern pattern = null;
        Matcher matcher = null;
        String regex_img = null;
        String str = null;
        String regex_str = null;
        String formatted = raw;
        List<Map<String, Object>> list = null;
        Map<String, Object> map = null;


        //regex_str = "(?s)(?i)<img.*?>";
        regex_str = "(?s)(?i)<img[^>]+";
        regex_img = "(src|original)=[\'|\"](.*?(?:[.(jpg|bmp|jpeg|gif|png)]))[\'|\"].*?[/]?";
        list = Regex.getStartAndEndIndex(formatted, Pattern.compile(regex_str));
        for (int idx=0; idx<list.size(); idx++) {
            map = list.get(idx);

            str = (String)map.get("match");

            pattern = Pattern.compile(regex_img);
            matcher = pattern.matcher(str);
            while(matcher.find()) {
                map.put(matcher.group(1), matcher.group(2));
            }
        }
        for (int idx = list.size() - 1; idx >= 0; idx--) {
            map = list.get(idx);
            str = (String)map.get("src");

            //if (TextUtils.isEmpty(str) == true) {
            if (str == "") {
                str = (String)map.get("original");
            }
            //if (TextUtils.isEmpty(str) == true) {
            if (str == "" || str == null) {
                continue;
            }
            str = "<img src=\"" + str.trim() + "\"/>";
            formatted = StringUtil.replace(formatted,
                    (Integer) map.get("startIndex"),
                    (Integer) map.get("endIndex"), str);
        }

        return formatted;
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
