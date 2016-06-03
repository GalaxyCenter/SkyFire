package apollo.tianya.api;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import apollo.tianya.bean.DataSet;
import apollo.tianya.bean.Thread;
import apollo.tianya.util.Transforms;

/**
 * Created by Texel on 2016/6/3.
 */
public class TianyaParser {

    /**
     * 解析推荐的帖子 (http://www.tianya.cn/m/find/index.shtml)
     *
     * @param source HTML string
     * @return
     */
    public static DataSet<Thread> parseRecommendThread(String source) {
        DataSet<Thread> datas = null;
        List<Thread> list = null;
        Thread thread = null;
        Pattern pattern = null;
        Matcher matcher = null;
        Matcher sub_matcher = null;
        String item = null;
        String match_content = null;

        list = new ArrayList<>();
        // 解析回帖信息
        pattern = Pattern.compile("(?s)<li>(.*?)</li>");
        matcher = pattern.matcher(source);
        while (matcher.find()) {
            item = matcher.group(1);
            thread = new Thread();
            list.add(thread);

            // 解析URL
            pattern = Pattern.compile("(?s)<a href=\"(.*?)\"[^>]*>");
            sub_matcher = pattern.matcher(item);
            if (sub_matcher.find()) {
                match_content = sub_matcher.group(1);
                thread.setUrl(match_content);
            }

            // 解析标题
            pattern = Pattern.compile("<h3 class=\"look-title\">(.*?)</h3>");
            sub_matcher = pattern.matcher(item);
            if (sub_matcher.find()) {
                match_content = sub_matcher.group(1);
                match_content = Transforms.stripHtmlXmlTags(match_content);
                thread.setTitle(match_content);
            }

            // 解析作者
            pattern = Pattern.compile("<span class=\"look-author\">文/(.*?)</span>");
            sub_matcher = pattern.matcher(item);
            if (sub_matcher.find()) {
                match_content = sub_matcher.group(1);
                thread.setAuthor(match_content);
            }

            // 解析板块名
            pattern = Pattern.compile("<span class=\"look-item\">(.*?)</span>");
            sub_matcher = pattern.matcher(item);
            if (sub_matcher.find()) {
                match_content = sub_matcher.group(1);
                thread.setSection(match_content);
            }

            // 解析访问量
            pattern = Pattern.compile("<span class=\"look-v-num\">(.*?)</span>");
            sub_matcher = pattern.matcher(item);
            if (sub_matcher.find()) {
                match_content = sub_matcher.group(1);
                match_content = Transforms.stripHtmlXmlTags(match_content);
                thread.setViews(Integer.parseInt(match_content));
            }
        }
        datas = new DataSet<Thread>();
        datas.setObjects(list);
        datas.setTotalRecords(list.size());
        return datas;
    }
}
