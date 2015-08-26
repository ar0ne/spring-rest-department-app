package com.ar0ne.dao;

import com.ar0ne.model.Employee;
import java.util.List;

public interface EmployeeDao {

    public void addEmployee(Employee employee);
    public void removeEmployee(long id);
    public void updateEmployee(Employee employee);
    public List<Employee> getAllEmployees();
    public Employee getEmployeeById(long id);

}
