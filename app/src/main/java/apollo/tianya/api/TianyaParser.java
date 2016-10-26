package apollo.tianya.api;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import apollo.tianya.bean.DataSet;
import apollo.tianya.bean.Message;
import apollo.tianya.bean.Notice;
import apollo.tianya.bean.Post;
import apollo.tianya.bean.Section;
import apollo.tianya.bean.Thread;
import apollo.tianya.fragment.UserPostsFragment;
import apollo.tianya.util.CookieUtil;
import apollo.tianya.util.DateTime;
import apollo.tianya.util.TLog;
import apollo.tianya.util.Transforms;
import cz.msebera.android.httpclient.cookie.Cookie;

/**
 * Created by Texel on 2016/6/3.
 */
public class TianyaParser {

    private static final String TAG = "TianyaParser";
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

            // 解析URL
            pattern = Pattern.compile("(?s)<a href=\"(http://bbs.tianya.cn/m/post-.*?-.*?-1.shtml)\"[^>]*>");
            sub_matcher = pattern.matcher(item);
            if (sub_matcher.find()) {
                match_content = sub_matcher.group(1);
                thread.setUrl(match_content);
            } else {
                continue;
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
                thread.setSectionName(match_content);
            }

            // 解析访问量
            pattern = Pattern.compile("<span class=\"look-v-num\">(.*?)</span>");
            sub_matcher = pattern.matcher(item);
            if (sub_matcher.find()) {
                match_content = sub_matcher.group(1);
                match_content = Transforms.stripHtmlXmlTags(match_content);
                if (!TextUtils.isEmpty(match_content))
                    thread.setViews(Integer.parseInt(match_content));
            }
            list.add(thread);
        }
        datas = new DataSet<Thread>();
        datas.setObjects(list);
        datas.setTotalRecords(list.size());
        return datas;
    }

    public static DataSet<Thread> parseThreads(String source) {
        DataSet<Thread> datas = null;
        Thread thread = null;
        ArrayList<Thread> threads = null;
        int idx = 0;
        boolean isFav = false;
        Pattern pattern = null;
        Matcher matcher = null;
        Matcher sub_matcher = null;
        String td_cnt = null;
        String sectionName = null;


        pattern = Pattern.compile("(?s)<title>(.*?)_天涯论坛</title>");
        matcher = pattern.matcher(source);
        if (matcher.find()) {
            sectionName = matcher.group(1);
        }

        pattern = Pattern.compile("(?s)<i class=\"collected\"></i>");
        matcher = pattern.matcher(source);
        if (matcher.find()) {
            isFav = true;
        }

        pattern = Pattern.compile("(?s)[\\<]td.*?[\\>](.*?)</td>");
        matcher = pattern.matcher(source);
        threads = new ArrayList<Thread>(80);
        datas = new DataSet<Thread>();
        datas.setObjects(threads);
        datas.setTotalRecords(Integer.MAX_VALUE);
        while (matcher.find()) {
            td_cnt = matcher.group(1);

            if (idx % 5 == 0) {
                thread = new Thread();
                thread.setSectionName(sectionName);
                thread.getSection().setFav(isFav);

                pattern = Pattern.compile("(?s)<a\\s.*?href=\"([^\"]+)\"[^>]*>(.*?)</a>");
                sub_matcher = pattern.matcher(td_cnt);
                if (sub_matcher.find()) {
                    String subject = null;
                    thread.setUrl(sub_matcher.group(1));

                    subject = Transforms.stripHtmlXmlTags(sub_matcher.group(2).replaceAll("\t", ""));
                    subject = subject.replaceAll("\r\n", "").replaceAll("\t", "");
                    thread.setTitle(subject);
                }
            } else if (idx % 5 == 1) {
                pattern = Pattern.compile("<a\\s.*?href=\"http://www.tianya.cn/([^\"]+)\"[^>]*>(.*?)</a>");
                sub_matcher = pattern.matcher(td_cnt);
                if (sub_matcher.find()) {
                    thread.setAuthorId(Integer.parseInt(sub_matcher.group(1)));
                    thread.setAuthor(sub_matcher.group(2));
                }
            } else if (idx % 5 == 2) {
                thread.setViews(Integer.parseInt(td_cnt));
            } else if (idx % 5 == 3) {
                thread.setReplies(Integer.parseInt(td_cnt));
            } else if (idx % 5 == 4) {
                td_cnt = matcher.group(0);
                pattern = Pattern.compile("<td\\s.*?title=\"([^\"]+)\"[^>]*>.*?</td>");
                sub_matcher = pattern.matcher(td_cnt);
                if (sub_matcher.find()) {
                    thread.setPostDate(DateTime.parse(sub_matcher.group(1), "yyyy-MM-dd HH:mm").getDate());
                    threads.add(thread);
                }
            }
            idx ++;
        }
        return datas;
    }

    public static DataSet<Thread> parseHotThread(String source) {
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

            pattern = Pattern.compile("post-(.*?)-(.*?)-");
            sub_matcher = pattern.matcher(thread.getUrl());
            if (sub_matcher.find()) {
                thread.setSectionId(sub_matcher.group(1));
                thread.setGuid(sub_matcher.group(2));
            }

            // 解析标题
            pattern = Pattern.compile("<p class=\"title\">(.*?)</p>");
            sub_matcher = pattern.matcher(item);
            if (sub_matcher.find()) {
                match_content = sub_matcher.group(1);
                match_content = Transforms.stripHtmlXmlTags(match_content);
                thread.setTitle(match_content);
            }
        }
        datas = new DataSet<Thread>();
        datas.setObjects(list);
        datas.setTotalRecords(list.size());
        return datas;
    }

    /**
     * 解析用户收藏
     * @param source
     * @return
     */
    public static DataSet<Thread> parseBookmarks(String source) {
        DataSet<Thread> datas = null;
        List<Thread> list = null;
        Thread thread = null;
        JSONObject json = null;
        JSONArray jarr = null;

        list = new ArrayList<Thread>();
        datas = new DataSet<Thread>();
        datas.setObjects(list);

        try {
            json = new JSONObject(source);
            json = json.getJSONObject("data");

            datas.setTotalRecords(json.getInt("total"));

            jarr = json.getJSONArray("list");
            for(int i=0; i<jarr.length(); i++) {
                json = jarr.getJSONObject(i);
                thread = new Thread();

                thread.setTitle(json.getString("title"));
                thread.setSectionId(json.getString("blockId"));
                thread.setId(json.getInt("articleId"));
                thread.setGuid(Integer.toString(json.getInt("articleId")));
                thread.setAuthor(json.getString("authorName"));
                thread.setAuthorId(json.getInt("authorId"));

                list.add(thread);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return datas;
    }

    /**
     * 解析用户收藏的板块
     * @param source
     * @return
     */
    public static DataSet<Section> parseSectionBookMarks(String source) {
        DataSet<Section> datas = null;
        List<Section> list = null;
        Section section = null;
        JSONObject json = null;
        JSONArray jarr = null;

        list = new ArrayList<Section>();
        datas = new DataSet<Section>();
        datas.setObjects(list);

        try {
            json = new JSONObject(source);
            json = json.getJSONObject("data");

            datas.setTotalRecords(json.getInt("total"));

            jarr = json.getJSONArray("items");
            for(int i=0; i<jarr.length(); i++) {
                json = jarr.getJSONObject(i);
                section = new Section();

                section.setGuid(json.getString("id"));
                section.setName(json.getString("name"));
                list.add(section);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return datas;
    }

    public static UserPostsFragment.DataSetEx parseUserPosts(String source) {
        UserPostsFragment.DataSetEx dsetex= null;
        DataSet<Thread> datas = null;
        List<Thread> list = null;
        Thread thread = null;
        JSONObject json = null;
        JSONArray jarr = null;

        list = new ArrayList<Thread>();
        datas = new DataSet<Thread>();
        datas.setObjects(list);
        datas.setTotalRecords(Integer.MAX_VALUE);
        dsetex = new UserPostsFragment.DataSetEx();

        try {
            json = new JSONObject(source);
            json = json.getJSONObject("data");

            dsetex.pubNextId = Integer.parseInt(json.getString("public_next_id"));
            dsetex.techNextId = Integer.parseInt(json.getString("tech_next_id"));
            dsetex.cityNextId = Integer.parseInt(json.getString("city_next_id"));

            jarr = json.getJSONArray("rows");
            for(int i=0; i<jarr.length(); i++) {
                json = jarr.getJSONObject(i);
                thread = new Thread();

                thread.setTitle(json.getString("title"));
                thread.setSectionId(json.getString("item"));
                thread.setId(Integer.parseInt(json.getString("art_id")));

                list.add(thread);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        dsetex.dataset = datas;
        return dsetex;
    }


    public static DataSet<Thread> parseHistory(String source) {
        DataSet<Thread> datas = null;
        List<Thread> list = null;
        Thread thread = null;
        Pattern pattern = null;
        Matcher matcher = null;
        Matcher sub_matcher = null;
        String item = null;
        String match_content = null;

        datas = new DataSet<Thread>();
        list = new ArrayList<Thread>();

        datas.setObjects(list);
        // 解析回帖信息
        pattern = Pattern.compile("(?s)<tr.*?>(.*?)</tr>");
        matcher = pattern.matcher(source);
        while (matcher.find()) {
            item = matcher.group(1);
            thread = new Thread();
            list.add(thread);

            // 栏目信息
            pattern = Pattern.compile("<a href=\"/post-(.*?)-(.*?)-1.shtml\".*?title=\"(.*?)\">");
            sub_matcher = pattern.matcher(item);
            if (sub_matcher.find()) {
                match_content = sub_matcher.group(1);
                thread.setSectionId(match_content);

                match_content = sub_matcher.group(2);
                thread.setId(Integer.parseInt(match_content));

                match_content = sub_matcher.group(3);
                thread.setTitle(match_content);
            }

            pattern = Pattern.compile("<a href=\"http://www.tianya.cn/n/(.*?)\"");
            sub_matcher = pattern.matcher(item);
            if (sub_matcher.find()) {
                match_content = sub_matcher.group(1);
                thread.setAuthor(match_content);
            }
        }
        list.remove(0);
        datas.setTotalRecords(list.size());
        return datas;
    }

    /**
     * 解析一个主题的所有帖子 (http://bbs.tianya.cn/m/post-no04-2668855-1.shtml)
     * @param source
     * @return
     */
    public static DataSet<Post> parsePosts(String source) {
        DataSet<Post> datas = null;
        Document doc = null;
        Elements elms = null;
        Elements comment_elms = null;
        Element bd = null;
        Element item = null;
        List<Node> post_items = null;
        Post post = null;
        Post comment = null;
        List<Post> list = null;

        list = new ArrayList<Post>();
        doc = Jsoup.parse(source);
        elms = doc.select("div.content div.item");
        if (elms == null || elms.size() ==0)
            return null;

        for(Element elm:elms) {
            if (list.size() == 0)
                post = new Thread();
            else
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
        post = list.get(0);

        // 解析标题
        post.setTitle(doc.select("div.title h1").text());

        // 解析帖子访问量
        int views = 0;
        item = doc.select("i.icon-view").first();
        views = Integer.parseInt(item.text());
        post.setViews(views);

        // 解析回帖数量
        int replies = 0;
        item = doc.select("i.icon-reply").first();
        replies = Integer.parseInt(item.text());
        post.setReplies(replies);

        // 解析板块信息
        Thread thread = (Thread)post;
        String url = null;

        item = doc.select("div.title a").first();
        url = item.attr("href");
        url = url.replace("list-", "").replace("-1.shtml", "");

        thread.setSectionId(url);
        thread.setSectionName(item.text());

        // 解析帖子id
        Pattern pattern = null;
        Matcher matcher = null;

        pattern = Pattern.compile("\"" + thread.getSectionId() + "-(.*?)\"");
        matcher = pattern.matcher(source);
        if (matcher.find()) {
            thread.setId(Integer.parseInt(matcher.group(1)));
        }

        // 解析页码
        int pages = 0;

        item = doc.select("i.icon-page").first();
        if (item != null) {
            String temp = item.text();

            pattern = Pattern.compile("\\d+/(\\d+)");
            matcher = pattern.matcher(source);
            if (matcher.find()) {
                temp = matcher.group(1);
                pages = Integer.parseInt(temp);
            }

            temp = temp.replace("1/", "");
            pages = Integer.parseInt(temp);
        }

        datas = new DataSet<Post>();
        datas.setObjects(list);
        datas.setTotalRecords(pages * 20);
        return datas;
    }

    /**
     * 简单解析一个页面内容
     * @param source
     * @return
     */
    public static Post parsePage(String source) {
        Post post = new Post();
        Document doc = null;

        doc = Jsoup.parse(source);
        post.setBody(doc.select("body").text());
        post.setPostDate(new Date());
        return post;
    }

    /**
     * 从页面上解析出用户Id
     * @param source
     * @return
     */
    public static int parseUserId(String source) {
        int userId = 0;
        Pattern pattern = null;
        Matcher matcher = null;
        String match_content = null;

        pattern = Pattern.compile("(?s)\"http://tx.tianyaui.com/logo/(.*?)\"");
        matcher = pattern.matcher(source);
        if (matcher.find()) {
            match_content = matcher.group(1);
            userId = Integer.parseInt(match_content);
        }
        return userId;
    }

    /**
     * 解析一个主贴的图片内容 (http://bbs.tianya.cn/m/post-no04-2668855-1.shtml)
     * @param source
     * @return
     */
    public static List<String> parseThreadImage(String source) {
        List<String> list = null;
        Document doc = null;
        Elements elms = null;

        doc = Jsoup.parse(source);
        elms = doc.select("div.item-zt img");
        list = new ArrayList<String>();
        for(Element elm:elms) {
            list.add(elm.attr("original"));
        }
        return list;
    }

    /**
     * 解析内容中的第一张图片
     * @param source
     * @return
     */
    public static String parseImage(String source) {
        String src = null;
        Document doc = null;
        Element elm = null;

        doc = Jsoup.parse(source);
        elm = doc.select("img").first();
        if (elm != null)
            src = elm.attr("src");

        return src;
    }

    /**
     * 解析Cookie中用户id
     * @param cookies
     * @return
     */
    public static int parseCookieUserId(String cookies) {
        Cookie[] _cookies = CookieUtil.parse(cookies);
        String userId = CookieUtil.getCookieValue(_cookies, "user", "id");

        return userId.equals("") ? 0 : Integer.parseInt(userId);
    }

    /**
     * 解析用户通知
     * @param source
     * @return
     */
    public static Notice parseNotices(String source) {
        Notice n = null;
        JSONObject json = null;

        try {
            n = new Notice();
            json = new JSONObject(source);
            json = json.getJSONObject("data");

            n.comments = Integer.parseInt(json.getString("comment_notice_count"));
            n.replies = Integer.parseInt(json.getString("reply_notice_count"));
            n.follows = Integer.parseInt(json.getString("attention_notice_count"));

        } catch (JSONException e) {
            TLog.error(e.getMessage());
        }
        return n;
    }

    /**
     * 解析用户通知
     * @param source
     * @return
     */
    public static Notice parseNoticesEx(String source) {
        Notice n = null;
        JSONObject json = null;

        try {
            n = new Notice();
            json = new JSONObject(source);
            json = json.getJSONObject("data");

            n.messages = json.getInt("userCount");
            n.notifictions = json.getInt("sysCount");

        } catch (JSONException e) {
        }
        return n;
    }

    public static boolean parseJsonResult(String source) {
        JSONObject json = null;
        int success = 0;

        try {
            json = new JSONObject(source);
            success = Integer.parseInt(json.getString("success"));
        } catch (JSONException e) {
            TLog.error(e.getMessage());
        }
        return success == 1;
    }

    public static DataSet<Thread> parseUserReplies(String source) {
        JSONObject json = null;
        JSONArray jarr = null;
        DataSet<Thread> datas = null;
        List<Thread> posts = null;
        Thread thread = null;
        Post comment = null;

        posts = new ArrayList<Thread>();
        datas = new DataSet<Thread>();
        datas.setObjects(posts);
        try {
            json = new JSONObject(source);
            json = json.getJSONObject("data");
            jarr = json.getJSONArray("rows");

            for(int i=0; i<jarr.length(); i++) {
                thread = new Thread();
                json = jarr.getJSONObject(i);

                thread.setSectionId(json.getString("item"));
                thread.setId(Integer.parseInt(json.getString("article_id")));

                thread.setTitle(json.getString("title"));

                thread.setComment(new ArrayList<Post>());
                comment = new Post();
                comment.setId(Integer.parseInt(json.getString("reply_id")));
                comment.setBody(json.getString("message"));
                comment.setPostDate(DateTime.parse(json.getString("reply_time"), "yyyy-MM-dd HH:mm").getDate());
                comment.setAuthor(json.getString("reply_user_name"));
                comment.setAuthorId(Integer.parseInt(json.getString("reply_user_id")));
                comment.setFloor(Integer.parseInt(json.getString("floor")));
                thread.getComment().add(comment);

                posts.add(thread);
            }
        } catch (JSONException e) {
            TLog.error(e.getMessage());
        }
        return datas;
    }

    public static DataSet<Thread> parseUserComments(String source) {
        JSONObject json = null;
        JSONArray jarr = null;
        DataSet<Thread> datas = null;
        List<Thread> posts = null;
        Thread thread = null;
        Post comment = null;

        posts = new ArrayList<Thread>();
        datas = new DataSet<Thread>();
        datas.setObjects(posts);
        try {
            json = new JSONObject(source);
            json = json.getJSONObject("data");
            jarr = json.getJSONArray("rows");

            for(int i=0; i<jarr.length(); i++) {
                thread = new Thread();
                json = jarr.getJSONObject(i);

                thread.setSectionId(json.getString("item"));
                thread.setId(Integer.parseInt(json.getString("article_id")));

                thread.setTitle(json.getString("title"));
                thread.setBody(json.getString("source_message"));
                thread.setAuthor(json.getString("reply_user_name"));
                thread.setAuthorId(Integer.parseInt(json.getString("reply_user_id")));

                thread.setComment(new ArrayList<Post>());

                comment = new Post();
                comment.setId(Integer.parseInt(json.getString("reply_id")));
                comment.setBody(json.getString("message"));
                comment.setPostDate(DateTime.parse(json.getString("comment_time"), "yyyy-MM-dd HH:mm").getDate());
                comment.setAuthor(json.getString("comment_user_name"));
                comment.setAuthorId(Integer.parseInt(json.getString("comment_user_id")));
                comment.setFloor(Integer.parseInt(json.getString("floor")));
                thread.getComment().add(comment);

                posts.add(thread);
            }
        } catch (JSONException e) {
            TLog.error(e.getMessage());
        }
        return datas;
    }

    public static DataSet<Thread> parseUserFollows(String source) {
        JSONObject json = null;
        JSONArray jarr = null;
        DataSet<Thread> datas = null;
        List<Thread> posts = null;
        Thread thread = null;
        Post comment = null;

        posts = new ArrayList<Thread>();
        datas = new DataSet<Thread>();
        datas.setObjects(posts);
        try {
            json = new JSONObject(source);
            json = json.getJSONObject("data");
            jarr = json.getJSONArray("rows");

            for(int i=0; i<jarr.length(); i++) {
                thread = new Thread();
                json = jarr.getJSONObject(i);

                thread.setSectionId(json.getString("item"));
                thread.setId(Integer.parseInt(json.getString("article_id")));

                thread.setTitle(json.getString("title"));

                thread.setComment(new ArrayList<Post>());

                comment = new Post();
                comment.setId(Integer.parseInt(json.getString("reply_id")));
                comment.setBody(json.getString("message"));
                comment.setPostDate(DateTime.parse(json.getString("attention_time"), "yyyy-MM-dd HH:mm").getDate());
                comment.setAuthor(json.getString("attention_user_name"));
                comment.setAuthorId(Integer.parseInt(json.getString("attention_user_id")));
                comment.setFloor(Integer.parseInt(json.getString("floor")));
                thread.getComment().add(comment);

                posts.add(thread);
            }
        } catch (JSONException e) {
            TLog.error(e.getMessage());
        }
        return datas;
    }

    public static DataSet<Message> parseMessages(String source) {
        JSONObject json = null;
        JSONArray jarr = null;
        DataSet<Message> datas = null;
        List<Message> msgs = null;
        Message msg = null;
        Post comment = null;

        msgs = new ArrayList<Message>();
        datas = new DataSet<Message>();
        datas.setObjects(msgs);
        try {
            json = new JSONObject(source);
            json = json.getJSONObject("data");
            datas.setTotalRecords(json.getInt("total"));
            jarr = json.getJSONArray("list");

            for(int i=0; i<jarr.length(); i++) {
                msg = new Message();
                json = jarr.getJSONObject(i);

                msg.setGuid(json.getString("messageId"));
                msg.setSenderId(json.getInt("otherUserId"));
                msg.setSenderName(json.getString("otherUserName"));

                msg.setReceiverId(json.getInt("userId"));
                msg.setReceiverName(json.getString("userName"));

                msg.setPostDate(DateTime.parse(json.getString("createDate"), "yyyy-MM-dd HH:mm").getDate());

                msg.setValue(json.getString("content"));

                msgs.add(msg);
            }
        } catch (JSONException e) {
            TLog.error(e.getMessage());
        }
        return datas;
    }

    public static DataSet<Message> parseNotifications(String source) {
        JSONObject json = null;
        JSONArray jarr = null;
        DataSet<Message> datas = null;
        List<Message> msgs = null;
        Message msg = null;
        Post comment = null;

        msgs = new ArrayList<Message>();
        datas = new DataSet<Message>();
        datas.setObjects(msgs);
        try {
            json = new JSONObject(source);
            json = json.getJSONObject("data");
            datas.setTotalRecords(json.getInt("total"));

            jarr = json.getJSONArray("list");

            for(int i=0; i<jarr.length(); i++) {
                msg = new Message();
                json = jarr.getJSONObject(i);

                msg.setGuid(json.getString("id"));
                msg.setSenderId(json.getInt("fromUserId"));
                msg.setSenderName(json.getString("fromUserName"));

                msg.setReceiverId(json.getInt("toUserId"));
                msg.setReceiverName(json.getString("toUserName"));

                msg.setPostDate(DateTime.parse(json.getString("createDate"), "yyyy-MM-dd HH:mm").getDate());

                msg.setValue(json.getString("content"));

                msgs.add(msg);
            }
        } catch (JSONException e) {
            TLog.error(e.getMessage());
        }
        return datas;
    }

    public static Thread parseThreadUrl(String url) {
        Thread thread = null;
        Pattern pattern = null;
        Matcher matcher = null;

        pattern = Pattern.compile("post-(.*?)-(.*?)-");
        matcher = pattern.matcher(url);
        if (matcher.find()) {
            thread = new Thread();

            thread.setSectionId(matcher.group(1));
            thread.setId(Integer.parseInt(matcher.group(2)));
            thread.setGuid(matcher.group(2));
        }
        return thread;
    }

    public static Section parseSectionUrl(String url) {
        Section section = null;
        Pattern pattern = null;
        Matcher matcher = null;

        pattern = Pattern.compile("(?s)list-(.*?)-1.shtml");
        matcher = pattern.matcher(url);
        if (matcher.find()) {
            section = new Section();

            section.setGuid(matcher.group(1));
        }
        return section;
    }


    /**
     * 解析板块信息
     * @param source
     * @return
     */
    public static Section parseSection(String source) {
        Section section = null;
        Pattern pattern = null;
        Matcher matcher = null;

        pattern = Pattern.compile("(?s)<title>(.*?)_天涯论坛</title>");
        matcher = pattern.matcher(source);
        if (matcher.find()) {
            section.setName(matcher.group(1));
        }

        pattern = Pattern.compile("(?s)<link.*?href=\"http://bbs.tianya.cn/list-(.*?)-1.shtml\"[^>]*>");
        matcher = pattern.matcher(source);
        if (matcher.find()) {
            section.setGuid(matcher.group(1));
        }

        pattern = Pattern.compile("(?s)<i class=\"collected\"></i>");
        matcher = pattern.matcher(source);
        if (matcher.find()) {
            section.setFav(true);
        } else {
            section.setFav(false);
        }
        return section;
    }

    /**
     * 解析搜索结果
     * @param source
     * @return
     */
    public static DataSet<Thread> parseSearchThreads(String source) {
        DataSet<Thread> datas = null;
        List<Thread> list = null;
        Thread thread = null;
        Document doc = null;
        Elements elms = null;
        Elements comment_elms = null;
        Element bd = null;
        Element item = null;
        String temp = null;
        Pattern pattern = null;
        Matcher matcher = null;

        doc = Jsoup.parse(source);
        elms = doc.select("div.searchListOne ul li");
        if (elms == null || elms.size() ==0)
            return null;

        list = new ArrayList<>();
        datas = new DataSet<Thread>();
        datas.setObjects(list);
        for(Element elm:elms) {
            thread = new Thread();

            // 解析标题
            item = elm.select("div h3 a").first();
            if (item == null)
                continue;

            thread.setTitle(item.text());
            // 解析帖子和板块id
            temp = item.attr("href");
            pattern = Pattern.compile("post-(.*?)-(.*?)-");
            matcher = pattern.matcher(temp);
            if (matcher.find()) {
                thread.setSectionId(matcher.group(1));
                thread.setGuid(matcher.group(2));
            }

            // 解析摘要内容
            item = elm.select("p.source a").first();
            thread.setBody(item.text());

            // 解析板块内容
            item = elm.select("p.source a").get(0);
            temp = item.attr("href");
            thread.setSectionName(item.text());


            // 解析作者
            item = elm.select("p.source a").get(1);
            thread.setAuthor(item.text());

            temp = item.attr("href");
            pattern = Pattern.compile("http://www.tianya.cn/(\\d+)");
            matcher = pattern.matcher(temp);
            if (matcher.find()) {
                thread.setAuthorId(Integer.parseInt(matcher.group(1)));
            }

            // 解析发帖时间
            item = elm.select("p.source span").get(0);
            thread.setPostDate(DateTime.parse(item.text(), "yyyy-MM-dd HH:mm").getDate());

            item = elm.select("p.source span").get(1);
            thread.setReplies(Integer.parseInt(item.text()));

            list.add(thread);
        }

        item = doc.select("div.long-pages em").first();
        pattern = Pattern.compile("共有(.*?) 条内容");
        matcher = pattern.matcher(item.text());
        if (matcher.find()) {
            datas.setTotalRecords(Integer.parseInt(matcher.group(1)));
        }
        return datas;
    }

}
