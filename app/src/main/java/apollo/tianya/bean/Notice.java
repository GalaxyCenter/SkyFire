package apollo.tianya.bean;

import java.io.Serializable;

/**
 * Created by Texel on 2016/7/20.
 */
public class Notice implements Serializable {

    public final static int TYPE_REPLY = 1;
    public final static int TYPE_COMMENT = 2;
    public final static int TYPE_FOLLOW = 3;

    public final static int TYPE_MESSAGE = 4;
    public final static int TYPE_NOTIFICATION = 5;

    /**
     * 用户回复数
     */
    public int replies;

    /**
     * 用户评论数
     */
    public int comments;

    /**
     * 被@次数
     */
    public int follows;

    /**
     * 短消息数量
     */
    public int messages;

    /**
     * 系统通知数
     */
    public int notifictions;
}
