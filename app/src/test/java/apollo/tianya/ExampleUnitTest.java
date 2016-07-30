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
import apollo.tianya.util.Transforms;

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

    @Test
    public void formatPost() {
        String source = "<br><img src=\"http://static.tianyaui.com/global/m/touch/images/loading.gif\"  original=\"http://img3.laibafile.cn/p/mh/255257041.jpg\" /><br>\n" +
                "                        \t\n" +
                "\t\t\t                    <div class=\"comments u-arrow\" data-total = \"6\">\n" +
                "\t\t\t                    \t<ul class=\"u-list-comment\">\n" +
                "\t\t\t                    \t    \n" +
                "\t                                    <li>\n" +
                "\t                                        <a href=\"http://www.tianya.cn/m/home.jsp?uid=111249560\" class=\"author fc-blue\" data-id=\"111249560\">八个中文不可修改 </a>\n" +
                "\t                                                                     \n" +
                "\t                                       \n" +
                "\t                                        <span class=\"time fc-gray\">2016-07-28 19:42</span>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t <p class=\"cnt\">@会哭的小马甲 这个标题起的好尴尬啊</p>\n" +
                "\t                                    </li>\n" +
                "\t                                    \n" +
                "\t                                    <li>\n" +
                "\t                                        <a href=\"http://www.tianya.cn/m/home.jsp?uid=106048791\" class=\"author fc-blue\" data-id=\"106048791\">gdlt2015 </a>\n" +
                "\t                                                                     \n" +
                "\t                                       \n" +
                "\t                                        <span class=\"time fc-gray\">2016-07-29 13:12</span>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t <p class=\"cnt\">呀这图好灵动，赶快下波多解解渴</p>\n" +
                "\t                                    </li>\n" +
                "\t                                    \n" +
                "\t                                    <li>\n" +
                "\t                                        <a href=\"http://www.tianya.cn/m/home.jsp?uid=22726007\" class=\"author fc-blue\" data-id=\"22726007\">抹不去的相思 </a>\n" +
                "\t                                                                     \n" +
                "\t                                       \n" +
                "\t                                        <span class=\"time fc-gray\">2016-07-29 19:42</span>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t <p class=\"cnt\">评论 <a href=\"http://www.tianya.cn/n/gdlt2015\">gdlt2015</a>：层主亮银枪，波多也解衣。</p>\n" +
                "\t                                    </li>\n" +
                "\t                                    \n" +
                "\t                                    <li>\n" +
                "\t                                        <a href=\"http://www.tianya.cn/m/home.jsp?uid=116060598\" class=\"author fc-blue\" data-id=\"116060598\">卖公主的青蛙 </a>\n" +
                "\t                                                                     \n" +
                "\t                                       \n" +
                "\t                                        <span class=\"time fc-gray\">2016-07-29 20:58</span>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t <p class=\"cnt\">现在的单身汪真多……</p>\n" +
                "\t                                    </li>\n" +
                "\t                                    \n" +
                "\t                                    <li>\n" +
                "\t                                        <a href=\"http://www.tianya.cn/m/home.jsp?uid=107687057\" class=\"author fc-blue\" data-id=\"107687057\">喝死拉倒 </a>\n" +
                "\t                                                                     \n" +
                "\t                                       \n" +
                "\t                                        <span class=\"time fc-gray\">2016-07-30 07:31</span>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t <p class=\"cnt\">一脸蒙圈.avi</p>\n" +
                "\t                                    </li>\n" +
                "\t                                    \n" +
                "\t                                    <li>\n" +
                "\t                                        <a href=\"http://www.tianya.cn/m/home.jsp?uid=107131327\" class=\"author fc-blue\" data-id=\"107131327\">天剑无名2015 </a>\n" +
                "\t                                                                     \n" +
                "\t                                       \n" +
                "\t                                        <span class=\"time fc-gray\">2016-07-30 11:38</span>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t <p class=\"cnt\">这张感觉有点像波多夜老师呀。</p>\n" +
                "\t                                    </li>\n" +
                "\t                                    \n" +
                "\t                                </ul>\n" +
                "\t                            </div>\n" +
                "\t                            \n" +
                "                        </div>\n" +
                "                        <div class=\"ft\">\n" +
                "                            <div class=\"toolbox\">\n" +
                "\t\t\t\t\t\t\t\t<a href=\"javascript:;\" class=\"at-btn\">@TA</a>\n" +
                "                                <a href=\"javascript:\" class=\"reply-btn\">评论</a>\n" +
                "                                <a href=\"post_author-funinfo-7015058-1.shtml#1\" class=\"see-ta-btn see-host-btn\">只看楼主</a>\n" +
                "\t\t\t\t                </a>\n" +
                "                            </div>\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                    \t\n" +
                "                    \n" +
                "                \n" +
                "                    \n" +
                "                    <div class=\"item item-ht item-lz\" data-replyid=\"261403738\" data-user=\"会哭的小马甲\" data-time=\"2016-07-27 23:12\" data-id=\"2\">\n" +
                "                        <div class=\"hd f-cf\">\n" +
                "                            <a class=\"info\" href=\"http://www.tianya.cn/m/home.jsp?uid=86114152\">\n" +
                "                            \t<h4 class=\"author\" data-id=\"86114152\">会哭的小马甲\n" +
                "                            \t\n" +
                "\t\t\t\t                    <span class=\"u-badge\">楼主</span>\n" +
                "\t\t\t\t                \n" +
                "                            \t</h4>\n" +
                "                            \t<p class=\"time fc-gray\">2016-07-27 23:12</p>\n" +
                "                            </a>\n" +
                "                            <span class=\"floor fc-gray\">2楼</span>                     \n" +
                "                        </div>\n" +
                "                        <div class=\"bd\">\n" +
                "                        \t<br><img src=\"http://static.tianyaui.com/global/m/touch/images/loading.gif\"  original=\"http://img3.laibafile.cn/p/mh/255257067.jpg\" /><br>\n" +
                "                        \t\n" +
                "\t\t\t                    <div class=\"comments u-arrow\" data-total = \"17\">\n" +
                "\t\t\t                    \t<ul class=\"u-list-comment\">\n" +
                "\t\t\t                    \t    \n" +
                "\t                                    <li>";

        source = Transforms.formatPost(source);

        System.out.print(source);
    }

}