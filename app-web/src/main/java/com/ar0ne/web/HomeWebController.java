package com.ar0ne.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Web controller for index page
 */
@Controller
public class HomeWebController {

    /**
     * Get page with information about this project
     * @return ModelAndView of index page
     */
    @RequestMapping(value = {SiteEndpointUrls.INDEX, ""}, method = RequestMethod.GET)
    public ModelAndView indexPage() {
        return new ModelAndView("web/index");
    }

}
