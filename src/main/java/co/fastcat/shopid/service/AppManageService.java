package co.fastcat.shopid.service;

import co.fastcat.shopid.db.entity.App;
import co.fastcat.shopid.db.entity.ResourcePlan;
import co.fastcat.shopid.db.mapper.AppMapper;
import co.fastcat.shopid.util.ParseUtil;
import co.fastcat.shopid.db.entity.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by swsong on 2015. 8. 17..
 */

@Service
public class AppManageService {
    private static final Logger logger = LoggerFactory.getLogger(AppManageService.class);

    private static final String APP_UPLOAD_ROOT = "/tmp/garuda/upload";
    @Autowired
    private AppMapper appMapper;

    public List<App> getAllApps() {
        return appMapper.listAll();
    }

    public List<App> getOrgApps(String orgId) {
        return appMapper.listByOrganization(orgId);
    }
    public List<App> getOuterApps(String orgId) {
        return appMapper.listOuterApp(orgId);
    }

    public App getApp(String appId) {
        return appMapper.select(appId);
    }

    public void updateApp(App app) {
        appMapper.update(app);
    }

    public String updateApp(Map<String, Object> data) {
        App app = parseApp(data);
        String id = (String) data.get("id");
        App oldApp = appMapper.select(id);

        /*
        * 1. file이 바뀌었는지 체크한다. checksum비교.
        * */
        int revision = oldApp.getAppFileRevision();
        if(isEquals(oldApp.getAppContext(), app.getAppContext())
                && isEquals(oldApp.getAppContext2(), app.getAppContext2())
                && isEquals(oldApp.getAppFileChecksum(), app.getAppFileChecksum())
                && isEquals(oldApp.getAppFileChecksum2(), app.getAppFileChecksum2())
                && isEquals(oldApp.getEnvironment(), app.getEnvironment())
        ) {
            //같으면
            //중요: deploy를 안하고 여러번 수정했을 경우, 중간에 appFile을 바꿨으면 그대로 바꾼상태로 놔둔다.
            // 그렇지 않으면, 중간에 바꼈음에도 불구하고, 마지막에 updated가 N이 되면, 도커를 교체하지 않는 버그가 생기게 된다.
            app.setAppFileUpdated(oldApp.getAppFileUpdated());
        } else {
            //달라졌으면
            app.setAppFileUpdated(App.CHECK_YES);
            //revision 을 1증가시킨다.
            revision++;
        }
        app.setAppFileRevision(revision);
        appMapper.update(app);
        return app.getId();
    }

    private boolean isEquals(String a, String b) {
        if(a == null && b == null) {
            return true;
        }
        if(a != null && b != null) {
            return a.equals(b);
        }
        return false;
    }

