package co.fastcat.shopid.controller;

import co.fastcat.shopid.db.entity.*;
import co.fastcat.shopid.entity.AppStatus;
import co.fastcat.shopid.service.AppManageService;
import co.fastcat.shopid.service.MemberService;
import co.fastcat.shopid.util.DateUtil;
import co.fastcat.shopid.util.JsonUtil;
import co.fastcat.shopid.service.GarudaService;
import co.fastcat.shopid.util.MessageDigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

/**
 * Created by swsong on 2015. 8. 18..
 */

@Controller
public class RestController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    @Autowired
    private MemberService memberService;

    @Autowired
    AppManageService appManageService;

    @Autowired
    private GarudaService garudaService;

    @RequestMapping(value = "/api/apps/{appId}", method = RequestMethod.HEAD)
    public void apps(@PathVariable String appId, HttpServletResponse response) throws IOException {
        App app = appManageService.getApp(appId);
        if (app != null) {
            response.setStatus(200);
        } else {
            response.setStatus(404, "no such app : " + appId);
        }
    }

    @RequestMapping(value = "/api/apps/{appId}/status", method = RequestMethod.GET)
    public void appStatus(@PathVariable String appId, HttpServletResponse response) throws IOException {
        AppStatus appStatus = garudaService.getAppStatus(appId);
        if(appStatus == null) {
            appStatus = new AppStatus("-", "-", "-");
        }
        response.setStatus(200);
        response.setCharacterEncoding("utf-8");
        response.getWriter().print(JsonUtil.object2String(appStatus));
    }

    @RequestMapping(value = "/api/apps/upload", method = RequestMethod.POST)
    public void uploadAppFile(@RequestParam("file") MultipartFile file, HttpServletResponse response, HttpSession session) throws IOException {
        User user = (User) session.getAttribute(User.USER_KEY);
        String orgId = user.getOrgId();
        File appFile = appManageService.saveMultipartFile(file, orgId);

        if (appFile != null) {
            // garuda에 올린다.
            String filePath = garudaService.uploadAppFile(orgId, appFile);
            if (filePath != null) {
                long length = appFile.length();
                String fileName = appFile.getName();
                String checksum = MessageDigestUtils.getMD5Checksum(appFile);
                UploadFile uploadFile = new UploadFile(fileName, filePath, length, checksum, DateUtil.getNow());
                response.setCharacterEncoding("utf-8");
                response.getWriter().print(JsonUtil.object2String(uploadFile));
                return;
            } else {
                response.sendError(500, "Cannot upload file to remote garuda server.");
            }
        } else {
            response.sendError(500, "File is empty");
        }
    }

    @RequestMapping(value = "/api/apps/{appId}/deploy", method = RequestMethod.POST)
    public void appDeploy(@PathVariable String appId, HttpServletResponse response) throws Exception {

        try {
            App app = appManageService.getApp(appId);
            if (app != null) {
                // 이미 실행중인 marathon app이 있는지 확인한다.
                AppStatus appStatus = garudaService.getAppStatus(appId);
                boolean isSuccess = false;
                if (appStatus == null) {
                    //신규실행.
                    isSuccess = garudaService.deployApp(app);
                } else {
                    //업데이트 실행.
                    Character isAppFileUpdated = app.getAppFileUpdated();
                    if(isAppFileUpdated != null && isAppFileUpdated.charValue() == App.CHECK_NO) {
                        //app 이 업데이트 되지 않았다면,
                        app.setAppContext(null);
                        app.setAppContext2(null);
                    }
                    isSuccess = garudaService.updateApp(app, true);
                }
                if (isSuccess) {
                    // 앱파일을 적용했으므로, 변경되지 않음으로 갱신한다.
                    appManageService.setAppFileUpdatedDone(appId);
                    response.setStatus(200);
                    return;
                } else {
                    response.sendError(500, "error : " + appId);
                }
            } else {
                response.sendError(404, "no such app : " + appId);
            }
        } catch (Exception e) {
            response.setStatus(500);
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(e.getMessage());
        }
    }

    @RequestMapping(value = "/api/apps/{appId}/scale/{scale}", method = RequestMethod.POST)
    public void appScale(@PathVariable String appId, @PathVariable String scale, HttpServletResponse response) throws Exception {

        try {
            int scaleInt = Integer.parseInt(scale);
            App app = new App();
            app.setId(appId);
            app.setScale(scaleInt);
            if (app != null) {
                if (garudaService.updateApp(app, true)) {
                    App dbApp = appManageService.getApp(appId);
                    dbApp.setScale(scaleInt);
                    appManageService.updateApp(dbApp);
                    response.setStatus(200);
                    return;
                } else {
                    response.sendError(500, "error : " + appId);
                }
            } else {
                response.sendError(404, "no such app : " + appId);
            }
        } catch (Exception e) {
            response.setStatus(500);
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(e.getMessage());
        }
    }

    @RequestMapping(value = "/api/apps/{appId}", method = RequestMethod.DELETE)
    public void deleteApp(@PathVariable String appId, HttpServletResponse response) throws IOException {
        ModelAndView mav = new ModelAndView();
        if (garudaService.destoryApp(appId)) {
            appManageService.deleteApp(appId);
            response.setStatus(200);
            return;
        } else {
            response.sendError(500, "no such app : " + appId);
        }
    }

    @RequestMapping(value = "/api/user/{userId:.+}", method = RequestMethod.HEAD)
    public void testUser(@PathVariable String userId, HttpServletResponse response) throws IOException {
        User user = memberService.getUser(userId);
        if (user != null) {
            response.setStatus(200);
        } else {
            response.sendError(404, "no such user : " + userId);
        }
    }

    @RequestMapping(value = "/api/organization/{orgId}", method = RequestMethod.GET)
    public void testOrganization(@PathVariable String orgId, HttpServletResponse response) throws IOException {
        Organization organization = memberService.getOrganization(orgId);
        if (organization != null) {
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(JsonUtil.object2String(organization));
        } else {
            response.sendError(404, "no such organization : " + orgId);
            return;
        }
    }

    /*
    *
    * TODO 로그인하면 결과로 사용가능 리소스정보를 넘겨준다.
    * */
    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public void loginAPI(@RequestBody String json, HttpServletResponse response) throws IOException {

        String appId = null;
        String id = null;
        String password = null;

        User user = new User();
        user.setId(id);
        user.setPassword(password);
        if (!memberService.isUserExistsWithPassword(user)) {
            response.sendError(401, "Incorrect user information : " + id);
            return;
        }
        user = memberService.getUser(id);
        String orgId = user.getOrgId();
        if (appManageService.isGranted(orgId, appId)) {
            // 사용가능한 리소스 정보를 전달.
            Resources allResources = garudaService.getResources();
            //조직별 사용할 리소스를 산출한다.
            Resources usingResources = appManageService.getUsingResources(appId, allResources);

            response.getWriter().print(JsonUtil.object2String(usingResources));
        } else {
            response.sendError(403, "App " + appId + " is not allowed to organization " + orgId);
        }

    }

    @RequestMapping(value = "/api/oauth2/token", method = RequestMethod.POST)
    public void oauth2Token(@RequestBody String json, HttpServletResponse response) throws IOException {
        //TODO
    }

    @RequestMapping(value = "/api/oauth2/authorization", method = RequestMethod.POST)
    public void oauth2Authrozation(@RequestBody String json, HttpServletResponse response) throws IOException {
        //TODO
    }

    @RequestMapping(value = "/api/subscribe/{appId}", method = RequestMethod.POST)
    @ResponseBody
    public String subscribe(@PathVariable String appId, HttpServletResponse response, HttpSession session) {
        User user = (User) session.getAttribute(User.USER_KEY);
        if(!user.getType().equals(User.ADMIN_TYPE)) {
            return "Only admin can subscribe apps.";
        }

        String orgId = user.getOrgId();
        if(appManageService.isGranted(orgId, appId)) {
            return "Already subscribed.";
        }

        appManageService.setGrant(orgId, appId);
        return "[SUCCESS] Now your organization members can use this app.";
    }

}
