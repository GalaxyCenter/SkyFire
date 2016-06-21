package apollo.tianya.bean;

/**
 * Created by Texel on 2016/6/2.
 */
public class Thread extends Post {

    private String sectionName;
    private String sectionId;

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
}
