package com.ar0ne.rest;

import com.ar0ne.model.Department;
import com.ar0ne.service.DepartmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


@RestController
public class DepartmentRestController {

    private static final Logger LOGGER = LogManager.getLogger(DepartmentRestController.class);

    @Autowired
    DepartmentService departmentService;

    @RequestMapping(value = SiteEndpointUrls.DEPARTMENT_GET_ALL, method = RequestMethod.GET)
    public ResponseEntity<List<Department>> getAllDepartments() {
        LOGGER.debug("get all departments()");
        List<Department> departmentList = departmentService.getAllDepartments();
        return new ResponseEntity(departmentList, HttpStatus.OK);
    }


    @RequestMapping(value = SiteEndpointUrls.DEPARTMENT_GET_BY_ID, method = RequestMethod.GET)
    public ResponseEntity<Department> getDepartmentById(@PathVariable("id") long id) {
        LOGGER.debug("get department by id ({})", id);
        try {
            Department department = departmentService.getDepartmentById(id);
            return new ResponseEntity<Department>(department, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity("Department not found for id=" + id + ", error:" + ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(value = SiteEndpointUrls.DEPARTMENT_GET_BY_NAME, method = RequestMethod.GET)
    public ResponseEntity<Department> getDepartmentByName(@PathVariable("name") String name) {
        LOGGER.debug("get department by name ({})", name);
        try {
            Department department = departmentService.getDepartmentByName(name);
            return new ResponseEntity<Department>(department, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity("Department not found for name=" + name + ", error:" + ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = SiteEndpointUrls.DEPARTMENT_CREATE, method = RequestMethod.POST)
    public ResponseEntity addDepartment(@RequestBody Department department) {
        LOGGER.debug("add department({})", department);
        try {
            long id = departmentService.addDepartment(department);
            return new ResponseEntity(id, HttpStatus.CREATED);
        } catch (Exception ex) {
            LOGGER.debug(ex);
            return new ResponseEntity(ex.getMessage(),  HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = SiteEndpointUrls.DEPARTMENT_DELETE, method = RequestMethod.DELETE)
    public ResponseEntity removeDepartment(@PathVariable Long id) {
        LOGGER.debug("remove department by id ({})", id);

        try {
            departmentService.removeDepartment(id);
            return new ResponseEntity("", HttpStatus.OK);
        }catch(Exception ex) {
            LOGGER.debug(ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = SiteEndpointUrls.DEPARTMENT_UPDATE, method = RequestMethod.POST)
    public ResponseEntity updateDepartment(@RequestBody Department department) {
        LOGGER.debug("update department({})", department);

        try {
            departmentService.updateDepartment(department);
            return new ResponseEntity("", HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.debug(ex);
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
