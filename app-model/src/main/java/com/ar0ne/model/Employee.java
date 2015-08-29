package com.ar0ne.model;

import org.joda.time.LocalDateTime;

import java.io.Serializable;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class Employee implements Serializable{

    private long            id;
    private long            department_id;
    private String          surname;
    private String          name;
    private String          patronymic;
    private long            salary;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime   date_of_birthday;

    public Employee(){
    }

    public Employee(long id, long department_id, String surname, String name,
                    String patronymic, LocalDateTime date_of_birthday, long salary) {
        this.id = id;
        this.department_id = department_id;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.date_of_birthday = date_of_birthday;
        this.salary = salary;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDepartmentId() {
        return department_id;
    }

    public void setDepartmentId(long department_id) {
        this.department_id = department_id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public LocalDateTime getDateOfBirthday() {
        return date_of_birthday;
    }

    public void setDateOfBirthday(LocalDateTime date_of_birthday) {
        this.date_of_birthday = date_of_birthday;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;

        if (id != employee.id) return false;
        if (department_id != employee.department_id) return false;
        if (salary != employee.salary) return false;
        if (!surname.equals(employee.surname)) return false;
        if (!name.equals(employee.name)) return false;
        if (!patronymic.equals(employee.patronymic)) return false;
        return date_of_birthday.equals(employee.date_of_birthday);

    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", department_id=" + department_id +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", date_of_birthday=" + date_of_birthday +
                ", salary=" + salary +
                '}';
    }
}
