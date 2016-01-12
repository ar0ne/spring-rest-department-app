package com.ar0ne.rest;

import com.ar0ne.model.Employee;
import com.ar0ne.service.EmployeeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Employee REST controller for employee entity
 */
@Component
@RestController
@RequestMapping(value = "/employee")
public class EmployeeRestController {

    private static final Logger LOGGER = LogManager.getLogger(EmployeeRestController.class);

    @Autowired
    @Qualifier("employeeServiceImpl")
    private EmployeeService employeeService;

    /**
     * Get list of all employee in database
     * @return list of employee in JSON format
     */
    @RequestMapping(value = {SiteEndpointUrls.GET_ALL, ""}, method = RequestMethod.GET)
    public ResponseEntity<List<Employee>> getAllEmployees() {
        LOGGER.debug("get all employees()");
        List<Employee> employeeList = employeeService.getAllEmployees();
        return new ResponseEntity<>(employeeList, HttpStatus.OK);
    }

    /**
     * Get employee from database with specified id.
     * @param id of employee in database
     * @return employee in JSON format
     */
    @RequestMapping(value = SiteEndpointUrls.GET_BY_ID, method = RequestMethod.GET)
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") long id) {
        LOGGER.debug("get employee by id ({})", id);
        try {
            Employee employee = employeeService.getEmployeeById(id);
            return new ResponseEntity<Employee>(employee, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity("Employee not found for id=" + id + ", error:" + ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Delete employee in database
     * @param id of employee in database
     * @return status 200 if all right
     */
    @RequestMapping(value = SiteEndpointUrls.DELETE, method = RequestMethod.DELETE)
    public ResponseEntity removeEmployee(@PathVariable Long id) {
        LOGGER.debug("remove employee by id ({})", id);

        try {
            employeeService.removeEmployee(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.debug(ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Replaces the employees in the database with the specified employee.
     * @param id of employee in database
     * @param surname new surname of employee
     * @param name new name of employee
     * @param patronymic new patronymic of employee
     * @param salary new salary of employee
     * @param date_of_birthday new date_of_birthday of employee
     * @param department_id new department_id of employee
     * @return status 200 if all right
     */
    @RequestMapping(value = SiteEndpointUrls.UPDATE, method = RequestMethod.POST)
    public ResponseEntity updateEmployee( @RequestParam(value = "id") Long id,
                                          @RequestParam(value = "surname") String surname,
                                          @RequestParam(value = "name") String name,
                                          @RequestParam(value = "patronymic") String patronymic,
                                          @RequestParam(value = "salary") Long salary,
                                          @RequestParam(value = "date_of_birthday") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date_of_birthday,
                                          @RequestParam(value = "department_id") Long department_id) {

        LOGGER.debug("update employee to name = {}, surname = {}, patronymic = {}, salary = {}, date_of_birthday = {}",
                name, surname, patronymic, salary, date_of_birthday);

        Employee employee = new Employee();
        employee.setId(id);
        employee.setName(name);
        employee.setSurname(surname);
        employee.setPatronymic(patronymic);
        employee.setSalary(salary);
        employee.setDateOfBirthday(date_of_birthday);
        employee.setDepartmentId(department_id);

        try {
            employeeService.updateEmployee(employee);
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.debug(ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Create new employee in database
     * @param id of new employee in database
     * @param surname of new employee in database
     * @param name of new employee in database
     * @param patronymic of new employee in database
     * @param salary of new employee in database
     * @param date_of_birthday of new employee in database
     * @param department_id of new employee in database
     * @return id of created employee in database
     */
    @RequestMapping(value = SiteEndpointUrls.CREATE, method = RequestMethod.POST)
    public ResponseEntity addEmployee(@RequestParam(value = "id", required = false) Long id,
                                      @RequestParam(value = "surname") String surname,
                                      @RequestParam(value = "name") String name,
                                      @RequestParam(value = "patronymic") String patronymic,
                                      @RequestParam(value = "salary") Long salary,
                                      @RequestParam(value = "date_of_birthday") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date_of_birthday,
                                      @RequestParam(value = "department_id") Long department_id) {

        LOGGER.debug("add employee with name = {}, surname = {}, patronymic = {}, salary = {}, date_of_birthday = {}",
                name, surname, patronymic, salary, date_of_birthday);

        try {

            Employee employee = new Employee();
            employee.setName(name);
            employee.setSurname(surname);
            employee.setPatronymic(patronymic);
            employee.setSalary(salary);
            employee.setDateOfBirthday(date_of_birthday);
            employee.setDepartmentId(department_id);

            long ret_id = employeeService.addEmployee(employee);
            return new ResponseEntity<>(ret_id, HttpStatus.CREATED);
        } catch (Exception ex) {
            LOGGER.debug(ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get employees from database by date of birthday criteria.
     * @param date of birthday in format "yyyy-MM-dd"
     * @return list of employees with specified date of birthday from database in JSON format.
     */
    @RequestMapping(value = SiteEndpointUrls.GET_BY_DATE, method = RequestMethod.GET)
    public ResponseEntity getEmployeeByDateOfBirthday(
            @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        LOGGER.debug("get employees with date of birthday = {}", date.toString());

        List<Employee> employeeList = null;
        try {
            employeeList = employeeService.getEmployeeByDateOfBirthday(date);
            return new ResponseEntity<>(employeeList, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.debug(ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get employees by date of birthday criteria in interval from-to from database.
     * @param from begin of interval
     * @param to end of interval
     * @return list of employees in JSON format
     */
    @RequestMapping(value = SiteEndpointUrls.GET_BETWEEN_DATES, method = RequestMethod.GET)
    public ResponseEntity getEmployeeByDoBBetweenDates(
            @PathVariable("from") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @PathVariable("to") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to) {

        LOGGER.debug("get employees with date of birthday between {} and {}", from.toString(), to.toString());

        List<Employee> employeeList = null;
        try {
            employeeList = employeeService.getEmployeeBetweenDatesOfBirtday(from, to);
            return new ResponseEntity<>(employeeList, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.debug(ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
