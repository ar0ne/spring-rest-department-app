package com.ar0ne.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.LocalDate;

import java.io.Serializable;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class Employee implements Serializable {

    private long            id;
    private String          surname;
    private String          name;
    private String          patronymic;
    private long            salary;
    private long            departmentId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate       dateOfBirthday;

    public Employee(){
    }

    public Employee(long id, long departmentId, String surname, String name,
                    String patronymic, LocalDate dateOfBirthday, long salary) {
        this.id = id;
        this.departmentId = departmentId;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.dateOfBirthday = dateOfBirthday;
        this.salary = salary;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
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

    public LocalDate getDateOfBirthday() {
        return dateOfBirthday;
    }

    public void setDateOfBirthday(LocalDate dateOfBirthday) {
        this.dateOfBirthday = dateOfBirthday;
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
        if ( departmentId != employee.departmentId ) return false;
        if (salary != employee.salary) return false;
        if (!surname.equals(employee.surname)) return false;
        if (!name.equals(employee.name)) return false;
        if (!patronymic.equals(employee.patronymic)) return false;
        return dateOfBirthday.equals(employee.dateOfBirthday );

    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString( this, ToStringStyle.MULTI_LINE_STYLE );
    }
}
