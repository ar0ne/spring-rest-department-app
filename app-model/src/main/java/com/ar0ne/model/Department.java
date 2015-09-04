package com.ar0ne.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Department implements Serializable{

    private long            id;
    private String          name;
    private List<Employee>  employees;

    public Department(){
        this.id = 0;
        this.name = "";
        this.employees = new ArrayList<Employee>();
    }

    public Department(String name, long id) {
        this.name = name;
        this.id = id;
        this.employees = new ArrayList<Employee>();
    }

    public Department(long id, String name, List<Employee> employees) {
        this.id = id;
        this.name = name;
        this.employees = employees;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public void addEmployee(Employee employee) {
        if (this.employees == null) {
            this.employees = new ArrayList<Employee>();
        }
        this.employees.add(employee);
    }

    /**
     * Calculate average salary of employees from one department
     * @return float value of average salary of employees
     */
    public float calcAverageSalary() {
        int size = this.employees.size();
        if(size == 0) {
            return 0.0f;
        }
        float sum = 0.0f;
        for (Employee employee: this.employees) {
            sum += employee.getSalary();
        }
        return sum / size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Department that = (Department) o;

        if (id != that.id) return false;
        if (!name.equals(that.name)) return false;
        return !(employees != null ? !employees.equals(that.employees) : that.employees != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + (employees != null ? employees.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", employees=" + employees +
                '}';
    }

    /**
     * Custom sort in alphabet order by name.
     * Ex: From A to Z
     */
    public class SortByDepartmentName implements Comparator<Department> {
        public int compare(Department d1, Department d2) {
            return d1.getName().compareTo(d2.getName());
        }
    }

}
