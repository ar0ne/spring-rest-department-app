package com.ar0ne.service.impl;

import com.ar0ne.dao.EmployeeDao;
import com.ar0ne.model.Employee;
import com.ar0ne.service.EmployeeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeeServiceImpl implements EmployeeService
{
    private static final Logger logger = LogManager.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeDao employeeDao;

    /**
     * Insert specified employee to DAO
     * @param employee employee to be inserted to DAO
     * @return id of employee in DAO
     */
    public long addEmployee(Employee employee) {

        logger.debug("addEmployee(employee = {})", employee);
        Assert.hasText(employee.getName(), "Employee NAME can't be NULL");
        Assert.notNull(employee.getSalary(), "Employee SALARY can't be NULL");
        Assert.hasText(employee.getSurname(), "Employee SURNAME can't be NULL");
        Assert.hasText(employee.getPatronymic(), "Employee Patronymic can't be NULL");
        Assert.notNull(employee.getDepartmentId(), "Employee Department ID can't be NULL");
        Assert.notNull(employee.getDateOfBirthday(), "Employee Date of Birthday can't be NULL");
        Assert.isTrue(employee.getName().length() < 100, "Employee Name can't be much then 100 chars");
        Assert.isTrue(employee.getSurname().length() < 100, "Employee Surname can't be much then 100 chars");
        Assert.isTrue(employee.getPatronymic().length() < 100, "Employee Patronymic can't be much then 100 chars");
        Assert.isTrue(employee.getSalary() >= 0L, "Employee Salary can't be negative");

        long id = employeeDao.addEmployee(employee);
        logger.debug("addEmployee(employee) : id = {}", id);
        return id;
    }

    /**
     * Remove employee from DAO
     * @param id of employee
     */
    public void removeEmployee(long id) {
        logger.debug("removeEmployee(id = {})", id);
                Assert.notNull(id, "Employee ID can't be NULL");

        Employee existEmployee;
        try {
            existEmployee = employeeDao.getEmployeeById(id);
            Assert.notNull(existEmployee, "Can't remove employee with this ID");
        } catch (EmptyResultDataAccessException ex) {
            logger.error("Can't remove employee with ID = {}, because he doesn't exist.", id);
            throw new IllegalArgumentException("Employee with this ID doesn't exist");
        }

        employeeDao.removeEmployee(id);
    }

    /**
     * Replaces the employee in the database with the specified employee.
     * @param employee to be employee in DAO
     */
    public void updateEmployee(Employee employee) {
        logger.debug("updateEmployee(employee = {})", employee);
        Assert.notNull(employee, "Employee can't be NULL");
        Assert.notNull(employee.getId(), "Employee ID cant be NULL");
        Assert.hasText(employee.getName(), "Employee NAME cant be NULL");
        Assert.notNull(employee.getSalary(), "Employee SALARY can't be NULL");
        Assert.hasText(employee.getSurname(), "Employee SURNAME cant be NULL");
        Assert.hasText(employee.getPatronymic(), "Employee PATRONYMIC cant be NULL");
        Assert.notNull(employee.getDepartmentId(), "Employee DEPARTMENT_ID cant be NULL");
        Assert.notNull(employee.getDateOfBirthday(), "Employee DATE_OF_BIRTHDAY cant be NULL");
        Assert.isTrue(employee.getName().length() < 100, "Employee Name can't be much then 100 chars");
        Assert.isTrue(employee.getSurname().length() < 100, "Employee Surname can't be much then 100 chars");
        Assert.isTrue(employee.getPatronymic().length() < 100, "Employee Patronymic can't be much then 100 chars");
        Assert.isTrue(employee.getSalary() >= 0L, "Employee Salary can't be negative");

        Employee existEmployee = null;
        try {
            existEmployee = employeeDao.getEmployeeById(employee.getId());
            Assert.notNull(existEmployee, "Can't update not existed employee. Or wrong ID!");
        } catch (EmptyResultDataAccessException ex) {
            logger.error("Can't update employee with ID = {}, because he didn't exist!");
            throw new IllegalArgumentException("Can't update employee what doesn't exist!");
        }

        employeeDao.updateEmployee(employee);
    }

    /**
     * Returns a list containing all of the employees in DAO.
     * @return a list containing all of the employees in DAO
     */
    public List<Employee> getAllEmployees() {
        logger.debug("getAllEmployees()");
        List<Employee> employees = employeeDao.getAllEmployees();
        Assert.notEmpty(employees, "Empty list of employees");
        logger.debug("getAllEmployees() : employees = {}", employees);
        return employees;
    }

    /**
     * Returns the employee with the specified employeetId from DAO.
     * @param id id of the employee to return
     * @return the employee with the specified employeeId from the database
     */
    public Employee getEmployeeById(long id) {
        logger.debug("getEmployeeById(id = {})", id);
        Assert.notNull(id, "Employee ID can't be NULL");

        Employee employee;
        try {
            employee = employeeDao.getEmployeeById(id);
        } catch (EmptyResultDataAccessException ex) {
            logger.error("Employee with ID = '{}' doesn't exist", id);
            throw new IllegalArgumentException("Employee with this ID doesn't exist");
        }
        logger.debug("getEmployeeById(id) : employee = {}", employee);
        return employee;
    }

    /**
     * Returns a list of employees with the specified dateOfBirthday from DAO
     * @param date Date of Birthday of the employees to return
     * @return list of the employees in the database
     */
    public List<Employee> getEmployeeByDateOfBirthday(LocalDate date) {
        logger.debug("getEmployeeByDateOfBirthday(date = {})", date);
        Assert.notNull(date, "Date of birthday can't be NULL");
        List<Employee> employeeList;
        try {
            employeeList = employeeDao.getEmployeeByDateOfBirthday(date);
        } catch (EmptyResultDataAccessException ex) {
            logger.error("Employees with date = {} not exist", date);
            throw new IllegalArgumentException("Employee with this date doesn't exist!");
        }

        return employeeList != null ? employeeList : new ArrayList<>();
    }

    /**
     * Returns a list of employees with the specified dateOfBirthday in interval from-to DAO
     * @param dateFrom start date of interval for searching
     * @param dateTo end date of interval for searching
     * @return list of the employees in the database
     */
    public List<Employee> getEmployeeBetweenDatesOfBirtday(LocalDate dateFrom, LocalDate dateTo ) {
        logger.debug("getEmployeeBetweenDatesOfBirtday(date_from = {}, date_to = {}", dateFrom, dateTo );
        Assert.notNull( dateFrom, "Date From cant be NULL");
        Assert.notNull( dateTo, "Date To cant be NULL");
        Assert.isTrue( dateFrom.isBefore( dateTo ), "Date From can't be earlier then Date To");

        List<Employee> employeeList;
        try {
            employeeList = employeeDao.getEmployeeBetweenDatesOfBirtday( dateFrom, dateTo );
        } catch (EmptyResultDataAccessException ex) {
            logger.error("Employees between date_from = {} and date_to not exist", dateFrom, dateTo);
            throw new IllegalArgumentException("Employee with this date doesn't exist!");
        }

        return employeeList != null ? employeeList : new ArrayList<>();
    }
}
