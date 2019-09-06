<%-- 
    Document   : settings.jsp
    Created on : 16 août 2017, 16:03:38
    Author     : Jc
--%>

<%@page import="agrold.ogust.beans.HistorySettings"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="o-frame">
    <h1 class="o-frame-title">History settings</h1>
    <table class="table table-hover">
        <thead>
            <tr>
                <th>Activated services</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach items="${inner_getHistorySettings}" var="settings">
            <tr><td>Quick Search history&nbsp;&nbsp;&nbsp; </td><td><label class="switch"><input id="settingsQuickSearch" class="history-settings o-mi" type="checkbox" ${settings.getQs() == 1 ? 'checked' : ''}><span class="slider round"></span></label></td></tr>
            <tr><td>Advanced Search history&nbsp;&nbsp;&nbsp; </td><td><label class="switch"><input id="settingsAdvancedSearch" class="history-settings o-mi" type="checkbox" ${settings.getAs() == 1 ? 'checked' : ''}><span class="slider round"></span></label></td></tr>
            <tr><td>SparQL Editor history&nbsp;&nbsp;&nbsp; </td><td><label class="switch"><input id="settingsSparqlEditor" class="history-settings o-mi" type="checkbox" ${settings.getSp() == 1 ? 'checked' : ''}><span class="slider round"></span></label></td></tr>
        </c:forEach>
        </tbody>
    </table>
</div>

