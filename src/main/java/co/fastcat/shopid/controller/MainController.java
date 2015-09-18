package co.fastcat.shopid.controller;

import co.fastcat.shopid.db.entity.App;
import co.fastcat.shopid.db.entity.Organization;
import co.fastcat.shopid.db.entity.User;
import co.fastcat.shopid.service.AppManageService;
import co.fastcat.shopid.service.MemberService;
import co.fastcat.shopid.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by swsong on 2015. 8. 16..
 */

@Controller
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private MemberService memberService;

    @Autowired
    private AppManageService appManageService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView root() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/store");
        return mav;
    }

    @RequestMapping(value = "/store", method = RequestMethod.GET)
    public ModelAndView viewStore() {
        List<App> appList = appManageService.getAllApps();
        makeUserFriendlyApp(appList);
        ModelAndView mav = new ModelAndView();
        mav.addObject("appList", appList);
        mav.setViewName("store");
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
    @RequestMapping(value = "/signUp", method = RequestMethod.GET)
    public ModelAndView signUp() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("signUp");
        return mav;
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public ModelAndView doSignUp(@RequestParam String orgId, @RequestParam String orgName, @RequestParam String userId
            , @RequestParam String password) {
        ModelAndView mav = new ModelAndView();

        //0. 사전점검.
        User user = memberService.getUser(userId);
        if(user != null) {
            //이메일이 중복이면, 다시 등록화면.
            mav.setViewName("redirect:/signUp");
            return mav;
        }

        //1. org 등록.

        mav.setViewName("index");

        boolean isAdmin = false;
        Organization organization = memberService.getOrganization(orgId);
        if(organization == null) {
            organization = new Organization(orgId, orgName);
            memberService.addOrganization(organization);
            isAdmin = true;
        }

        //2. id 등록.
        user = new User(userId, orgId, isAdmin? User.ADMIN_TYPE : User.USER_TYPE);
        user.setPassword(password);
        memberService.addUser(user);
        mav.setViewName("redirect:/login");
        return mav;
    }

    @RequestMapping(value = "/account/{userId}/delete", method = RequestMethod.POST)
    public ModelAndView deleteAccount(@PathVariable String userId, HttpSession session) {
        ModelAndView mav = new ModelAndView();

        // 관리자이면 삭제가 안됨. 다른 사람을 관리자로 먼저 지정필요.
        User user = memberService.getUser(userId);
        if(user.getType().equals(User.ADMIN_TYPE)) {
            //삭제 불가메시지.
            mav.setViewName("redirect:/o/profile?message=Cannot delete admin account.");
        } else {
            memberService.deleteUser(userId);
            session.invalidate();
            mav.setViewName("redirect:/login");
        }
        return mav;
    }

    @RequestMapping(value = "/organization/{orgId}/delete", method = RequestMethod.POST)
    public ModelAndView deleteOrganization(@PathVariable String orgId, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        memberService.deleteOrganization(orgId);
        session.invalidate();
        mav.setViewName("redirect:/login");
        return mav;
    }


        @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpSession session) {
        ModelAndView mav = new ModelAndView();
        session.invalidate();
        mav.setViewName("redirect:/login");
        return mav;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        return mav;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView doLogin(HttpServletRequest request, @RequestParam String userId, @RequestParam String password
            , @RequestParam(value="redirect", required=false) String redirect) {
        ModelAndView mav = new ModelAndView();
        User user = new User();
        user.setId(userId);
        user.setPassword(password);
        // 로그인 처리 한다.
        if(!memberService.isUserExistsWithPassword(user)) {
            String target = request.getRequestURI();
            String queryString = request.getQueryString();
            if(queryString != null && queryString.length() > 0){
                target += ("?" + queryString);
            }
            mav.setViewName("redirect:" + target);
        } else {
            user = memberService.getUser(userId);
            HttpSession session = request.getSession(true);
            session.setAttribute(User.USER_KEY, user);
            if(redirect != null && redirect.length() > 0){
                mav.setViewName("redirect:"+redirect);
            }else{
                mav.setViewName("redirect:/index");
            }
        }
        return mav;
    }

}
