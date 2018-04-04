package com.ar0ne.dao;

import com.ar0ne.model.Employee;
import org.joda.time.LocalDate;

import java.util.List;

/**
 * A simple DAO interface to handle the database operation
 * required to manipulate a Employee entity.
 */
public interface EmployeeDao {
    /**
     * Insert specified employee to the database
     * @param employee employee to be inserted to the database
     * @return id of employee in database
     */
    long addEmployee(Employee employee);

    /**
     * Remove employee from database
     * @param id of employee
     */
    void removeEmployee(long id);

    /**
     * Replaces the employee in the database with the specified employee.
     * @param employee to be employee in the database
     */
    void updateEmployee(Employee employee);

    /**
     * Returns a list containing all of the employees in the database.
     * @return a list containing all of the employees in the database
     */
    List<Employee> getAllEmployees();

    /**
     * Returns the employee with the specified employeetId from database.
     * @param id id of the employee to return
     * @return the employee with the specified employeeId from the database
     */
    Employee getEmployeeById(long id);

    /**
     * Returns list of employees with the specified dateOfBirthday from database
     * @param date Date of Birthday of the employees to return
     * @return list of the employees in the database
     */
    List<Employee> getEmployeeByDateOfBirthday(LocalDate date);

    /**
     * Returns list of employees with the specified dateOfBirthday in interval from-to from database
     * @param dateFrom start date of interval for searching
     * @param dateTo end date of interval for searching
     * @return list of the employees in the database
     */
    List<Employee> getEmployeeBetweenDatesOfBirtday(LocalDate dateFrom, LocalDate dateTo);
}
