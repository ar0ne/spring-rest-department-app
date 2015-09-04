<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div id="container" class="container">
    <div class="row">
        <div class="col-md-8">

            <h2><a href="#introduce" name="introduce">Introduce</a></h2>
            <p>This demo application was written on Java and Spring 4 Framework.<br/>In this application we have two *.war files, which can be deployed on your Apache Tomcat server.<br/>First file is example of RESTful service. And second is Web application which use this service for displaying information.<br/>Database stored on MySQL server.</p>
            <p>In this example we have two entity: departments and employees. And we can get information about them from RESTful service in JSON format. Or we can work with her on html pages.</p>
            <h3><a href="#how-to-use" name="how-to-use">How to use</a></h3>
            <ol>
                <li>
                    <p>Download source code:<br/><code>git clone https://github.com/ar0ne/spring-rest-department-app.git</code></p></li>
                <li>
                    <p>In folder you can find <code>demo_init.sql</code>. First you must create database on your server <code>spring_department_rest_app</code>. And then run it for initialization of tables.</p></li>
                <li>
                    <p>Then you must create war-files:<br/><code>mvn install</code></p></li>
                <li>
                    <p>Copy war-files which you can find in <code>/app-rest/target/rest.war</code> and <code>/app-web/target/web.war</code> to your tomcat server <code>webapps</code> folder.</p></li>
                <li>
                    <p>And then you can see result in browser:<br/><code>http://localhost:8080/web/index</code><br/>For REST service: <code>http://localhost:8080/rest/department</code></p></li>
            </ol>
            <h2><a href="#rest-api" name="rest-api">REST api</a></h2>
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>URL </th>
                    <th>Method </th>
                    <th>Description </th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>"/rest/department" </td>
                    <td>GET </td>
                    <td>Return all departments </td>
                </tr>
                <tr>
                    <td>"/rest/department/id/{id}" </td>
                    <td>GET </td>
                    <td>Return department by ID </td>
                </tr>
                <tr>
                    <td>"/rest/department/name/{name}" </td>
                    <td>GET </td>
                    <td>Return department by NAME </td>
                </tr>
                <tr>
                    <td>"/rest/department/create" </td>
                    <td>POST </td>
                    <td>Create new department </td>
                </tr>
                <tr>
                    <td>"/rest/department/update" </td>
                    <td>POST </td>
                    <td>Update department </td>
                </tr>
                <tr>
                    <td>"/rest/department/delete/{id}" </td>
                    <td>DELETE </td>
                    <td>Remove department by ID </td>
                </tr>
                <tr>
                    <td>"/rest/employee" </td>
                    <td>GET </td>
                    <td>Return all employees </td>
                </tr>
                <tr>
                    <td>"/rest/employee/id/{id}" </td>
                    <td>GET </td>
                    <td>Return employee by ID </td>
                </tr>
                <tr>
                    <td>"/rest/employee/create" </td>
                    <td>POST </td>
                    <td>Create new employee </td>
                </tr>
                <tr>
                    <td>"/rest/employee/update" </td>
                    <td>POST </td>
                    <td>Update employee </td>
                </tr>
                <tr>
                    <td>"/rest/employee/delete/{id}" </td>
                    <td>DELETE </td>
                    <td>Remove employee by ID </td>
                </tr>
                <tr>
                    <td>"/rest/employee/date/{date}" </td>
                    <td>GET </td>
                    <td>Return employees by Date of Birthday </td>
                </tr>
                <tr>
                    <td>"/rest/employee/date/{from}/{to}" </td>
                    <td>GET </td>
                    <td>Return employees with Date of Birthday in interval </td>
                </tr>
                </tbody>
            </table>


        </div>
    </div>
</div>