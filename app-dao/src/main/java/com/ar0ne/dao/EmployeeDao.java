package com.ar0ne.dao;

import com.ar0ne.model.Employee;
import java.util.List;

public interface EmployeeDao {

    public long addEmployee(Employee employee);
    public void removeEmployee(long id);
    public void updateEmployee(Employee employee);
    public List<Employee> getAllEmployees();
    public List<Employee> getEmployeesByDepartmentId(long id);
    public Employee getEmployeeById(long id);

}
