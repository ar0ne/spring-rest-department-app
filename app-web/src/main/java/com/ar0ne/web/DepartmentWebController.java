package com.ar0ne.web;

import com.ar0ne.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class DepartmentWebController {

    public final static String URL = "http://localhost:8080/rest/department";

    @RequestMapping(value = SiteEndpointUrls.DEPARTMENT_GET_ALL, method = RequestMethod.GET)
    public ModelAndView getAllDepartments() {

        ModelAndView view = new ModelAndView("web/index");

        return view;
    }

    @RequestMapping(value = SiteEndpointUrls.DEPARTMENT_GET_BY_ID, method = RequestMethod.GET)
    public ModelAndView getDepartmentsById(@PathVariable long id) {

        RestTemplate restTemplate = new RestTemplate();
        Department department = restTemplate.getForObject(URL + "/id/" + id, Department.class);

        ModelAndView view = new ModelAndView("web/department/details");
        view.addObject("id", department.getId());
        view.addObject("name", department.getName());

        return view;
    }

}
