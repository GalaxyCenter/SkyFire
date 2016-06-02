package apollo.tianya.bean;

import java.io.Serializable;

/**
 * 实体类
 *
 * Created by Texel on 2016/6/2.
 */
public abstract class Entity implements Serializable {

    private int id;
    private String guid;
    private String name;
    private String value;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
