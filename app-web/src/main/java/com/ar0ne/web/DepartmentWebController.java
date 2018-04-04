package com.ar0ne.web;

import com.ar0ne.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

/**
 * Web controller for Department entity.
 */
@Controller
@RequestMapping("/department")
public class DepartmentWebController {

    private static final Logger logger = LogManager.getLogger(DepartmentWebController.class);

    @Value( "${url.rest.department}" )
    private String URL_DEPARTMENT;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Get page of all departments with average salary for every departments
     * @return ModelAndView of all departments page
     */
    @RequestMapping(value = {SiteEndpointUrls.GET_ALL, ""}, method = RequestMethod.GET)
    public ModelAndView getAllDepartments() {

        ModelAndView view = new ModelAndView("web/department/all");
        try {
            Map<Long, Float> departmentAvgSalaryMap = new HashMap<>();
            // get departments and convert array to list
            Department[] departments = restTemplate.getForObject( URL_DEPARTMENT, Department[].class);
            List<Department> departmentList = Arrays.asList(departments);

            Float avgSalary;
            // sort list for better appearance
            Department.SortByDepartmentName sn = new Department().new SortByDepartmentName();
            Collections.sort(departmentList, sn);

            for(Department dep: departmentList) {
                // calculate average salary for department and then place value in map with key == departmentId
                avgSalary = dep.calcAverageSalary();
                departmentAvgSalaryMap.put(dep.getId(), avgSalary);
            }

            view.addObject("departmentAvgSalaryMap", departmentAvgSalaryMap);
            view.addObject("departmentList", departmentList);
        } catch (Exception ex) {
            logger.debug(ex);
            view.addObject("error", "Database doesn't consist any departments yet.");
        }

        return view;
    }

    /**
     * Get page for department with specified ID from database
     * @param id of department in database
     * @return ModelAndView of page for department with specified ID.
     */
    @RequestMapping(value = SiteEndpointUrls.GET_BY_ID, method = RequestMethod.GET)
    public ModelAndView getDepartmentsById(@PathVariable long id) {

        ModelAndView view = new ModelAndView("web/department/details");
        Department department;

        try {

            department = restTemplate.getForObject(
                URL_DEPARTMENT + "/id/" + id,
                Department.class
            );

            view.addObject("department", department);
        } catch (Exception ex) {
            logger.debug(ex);
            view.addObject("error", "Can't find this department");
        }

        return view;
    }

    /**
     * Get page for department with specified NAME from database
     * @param name of department in database
     * @return ModelAndView of page for department with specified ID.
     */
    @RequestMapping(value = SiteEndpointUrls.GET_BY_NAME, method = RequestMethod.GET)
    public ModelAndView getDepartmentsByName(@PathVariable String name) {

        ModelAndView view = new ModelAndView("web/department/details");
        Department department;

        try {
            department = restTemplate.getForObject(
                URL_DEPARTMENT + "/name/" + name,
                Department.class
            );

            view.addObject("department", department);
        } catch (Exception ex) {
            logger.debug(ex);
            view.addObject("error", "Can't find this department");
        }

        return view;
    }

    /**
     * Get page with form for creating new department
     * @return ModelAndView of page with form for creating new department
     */
    @RequestMapping(value = SiteEndpointUrls.CREATE, method = RequestMethod.GET)
    public ModelAndView addDepartmentForm() {
        return new ModelAndView("web/department/add");
    }

    /**
     * Post request for create new department
     * @param redirectAttributes redirectAttributes for notify users.
     * @param name name of new department
     * @return ModelAndView of all departments page if all right, or notify message.
     */
    @RequestMapping(value = SiteEndpointUrls.CREATE, method = RequestMethod.POST)
    public ModelAndView addDepartment(RedirectAttributes redirectAttributes,
                                      @RequestParam("name") String name) {

        try {

            MultiValueMap<String, Object> request = new LinkedMultiValueMap<String, Object>();
            request.add("name", name);

            restTemplate.postForObject(
                URL_DEPARTMENT + "/create",
                request,
                String.class
            );

            redirectAttributes.addFlashAttribute( "message", "New department added.");
            return new ModelAndView("redirect:/department" + SiteEndpointUrls.GET_ALL);

        } catch (Exception ex) {
            logger.debug(ex);
            redirectAttributes.addFlashAttribute( "error", "Can't add department! Check input data!");
            return new ModelAndView("redirect:/department" + SiteEndpointUrls.CREATE);
        }

    }

    /**
     * Delete department in database by ID.
     * @param redirectAttributes redirectAttributes for notify users
     * @param id id of department in database
     * @return ModelAndView of all departments page with notify message.
     */
    @RequestMapping(value = SiteEndpointUrls.DELETE, method = RequestMethod.GET)
    public ModelAndView deleteDepartmentById(RedirectAttributes redirectAttributes,
                                             @PathVariable long id) {

        ModelAndView view = new ModelAndView("redirect:/department" + SiteEndpointUrls.GET_ALL);

        try {

            restTemplate.delete(
                URL_DEPARTMENT + "/delete/" + id
            );

            redirectAttributes.addFlashAttribute( "message", "Department removed");
        } catch (Exception ex) {
            logger.debug(ex);
            redirectAttributes.addFlashAttribute( "error", "Can't remove department with ID = " + id);
        }

        return view;
    }

    /**
     * Get page with form for updating department
     * @param redirectAttributes redirectAttributes for notify user
     * @param id id of department in database
     * @return ModelAndView of page with update form for department
     */
    @RequestMapping(value = SiteEndpointUrls.UPDATE_PAGE, method = RequestMethod.GET)
    public ModelAndView updateDepartmentByIdForm(RedirectAttributes redirectAttributes,
                                                 @PathVariable long id) {

        ModelAndView view;
        Department department;
        try {

            department = restTemplate.getForObject(
                URL_DEPARTMENT + "/id/" + id,
                Department.class
            );

            view = new ModelAndView("web/department/update");
            view.addObject("department", department);
        } catch (Exception ex) {
            logger.debug(ex);
            view = new ModelAndView("redirect:/department" + SiteEndpointUrls.GET_ALL);
            view.addObject("error", "Can't get update page for this department! Check input data");
        }

        return view;
    }

    /**
     * Update department in database
     * @param redirectAttributes redirectAttributes for notify user
     * @param name new name for department
     * @param id id of department in database
     * @return ModelAndView of all departments page with redirect attributes
     */
    @RequestMapping(value = SiteEndpointUrls.UPDATE, method = RequestMethod.POST)
    public ModelAndView updateDepartmentById(RedirectAttributes redirectAttributes,
                                             @RequestParam("name") String   name,
                                             @RequestParam("id")   String   id) {

        try {
            MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
            request.add("name", name);
            request.add("id", id);

            restTemplate.postForObject(
                URL_DEPARTMENT + "/update",
                request,
                String.class
            );

            redirectAttributes.addFlashAttribute("message", "Department updated.");
        } catch (Exception ex) {
            logger.debug(ex);
            redirectAttributes.addFlashAttribute( "error", "Can't update department! Check input data!");
        }

        return new ModelAndView("redirect:/department" + SiteEndpointUrls.GET_ALL);

    }
}
