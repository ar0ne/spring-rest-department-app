package com.ar0ne.service;

import com.ar0ne.model.Employee;

import java.util.List;

public interface EmployeeService {

    public long addEmployee(Employee employee);
    public void removeEmployee(long id);
    public void updateEmployee(Employee employee);
    public List<Employee> getAllEmployees();
    public Employee getEmployeeById(long id);
    public List<Employee> getEmployeesByDepartmentId(long id);

}
