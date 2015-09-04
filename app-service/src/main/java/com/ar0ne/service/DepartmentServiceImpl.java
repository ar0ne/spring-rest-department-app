package com.ar0ne.service;

import com.ar0ne.dao.DepartmentDao;
import com.ar0ne.model.Department;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

@Component
public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentDao departmentDao;

    private static final Logger LOGGER = LogManager.getLogger();

    public void setDepartmentDao(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }

    /**
     * Insert specified department to the database
     * @param department department to be inserted to the database
     * @return id of department in database
     */
    public long addDepartment(Department department) {
        LOGGER.debug("DepartmentServiceImpl.addDepartment(department = {})", department);
        Assert.hasText(department.getName(), "Department NAME can't be NULL");
        Assert.isTrue(department.getName().length() < 100, "Department NAME can't be more then 100 chars");
        Assert.notNull(department.getEmployees(), "Department Employees can't be NULL");
        Department existDepartment = departmentDao.getDepartmentByName(department.getName());
        if (existDepartment != null) {
            LOGGER.error("Department with NAME = {} exists", department.getName());
            throw new IllegalArgumentException("Department with this NAME exist");
        }

        long id = departmentDao.addDepartment(department);
        LOGGER.debug("DepartmentServiceImpl.addDepartment() : id = {}", id);
        return id;
    }

    /**
     * Remove department from database
     * @param id of department
     */
    public void removeDepartment(long id) {
        LOGGER.debug("DepartmentServiceImpl.removeDepartment(id = {})", id);
        Assert.notNull(id, "Department ID can't be NULL");

        Department existDepartment = null;
        try{
            existDepartment = departmentDao.getDepartmentById(id);
            Assert.notNull(existDepartment, "Can't remove not existed department");
        }catch (EmptyResultDataAccessException ex) {
            LOGGER.error("Can't remove department with ID = {}, because he doesn't exist.", id);
            throw new IllegalArgumentException("Department with this ID doesn't exist");
        }

        departmentDao.removeDepartment(id);
    }

    /**
     * Replaces the department in the database with the specified department.
     * @param department to be updated in the database
     */
    public void updateDepartment(Department department) {
        LOGGER.debug("DepartmentServiceImpl.updateDepartment(department = {})", department);
        Assert.notNull(department, "Department can't be NULL");
        Assert.notNull(department.getId(), "Department ID can't be NULL");
        Assert.hasText(department.getName(), "Department NAME can't be NULL");
        Assert.isTrue(department.getName().length() < 100, "Department NAME can't be more then 100 chars");
        Assert.notNull(department.getEmployees(), "Department Employees can't be NULL");

        Department existDepartment = null;
        try {
            existDepartment = departmentDao.getDepartmentById(department.getId());
            Assert.notNull(existDepartment, "Can update not existed department. Or wrong ID!");
        } catch (EmptyResultDataAccessException ex) {
            LOGGER.error("Can't update employee with ID = {}, because he didn't exist!");
            throw new IllegalArgumentException("Can't update employee what doesn't exist!");
        }

        departmentDao.updateDepartment(department);
    }

    /**
     * Returns a list containing all of the departments in the database.
     * @return a list containing all of the departments in the database
     */
    public List<Department> getAllDepartments() {
        LOGGER.debug("DepartmentServiceImpl.getAllDepartments()");
        List<Department> departments = departmentDao.getAllDepartments();
        Assert.notEmpty(departments, "Empty list of departments");
        LOGGER.debug("DepartmentServiceImpl.getAllDepartments() : departments = {}", departments);
        return departments;
    }

    /**
     * Returns the department with the specified departmentId from database.
     * @param id id of the department to return
     * @return the department with the specified departmentId from the database
     */
    public Department getDepartmentById(long id) {
        LOGGER.debug("DepartmentServiceImpl.getDepartmentById(id = {})", id);
                Assert.notNull(id, "Department ID can't be NULL");
        Department department = null;
        try {
            department = departmentDao.getDepartmentById(id);
            Assert.notNull(department, "Department can't be NULL");
            Assert.hasText(department.getName(), "Department can't be without NAME");
        } catch (EmptyResultDataAccessException ex) {
            LOGGER.error("Department with ID = '{}' doesn't exist", id);
            throw new IllegalArgumentException("Department with this ID doesn't exist");
        }
        LOGGER.debug("DepartmentServiceImpl.getDepartmentById(id) : department = {}", department);
        return department;
    }

    /**
     * Returns the department with the specified departmentName from database.
     * @param name name of the department to return
     * @return the department with the specified departmentName from the database
     */
    public Department getDepartmentByName(String name) {
        LOGGER.debug("DepartmentServiceImpl.getDepartmentByName(name = {})", name);
        Assert.hasText(name, "Department NAME can't be NULL");
        Assert.isTrue(name.length() < 100, "Department NAME can't be more then 100 chars");
        Department department = null;
        try {
            department = departmentDao.getDepartmentByName(name);
            Assert.notNull(department, "Department can't be NULL");
            Assert.hasText(department.getName(), "Department can't be without NAME");
        } catch (EmptyResultDataAccessException ex) {
            LOGGER.error("Department with NAME = '{}' doesn't exist", name);
            throw new IllegalArgumentException("Department with this NAME doesn't exist");
        }
        LOGGER.debug("DepartmentServiceImpl.getDepartmentByName(name) : department = {}", department);
        return department;
    }
}
