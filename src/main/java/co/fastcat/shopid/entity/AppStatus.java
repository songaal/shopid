package co.fastcat.shopid.entity;

/**
 * Created by swsong on 2015. 8. 22..
 */
public class AppStatus {
    public static final String STATUS_OK = "OK";
    public static final String STATUS_OFF = "OFF";
    public static final String STATUS_SCALE = "SCALE";
    private String status;
    private String elapsed;
    private String scale;

    public AppStatus(String status, String elapsed, String scale) {
        this.status = status;
        this.elapsed = elapsed;
        this.scale = scale;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getElapsed() {
        return elapsed;
    }

    public void setElapsed(String elapsed) {
        this.elapsed = elapsed;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }
}
