package com.ar0ne.service.impl;

import com.ar0ne.dao.DepartmentDao;
import com.ar0ne.model.Department;

import com.ar0ne.service.DepartmentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

@Component
public class DepartmentServiceImpl implements DepartmentService
{
    private static final Logger logger = LogManager.getLogger(DepartmentServiceImpl.class);

    @Autowired
    private DepartmentDao departmentDao;

    /**
     * Insert specified department to the DAO
     * @param department department to be inserted to the DAO
     * @return id of department in DAO
     */
    public long addDepartment(Department department) {
        logger.debug("addDepartment(department = {})", department);
        Assert.hasText(department.getName(), "Department NAME can't be NULL");
        Assert.isTrue(department.getName().length() < 100, "Department NAME can't be more then 100 chars");
        Assert.notNull(department.getEmployees(), "Department Employees can't be NULL");
        Department existDepartment = departmentDao.getDepartmentByName(department.getName());
        if (existDepartment != null) {
            logger.error("Department with NAME = {} exists", department.getName());
            throw new IllegalArgumentException("Department with this NAME exist");
        }

        long id = departmentDao.addDepartment(department);
        logger.debug("addDepartment() : id = {}", id);
        return id;
    }

    /**
     * Remove department from DAO
     * @param id of department
     */
    public void removeDepartment(long id) {
        logger.debug("removeDepartment(id = {})", id);
        Assert.notNull(id, "Department ID can't be NULL");

        Department existDepartment;
        try {
            existDepartment = departmentDao.getDepartmentById(id);
            Assert.notNull(existDepartment, "Can't remove not existed department");
        } catch (EmptyResultDataAccessException ex) {
            logger.error("Can't remove department with ID = {}, because he doesn't exist.", id);
            throw new IllegalArgumentException("Department with this ID doesn't exist");
        }

        departmentDao.removeDepartment(id);
    }

    /**
     * Replaces the department in the DAO with the specified department.
     * @param department to be updated in the DAO
     */
    public void updateDepartment(Department department) {
        logger.debug("updateDepartment(department = {})", department);
        Assert.notNull(department, "Department can't be NULL");
        Assert.notNull(department.getId(), "Department ID can't be NULL");
        Assert.hasText(department.getName(), "Department NAME can't be NULL");
        Assert.isTrue(department.getName().length() < 100, "Department NAME can't be more then 100 chars");
        Assert.notNull(department.getEmployees(), "Department Employees can't be NULL");

        Department existDepartment;
        try {
            existDepartment = departmentDao.getDepartmentById(department.getId());
            Assert.notNull(existDepartment, "Can update not existed department. Or wrong ID!");
        } catch (EmptyResultDataAccessException ex) {
            logger.error("Can't update employee with ID = {}, because he didn't exist!");
            throw new IllegalArgumentException("Can't update employee what doesn't exist!");
        }

        departmentDao.updateDepartment(department);
    }

    /**
     * Returns a list containing all of the departments with specified employees in the DAO.
     * @return a list containing all of the departments with specified employees in the DAO
     */
    public List<Department> getAllDepartments() {
        logger.debug("DepartmentServiceImpl.getAllDepartments()");
        List<Department> departments = departmentDao.getAllDepartments();
        Assert.notEmpty(departments, "Empty list of departments");
        logger.debug("DepartmentServiceImpl.getAllDepartments() : departments = {}", departments);
        return departments;
    }

    /**
     * Returns a list containing all of the departments, but without specified employees in the DAO.
     * @return a list containing all of the departments, but without specified employees in the DAO
     */
    public List<Department> getAllDepartmentsWithoutEmployees() {
        logger.debug("getAllDepartmentsWithoutEmployees()");
        List<Department> departments = departmentDao.getAllDepartmentsWithoutEmployees();
        Assert.notEmpty(departments, "Empty list of departments");
        logger.debug("getAllDepartmentsWithoutEmployees() : departments = {}", departments);
        return departments;
    }

    /**
     * Returns the department with the specified departmentId from DAO.
     * @param id id of the department to return
     * @return the department with the specified departmentId from the DAO
     */
    public Department getDepartmentById(long id) {
        logger.debug("getDepartmentById(id = {})", id);
                Assert.notNull(id, "Department ID can't be NULL");
        Department department;
        try {
            department = departmentDao.getDepartmentById(id);
            Assert.notNull(department, "Department can't be NULL");
            Assert.hasText(department.getName(), "Department can't be without NAME");
        } catch (EmptyResultDataAccessException ex) {
            logger.error("Department with ID = '{}' doesn't exist", id);
            throw new IllegalArgumentException("Department with this ID doesn't exist");
        }
        logger.debug("getDepartmentById(id) : department = {}", department);
        return department;
    }

    /**
     * Returns the department with the specified departmentName from DAO.
     * @param name name of the department to return
     * @return the department with the specified departmentName from the DAO
     */
    public Department getDepartmentByName(String name) {
        logger.debug("getDepartmentByName(name = {})", name);
        Assert.hasText(name, "Department NAME can't be NULL");
        Assert.isTrue(name.length() < 100, "Department NAME can't be more then 100 chars");
        Department department;
        try {
            department = departmentDao.getDepartmentByName(name);
            Assert.notNull(department, "Department can't be NULL");
            Assert.hasText(department.getName(), "Department can't be without NAME");
        } catch (EmptyResultDataAccessException ex) {
            logger.error("Department with NAME = '{}' doesn't exist", name);
            throw new IllegalArgumentException("Department with this NAME doesn't exist");
        }
        logger.debug("getDepartmentByName(name) : department = {}", department);
        return department;
    }
}
