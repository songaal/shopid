package co.fastcat.shopid.db.entity;

import co.fastcat.shopid.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by swsong on 2015. 8. 17..
 */
public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static final Character CHECK_YES = 'Y';
    public static final Character CHECK_NO = 'N';

    /* General Information */
    private String id;
    private String orgId;
    private String orgName; //Organization 테이블 조인결과.
    private String name;
    private String description;

    /* Operating Plan */
    private String appContext;
    private String appFile;
    private String appFilePath;
    private Long appFileLength;
    private String appFileLengthDisplay; // human readable 수치.
    private String appFileDate;
    private String appFileChecksum;
    private String appContext2;
    private String appFile2;
    private String appFilePath2;
    private Long appFileLength2;
    private String appFileLengthDisplay2; // human readable 수치.
    private String appFileDate2;
    private String appFileChecksum2;
    private Character appFileUpdated;   //appFile이 update 되었는지 여부. context, appFileChecksum을 기존값과 비교해서 셋팅해준다.
    private Integer appFileRevision;    //docker image revision을 설정한다.
    private String environment;
    private Float cpus;
    private Integer memory;
    private String memoryDisplay;
    private Integer scale;
    private Date updateDate;
    private String updateDateDisplay;

    /* Resource Plan */
    private String resources;
    private ResourcesPlan resourcesPlan;

    /* Auto Scaling Plan */
    private Character autoScaleOutUse;
    private Character autoScaleInUse;
    private String autoScaleOutConf;
    private String autoScaleInConf;
    private AutoScaleOutConfig autoScaleOutConfig;
    private AutoScaleInConfig autoScaleInConfig;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAppContext() {
        return appContext;
    }

    public void setAppContext(String appContext) {
        this.appContext = appContext;
    }

    public String getAppFile() {
        return appFile;
    }

    public void setAppFile(String appFile) {
        this.appFile = appFile;
    }

    public String getAppFilePath() {
        return appFilePath;
    }

    public void setAppFilePath(String appFilePath) {
        this.appFilePath = appFilePath;
    }

    public Long getAppFileLength() {
        return appFileLength;
    }

    public void setAppFileLength(Long appFileLength) {
        this.appFileLength = appFileLength;
    }

    public String getAppFileLengthDisplay() {
        return appFileLengthDisplay;
    }

    public void setAppFileLengthDisplay(String appFileLengthDisplay) {
        this.appFileLengthDisplay = appFileLengthDisplay;
    }

    public String getAppFileDate() {
        return appFileDate;
    }

    public void setAppFileDate(String appFileDate) {
        this.appFileDate = appFileDate;
    }

    public String getAppFileChecksum() {
        return appFileChecksum;
    }

    public void setAppFileChecksum(String appFileChecksum) {
        this.appFileChecksum = appFileChecksum;
    }

    public String getAppContext2() {
        return appContext2;
    }

    public void setAppContext2(String appContext2) {
        this.appContext2 = appContext2;
    }

    public String getAppFile2() {
        return appFile2;
    }

    public void setAppFile2(String appFile2) {
        this.appFile2 = appFile2;
    }

    public String getAppFilePath2() {
        return appFilePath2;
    }

    public void setAppFilePath2(String appFilePath2) {
        this.appFilePath2 = appFilePath2;
    }

    public Long getAppFileLength2() {
        return appFileLength2;
    }

    public void setAppFileLength2(Long appFileLength2) {
        this.appFileLength2 = appFileLength2;
    }

    public String getAppFileLengthDisplay2() {
        return appFileLengthDisplay2;
    }

    public void setAppFileLengthDisplay2(String appFileLengthDisplay2) {
        this.appFileLengthDisplay2 = appFileLengthDisplay2;
    }

    public String getAppFileDate2() {
        return appFileDate2;
    }

    public void setAppFileDate2(String appFileDate2) {
        this.appFileDate2 = appFileDate2;
    }

    public String getAppFileChecksum2() {
        return appFileChecksum2;
    }

    public void setAppFileChecksum2(String appFileChecksum2) {
        this.appFileChecksum2 = appFileChecksum2;
    }

    public Character getAppFileUpdated() {
        return appFileUpdated;
    }

    public void setAppFileUpdated(Character appFileUpdated) {
        this.appFileUpdated = appFileUpdated;
    }

    public Integer getAppFileRevision() {
        return appFileRevision;
    }

    public void setAppFileRevision(Integer appFileRevision) {
        this.appFileRevision = appFileRevision;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
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

    public String getMemoryDisplay() {
        return memoryDisplay;
    }

    public void setMemoryDisplay(String memoryDisplay) {
        this.memoryDisplay = memoryDisplay;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateDateDisplay() {
        return updateDateDisplay;
    }

    public void setUpdateDateDisplay(String updateDateDisplay) {
        this.updateDateDisplay = updateDateDisplay;
    }

    public void fillUpdateDateDisplay(DateFormat dateFormat) {
        this.updateDateDisplay = dateFormat.format(updateDate);
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
        try{
            this.resourcesPlan = JsonUtil.json2Object(resources, ResourcesPlan.class);
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    public ResourcesPlan getResourcesPlan() {
        return resourcesPlan;
    }

    public void setResourcesPlan(ResourcesPlan resourcesPlan) {
        this.resourcesPlan = resourcesPlan;
        try{
            this.resources = JsonUtil.object2String(resourcesPlan);
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    public Character getAutoScaleOutUse() {
        return autoScaleOutUse;
    }

    public void setAutoScaleOutUse(Character autoScaleOutUse) {
        this.autoScaleOutUse = autoScaleOutUse;
    }

    public Character getAutoScaleInUse() {
        return autoScaleInUse;
    }

    public void setAutoScaleInUse(Character autoScaleInUse) {
        this.autoScaleInUse = autoScaleInUse;
    }

    public void setAutoScaleOutConf(String autoScaleOutConf) {
        this.autoScaleOutConf = autoScaleOutConf;
        try{
           this.autoScaleOutConfig = JsonUtil.json2Object(autoScaleOutConf, AutoScaleOutConfig.class);
        } catch (IOException e) {
            logger.error("", e);
        }
    }
    public String getAutoScaleOutConf() {
        return autoScaleOutConf;
    }

    public void setAutoScaleOutConfig(AutoScaleOutConfig autoScaleOutConfig) {
        this.autoScaleOutConfig  = autoScaleOutConfig;
        try{
            this.autoScaleOutConf = JsonUtil.object2String(autoScaleOutConfig);
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    public AutoScaleOutConfig getAutoScaleOutConfig() {
        return autoScaleOutConfig;
    }

    public void setAutoScaleInConf(String autoScaleInConf) {
        this.autoScaleInConf = autoScaleInConf;
        try {
            this.autoScaleInConfig = JsonUtil.json2Object(autoScaleInConf, AutoScaleInConfig.class);
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    public String getAutoScaleInConf() {
        return autoScaleInConf;
    }

    public void setAutoScaleInConfig(AutoScaleInConfig autoScaleInConfig) {
        this.autoScaleInConfig = autoScaleInConfig;
        try{
            this.autoScaleInConf = JsonUtil.object2String(autoScaleInConfig);
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    public AutoScaleInConfig getAutoScaleInConfig() {
        return autoScaleInConfig;
    }

    public static class ResourcesPlan {

        private List<ResourcePlan> planList;

        public List<ResourcePlan> getPlanList() {
            return planList;
        }

        public void setPlanList(List<ResourcePlan> planList) {
            this.planList = planList;
        }

        public void addPlan(ResourcePlan resourcePlan) {
            if(planList == null) {
                planList = new ArrayList<>();
            }
            planList.add(resourcePlan);
        }

    }

    public static class AutoScaleOutConfig {
        private Integer cpuHigher;
        private Integer cpuHigherDuring;
        private Integer addScale;

        public Integer getCpuHigher() {
            return cpuHigher;
        }

        public void setCpuHigher(Integer cpuHigher) {
            this.cpuHigher = cpuHigher;
        }

        public Integer getCpuHigherDuring() {
            return cpuHigherDuring;
        }

        public void setCpuHigherDuring(Integer cpuHigherDuring) {
            this.cpuHigherDuring = cpuHigherDuring;
        }

        public Integer getAddScale() {
            return addScale;
        }

        public void setAddScale(Integer addScale) {
            this.addScale = addScale;
        }

    }

    public static class AutoScaleInConfig {

        private Integer cpuLower;
        private Integer cpuLowerDuring;

        public Integer getCpuLower() {
            return cpuLower;
        }

        public void setCpuLower(Integer cpuLower) {
            this.cpuLower = cpuLower;
        }

        public Integer getCpuLowerDuring() {
            return cpuLowerDuring;
        }

        public void setCpuLowerDuring(Integer cpuLowerDuring) {
            this.cpuLowerDuring = cpuLowerDuring;
        }

    }
}
