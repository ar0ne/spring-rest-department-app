package com.ar0ne.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeWebController {

    @RequestMapping(value = {SiteEndpointUrls.INDEX, ""}, method = RequestMethod.GET)
    public ModelAndView indexPage() {

        ModelAndView view = new ModelAndView("web/index");

        return view;
    }

}
