package com.ar0ne.rest;

import com.ar0ne.model.Department;
import com.ar0ne.service.DepartmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Department REST controller for department entity
 */
@Component
@RestController
@RequestMapping(value = "/department")
public class DepartmentRestController {

    private static final Logger LOGGER = LogManager.getLogger(DepartmentRestController.class);

    @Autowired
    @Qualifier("departmentServiceImpl")
    DepartmentService departmentService;

    /**
     *  Get list of all departments from database
     * @return list of all departments in JSON format
     */
    @RequestMapping(value = {SiteEndpointUrls.GET_ALL, ""}, method = RequestMethod.GET)
    public ResponseEntity<List<Department>> getAllDepartments() {
        LOGGER.debug("get all departments()");
        List<Department> departmentList = departmentService.getAllDepartments();
        return new ResponseEntity<>(departmentList, HttpStatus.OK);
    }

    /**
     *  Get list of all departments, but without employees from database
     * @return list of all departments, but without employees in JSON format
     */
    @RequestMapping(value = SiteEndpointUrls.GET_ONLY, method = RequestMethod.GET)
    public ResponseEntity<List<Department>> getAllDepartmentsWithoutEmployees() {
        LOGGER.debug("get all departments without employees()");
        List<Department> departmentList = departmentService.getAllDepartmentsWithoutEmployees();
        return new ResponseEntity<>(departmentList, HttpStatus.OK);
    }

    /**
     * Get department by ID field from database
     * @param id of department in database
     * @return department from database in JSON format
     */
    @RequestMapping(value = SiteEndpointUrls.GET_BY_ID, method = RequestMethod.GET)
    public ResponseEntity<Department> getDepartmentById(@PathVariable("id") long id) {
        LOGGER.debug("get department by id ({})", id);
        try {
            Department department = departmentService.getDepartmentById(id);
            return new ResponseEntity<Department>(department, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity("Department not found for id=" + id + ", error:" + ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get department by NAME field from database
     * @param name of department in database
     * @return department from database in JSON format
     */
    @RequestMapping(value = SiteEndpointUrls.GET_BY_NAME, method = RequestMethod.GET)
    public ResponseEntity<Department> getDepartmentByName(@PathVariable("name") String name) {
        LOGGER.debug("get department by name ({})", name);
        try {
            Department department = departmentService.getDepartmentByName(name);
            return new ResponseEntity<>(department, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity("Department not found for name=" + name + ", error:" + ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Create new department in database
     * @param name of new department
     * @return id of new department
     */
    @RequestMapping(value = SiteEndpointUrls.CREATE, method = RequestMethod.POST)
    public ResponseEntity addDepartment(@RequestParam String  name) {
        LOGGER.debug("add department with name = {}", name);
        Department department = new Department();
        department.setName(name);
        try {
            long id = departmentService.addDepartment(department);
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        } catch (Exception ex) {
            LOGGER.debug(ex);
            return new ResponseEntity<>(ex.getMessage(),  HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delete department in database
     * @param id of department for delete
     * @return status 200 if all right
     */
    @RequestMapping(value = SiteEndpointUrls.DELETE, method = RequestMethod.DELETE)
    public ResponseEntity removeDepartment(@PathVariable("id") Long id) {
        LOGGER.debug("remove department by id ({})", id);

        try {
            departmentService.removeDepartment(id);
            return new ResponseEntity<>("", HttpStatus.OK);
        }catch(Exception ex) {
            LOGGER.debug(ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Replaces the departments in the database with the specified department.
     * @param name new name for department
     * @param id of department for update
     * @return status 200 if all right
     */
    @RequestMapping(value = SiteEndpointUrls.UPDATE, method = RequestMethod.POST)
    public ResponseEntity updateDepartment(@RequestParam("name") String  name,
                                           @RequestParam("id")   String  id) {

        LOGGER.debug("update department id = {}, name = {}", id, name);

        Department department = new Department();
        department.setName(name);
        department.setId(new Long(id));

        try {
            departmentService.updateDepartment(department);
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.debug(ex);
            return new ResponseEntity<>("Check input data!", HttpStatus.BAD_REQUEST);
        }
    }

}
