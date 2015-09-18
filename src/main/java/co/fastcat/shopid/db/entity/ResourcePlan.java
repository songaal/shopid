package co.fastcat.shopid.db.entity;

/**
 * Created by swsong on 2015. 8. 17..
 */
public class ResourcePlan {
    public static final String DB_TYPE_SEPARATE_DB = "Separate DB";
    public static final String DB_TYPE_SEPARATE_Schema = "Separate Schema";
    public static final String DB_TYPE_SHARED = "Shared";
    public static final String FTP_TYPE_PRIVATE = "Private";
    public static final String FTP_TYPE_SHARED = "Shared";

    private String id;
    private String type;

    public ResourcePlan() { }

    public ResourcePlan(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
