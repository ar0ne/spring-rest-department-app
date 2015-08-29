package com.ar0ne.rest;

import com.ar0ne.dao.DepartmentDao;
import com.ar0ne.model.Department;

import com.ar0ne.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class DepartmentRestController {

    @Autowired
    DepartmentService departmentService;

    @RequestMapping(value = SiteEndpointUrls.DEPARTMENT, method = RequestMethod.GET)
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departmentList = departmentService.getAllDepartments();
        return new ResponseEntity(departmentList, HttpStatus.OK);
    }


    @RequestMapping(value = SiteEndpointUrls.DEPARTMENT + "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Department> getDepartment(@PathVariable("id") long id) {

        try {
            Department department = departmentService.getDepartmentById(id);
            return new ResponseEntity(department, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity("Department not found for id=" + id + " error:" + ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
