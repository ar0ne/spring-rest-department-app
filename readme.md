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

4. Copy war-files what you can find in `/app-rest/target/rest.war` and `/app-web/target/web.war` to your tomcat server `webapps` folder.

5. And now you can see result in browser:
`http://localhost:8080/web/index`
For REST service: `http://localhost:8080/rest/department`

## REST api

| URL                          | Method | Description                                        |
|------------------------------|--------|----------------------------------------------------|
| "/department"                | GET    | Return all departments                             |
| "/department/id/{id}"        | GET    | Return department by ID                            |
| "/department/name/{name}"    | GET    | Return department by NAME                          |
| "/department/create"         | POST   | Create new department                              |
| "/department/update"         | POST   | Update department                                  |
| "/department/delete/{id}"    | DELETE | Remove department by ID                            |
| "/employee"                  | GET    | Return all employees                               |
| "/employee/id/{id}"          | GET    | Return employee by ID                              |
| "/employee/create"           | POST   | Create new employee                                |
| "/employee/update"           | POST   | Update employee                                    |
| "/employee/delete/{id}"      | DELETE | Remove employee by ID                              |
| "/employee/date/{date}"      | GET    | Return employees by Date of Birthday               |
| "/employee/date/{from}/{to}" | GET    | Return employees with Date of Birthday in interval |