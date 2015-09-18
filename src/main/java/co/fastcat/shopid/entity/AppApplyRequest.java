package co.fastcat.shopid.entity;

import co.fastcat.shopid.db.entity.App;

/**
 * Created by swsong on 2015. 8. 19..
 */
public class AppApplyRequest {
    private String id;
    private String cmd;
    private Integer revision;
    private String context;
    private String file;
    private String context2;
    private String file2;
    private String type;
    private Float cpus;
    private Integer memory;
    private Integer scale;

    public AppApplyRequest() {}

    public AppApplyRequest(App app) {
        this.id = app.getId();
        this.revision = app.getAppFileRevision();
        this.context = app.getAppContext();
        this.file = app.getAppFilePath();
        this.context2 = app.getAppContext2();
        this.file2 = app.getAppFilePath2();
        this.type = app.getEnvironment();
        this.cpus = app.getCpus();
        this.memory = app.getMemory();
        this.scale = app.getScale();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getContext2() {
        return context2;
    }

    public void setContext2(String context2) {
        this.context2 = context2;
    }

    public String getFile2() {
        return file2;
    }

    public void setFile2(String file2) {
        this.file2 = file2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getCpus() {
        return cpus;
    }

    public void setCpus(Float cpus) {
        this.cpus = cpus;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }
}
