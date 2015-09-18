package co.fastcat.shopid.db.entity;

import java.io.Serializable;
import java.util.Date;

public class User {

	public static final String USER_KEY = "_user";
    public static final String ADMIN_TYPE = "A";
	public static final String USER_TYPE = "U";

	private String id;
	
	private String password;
	
	private String orgId;

	private String type;

	private Date joinDate;

    public User(){
    }

    public User(String id, String orgId, String type) {
        this.id = id;
        this.orgId = orgId;
        this.type = type;
    }

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", orgId=" + orgId + ", type="+ type + ", joinDate="+ joinDate +"]";
	}
}
