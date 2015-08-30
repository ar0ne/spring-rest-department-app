package com.ar0ne.web;

import com.ar0ne.model.Department;
import com.ar0ne.model.Employee;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class DepartmentWebController {

    public final static String URL = "http://localhost:8080/rest/department";
    private static final Logger LOGGER = LogManager.getLogger(DepartmentWebController.class);

    private float calcAverageSalary(Department departmentList) {
        int size = departmentList.getEmployees().size();
        if(size == 0) {
            return 0.0f;
        }
        float sum = 0.0f;
        for (Employee employee: departmentList.getEmployees()) {
            sum += employee.getSalary();
        }
        return sum / size;
    }

    @RequestMapping(value = SiteEndpointUrls.DEPARTMENT_GET_ALL, method = RequestMethod.GET)
    public ModelAndView getAllDepartments() {

        ModelAndView view = new ModelAndView("web/department/all");
        RestTemplate restTemplate = new RestTemplate();

        try {
            Map<Department, Float> departmentMap = new HashMap<>();

            Department[] departments = restTemplate.getForObject(URL, Department[].class);
            Float avg_salary = 0.0f;

            for(Department dep: departments) {
                avg_salary = calcAverageSalary(dep);
                departmentMap.put(dep, avg_salary);
            }

            view.addObject("departmentMap", departmentMap);
        } catch (Exception ex) {
            LOGGER.debug(ex);
            view.addObject("error", "Database doesn't consist any departments yet.");
        }

        return view;
    }

    @RequestMapping(value = SiteEndpointUrls.DEPARTMENT_GET_BY_ID, method = RequestMethod.GET)
    public ModelAndView getDepartmentsById(@PathVariable long id) {

        ModelAndView view = new ModelAndView("web/department/details");

        RestTemplate restTemplate = new RestTemplate();
        Department department = null;
        try {
            department = restTemplate.getForObject(URL + "/id/" + id, Department.class);
            view.addObject("department", department);
        } catch (Exception ex) {
            LOGGER.debug(ex);
            view.addObject("error", "Can't find this department");
        }

        return view;
    }

    @RequestMapping(value = SiteEndpointUrls.DEPARTMENT_GET_BY_NAME, method = RequestMethod.GET)
    public ModelAndView getDepartmentsByName(@PathVariable String name) {

        ModelAndView view = new ModelAndView("web/department/details");

        RestTemplate restTemplate = new RestTemplate();
        Department department = null;
        try {
            department = restTemplate.getForObject(URL + "/name/" + name, Department.class);
            view.addObject("department", department);
        } catch (Exception ex) {
            LOGGER.debug(ex);
            view.addObject("error", "Can't find this department");
        }

        return view;
    }

    @RequestMapping(value = SiteEndpointUrls.DEPARTMENT_CREATE, method = RequestMethod.GET)
    public ModelAndView addDepartmentForm() {

        ModelAndView view = new ModelAndView("web/department/add");
        return view;
    }

    @RequestMapping(value = SiteEndpointUrls.DEPARTMENT_CREATE, method = RequestMethod.POST)
    public ModelAndView addDepartment(RedirectAttributes redirectAttributes,
                                      @RequestParam("name") String name) {

        try{

            RestTemplate restTemplate = new RestTemplate();

            MultiValueMap<String, Object> request = new LinkedMultiValueMap<String, Object>();
            request.add("name", name);

            restTemplate.postForObject("http://localhost:8080/rest/department/create", request, String.class);
            return new ModelAndView("redirect:" + SiteEndpointUrls.DEPARTMENT_GET_ALL);

        } catch (Exception ex) {
            LOGGER.debug(ex);
            redirectAttributes.addFlashAttribute( "error", "Check input data!");
            return new ModelAndView("redirect:" + SiteEndpointUrls.DEPARTMENT_CREATE);
        }

    }



}
