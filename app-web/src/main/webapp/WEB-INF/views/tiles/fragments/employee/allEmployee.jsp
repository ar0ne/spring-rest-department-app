<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="container">
    <div class="row">

        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>

        <c:if test="${not empty message}">
            <div class="message">${message}</div>
        </c:if>

        <div class="col-md-8">

            <h3>All Employees</h3>

            <p>
                <a href="<spring:url value='/department' ></spring:url>" class="btn btn-default">All Departments</a>
            </p>

            <c:if test="${not empty employees}">
                <table class="table table-bordered table-striped table-hover">
                    <tr>
                        <th>Name</th>
                        <th>Surname</th>
                        <th>Patronymic</th>
                        <th>Date of Birthday</th>
                        <th>Salary</th>
                        <th>Department</th>
                        <th>Edit</th>
                        <th>Delete</th>
                    </tr>
                    <c:forEach items="${employees}" var="employee">
                        <tr>
                            <td>${employee.name}</td>
                            <td>${employee.surname}</td>
                            <td>${employee.patronymic}</td>
                            <td>${employee.dateOfBirthday}</td>
                            <td>${employee.salary}</td>
                            <td><a href="<spring:url value='/department/id/${employee.departmentId}' ></spring:url>" class="btn btn-default">See</a></td>
                            <td><a href="<spring:url value='/employee/update/${employee.id}' ></spring:url>" class="btn btn-default">Edit</a></td>
                            <td><a href="<spring:url value='/employee/delete/${employee.id}' ></spring:url>" class="btn btn-default">Delete</a></td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>

            <div class="row">
                <div class="col-md-8">

                    <h3>Search employee by date of birthday</h3>

                    <label for="search_date_1">Date of Birthday(or begin of interval)</label>
                    <div id="search_date_1" class="">
                        <div id="datetimepicker1" class="input-append date">
                            <input data-format="yyyy-MM-dd" type="text" />
                            <span class="add-on">
                                <i data-time-icon="icon-time" data-date-icon="icon-calendar"></i>
                            </span>
                        </div>
                    </div>

                    <label for="search_date_2">end of interval</label>
                    <div id="search_date_2" class="">
                        <div id="datetimepicker2" class="input-append date">
                            <input data-format="yyyy-MM-dd" type="text" />
                            <span class="add-on">
                                <i data-time-icon="icon-time" data-date-icon="icon-calendar"></i>
                            </span>
                        </div>
                    </div>

                    <a href="#" id="search__button" class="btn btn-success">Find</a>

                    <div id="domain_url" class="hidden"><spring:url value='/employee' ></spring:url></div>

                </div>
            </div>

        </div>
    </div>
</div>
