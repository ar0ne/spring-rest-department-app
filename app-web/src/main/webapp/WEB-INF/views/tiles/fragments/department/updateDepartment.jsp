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
      <h3>Update Department</h3>
      <form action="<spring:url value='/department/update/' ></spring:url>" method="POST">

        <label for="name">Name: </label>
        <input type="text" name="name" class="form-control" maxlength="255" minlength="2" required autofocus value="${department.name}"/><br/>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <input type="hidden" name="id" value="${department.id}"/>
        <br/>
        <button type="submit" class="btn btn-success">Save</button>

      </form>
    </div>
  </div>
</div>