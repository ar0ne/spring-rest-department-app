package com.ar0ne.rest;

import com.ar0ne.model.Employee;
import com.ar0ne.service.EmployeeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@RestController
public class EmployeeRestController {

    private static final Logger LOGGER = LogManager.getLogger(DepartmentRestController.class);

    @Resource
    EmployeeService employeeService;

    @RequestMapping(value = SiteEndpointUrls.EMPLOYEE_GET_ALL, method = RequestMethod.GET)
    public ResponseEntity<List<Employee>> getAllEmployees() {
        LOGGER.debug("get all employees()");
        List<Employee> departmentList = employeeService.getAllEmployees();
        return new ResponseEntity(departmentList, HttpStatus.OK);
    }

    @RequestMapping(value = SiteEndpointUrls.EMPLOYEE_GET_BY_ID, method = RequestMethod.GET)
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") long id) {
        LOGGER.debug("get employee by id ({})", id);
        try {
            Employee employee = employeeService.getEmployeeById(id);
            return new ResponseEntity<Employee>(employee, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity("Employee not found for id=" + id + ", error:" + ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }



    @RequestMapping(value = SiteEndpointUrls.EMPLOYEE_DELETE, method = RequestMethod.DELETE)
    public ResponseEntity removeEmployee(@PathVariable Long id) {
        LOGGER.debug("remove employee by id ({})", id);

        try {
            employeeService.removeEmployee(id);
            return new ResponseEntity("Deleted", HttpStatus.OK);
        }catch(Exception ex) {
            LOGGER.debug(ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = SiteEndpointUrls.EMPLOYEE_UPDATE, method = RequestMethod.POST)
    public ResponseEntity updateEmployee(   @RequestParam Long id,
                                            @RequestParam String  surname,
                                            @RequestParam String  name,
                                            @RequestParam String  patronymic,
                                            @RequestParam Long    salary,
                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date_of_birthday,
                                            @RequestParam Long    department_id ){

        LOGGER.debug("update employee to name = {}, surname = {}, patronymic = {}, salary = {}, date_of_birthday = {}",
                name, surname, patronymic, salary, date_of_birthday );

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
            return new ResponseEntity("", HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.debug(ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = SiteEndpointUrls.EMPLOYEE_CREATE, method = RequestMethod.POST)
    public ResponseEntity addEmployee(@RequestParam(value = "id", required = false) Long    id,
                                      @RequestParam(value = "surname")          String      surname,
                                      @RequestParam(value = "name")             String      name,
                                      @RequestParam(value = "patronymic")       String      patronymic,
                                      @RequestParam(value = "salary")           Long        salary,
                                      @RequestParam("date_of_birthday") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date_of_birthday,
                                      @RequestParam(value = "department_id")    Long        department_id ) {

        LOGGER.debug("add employee with name = {}, surname = {}, patronymic = {}, salary = {}, date_of_birthday = {}",
                name, surname, patronymic, salary, date_of_birthday );
        Employee employee = new Employee();
        employee.setName(name);
        employee.setSurname(surname);
        employee.setPatronymic(patronymic);
        employee.setSalary(salary);
        employee.setDateOfBirthday(date_of_birthday);
        employee.setDepartmentId(department_id);

        try {
            long ret_id = employeeService.addEmployee(employee);
            return new ResponseEntity(ret_id, HttpStatus.CREATED);
        } catch (Exception ex) {
            LOGGER.debug(ex);
            return new ResponseEntity(ex.getMessage(),  HttpStatus.BAD_REQUEST);
        }
    }



}
