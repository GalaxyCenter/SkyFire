package apollo.tianya;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import apollo.tianya.bean.Post;
import apollo.tianya.util.DateTime;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void parsePosts() throws Exception {
        Document doc = Jsoup.connect("http://bbs.tianya.cn/m/post-free-5500114-1.shtml").timeout(10000).get();
        Elements elms = null;
        Elements comment_elms = null;
        Element item = null;
        Element bd = null;
        Post post = null;
        Post comment = null;
        List<Post> list = null;

        list = new ArrayList<Post>();
        elms = doc.select("div.content div.item");
        for(Element elm:elms) {
            post = new Post();

            bd = elm.select(".bd").first();
            if (bd == null)
                continue;

            // 解析评论
            comment_elms = elm.select(".bd .comments li");
            post.setComment(new ArrayList<Post>());
            for(Element celm:comment_elms) {
                comment = new Post();

                // 解析内容
                comment.setBody(celm.select(".cnt").text());

                // 解析作者
                item = celm.select(".author").first();
                comment.setAuthor(item.text());
                comment.setAuthorId(Integer.parseInt(item.attr("data-id")));

                // 解析时间
                item = celm.select(".time").first();
                comment.setPostDate(DateTime.parse(item.text(), "yyyy-MM-dd HH:mm").getDate());

                post.getComment().add(comment);
            }
            // 解析内容
            if (bd.children().size() != 0) {
               bd.select("div.comments").remove();
            }
            post.setBody(bd.html());

            // 解析时间
            item = elm.select("a p").first();
            post.setPostDate(DateTime.parse(item.text(), "yyyy-MM-dd HH:mm").getDate());

            // 解析作者
            post.setAuthor(elm.attr("data-user"));
            post.setAuthorId(Integer.parseInt(elm.select("h4").first().attr("data-id")));

            // 解析PostId
            if (elm.hasAttr("data-replyid"))
                post.setId(Integer.parseInt(elm.attr("data-replyid")));

            list.add(post);
        }

        int total_pages = 1;
        Element span = null;
        span = doc.select(".u-pager span").get(1);
        total_pages = Integer.parseInt(span.text());
    }
}