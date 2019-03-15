<%-- 
    Document   : QuickSearchList
    Created on : 10 août 2017, 15:10:08
    Author     : Jc
--%>

<%@page import="agrold.ogust.beans.QuickSearch"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="o-frame">
    <h1 class="o-frame-title">History from the Quick Search tool</h1>
    <table class="table table-hover">
        <thead>
            <tr>
                <th>#</th>
                <th><i class="fa fa-calendar"></i>&nbsp;&nbsp;Date</th>
                <th><i class="fa fa-key"></i>&nbsp;&nbsp;Keyword</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${inner_getQuickSearch}" var="name">
                <tr id="qs-entry-${name.getId()}">
                    <td>${name.getId()}</td>
                    <td>${name.getDate()}</td>
                    <td>${name.getKeyword()}</td>
                    <td><div class="btn-group">
                            <button type="button" class="btn btn-info btn-sm dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                Options
                            </button>
                            <div class="dropdown-menu">
                                <form  action="http://volvestre.cirad.fr:8890/fct/" method="post" target="_blank">                   
                                    <input class="keyword form-control" name="q" type="text" value="${name.getKeyword()}" data-step="1" hidden/> 
                                    <a class="dropdown-item" href="#">
                                        <button class="btn btn-secondary" type="submit" value="Search" data-step="2" data-intro="launch the search engine!">
                                            <i style="color:grey" class="fa fa-play"></i> Display in tool
                                        </button>
                                    </a>

                                </form>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item o-mi" id="deleteQuickSearch" value="id:${name.getId()}" href="#"><i style="color:grey" class="fa fa-close"></i> Delete</a>
                            </div>
                        </div>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

