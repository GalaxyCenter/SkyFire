package apollo.tianya.bean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Texel on 2016/6/2.
 */
public class Thread extends Post {

    private Section section = new Section();
    private String url;

    public String getSectionId() {
        return section.getGuid();
    }

    public void setSectionId(String sectionId) {
        this.section.setGuid(sectionId);
    }

    public String getSectionName() {
        return section.getName();
    }

    public void setSectionName(String sectionName) {
        this.section.setName(sectionName);
    }

    public Section getSection() {
        return section;
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

            this.url = "http://bbs.tianya.cn/m/post-" + getSectionId() + "-" + getId() + "-1.shtml";
        }
    }
}
