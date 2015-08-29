package com.ar0ne.service;

import com.ar0ne.model.Department;

import java.util.List;

public interface DepartmentService {

    public long addDepartment(Department department);
    public void removeDepartment(long id);
    public void updateDepartment(Department department);
    public List<Department> getAllDepartments();
    public Department getDepartmentById(long id);
    public Department getDepartmentByName(String name);

}