    public App parseApp(Map<String, Object> data) {
        String id = (String) data.get("id");
        String orgId = (String) data.get("orgId");
        String name = (String) data.get("name");
        String description = (String) data.get("description");

        String appContext = (String) data.get("context1");
        String appFile = (String) data.get("fileName1");
        String appFilePath = (String) data.get("filePath1");
        String appFileLength = (String) data.get("fileLength1");
        String appFileDate = (String) data.get("fileDate1");
        String appFileChecksum = (String) data.get("fileChecksum1");

        String appContext2 = (String) data.get("context2");
        String appFile2 = (String) data.get("fileName2");
        String appFilePath2 = (String) data.get("filePath2");
        String appFileLength2 = (String) data.get("fileLength2");
        String appFileDate2 = (String) data.get("fileDate2");
        String appFileChecksum2 = (String) data.get("fileChecksum2");
        if(appFile2.length() == 0 || appFile2.length() == 0) {
            appContext2 = null;
        }

        String environment = (String) data.get("environment");
        String cpus = (String) data.get("cpus");
        String memory = (String) data.get("memory");
        String scale = (String) data.get("scale");

        Integer dbResourceSize = ParseUtil.parseInt(data.get("db_resource_size"));
        Integer ftpResourceSize = ParseUtil.parseInt(data.get("ftp_resource_size"));

        String autoScaleOutUse = (String) data.get("autoScaleOutUse");
        Integer cpuHigher = ParseUtil.parseInt(data.get("cpuHigher"));
        Integer cpuHigherDuring = ParseUtil.parseInt(data.get("cpuHigherDuring"));
        Integer autoScaleOutSize = ParseUtil.parseInt(data.get("autoScaleOutSize"));
        String autoScaleInUse = (String) data.get("autoScaleInUse");
        Integer cpuLower = ParseUtil.parseInt(data.get("cpuLower"));
        Integer cpuLowerDuring = ParseUtil.parseInt(data.get("cpuLowerDuring"));

        App app = new App();
        app.setId(id);
        app.setOrgId(orgId);
        app.setName(name);
        app.setDescription(description);
        //app file1
        app.setAppContext(appContext);
        app.setAppFile(appFile);
        app.setAppFilePath(appFilePath);
        app.setAppFileLength(ParseUtil.parseLong(appFileLength));
        app.setAppFileDate(appFileDate);
        app.setAppFileChecksum(appFileChecksum);
        //app file2
        if(appContext2 != null) {
            app.setAppContext2(appContext2);
            app.setAppFile2(appFile2);
            app.setAppFilePath2(appFilePath2);
            app.setAppFileLength2(ParseUtil.parseLong(appFileLength2));
            app.setAppFileDate2(appFileDate2);
            app.setAppFileChecksum2(appFileChecksum2);
        }
        //environment
        app.setEnvironment(environment);
        app.setCpus(ParseUtil.parseFloat(cpus));
        app.setMemory(ParseUtil.parseInt(memory));
        app.setScale(ParseUtil.parseInt(scale));

        /* resources plan */
        App.ResourcesPlan resourcesPlan = new App.ResourcesPlan();
        String dbPrefix = "db";
        String ftpPrefix = "ftp";
        String optionSuffix = "_option";
        for (int i = 0; i < dbResourceSize; i++){
            String key = dbPrefix + i;
            String dbId = (String) data.get(key);
            if(dbId != null) {
                String optionKey = key + optionSuffix;
                String option = (String) data.get(optionKey);
                ResourcePlan p = new ResourcePlan(dbId, option);
                resourcesPlan.addPlan(p);
            }
        }
        for (int i = 0; i < ftpResourceSize; i++){
            String key = ftpPrefix + i;
            String ftpId = (String) data.get(key);
            if(ftpId != null) {
                String optionKey = key + optionSuffix;
                String option = (String) data.get(optionKey);
                ResourcePlan p = new ResourcePlan(ftpId, option);
                resourcesPlan.addPlan(p);
            }
        }
        app.setResourcesPlan(resourcesPlan);

        /* auto scale */
        App.AutoScaleOutConfig autoScaleOutConfig = new App.AutoScaleOutConfig();
        autoScaleOutConfig.setCpuHigher(cpuHigher);
        autoScaleOutConfig.setCpuHigherDuring(cpuHigherDuring);
        autoScaleOutConfig.setAddScale(autoScaleOutSize);

        App.AutoScaleInConfig autoScaleInConfig = new App.AutoScaleInConfig();
        autoScaleInConfig.setCpuLower(cpuLower);
        autoScaleInConfig.setCpuLowerDuring(cpuLowerDuring);

        app.setAutoScaleOutUse(ParseUtil.parseChar(autoScaleOutUse));
        app.setAutoScaleInUse(ParseUtil.parseChar(autoScaleInUse));
        app.setAutoScaleOutConfig(autoScaleOutConfig);
        app.setAutoScaleInConfig(autoScaleInConfig);
        return app;
    }
    public String createApp(Map<String, Object> data) {
        App app = parseApp(data);
        appMapper.insert(app);
        return app.getId();
    }

    public void createApp(App app) {
        appMapper.insert(app);
    }

    public boolean isGranted(String orgId, String appId) {
        return appMapper.getGrant(orgId, appId) > 0;
    }

    public void setGrant(String orgId, String appId) {
        appMapper.setGrant(orgId, appId);
    }

    public void deleteApp(String appId) {
        appMapper.delete(appId);
    }

    public Resources getUsingResources(String appId, Resources allResources) {
        Resources usingResources = allResources;
        App app = getApp(appId);

        App.ResourcesPlan resourcesPlan = app.getResourcesPlan();

        /* DB Plan Check */
        Iterator<Resources.DBResource> dbIter = allResources.getDbResourceList().iterator();
        while(dbIter.hasNext()) {
            String resourceId = dbIter.next().getId();
            if(!isPlanContainsResource(resourcesPlan, resourceId)) {
                //plan에 속하지 않는 resource는 삭제한다.
                dbIter.remove();
            }
        }

        /* DB Plan Check */
        Iterator<Resources.FTPResource> ftpIter = allResources.getFtpResourceList().iterator();
        while(ftpIter.hasNext()) {
            String resourceId = ftpIter.next().getId();
            if(!isPlanContainsResource(resourcesPlan, resourceId)) {
                //plan에 속하지 않는 resource는 삭제한다.
                ftpIter.remove();
            }
        }

        return usingResources;
    }

    private boolean isPlanContainsResource(App.ResourcesPlan resourcesPlan, String resourceId) {
        for(ResourcePlan plan : resourcesPlan.getPlanList()) {
            plan.getId().equalsIgnoreCase(resourceId);
            return true;
        }
        return false;
    }

    public File saveMultipartFile(MultipartFile file, String orgId) throws IOException {

        if(file == null || file.isEmpty()) {
            return null;
        }

        File dir = new File(APP_UPLOAD_ROOT, orgId);
        dir.mkdirs();

        String fileName = file.getOriginalFilename();
        fileName = URLDecoder.decode(fileName, "utf-8");
        File f = new File(dir, fileName);
        logger.debug("App uploaded >> {}", f.getAbsolutePath());
        InputStream is = file.getInputStream();
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(f));
        try {
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) >= 0) {
                os.write(buffer, 0, len);
            }
        } finally {
            os.close();
            is.close();
        }
        return f;
    }

    public void setAppFileUpdatedDone(String appId) {
        appMapper.setAppFileUpdatedDone(appId);
    }
}
