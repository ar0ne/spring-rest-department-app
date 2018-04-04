package com.ar0ne.model.test;

import com.ar0ne.model.Department;
import com.ar0ne.model.Employee;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class DepartmentModelTest {

    private Employee createEmployeeWithSalary(long salary) {
        return new Employee(1L, 1L, "Surname", "Name", "Patronymic", new LocalDate("1999-02-02"), salary);
    }

    @Test
    public void calcAverageSalaryTestWithEmptyEmployees() {
        Department department = new Department();
        department.setEmployees(new ArrayList<Employee>());
        float result = department.calcAverageSalary();
        Assert.assertTrue(result == 0.0f);
    }

    @Test
    public void calcAverageSalaryTestWithOneEmployees() {
        Department department = new Department();
        List<Employee> employees = new ArrayList<Employee>();
        employees.add(createEmployeeWithSalary(100L));
        department.setEmployees(employees);
        float result = department.calcAverageSalary();
        Assert.assertTrue(result == 100f);
    }

    @Test
    public void calcAverageSalaryTestWithTwoEmployees() {
        Department department = new Department();
        List<Employee> employees = new ArrayList<Employee>();
        employees.add(createEmployeeWithSalary(100L));
        employees.add(createEmployeeWithSalary(100L));
        department.setEmployees(employees);
        float result = department.calcAverageSalary();
        Assert.assertTrue(result == 100f);
    }

    @Test
    public void calcAverageSalaryTestWithTwoEmployeesAndOneZero() {
        Department department = new Department();
        List<Employee> employees = new ArrayList<Employee>();
        employees.add(createEmployeeWithSalary(100L));
        employees.add(createEmployeeWithSalary(0L));
        department.setEmployees(employees);
        float result = department.calcAverageSalary();
        Assert.assertTrue(result == 50f);
    }

    @Test
    public void sortByDepartmentName1() {
        Department department1 = new Department("A", 1L);
        Department department2 = new Department("B", 1L);
        Department department3 = new Department("C", 1L);
        Department department4 = new Department("D", 1L);

        List<Department> list = new ArrayList<Department>();
        list.add(department2);
        list.add(department4);
        list.add(department3);
        list.add(department1);

        Department.SortByDepartmentName sort = new Department().new SortByDepartmentName();
        list.sort( sort );

        Assert.assertEquals(list.get(0).getName(), "A");
        Assert.assertEquals(list.get(1).getName(), "B");
        Assert.assertEquals(list.get(2).getName(), "C");
        Assert.assertEquals(list.get(3).getName(), "D");
    }


    @Test
    public void sortByDepartmentName2() {
        Department department1 = new Department("A", 1L);
        Department department2 = new Department("B", 1L);
        Department department3 = new Department("C", 1L);
        Department department4 = new Department("D", 1L);

        List<Department> list = new ArrayList<Department>();
        list.add(department3);
        list.add(department1);
        list.add(department4);
        list.add(department2);

        Department.SortByDepartmentName sort = new Department().new SortByDepartmentName();
        list.sort( sort );

        Assert.assertEquals(list.get(0).getName(), "A");
        Assert.assertEquals(list.get(1).getName(), "B");
        Assert.assertEquals(list.get(2).getName(), "C");
        Assert.assertEquals(list.get(3).getName(), "D");
    }


}
