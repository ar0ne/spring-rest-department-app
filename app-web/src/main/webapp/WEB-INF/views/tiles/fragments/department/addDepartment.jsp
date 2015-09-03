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
      <h3>Add Department</h3>
      <form action="<spring:url value='/department/create' ></spring:url>" method="POST">

        <label for="name">Name: </label>
        <input type="text" name="name" class="form-control" maxlength="255" minlength="2" required autofocus /><br/>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <br/>
        <button type="submit" class="btn btn-success">Add</button>

      </form>
    </div>
  </div>
</div>