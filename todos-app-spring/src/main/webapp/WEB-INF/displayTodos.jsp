<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="de">
<head>
    <meta charset="UTF-8">
    <title>Todos (Spring)</title>
</head>
<body>
    <h1>Todos (Spring)</h1>

    <c:choose>
    	<c:when test="${empty todos}">
    		<p>Es sind keine Todos vorhanden.</p>
    	</c:when>
    	<c:otherwise>
    	    <ul>
		        <c:forEach var="todo" items="${todos}">
		            <li>
		                [<c:out value="${todo.status}"/>]
		                <c:out value="${todo.title}"/>
		                &ndash; <c:out value="${todo.description}"/>
		                <c:if test="${not empty todo.dueDate}">
		                    (bis <c:out value="${todo.dueDate}"/>)
		                </c:if>
		                <form method="post" action="delete-todo" style="display:inline">
		                    <input type="hidden" name="id" value="${todo.id}"/>
		                    <button type="submit" title="Todo löschen">🗑️ Löschen</button>
		                </form>
		            </li>
		        </c:forEach>
		    </ul>
    	</c:otherwise>
    </c:choose>

    <p><a href="index.html">Zur Startseite</a></p>
</body>
</html>
