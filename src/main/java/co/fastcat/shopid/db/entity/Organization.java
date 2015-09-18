package co.fastcat.shopid.db.entity;

import java.util.Date;

/**
 * Created by swsong on 2015. 2. 2..
 */
public class Organization {
    private String id;
    private String name;
    private Date joinDate;

    public Organization(){
    }

    public Organization(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", joinDate="+ joinDate +"]";
    }
}
