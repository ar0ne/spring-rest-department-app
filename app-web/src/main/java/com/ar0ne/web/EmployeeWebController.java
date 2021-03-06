package com.ar0ne.web;

import com.ar0ne.model.Department;
import com.ar0ne.model.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
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

/**
 * Web controller for employee entity
 */
@Controller
@RequestMapping("/employee")
public class EmployeeWebController {

    private static final Logger logger = LogManager.getLogger(EmployeeWebController.class);

    @Value( "${url.rest.employee}" )
    private String URL_EMPLOYEE;

    @Value( "${url.rest.department}" )
    private String URL_DEPARTMENT;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Get page of all employees
     * @return ModelAndView of get all employees page
     */
    @RequestMapping(value = {SiteEndpointUrls.GET_ALL, ""}, method = RequestMethod.GET)
    public ModelAndView getAllEmployee() {

        ModelAndView view = new ModelAndView("web/employee/all");
        try {

            Employee[] employees = restTemplate.getForObject(
                URL_EMPLOYEE,
                Employee[].class
            );

            view.addObject("employees", employees);

        } catch (Exception ex) {

            logger.debug(ex);
            view.addObject("error", "Database doesn't consist any employees yet.");
        }

        return view;
    }

    /**
     * Get page of employee by id field from database
     * @param id id of employee in database
     * @return ModelAndView of page for employee with specified ID.
     */
    @RequestMapping(value = SiteEndpointUrls.GET_BY_ID, method = RequestMethod.GET)
    public ModelAndView getEmployeeById(@PathVariable long id) {

        ModelAndView view = new ModelAndView("web/employee/details");
        Employee employee;
        try {

            employee = restTemplate.getForObject(
                URL_EMPLOYEE + "/id/" + id,
                Employee.class
            );
            view.addObject("employee", employee);

        } catch (Exception ex) {
            logger.debug(ex);
            view.addObject("error", "Can't find this employee");
        }

        return view;
    }


    /**
     * Get page with form for creating new employee
     * @return ModelAndView of page with form for creating new employee
     */
    @RequestMapping(value = SiteEndpointUrls.CREATE, method = RequestMethod.GET)
    public ModelAndView addDepartmentForm() {

        ModelAndView view = new ModelAndView("web/employee/add");
        try {
            Department[] departments = restTemplate.getForObject(
                URL_DEPARTMENT + SiteEndpointUrls.GET_ONLY,
                Department[].class
            );

            view.addObject("departments", departments);
        } catch (Exception ex) {
            logger.debug(ex);
            view.addObject("error", "Can't create new employee");
        }

        return view;
    }

    /**
     * Post request for create new employee
     * @param redirectAttributes redirectAttributes for notify users
     * @param name name of new employee
     * @param surname surname of new employee
     * @param patronymic patronymic of new employee
     * @param departmentId departmentId of new employee
     * @param salary salary of new employee
     * @param dateOfBirthday dateOfBirthday of new employee
     * @return ModelAndView of all employee page if all right.
     */
    @RequestMapping(value = SiteEndpointUrls.CREATE, method = RequestMethod.POST)
    public ModelAndView addDepartment(RedirectAttributes redirectAttributes,
                                      @RequestParam("name")             String   name,
                                      @RequestParam("surname")          String   surname,
                                      @RequestParam("patronymic")       String   patronymic,
                                      @RequestParam("departmentId")    Long   departmentId,
                                      @RequestParam("salary")           Long   salary,
                                      @RequestParam("dateOfBirthday") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateOfBirthday) {

        try {

            RestTemplate restTemplate = new RestTemplate();

            MultiValueMap<String, Object> request = new LinkedMultiValueMap<String, Object>();
            request.add("name", name);
            request.add("surname", surname);
            request.add("patronymic", patronymic);
            request.add("departmentId", departmentId.toString());
            request.add("salary", salary.toString());
            request.add("dateOfBirthday", dateOfBirthday.toString());

            restTemplate.postForObject(
                URL_EMPLOYEE + "/create",
                request,
                String.class
            );

            redirectAttributes.addFlashAttribute( "message", "New employee added.");
            return new ModelAndView("redirect:/employee" + SiteEndpointUrls.GET_ALL);

        } catch (Exception ex) {
            logger.debug(ex);
            redirectAttributes.addFlashAttribute( "error", "Can't add employee! Check input data!");
            return new ModelAndView("redirect:/employee" + SiteEndpointUrls.CREATE);
        }

    }

