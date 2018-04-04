package com.ar0ne.service;

import com.ar0ne.model.Department;

import java.util.List;

/**
 * A simple DepartmentService interface to handle the operation required
 * to manipulate a Department entity.
 */
public interface DepartmentService {

    /**
     * Insert specified department to the database
     * @param department department to be inserted to the database
     * @return id of department in database
     */
    long addDepartment(Department department);

    /**
     * Remove department from database
     * @param id of department
     */
    void removeDepartment(long id);

    /**
     * Replaces the department in the database with the specified department.
     * @param department to be updated in the database
     */
    void updateDepartment(Department department);

    /**
     * Returns a list containing all of the departments with specified employees in the database.
     * @return a list containing all of the departments with specified employees in the database
     */
    List<Department> getAllDepartments();

    /**
     * Returns a list containing all of the departments, but without specified employees in the database.
     * @return a list containing all of the departments, but without specified employees in the database
     */
    List<Department> getAllDepartmentsWithoutEmployees();

    /**
     * Returns the department with the specified departmentId from database.
     * @param id id of the department to return
     * @return the department with the specified departmentId from the database
     */
    Department getDepartmentById(long id);

    /**
     * Returns the department with the specified departmentName from database.
     * @param name name of the department to return
     * @return the department with the specified departmentName from the database
     */
    Department getDepartmentByName(String name);

}
