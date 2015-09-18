package co.fastcat.shopid.db.entity;

import java.util.List;

/**
 * Created by swsong on 2015. 8. 17..
 */
public class Resources {

    private List<DBResource> dbResourceList;
    private List<FTPResource> ftpResourceList;

    public List<DBResource> getDbResourceList() {
        return dbResourceList;
    }

    public void setDbResourceList(List<DBResource> dbResourceList) {
        this.dbResourceList = dbResourceList;
    }

    public List<FTPResource> getFtpResourceList() {
        return ftpResourceList;
    }

    public void setFtpResourceList(List<FTPResource> ftpResourceList) {
        this.ftpResourceList = ftpResourceList;
    }

    public static class DBResource {

        private String id;
        private String vendor;
        private String ipAddress;
        private int port;
        private String db;
        private String schema;
        private String userId;
        private String userPassword;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getVendor() {
            return vendor;
        }

        public void setVendor(String vendor) {
            this.vendor = vendor;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getDb() {
            return db;
        }

        public void setDb(String db) {
            this.db = db;
        }

        public String getSchema() {
            return schema;
        }

        public void setSchema(String schema) {
            this.schema = schema;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserPassword() {
            return userPassword;
        }

        public void setUserPassword(String userPassword) {
            this.userPassword = userPassword;
        }
    }

    public static class FTPResource {
        private String id;
        private String vendor;
        private String ipAddress;
        private int port;
        private String userId;
        private String userPassword;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getVendor() {
            return vendor;
        }

        public void setVendor(String vendor) {
            this.vendor = vendor;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserPassword() {
            return userPassword;
        }

        public void setUserPassword(String userPassword) {
            this.userPassword = userPassword;
        }
    }
}
