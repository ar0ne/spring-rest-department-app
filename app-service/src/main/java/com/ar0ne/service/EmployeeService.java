package com.ar0ne.service;

import com.ar0ne.model.Employee;
import org.joda.time.LocalDate;

import java.util.List;

public interface EmployeeService {

    public long addEmployee(Employee employee);
    public void removeEmployee(long id);
    public void updateEmployee(Employee employee);
    public List<Employee> getAllEmployees();
    public Employee getEmployeeById(long id);
    public List<Employee> getEmployeeByDateOfBirthday(LocalDate date);
    public List<Employee> getEmployeeBetweenDatesOfBirtday(LocalDate date_from, LocalDate date_to);

}
