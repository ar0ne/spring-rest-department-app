package com.ar0ne.model;


public class Department {

    private long        id;
    private String      name;

    public Department(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public Department(){

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

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
