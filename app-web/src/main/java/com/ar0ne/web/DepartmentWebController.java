package com.ar0ne.web;

import com.ar0ne.model.Department;
import com.ar0ne.model.Employee;
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

import java.util.*;


@Controller
@RequestMapping("/department")
public class DepartmentWebController {

    public final static String URL = "http://localhost:8080/rest/department";

    private static final Logger LOGGER = LogManager.getLogger(DepartmentWebController.class);

    /**
     * Calculate average salary of employees from one department
     * @param department Department object with field of the list of Employees
     * @return float value of average salary of employees
     */
    private float calcAverageSalary(Department department) {
        int size = department.getEmployees().size();
        if(size == 0) {
            return 0.0f;
        }
        float sum = 0.0f;
        for (Employee employee: department.getEmployees()) {
            sum += employee.getSalary();
        }
        return sum / size;
    }

    @RequestMapping(value = {SiteEndpointUrls.GET_ALL, ""}, method = RequestMethod.GET)
    public ModelAndView getAllDepartments() {

        ModelAndView view = new ModelAndView("web/department/all");
        RestTemplate restTemplate = new RestTemplate();

        try {
            Map<Long, Float> department_avg_salary_map = new HashMap<>();

            Department[] departments = restTemplate.getForObject(URL, Department[].class);
            List<Department> departmentList = Arrays.asList(departments);

            Float avg_salary = 0.0f;

            Department.SortByDepartmentName sn = new Department().new SortByDepartmentName();
            Collections.sort(departmentList, sn);

            for(Department dep: departmentList) {
                avg_salary = calcAverageSalary(dep);
                department_avg_salary_map.put(dep.getId(), avg_salary);
            }

            view.addObject("department_avg_salary_map", department_avg_salary_map);
            view.addObject("departmentList", departmentList);
        } catch (Exception ex) {
            LOGGER.debug(ex);
            view.addObject("error", "Database doesn't consist any departments yet.");
        }

        return view;
    }

    @RequestMapping(value = SiteEndpointUrls.GET_BY_ID, method = RequestMethod.GET)
    public ModelAndView getDepartmentsById(@PathVariable long id) {

        ModelAndView view = new ModelAndView("web/department/details");

        RestTemplate restTemplate = new RestTemplate();
        Department department = null;

        try {

            department = restTemplate.getForObject(
                URL + "/id/" + id,
                Department.class
            );

            view.addObject("department", department);
        } catch (Exception ex) {
            LOGGER.debug(ex);
            view.addObject("error", "Can't find this department");
        }

        return view;
    }

    @RequestMapping(value = SiteEndpointUrls.GET_BY_NAME, method = RequestMethod.GET)
    public ModelAndView getDepartmentsByName(@PathVariable String name) {

        ModelAndView view = new ModelAndView("web/department/details");

        RestTemplate restTemplate = new RestTemplate();
        Department department = null;

        try {
            department = restTemplate.getForObject(
                URL + "/name/" + name,
                Department.class
            );

            view.addObject("department", department);
        } catch (Exception ex) {
            LOGGER.debug(ex);
            view.addObject("error", "Can't find this department");
        }

        return view;
    }

    @RequestMapping(value = SiteEndpointUrls.CREATE, method = RequestMethod.GET)
    public ModelAndView addDepartmentForm() {

        ModelAndView view = new ModelAndView("web/department/add");
        return view;
    }

    @RequestMapping(value = SiteEndpointUrls.CREATE, method = RequestMethod.POST)
    public ModelAndView addDepartment(RedirectAttributes redirectAttributes,
                                      @RequestParam("name") String name) {

        try {

            RestTemplate restTemplate = new RestTemplate();

            MultiValueMap<String, Object> request = new LinkedMultiValueMap<String, Object>();
            request.add("name", name);

            restTemplate.postForObject(
                URL + "/create",
                request,
                String.class
            );

            redirectAttributes.addFlashAttribute( "message", "New department added.");
            return new ModelAndView("redirect:/department" + SiteEndpointUrls.GET_ALL);

        } catch (Exception ex) {
            LOGGER.debug(ex);
            redirectAttributes.addFlashAttribute( "error", "Can't add department! Check input data!");
            return new ModelAndView("redirect:/department" + SiteEndpointUrls.CREATE);
        }

    }

    @RequestMapping(value = SiteEndpointUrls.DELETE, method = RequestMethod.GET)
    public ModelAndView deleteDepartmentById(RedirectAttributes redirectAttributes,
                                             @PathVariable long id) {

        ModelAndView view = new ModelAndView("redirect:/department" + SiteEndpointUrls.GET_ALL);

        try {

            RestTemplate restTemplate = new RestTemplate();

            restTemplate.delete(
                URL + "/delete/" + id
            );

            redirectAttributes.addFlashAttribute( "message", "Department removed");
        } catch (Exception ex) {
            LOGGER.debug(ex);
            redirectAttributes.addFlashAttribute( "error", "Can't remove department with ID = " + id);
        }

        return view;
    }

    @RequestMapping(value = SiteEndpointUrls.UPDATE_PAGE, method = RequestMethod.GET)
    public ModelAndView updateDepartmentByIdForm(RedirectAttributes redirectAttributes,
                                                 @PathVariable long id) {

        ModelAndView view = null;
        RestTemplate restTemplate = new RestTemplate();
        Department department = null;

        try {

            department = restTemplate.getForObject(
                URL + "/id/" + id,
                Department.class
            );

            view = new ModelAndView("web/department/update");
            view.addObject("department", department);
        } catch (Exception ex) {
            LOGGER.debug(ex);
            view = new ModelAndView("redirect:/department" + SiteEndpointUrls.GET_ALL);
            view.addObject("error", "Can't get update page for this department! Check input data");
        }

        return view;
    }

    @RequestMapping(value = SiteEndpointUrls.UPDATE, method = RequestMethod.POST)
    public ModelAndView updateDepartmentById(RedirectAttributes redirectAttributes,
                                             @RequestParam("name") String   name,
                                             @RequestParam("id")   String   id) {

        ModelAndView view = null;
        RestTemplate restTemplate = new RestTemplate();

        try {

            MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
            request.add("name", name);
            request.add("id", id);

            restTemplate.postForObject(
                URL + "/update",
                request,
                String.class
            );

            redirectAttributes.addFlashAttribute("message", "Department updated.");
        } catch (Exception ex) {
            LOGGER.debug(ex);
            redirectAttributes.addFlashAttribute( "error", "Can't update department! Check input data!");
        }

        return new ModelAndView("redirect:/department" + SiteEndpointUrls.GET_ALL);

    }
}
