package apollo.tianya.bean;

import android.text.TextUtils;

/**
 * Created by Texel on 2016/6/2.
 */
public class Thread extends Post {

    private String sectionName;
    private String sectionId;
    private String url;

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getUrl() {
        if (TextUtils.isEmpty(url) || url.indexOf("http") != 0)
            url = "http://bbs.tianya.cn/m/post-" + sectionId + "-" + getId() + "-1.shtml";

        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
