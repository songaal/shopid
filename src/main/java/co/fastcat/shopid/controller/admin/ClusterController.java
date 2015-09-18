package co.fastcat.shopid.controller.admin;

import co.fastcat.shopid.service.GarudaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by swsong on 2015. 5. 11..
 */
@Controller
@RequestMapping("/a")
public class ClusterController {

    @Autowired
    private GarudaService garudaService;

    @RequestMapping(value = "/cluster", method = RequestMethod.GET)
    public ModelAndView clusters() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("a/cluster");
        return mav;
    }

    @RequestMapping(value = "/addCluster", method = RequestMethod.GET)
    public ModelAndView addCluster() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("a/addCluster");
        return mav;
    }

    @RequestMapping(value = "/apps", method = RequestMethod.GET)
    public ModelAndView apps() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("a/apps");
        return mav;
    }

    @RequestMapping(value = "/monitoring", method = RequestMethod.GET)
    public ModelAndView monitoring() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("a/monitoring");
        return mav;
    }

}
