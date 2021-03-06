package co.fastcat.shopid.controller.admin;

import co.fastcat.shopid.service.GarudaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * Created by swsong on 2015. 5. 11..
 */
@Controller
@RequestMapping("/a")
public class AdminController {

    @Autowired
    private GarudaService garudaService;

    @RequestMapping(value = { "", "/", "/index" }, method = RequestMethod.GET)
    public ModelAndView index() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/a/cluster");
        return mav;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("a/login");
        return mav;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView doLogin(HttpSession session, @RequestParam("userId") String userId, @RequestParam("password") String password,
                                @RequestParam(value="redirect", required=false) String redirect) {
        ModelAndView mav = new ModelAndView();
//        if(!garudaService.isAlive()) {
//            mav.setViewName("redirect:/login?e=Cannot connect to server.");
//        } else {
//            User user = new User(userId, password);
//            if(garudaService.login(user)) {
//                session.setAttribute(User.USER_KEY, user);
//                mav.setViewName("redirect:/index");
//            } else {
//                mav.setViewName("redirect:/login?e=Invalid user or password.");
//            }
//        }

        return mav;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ModelAndView logout(HttpSession session) throws Exception {
        //세션삭제를 처리한다.
        session.invalidate();
        // 로긴 화면으로 이동한다.
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/a");
        return mav;
    }

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public ModelAndView settings() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("a/settings");
        return mav;
    }

}
