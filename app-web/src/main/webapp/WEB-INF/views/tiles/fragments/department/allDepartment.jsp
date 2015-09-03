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
            <h3>All Departments</h3>

            <a href="<spring:url value='/department/create' ></spring:url>" class="btn btn-default" role="button" id="add_department__button">Add department</a>
            <br/>

            <c:if test="${not empty departmentList}">
                <table class="table">
                    <tr>
                        <th>Department name</th>
                        <th>Average salary</th>
                        <th>Edit</th>
                        <th>Delete</th>
                    </tr>
                    <c:forEach items="${departmentList}" var="department">
                        <tr>
                            <td>
                                <a href='<spring:url value="/department/id/${department.id}"></spring:url>'>${department.name}</a>
                            </td>
                            <td>
                                ${department_avg_salary_map[department.id]}
                            </td>
                            <td>
                                <a href='<spring:url value="/department/update/${department.id}"></spring:url>' class="btn btn-warning" id="update_department__button" role="button">Edit</a>
                            </td>
                            <td>
                                <a href='<spring:url value="/department/delete/${department.id}"></spring:url>' class="btn btn-danger" id="delete_department__button" role="button">Delete</a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>
        </div>
    </div>
</div>
