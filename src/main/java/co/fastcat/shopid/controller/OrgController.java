package co.fastcat.shopid.controller;

import co.fastcat.shopid.db.entity.App;
import co.fastcat.shopid.db.entity.Organization;
import co.fastcat.shopid.db.entity.User;
import co.fastcat.shopid.service.AppManageService;
import co.fastcat.shopid.service.GarudaService;
import co.fastcat.shopid.service.MemberService;
import co.fastcat.shopid.util.DateUtil;
import co.fastcat.shopid.util.ParseUtil;
import co.fastcat.shopid.util.SizeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Map;

/**
 * Created by swsong on 2015. 8. 17..
 */

@Controller
@RequestMapping("/o")
public class OrgController {
    private static final Logger logger = LoggerFactory.getLogger(OrgController.class);

    private static final String clusterId = "test-cluster";

    @Autowired
    private AppManageService appManageService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private GarudaService garudaService;

    private User getUser(HttpSession session) {
        return (User) session.getAttribute(User.USER_KEY);
    }
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/o/apps");
        return mav;
    }

    @RequestMapping(value = "/apps", method = RequestMethod.GET)
    public ModelAndView apps(HttpSession session) {
        User user = getUser(session);
        String orgId = user.getOrgId();
        List<App> appList = appManageService.getOrgApps(orgId);
        List<App> outerAppList = appManageService.getOuterApps(orgId);
        makeUserFriendlyApp(appList);
        makeUserFriendlyApp(outerAppList);
        ModelAndView mav = new ModelAndView();
        mav.addObject("domain", garudaService.getDomainName());
        mav.addObject("appList", appList);
        mav.addObject("outerAppList", outerAppList);
        mav.setViewName("o/apps");
        return mav;
    }

    @RequestMapping(value = "/manage", method = RequestMethod.GET)
    public ModelAndView manage(HttpSession session) {
        User user = getUser(session);
        String orgId = user.getOrgId();
        List<App> appList = appManageService.getOrgApps(orgId);
        List<App> outerAppList = appManageService.getOuterApps(orgId);
        makeUserFriendlyApp(appList);
        makeUserFriendlyApp(outerAppList);
        ModelAndView mav = new ModelAndView();
        int appSize = appList != null ? appList.size() : 0;
        int outerAppSize = outerAppList != null ? outerAppList.size() : 0;
        float totalCpus = 0;
        int totalMemory = 0;

        if(appList != null) {
            for(App app : appList) {
                totalCpus += app.getCpus();
                totalMemory += app.getMemory() * app.getScale();
            }
        }
        String totalMemoryString = ParseUtil.toHumanSizeOverMB(totalMemory * 1000000L);

        mav.addObject("appSize", appSize);
        mav.addObject("outerAppSize", outerAppSize);
        mav.addObject("totalCpus", totalCpus);
        mav.addObject("totalMemory", totalMemoryString);
        mav.addObject("appList", appList);
        mav.addObject("outerAppList", outerAppList);
        mav.addObject("domain", garudaService.getDomainName());
        mav.setViewName("o/manage");
        return mav;
    }
    private void makeUserFriendlyApp(List<App> appList) {
        if(appList == null) {
            return;
        }
        // applied date "yyyy.MM.dd"
        for(App app : appList) {
            app.fillUpdateDateDisplay(DateUtil.getShortDateFormat());
        }
    }

    @RequestMapping(value = "/apps/{appId}/edit", method = RequestMethod.GET)
    public ModelAndView appEdit(@PathVariable String appId) {
        App app = appManageService.getApp(appId);
        if(app == null) {
            throw new NotFoundException();
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("domain", garudaService.getDomainName());
        mav.addObject("app", app);
        app.setAppFileLengthDisplay(ParseUtil.toHumanSize(app.getAppFileLength()));
        mav.setViewName("o/appEdit");
        return mav;
    }

    @RequestMapping(value = "/apps/{appId}/edit", method = RequestMethod.POST)
    public ModelAndView appEditUpdate(@PathVariable String appId, @RequestParam Map<String, Object> data, HttpSession session) {
        User user = getUser(session);
        String orgId = user.getOrgId();
        data.put("id", appId);
        data.put("orgId", orgId);
        appManageService.updateApp(data);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/o/apps/" + appId);
        return mav;
    }

    @RequestMapping(value = "/apps/{appId}", method = RequestMethod.GET)
    public ModelAndView appInfo(@PathVariable String appId) {
        App app = appManageService.getApp(appId);
        if(app == null) {
            throw new NotFoundException();
        }
//        String elapsed = DateUtil.getElapsedTimeDisplay(app.getApplyDate());
        ModelAndView mav = new ModelAndView();
        app.setAppFileLengthDisplay(ParseUtil.toHumanSize(app.getAppFileLength()));
        app.setAppFileLengthDisplay2(ParseUtil.toHumanSize(app.getAppFileLength2()));
        app.setMemoryDisplay(ParseUtil.toHumanSizeOverMB(app.getMemory() * SizeUnit.MB));
        mav.addObject("app", app);
        mav.addObject("domain", garudaService.getDomainName());
        mav.setViewName("o/appInfo");
        return mav;
    }

    @RequestMapping(value = "/apps/new", method = RequestMethod.GET)
    public ModelAndView appNew() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("o/appNew");
        mav.addObject("domain", garudaService.getDomainName());
        return mav;
    }


    @RequestMapping(value = "/apps", method = RequestMethod.POST)
    public ModelAndView appNewCreate(@RequestParam Map<String, Object> data, HttpSession session) {
        User user = getUser(session);
        String orgId = user.getOrgId();
        data.put("orgId", orgId);
        logger.debug("appNew data : {}", data);

        String id = appManageService.createApp(data);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/o/apps/" + id);
        return mav;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ModelAndView profile(HttpSession session) {

        User user = (User) session.getAttribute(User.USER_KEY);
        Organization organization = memberService.getOrganization(user.getOrgId());
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);

        if(user.getType().equals(User.ADMIN_TYPE)) {
            mav.addObject("userType", "Administrator");
        } else {
            mav.addObject("userType", "User");
        }
        String joinDate = DateUtil.getShortDateString(user.getJoinDate());
        mav.addObject("joinDate", joinDate);
        mav.addObject("orgName", organization.getName());
        mav.setViewName("o/profile");
        return mav;
    }

    @RequestMapping(value = "/organization", method = RequestMethod.GET)
    public ModelAndView settings(HttpSession session) {
        User user = (User) session.getAttribute(User.USER_KEY);
        Organization organization = memberService.getOrganization(user.getOrgId());
        List<User> users = memberService.getUsers(organization.getId());
        ModelAndView mav = new ModelAndView();
        String joinDate = DateUtil.getShortDateString(organization.getJoinDate());
        mav.addObject("joinDate", joinDate);
        mav.addObject("organization", organization);
        mav.addObject("users", users);
        mav.setViewName("o/organization");
        return mav;
    }
}
