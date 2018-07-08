<%@ page import="ru.javawebinar.topjava.util.DateTimeUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .exceeded {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <h2>Meals</h2>
    <a href="meals?action=create">Add Meal</a>
    <hr/>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealWithExceed"/>
            <tr class="${meal.exceed ? 'exceeded' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=DateTimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>

<section>

    <h2>Фильтрация по дате/времени</h2>
    <hr>

    <form method="get" action="meals" >
        <input type="hidden" name="command" value="filterForm"/>
        <dl>
            <dt>StartDate:</dt>
            <dd><input type="date" value="${param.startD}" name="startD"></dd>
        </dl>
        <dl>
            <dt>EndDate:</dt>
            <dd><input type="date" value="${param.endD}" name="endD"></dd>
        </dl>
        <dl>
            <dt>StartTime:</dt>
            <dd><input type="time" value="${param.startT}" name="startT"></dd>
        </dl>
        <dl>
            <dt>EndTime:</dt>
            <dd><input type="time" value="${param.endT}" name="endT"></dd>
        </dl>

        <button type="submit">Filter</button>
        <button type="reset">Reset</button>
        <%--<button onclick="window.history.back()" type="button">Cancel</button>--%>
    </form>
</section>
</body>
</html>