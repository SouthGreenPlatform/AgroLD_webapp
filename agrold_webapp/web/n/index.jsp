<%-- 
    Document   : newjsp
    Created on : 20 juin 2017, 14:45:35
    Author     : Jc
--%>
<%@page import="agrold.ogust.membre.testCo"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <% 
            testCo co = new testCo();
            out.print("<h1>Résultat de la demande de connexion : " + co.getConnection());
        %>
    </body>
</html>
