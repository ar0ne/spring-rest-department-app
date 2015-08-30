<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="container" id="container">
    <div class="row">

        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>

        <div class="col-md-8">
            <h3>Departments</h3>
            <c:if test="${not empty departmentMap}">
                <table class="table">
                    <tr>
                        <th>Department name</th>
                        <th>Average salary</th>
                    </tr>
                    <c:forEach items="${departmentMap}" var="entry">
                        <tr>
                            <td>
                                <a href='<spring:url value="/department/id/${entry.key.id}"></spring:url>'>${entry.key.name}</a>
                            </td>
                            <td>
                                ${entry.value}
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>
            <a href="<spring:url value='/department/create' ></spring:url>" class="btn btn-default" role="button" id="add_department__button">Add department</a>
        </div>
    </div>
</div>
