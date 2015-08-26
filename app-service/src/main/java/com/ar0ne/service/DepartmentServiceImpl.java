package com.ar0ne.service;

import com.ar0ne.dao.DepartmentDao;
import com.ar0ne.model.Department;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.util.Assert;

import java.util.List;

public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentDao departmentDao;
    private static final Logger LOGGER = LogManager.getLogger();

    public void setDepartmentDao(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }

    @Override
    public void addDepartment(Department department) {
        Assert.isNull(department.getId(), "Department ID must to be NULL");
        Assert.notNull(department.getName(), "Department NAME can't ne NULL");

        Department existDepartment = departmentDao.getDepartmentByName(department.getName());
        if (existDepartment != null) {
            LOGGER.error("Department with NAME = {} exists", department.getName());
            throw new IllegalArgumentException("Department with this NAME exist");
        }

        departmentDao.addDepartment(department);

    }

    @Override
    public void removeDepartment(long id) {
        Assert.notNull(id, "Department ID can't be NULL");

        Department existDepartment = null;
        try{
            existDepartment = departmentDao.getDepartmentById(id);
        }catch (EmptyResultDataAccessException ex) {
            LOGGER.error("Can't remove department with ID = {}, because he doesn't exist.", id);
            throw new IllegalArgumentException("Department with this ID doesn't exist");
        }

        departmentDao.removeDepartment(id);
    }

    @Override
    public void updateDepartment(Department department) {
        Assert.notNull(department, "Department can't be NULL");
        Assert.notNull(department.getId(), "Department ID can't be NULL");
        Assert.notNull(department.getName(), "Department NAME can't be NULL");

        Department existDepartment = null;
        try{
            existDepartment = departmentDao.getDepartmentById(department.getId());
        }catch (EmptyResultDataAccessException ex) {
            LOGGER.error("Can't update department with ID = {}, because he doesn't exist.", department.getId());
            throw new IllegalArgumentException("Department with this ID doesn't exist in DB");
        }

        departmentDao.updateDepartment(department);

    }

    @Override
    public List<Department> getAllDepartments() {

        List<Department> departments = departmentDao.getAllDepartments();
        Assert.notEmpty(departments, "Empty list of departments");
        return departments;
    }

    @Override
    public Department getDepartmentById(long id) {

        Assert.notNull(id, "Department ID can't be NULL");
        Department department = null;
        try {
            department = departmentDao.getDepartmentById(id);
        } catch (EmptyResultDataAccessException ex) {
            LOGGER.error("Department with ID = '{}' doesn't exist", id);
            throw new IllegalArgumentException("Department with this ID doesn't exist");
        }
        return department;
    }

    @Override
    public Department getDepartmentByName(String name) {
        Assert.notNull(name, "Department NAME can't be NULL");
        Department department = null;
        try {
            department = departmentDao.getDepartmentByName(name);
        } catch (EmptyResultDataAccessException ex) {
            LOGGER.error("Department with NAME = '{}' doesn't exist", name);
            throw new IllegalArgumentException("Department with this NAME doesn't exist");
        }
        return department;
    }
}
