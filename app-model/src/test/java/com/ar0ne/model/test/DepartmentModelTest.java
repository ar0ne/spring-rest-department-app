package com.ar0ne.model.test;

import com.ar0ne.model.Department;
import com.ar0ne.model.Employee;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class DepartmentModelTest {

    private final Employee createEmployeeWithSalary(long salary) {
        return new Employee(1l, 1l, "Surname", "Name", "Patronymic", new LocalDate("1999-02-02"), salary);
    }

    @Test
    public void calcAverageSalaryTestWithEmptyEmployees() {
        Department department = new Department();
        department.setEmployees(new ArrayList<Employee>());
        float result = department.calcAverageSalary();
        assertTrue(result == 0.0f);
    }

    @Test
    public void calcAverageSalaryTestWithOneEmployees() {
        Department department = new Department();
        List<Employee> employees = new ArrayList<Employee>();
        employees.add(createEmployeeWithSalary(100l));
        department.setEmployees(employees);
        float result = department.calcAverageSalary();
        assertTrue(result == 100f);
    }

    @Test
    public void calcAverageSalaryTestWithTwoEmployees() {
        Department department = new Department();
        List<Employee> employees = new ArrayList<Employee>();
        employees.add(createEmployeeWithSalary(100l));
        employees.add(createEmployeeWithSalary(100l));
        department.setEmployees(employees);
        float result = department.calcAverageSalary();
        assertTrue(result == 100f);
    }

    @Test
    public void calcAverageSalaryTestWithTwoEmployeesAndOneZero() {
        Department department = new Department();
        List<Employee> employees = new ArrayList<Employee>();
        employees.add(createEmployeeWithSalary(100l));
        employees.add(createEmployeeWithSalary(0l));
        department.setEmployees(employees);
        float result = department.calcAverageSalary();
        assertTrue(result == 50f);
    }

    @Test
    public void sortByDepartmentName1() {
        Department department1 = new Department("A", 1l);
        Department department2 = new Department("B", 1l);
        Department department3 = new Department("C", 1l);
        Department department4 = new Department("D", 1l);

        List<Department> list = new ArrayList<Department>();
        list.add(department2);
        list.add(department4);
        list.add(department3);
        list.add(department1);

        Department.SortByDepartmentName sort = new Department().new SortByDepartmentName();
        Collections.sort(list, sort);

        assertEquals(list.get(0).getName(), "A");
        assertEquals(list.get(1).getName(), "B");
        assertEquals(list.get(2).getName(), "C");
        assertEquals(list.get(3).getName(), "D");
    }


    @Test
    public void sortByDepartmentName2() {
        Department department1 = new Department("A", 1l);
        Department department2 = new Department("B", 1l);
        Department department3 = new Department("C", 1l);
        Department department4 = new Department("D", 1l);

        List<Department> list = new ArrayList<Department>();
        list.add(department3);
        list.add(department1);
        list.add(department4);
        list.add(department2);

        Department.SortByDepartmentName sort = new Department().new SortByDepartmentName();
        Collections.sort(list, sort);

        assertEquals(list.get(0).getName(), "A");
        assertEquals(list.get(1).getName(), "B");
        assertEquals(list.get(2).getName(), "C");
        assertEquals(list.get(3).getName(), "D");
    }


}
