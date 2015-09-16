package com.ar0ne.service;

import com.ar0ne.dao.EmployeeDao;
import com.ar0ne.model.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

@Component
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeDao employeeDao;

    private static final Logger LOGGER = LogManager.getLogger(EmployeeServiceImpl.class);

    /**
     * Insert specified employee to the database
     * @param employee employee to be inserted to the database
     * @return id of employee in database
     */
    public long addEmployee(Employee employee) {

        LOGGER.debug("addEmployee(employee = {})", employee);
        Assert.hasText(employee.getName(), "Employee NAME can't be NULL");
        Assert.notNull(employee.getSalary(), "Employee SALARY can't be NULL");
        Assert.hasText(employee.getSurname(), "Employee SURNAME can't be NULL");
        Assert.hasText(employee.getPatronymic(), "Employee Patronymic can't be NULL");
        Assert.notNull(employee.getDepartmentId(), "Employee Department ID can't be NULL");
        Assert.notNull(employee.getDateOfBirthday(), "Employee Date of Birthday can't be NULL");
        Assert.isTrue(employee.getName().length() < 100, "Employee Name can't be much then 100 chars");
        Assert.isTrue(employee.getSurname().length() < 100, "Employee Surname can't be much then 100 chars");
        Assert.isTrue(employee.getPatronymic().length() < 100, "Employee Patronymic can't be much then 100 chars");
        Assert.isTrue(employee.getSalary() >= 0l, "Employee Salary can't be negative");

        long id = employeeDao.addEmployee(employee);
        LOGGER.debug("addEmployee(employee) : id = {}", id);
        return id;
    }

    /**
     * Remove employee from database
     * @param id of employee
     */
    public void removeEmployee(long id) {
        LOGGER.debug("removeEmployee(id = {})", id);
                Assert.notNull(id, "Employee ID can't be NULL");

        Employee existEmployee = null;
        try {
            existEmployee = employeeDao.getEmployeeById(id);
            Assert.notNull(existEmployee, "Can't remove employee with this ID");
        } catch (EmptyResultDataAccessException ex) {
            LOGGER.error("Can't remove employee with ID = {}, because he doesn't exist.", id);
            throw new IllegalArgumentException("Employee with this ID doesn't exist");
        }

        employeeDao.removeEmployee(id);
    }

    /**
     * Replaces the employee in the database with the specified employee.
     * @param employee to be employee in the database
     */
    public void updateEmployee(Employee employee) {
        LOGGER.debug("updateEmployee(employee = {})", employee);
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
        Assert.isTrue(employee.getSalary() >= 0l, "Employee Salary can't be negative");

        Employee existEmployee = null;
        try {
            existEmployee = employeeDao.getEmployeeById(employee.getId());
            Assert.notNull(existEmployee, "Can't update not existed employee. Or wrong ID!");
        } catch (EmptyResultDataAccessException ex) {
            LOGGER.error("Can't update employee with ID = {}, because he didn't exist!");
            throw new IllegalArgumentException("Can't update employee what doesn't exist!");
        }

        employeeDao.updateEmployee(employee);
    }

    /**
     * Returns a list containing all of the employees in the database.
     * @return a list containing all of the employees in the database
     */
    public List<Employee> getAllEmployees() {
        LOGGER.debug("getAllEmployees()");
        List<Employee> employees = employeeDao.getAllEmployees();
        Assert.notEmpty(employees, "Empty list of employees");
        LOGGER.debug("getAllEmployees() : employees = {}", employees);
        return employees;
    }

    /**
     * Returns the employee with the specified employeetId from database.
     * @param id id of the employee to return
     * @return the employee with the specified employeeId from the database
     */
    public Employee getEmployeeById(long id) {
        LOGGER.debug("getEmployeeById(id = {})", id);
        Assert.notNull(id, "Employee ID can't be NULL");

        Employee employee = null;
        try {
            employee = employeeDao.getEmployeeById(id);
        } catch (EmptyResultDataAccessException ex) {
            LOGGER.error("Employee with ID = '{}' doesn't exist", id);
            throw new IllegalArgumentException("Employee with this ID doesn't exist");
        }
        LOGGER.debug("getEmployeeById(id) : employee = {}", employee);
        return employee;
    }

    /**
     * Returns list of employees with the specified dateOfBirthday from database
     * @param date Date of Birthday of the employees to return
     * @return list of the employees in the database
     */
    public List<Employee> getEmployeeByDateOfBirthday(LocalDate date) {
        LOGGER.debug("getEmployeeByDateOfBirthday(date = {})", date);
        Assert.notNull(date, "Date of birthday can't be NULL");
        List<Employee> employeeList = null;
        try {
            employeeList = employeeDao.getEmployeeByDateOfBirthday(date);
            Assert.notNull(employeeList, "Employee can't be NULL");
            Assert.notEmpty(employeeList, "Employee can't be empty");
        } catch (EmptyResultDataAccessException ex) {
            LOGGER.error("Employees with date = {} not exist", date.toString());
            throw new IllegalArgumentException("Employee with this date doesn't exist!");
        }

        LOGGER.debug("getEmployeeByDateOfBirthday(date}) : employeeList = {}", employeeList);
        return employeeList;
    }

    /**
     * Returns list of employees with the specified dateOfBirthday in interval from-to from database
     * @param date_from start date of interval for searching
     * @param date_to end date of interval for searching
     * @return list of the employees in the database
     */
    public List<Employee> getEmployeeBetweenDatesOfBirtday(LocalDate date_from, LocalDate date_to) {
        LOGGER.debug("getEmployeeBetweenDatesOfBirtday(date_from = {}, date_to = {}", date_from, date_to);
        Assert.notNull(date_from, "Date From cant be NULL");
        Assert.notNull(date_to, "Date To cant be NULL");
        Assert.isTrue(date_from.isBefore(date_to), "Date From can't be earlier then Date To");

        List<Employee> employeeList = null;
        try {
            employeeList = employeeDao.getEmployeeBetweenDatesOfBirtday(date_from, date_to);
            Assert.notNull(employeeList, "Employee can't be NULL");
            Assert.notEmpty(employeeList, "Employee can't be empty");
        } catch (EmptyResultDataAccessException ex) {
            LOGGER.error("Employees between date_from = {} and date_to not exist", date_from.toString(), date_to.toString());
            throw new IllegalArgumentException("Employee with this date doesn't exist!");
        }

        LOGGER.debug("getEmployeeBetweenDatesOfBirtday(date_from, date_to) : employeeList = {}", employeeList);
        return employeeList;


    }
}
