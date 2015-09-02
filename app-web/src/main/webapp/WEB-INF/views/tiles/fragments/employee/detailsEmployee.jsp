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
            <h3>Employee details</h3>

            <a href="<spring:url value='/department' ></spring:url>" class="btn btn-default">Back to All Departments</a>

            <c:if test="${not empty employee}">
                <table class="table table-bordered table-striped table-hover">
                    <tr>
                        <th>Name</th>
                        <th>Surname</th>
                        <th>Patronymic</th>
                        <th>Date of Birthday</th>
                        <th>Salary</th>
                        <th>Edit</th>
                        <th>Delete</th>
                    </tr>
                    <tr>
                        <td>${employee.name}</td>
                        <td>${employee.surname}</td>
                        <td>${employee.patronymic}</td>
                        <td>${employee.dateOfBirthday}</td>
                        <td>${employee.salary}</td>
                        <td></td>
                        <td></td>
                    </tr>

                </table>
            </c:if>

        </div>

    </div>
</div>
