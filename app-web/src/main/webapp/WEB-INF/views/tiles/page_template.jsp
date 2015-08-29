<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <tiles:insertAttribute name="head"/>

        <title><tiles:insertAttribute name="title"/></title>

        <tiles:importAttribute name="stylesheets"/>

        <c:forEach var="css" items="${stylesheets}">
            <link rel="stylesheet" type="text/css" href="<c:url value="${css}"/>">
        </c:forEach>
    </head>
    <body>
        <tiles:insertAttribute name="header"/>

        <tiles:insertAttribute name="content"/>
        
        <tiles:insertAttribute name="footer"/>

        <tiles:importAttribute name="scripts"/>

        <c:forEach var="jsFileName" items="${scripts}">
            <script src="<%request.getContextPath(); %><c:url value='${jsFileName}' />" ></script>
        </c:forEach>
    </body>

</html>