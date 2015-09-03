<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="container" id="container">
    <div class="row">

        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>

        <c:if test="${not empty message}">
            <div class="message">${message}</div>
        </c:if>


        <div class="col-md-8">
            <h3>Update Department</h3>

            <a href="<spring:url value='/department' ></spring:url>" class="btn btn-default">Back to All Departments</a>

            <form action="<spring:url value='/employee/update/' ></spring:url>" method="POST">

                <label for="name">Name: </label>
                <input type="text" name="name" class="form-control" maxlength="255" minlength="2" required autofocus value="${employee.name}"/><br/>

                <label for="surname">Name: </label>
                <input type="text" name="surname" class="form-control" maxlength="255" minlength="2" required  value="${employee.surname}" /><br/>

                <label for="patronymic">Name: </label>
                <input type="text" name="patronymic" class="form-control" maxlength="255" minlength="2" required value="${employee.patronymic}" /><br/>

                <label for="salary">Name: </label>
                <input type="number" name="salary" class="form-control" maxlength="255" minlength="2" required value="${employee.salary}" /><br/>

                <label for="department__select">Department</label>
                <select id="department__select" class="form-control" name="department_id">
                    <c:forEach items="${departments}" var="department">
                        <c:choose>
                            <c:when test="${department.id == employee.departmentId}">
                                <option value="${department.id}" selected>${department.name}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${department.id}">${department.name}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>

                <label for="datetimepicker__wrapper">Date of Birthday</label>
                <div id="datetimepicker__wrapper" class="">
                    <div id="datetimepicker" class="input-append date input-group">
                        <input data-format="yyyy-MM-dd" type="text" class="form-control" name="date_of_birthday" value="${employee.dateOfBirthday}"/>
                        <span class="add-on input-group-addon">
                            <span class="glyphicon glyphicon-calendar"></span>
                        </span>
                    </div>
                </div>

                <input type="hidden" name="id" value="${employee.id}"/>

                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <br/>
                <button type="submit" class="btn btn-success">Save</button>

            </form>
        </div>

    </div>
</div>
