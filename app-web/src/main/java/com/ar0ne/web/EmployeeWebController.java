package com.ar0ne.web;

import com.ar0ne.model.Department;
import com.ar0ne.model.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
public class EmployeeWebController {


    public final static String URL = "http://localhost:8080/rest/employee";
    private static final Logger LOGGER = LogManager.getLogger(EmployeeWebController.class);


    @RequestMapping(value = SiteEndpointUrls.EMPLOYEE_GET_BY_ID, method = RequestMethod.GET)
    public ModelAndView getEmployeeById(@PathVariable long id) {

        ModelAndView view = new ModelAndView("web/employee/details");

        RestTemplate restTemplate = new RestTemplate();
        Employee employee = null;
        try {
            employee = restTemplate.getForObject(URL + "/id/" + id, Employee.class);
            view.addObject("employee", employee);
        } catch (Exception ex) {
            LOGGER.debug(ex);
            view.addObject("error", "Can't find this employee");
        }

        return view;
    }


    @RequestMapping(value = SiteEndpointUrls.EMPLOYEE_CREATE, method = RequestMethod.GET)
    public ModelAndView addDepartmentForm() {

        ModelAndView view = new ModelAndView("web/employee/add");

        RestTemplate restTemplate = new RestTemplate();

        try {
            Department[] departments = restTemplate.getForObject("http://localhost:8080/rest/department", Department[].class);
            List<Department> departmentList = Arrays.asList(departments);

            Department.SortByDepartmentName sn = new Department().new SortByDepartmentName();
            Collections.sort(departmentList, sn);

            view.addObject("departments", departmentList);
        } catch (Exception ex) {
            LOGGER.debug(ex);
            view.addObject("error", "Can't create new employee");
        }

        return view;
    }

    @RequestMapping(value = SiteEndpointUrls.EMPLOYEE_CREATE, method = RequestMethod.POST)
    public ModelAndView addDepartment(RedirectAttributes redirectAttributes,
                                      @RequestParam("name")             String   name,
                                      @RequestParam("surname")          String   surname,
                                      @RequestParam("patronymic")       String   patronymic,
                                      @RequestParam("department_id")    Long   department_id,
                                      @RequestParam("salary")           Long   salary,
                                      @RequestParam("date_of_birthday") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date_of_birthday) {

        try {

            RestTemplate restTemplate = new RestTemplate();

            MultiValueMap<String, Object> request = new LinkedMultiValueMap<String, Object>();
            request.add("name", name);
            request.add("surname", surname);
            request.add("patronymic", patronymic);
            request.add("department_id", department_id.toString());
            request.add("salary", salary.toString());
            request.add("date_of_birthday", date_of_birthday.toString());

            restTemplate.postForObject(URL + "/create", request, String.class);
            redirectAttributes.addFlashAttribute( "message", "New employee added.");
            return new ModelAndView("redirect:" + SiteEndpointUrls.EMPLOYEE_CREATE);

        } catch (Exception ex) {
            LOGGER.debug(ex);
            redirectAttributes.addFlashAttribute( "error", "Can't add employee! Check input data!");
            return new ModelAndView("redirect:" + SiteEndpointUrls.EMPLOYEE_CREATE);
        }

    }


    @RequestMapping(value = SiteEndpointUrls.EMPLOYEE_UPDATE_PAGE, method = RequestMethod.GET)
    public ModelAndView updateEmployeeByIdForm(RedirectAttributes redirectAttributes,
                                                 @PathVariable long id) {

        ModelAndView view = null;
        RestTemplate restTemplate = new RestTemplate();
        Employee employee = null;
        try {
            employee = restTemplate.getForObject(URL + "/id/" + id, Employee.class);

            Department[] departments = restTemplate.getForObject("http://localhost:8080/rest/department", Department[].class);
            List<Department> departmentList = Arrays.asList(departments);

            Department.SortByDepartmentName sn = new Department().new SortByDepartmentName();
            Collections.sort(departmentList, sn);

            view = new ModelAndView("web/employee/update");
            view.addObject("employee", employee);
            view.addObject("departments", departmentList);

        } catch (Exception ex) {
            LOGGER.debug(ex);
            view = new ModelAndView("redirect:" + SiteEndpointUrls.DEPARTMENT_GET_ALL);
            view.addObject("error", "Can't get update page for this employee! Check input data");
        }

        return view;
    }


    @RequestMapping(value = SiteEndpointUrls.EMPLOYEE_UPDATE, method = RequestMethod.POST)
    public ModelAndView    updateEmployee(  RedirectAttributes redirectAttributes,
                                            @RequestParam Long    id,
                                            @RequestParam String    name,
                                            @RequestParam String    surname,
                                            @RequestParam String    patronymic,
                                            @RequestParam Long    salary,
                                            @RequestParam Long    department_id,
                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date_of_birthday ){

        ModelAndView view = null;
        RestTemplate restTemplate = new RestTemplate();
        try {

            MultiValueMap<String, Object> request = new LinkedMultiValueMap<String, Object>();
            request.add("name", name);
            request.add("surname", surname);
            request.add("patronymic", patronymic);
            request.add("department_id", department_id.toString());
            request.add("salary", salary.toString());
            request.add("date_of_birthday", date_of_birthday.toString());
            request.add("id", id.toString());

            restTemplate.postForObject(URL + "/update", request, String.class);
            redirectAttributes.addFlashAttribute("message", "Employee updated.");
            return new ModelAndView("redirect:" + SiteEndpointUrls.DEPARTMENT_GET_ALL);
        } catch (Exception ex) {
            LOGGER.debug(ex);
            redirectAttributes.addFlashAttribute( "error", "Can't update employee! Check input data!");
            return new ModelAndView("redirect:" + SiteEndpointUrls.EMPLOYEE_UPDATE_PAGE);
        }

    }

    @RequestMapping(value = SiteEndpointUrls.EMPLOYEE_DELETE, method = RequestMethod.GET)
    public ModelAndView deleteDepartmentById(RedirectAttributes redirectAttributes,
                                             @PathVariable long id) {

        ModelAndView view = new ModelAndView("redirect:" + SiteEndpointUrls.DEPARTMENT_GET_ALL);
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.delete(URL + "/delete/" + id);
            redirectAttributes.addFlashAttribute( "message", "Employee removed");
        } catch (Exception ex) {
            LOGGER.debug(ex);
            redirectAttributes.addFlashAttribute( "error", "Can't remove employee with ID = " + id);
        }

        return view;
    }

}
