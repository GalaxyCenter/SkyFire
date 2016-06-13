package apollo.tianya;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import apollo.tianya.bean.Post;
import apollo.tianya.util.DateTime;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void parsePosts() throws Exception {
        Document doc = Jsoup.connect("http://bbs.tianya.cn/m/post-funinfo-6960592-1.shtml").timeout(10000).get();
        Elements elms = null;
        Element bd = null;
        Element item = null;
        Post post = null;

        elms = doc.select("div.content div.item");
        for(Element elm:elms) {
            post = new Post();

            // 解析内容
            bd = elm.select(".bd").first();
            if (bd == null)
                continue;

            post.setBody(bd.text());

            // 解析时间
            item = elm.select("a p").first();
            post.setPostDate(DateTime.parse(item.text(), "yyyy-MM-dd HH:mm").getDate());

            // 解析作者
            post.setAuthor(elm.attr("data-user"));
            post.setAuthorId(Integer.parseInt(elm.select("h4").first().attr("data-id")));

            // 解析PostId
            if (elm.hasAttr("data-replyid"))
                post.setId(Integer.parseInt(elm.attr("data-replyid")));
        }

        int total_pages = 1;
        Element span = null;
        span = doc.select(".u-pager span").get(1);
        total_pages = Integer.parseInt(span.text());
    }
}