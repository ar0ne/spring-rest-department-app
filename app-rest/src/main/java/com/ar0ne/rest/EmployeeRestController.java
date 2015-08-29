package com.ar0ne.rest;

import com.ar0ne.dao.EmployeeDao;
import com.ar0ne.model.Employee;
import com.ar0ne.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class EmployeeRestController {

     @Autowired
     EmployeeService employeeService;

    @RequestMapping(value = SiteEndpointUrls.EMPLOYEE, method = RequestMethod.GET)
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> departmentList = employeeService.getAllEmployees();
        return new ResponseEntity(departmentList, HttpStatus.OK);
    }


}
