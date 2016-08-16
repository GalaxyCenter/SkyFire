package apollo.tianya.bean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        return url;
    }

    public void setUrl(String url) {
        Pattern pattern = null;
        Matcher matcher = null;

        pattern = Pattern.compile("post-(.*?)-(.*?)-1");
        matcher = pattern.matcher(url);
        if (matcher.find()) {
            setSectionId(matcher.group(1));
            setId(Integer.parseInt(matcher.group(2)));
            setGuid(matcher.group(2));

            this.url = "http://bbs.tianya.cn/m/post-" + sectionId + "-" + getId() + "-1.shtml";
        }
    }
}
