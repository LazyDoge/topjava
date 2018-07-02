<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<%--
  Created by IntelliJ IDEA.
  User: Test
  Date: 28.06.2018
  Time: 19:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Title</title>
    <style type="text/css">
        .tg td {
            color: red;
            border-style: solid;
            border-width: 1px;
            border-color: #ccc;
            text-align: center;

        }

        .tg2 td {
            border-style: solid;
            border-width: 1px;
            border-color: #ccc;
            color: green;
            text-align: center;

        }

        .tg3 th {
            border-style: solid;
            border-width: 1px;
            border-color: #ccc;
            text-align: center;
        }
    </style>
</head>
<body>
<br/>
<br/>
<br/>


<table align="center">

    <tr>
        <th class="tg3" width="120">ID</th>
        <th class="tg3" width="120">Description</th>
        <th class="tg3" width="120">Date</th>
        <th class="tg3" width="120">Time</th>
        <th class="tg3" width="120">Calories</th>
        <th class="tg3" width="120" colspan="2">Action</th>
    </tr>


    <jsp:useBean id="list" scope="request" type="java.util.List"/>
    <c:forEach items="${list}" var="meal">


        <tr class="${meal.exceed ? 'tg' : 'tg2'}">
            <td>${meal.id}</td>
            <td class="tg">${meal.description}</td>
            <td class="tg2">${meal.dateTime.toLocalDate()}</td>
            <td>${meal.getDateTime().toLocalTime()}</td>
            <td>${meal.getCalories()}</td>
            <td><a href="${pageContext.request.contextPath}/meals?action=edit&id=<c:out value="${meal.id}"/>">edit</a>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/meals?action=delete&id=<c:out value="${meal.id}"/>">remove</a>
            </td>
        </tr>
    </c:forEach>


    </tr>
</table>

<br/>
<br/>
<br/>

<form action="${pageContext.request.contextPath}/meals" method="post">
    <table align="center">
        <tr>
            <td align="center">
                id:
            </td>
            <td>
                <input type="text" name="ID" readonly="readonly" value="<c:out value="${meal.id}"/>">
            </td>
        </tr>
        <tr>
            <td align="center">
                Description:
            </td>
            <td>
                <input type="text" name="description" value="<c:out value="${meal.description}"/> "/>
            </td>
        </tr>
        <tr></tr>
        <td align="center">
            DateTime:
        </td>
        <td>
            <input type="datetime-local" name="dateTime" value=
                    "<javatime:format value="${meal.dateTime}" style="MS" pattern="yyyy-MM-dd'T'HH:mm"/>"
            />
        </td>
        </tr>
        <tr>
            <td align="center">
                Calories:
            </td>
            <td>
                <input type="text" name="calories" value="<c:out value="${meal.calories}"/>"/>
            </td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                <input type="submit" value="${meal==null?'ADD':'UPDATE'}">
            </td>
        </tr>

    </table>
</form>

</body>
</html>
