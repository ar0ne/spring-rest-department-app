## Introduce

This demo application was written on Java and Spring 4 Framework.
In this application we have two *.war files, which can be deployed on your Apache Tomcat server.
First file is example of RESTful service. And second is Web application which use this service for displaying information.
Database stored on MySQL server.

In this example we have two entity: departments and employees. And we can get information about them from RESTful service in JSON format. Or we can work with her on html pages.

### How to use

1. Download source code:
`git clone https://github.com/ar0ne/spring-rest-department-app.git`

2. In folder you can find `demo_init.sql`. First you must create database on your server `spring_department_rest_app`. And then run it for initialization of tables.

3. Then you must create war-files:
`mvn install`

4. Copy war-files which you can find in `/app-rest/target/rest.war` and `/app-web/target/web.war` to your tomcat server `webapps` folder.

5. And then you can see result in browser:
`http://localhost:8080/web/index`
For REST service: `http://localhost:8080/rest/department`

## REST api

| URL                               | Method | Description                                        |
|-----------------------------------|--------|----------------------------------------------------|
| "/rest/department"                | GET    | Return all departments                             |
| "/rest/department/id/{id}"        | GET    | Return department by ID                            |
| "/rest/department/name/{name}"    | GET    | Return department by NAME                          |
| "/rest/department/create"         | POST   | Create new department                              |
| "/rest/department/update"         | POST   | Update department                                  |
| "/rest/department/delete/{id}"    | DELETE | Remove department by ID                            |
| "/rest/employee"                  | GET    | Return all employees                               |
| "/rest/employee/id/{id}"          | GET    | Return employee by ID                              |
| "/rest/employee/create"           | POST   | Create new employee                                |
| "/rest/employee/update"           | POST   | Update employee                                    |
| "/rest/employee/delete/{id}"      | DELETE | Remove employee by ID                              |
| "/rest/employee/date/{date}"      | GET    | Return employees by Date of Birthday               |
| "/rest/employee/date/{from}/{to}" | GET    | Return employees with Date of Birthday in interval |