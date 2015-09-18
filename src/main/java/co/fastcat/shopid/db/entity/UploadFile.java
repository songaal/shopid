package co.fastcat.shopid.db.entity;

/**
 * Created by swsong on 2015. 8. 17..
 */
public class UploadFile {
    private String name;
    private String path;
    private long length;
    private String checksum;
    private String date;

    public UploadFile() {}
    public UploadFile(String name, String path, long length, String checksum, String date) {
        this.name = name;
        this.path = path;
        this.length = length;
        this.checksum = checksum;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
