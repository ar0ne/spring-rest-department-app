package com.ar0ne.service;

import com.ar0ne.dao.EmployeeDao;
import com.ar0ne.model.Department;
import com.ar0ne.model.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.util.Assert;

import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeDao employeeDao;

    private static final Logger LOGGER = LogManager.getLogger();

    public void setEmployeeDao(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    public long addEmployee(Employee employee) {

        Assert.hasText(employee.getName(), "Employee NAME can't be NULL");
        Assert.notNull(employee.getSalary(), "Employee SALARY can't be NULL");
        Assert.hasText(employee.getSurname(), "Employee SURNAME can't be NULL");
        Assert.hasText(employee.getPatronymic(), "Employee Patronymic can't be NULL");
        Assert.notNull(employee.getDepartmentId(), "Employee Department ID can't be NULL");
        Assert.notNull(employee.getDateOfBirthday(), "Employee Date of Birthday can't be NULL");
        Assert.isTrue(employee.getName().length() < 100, "Employee Name can't be much then 100 chars");
        Assert.isTrue(employee.getSurname().length() < 100, "Employee Surname can't be much then 100 chars");
        Assert.isTrue(employee.getPatronymic().length() < 100, "Employee Patronymic can't be much then 100 chars");

        long id = employeeDao.addEmployee(employee);
        return id;
    }

    public void removeEmployee(long id) {
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

    public void updateEmployee(Employee employee) {

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

    public List<Employee> getAllEmployees() {
        List<Employee> employees = employeeDao.getAllEmployees();
        Assert.notEmpty(employees, "Empty list of employees");
        return employees;
    }

    public Employee getEmployeeById(long id) {
        Assert.notNull(id, "Employee ID can't be NULL");

        Employee employee = null;
        try {
            employee = employeeDao.getEmployeeById(id);
        } catch (EmptyResultDataAccessException ex) {
            LOGGER.error("Employee with ID = '{}' doesn't exist", id);
            throw new IllegalArgumentException("Employee with this ID doesn't exist");
        }

        return employee;
    }

}