    /**
     * Get page with form for updating employee
     * @param redirectAttributes redirectAttributes for notify users
     * @param id id of employee in database
     * @return ModelAndview of page
     */
    @RequestMapping(value = SiteEndpointUrls.UPDATE_PAGE, method = RequestMethod.GET)
    public ModelAndView updateEmployeeByIdForm(RedirectAttributes redirectAttributes,
                                               @PathVariable long id) {

        ModelAndView view;
        Employee employee;
        try {
            employee = restTemplate.getForObject(
                URL_EMPLOYEE + "/id/" + id,
                Employee.class
            );

            Department[] departments = restTemplate.getForObject(
                    URL_DEPARTMENT + SiteEndpointUrls.GET_ONLY,
                    Department[].class
            );

            view = new ModelAndView("web/employee/update");
            view.addObject("employee", employee);
            view.addObject("departments", departments);

        } catch (Exception ex) {
            logger.debug(ex);
            view = new ModelAndView("redirect:/employee" + SiteEndpointUrls.GET_ALL);
            view.addObject("error", "Can't get update page for this employee! Check input data");
        }

        return view;
    }

    /**
     * Update employee in database
     * @param redirectAttributes  redirectAttributes for notify users
     * @param id id of employee in database
     * @param name new name of employee
     * @param surname new surname of employee
     * @param patronymic new patronymic of employee
     * @param salary new salary of employee
     * @param departmentId new departmentId of employee
     * @param dateOfBirthday new dateOfBirthday of employee
     * @return ModelAndView of page get all employees if all right
     */
    @RequestMapping(value = SiteEndpointUrls.UPDATE, method = RequestMethod.POST)
    public ModelAndView    updateEmployee(  RedirectAttributes redirectAttributes,
                                            @RequestParam Long      id,
                                            @RequestParam String    name,
                                            @RequestParam String    surname,
                                            @RequestParam String    patronymic,
                                            @RequestParam Long      salary,
                                            @RequestParam Long      departmentId,
                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateOfBirthday ){

        try {

            MultiValueMap<String, Object> request = new LinkedMultiValueMap<String, Object>();
            request.add("name", name);
            request.add("surname", surname);
            request.add("patronymic", patronymic);
            request.add("departmentId", departmentId.toString());
            request.add("salary", salary.toString());
            request.add("dateOfBirthday", dateOfBirthday.toString());
            request.add("id", id.toString());

            restTemplate.postForObject(
                URL_EMPLOYEE + "/update",
                request,
                String.class
            );

            redirectAttributes.addFlashAttribute("message", "Employee updated.");
            return new ModelAndView("redirect:/employee" + SiteEndpointUrls.GET_ALL);

        } catch (Exception ex) {

            logger.debug(ex);
            redirectAttributes.addFlashAttribute( "error", "Can't update employee! Check input data!");
            return new ModelAndView("redirect:/employee" + SiteEndpointUrls.UPDATE_PAGE);
        }

    }

    /**
     *
     * @param redirectAttributes redirectAttributes for notify users
     * @param id id of employee in database
     * @return  ModelAndview of page
     */
    @RequestMapping(value = SiteEndpointUrls.DELETE, method = RequestMethod.GET)
    public ModelAndView deleteDepartmentById(RedirectAttributes redirectAttributes,
                                             @PathVariable long id) {

        ModelAndView view = new ModelAndView("redirect:/employee" + SiteEndpointUrls.GET_ALL);

        try {
            restTemplate.delete(URL_EMPLOYEE + "/delete/" + id);
            redirectAttributes.addFlashAttribute( "message", "Employee removed");
        } catch (Exception ex) {
            logger.debug(ex);
            redirectAttributes.addFlashAttribute( "error", "Can't remove employee with ID = " + id);
        }

        return view;
    }


    /**
     * Get page with employees with date of birthday with specified date of birthday
     * @param redirectAttributes redirectAttributes for notify users
     * @param date date of birthday
     * @return ModelAndview of page
     */
    @RequestMapping(value = SiteEndpointUrls.GET_BY_DATE, method = RequestMethod.GET)
    public ModelAndView getEmployeeByDate(RedirectAttributes redirectAttributes,
                                          @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        ModelAndView view = new ModelAndView("web/employee/all");
        try {

            Employee[] employees = restTemplate.getForObject(
                URL_EMPLOYEE + "/date/" + date.toString(),
                Employee[].class
            );
            view.addObject("employees", employees);

        } catch (Exception ex) {

            logger.debug(ex);
            view.addObject("error", "Can't find employees for this date!");

        }

        return view;
    }

    /**
     * Get page with employees with dates of birthday from specified interval
     * @param redirectAttributes redirectAttributes for notify users
     * @param from begin of interval
     * @param to end of interval
     * @return ModelAndview of page get all employees with list of employees from interval
     */
    @RequestMapping(value = SiteEndpointUrls.GET_BETWEEN_DATES, method = RequestMethod.GET)
    public ModelAndView getEmployeeByDatesFromInterval(RedirectAttributes redirectAttributes,
                                          @PathVariable("from") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
                                          @PathVariable("to")   @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to) {

        ModelAndView view = new ModelAndView("web/employee/all");
        try {

            Employee[] employees = restTemplate.getForObject(
                URL_EMPLOYEE + "/date/" + from.toString() + "/" + to.toString(),
                Employee[].class
            );

            view.addObject("employees", employees);

        } catch (Exception ex) {

            logger.debug(ex);
            view.addObject("error", "Can't find employees for this date!");

        }

        return view;
    }

}
