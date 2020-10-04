<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="ru">
<head>
    <title>Edit meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<c:set var="actionName" value="${(action == 'add') ? 'Add' : 'Update'}"/>
<h2>${actionName} meal</h2>
<form method="POST" action='meals' name="formEditMeal">
    <input type="hidden" readonly="readonly" name="action" value="${action}"/>
    <input type="hidden" readonly="readonly" name="mealId" value="${meal.id}"/>
    <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
    <fmt:formatDate pattern="yyyy-MM-dd'T'HH:mm" value="${parsedDateTime}" var="formattedDateTime"/>
    DateTime: <input type="datetime-local" name="dateTime" value="${formattedDateTime}"/><br/>
    Description: <input type="text" name="description" value="${meal.description}"/><br/>
    Calories: <input type="number" name="calories" value="${meal.calories}"/><br/>
    <input type="submit" value="${actionName}"/>
</form>
</body>
</html>