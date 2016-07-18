package apollo.tianya.bean;

import android.text.TextUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Texel on 2016/6/2.
 */
public class Post extends Entity {

    private String title;
    private String body;
    private String author;

    private Date postDate;

    private int authorId;
    private int replies;
    private int views;
    List<String> photos;
    List<Post> comment;

    public List<Post> getComment() {
        return comment;
    }

    public void setComment(List<Post> comment) {
        this.comment = comment;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getReplies() {
        return replies;
    }

    public void setReplies(int replies) {
        this.replies = replies;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }
}
