<%-- 
    Document   : FutureUser
    Created on : 28 août 2017, 16:50:46
    Author     : Jc
--%>

<%@page import="agrold.ogust.beans.HistorySettings"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="o-frame">
    <h1 class="o-frame-title">Toggle user to Super User</h1>
    <h4 class="o-frame-title">Total : ${total_users} users</h4>
    <table class="table table-hover">
        <thead>
            <tr>
                <th>
                    <input type="checkbox" id="checkALL" class="table-get-all">
                </th>
                <th>User ID</th>
            </tr>
        </thead>
        <tbody id="userListFutureAdmin">
            <c:forEach items="${user_list}" var="user">
                <tr>
                    <td>
                        <input type="checkbox" value="${user.getEmail()}">
                    </td>
                    <td>
                        ${user.getEmail()}
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <div class="row">
        <div class="col align-self-start">
            <button id="toggleAdmin" class="btn btn-outline-warning o-mi">Set as super user</button>
        </div>
        <div class="col align-self-end">
            <nav aria-label="...">
                <ul class="pagination" value="${(nb_page/10)%1}">
                    <li class="page-item disabled">
                        <a value="0" onClick="Util.paginator({f: 'userListFutureAdmin', offset: $(this).attr(value), btn: this})" class="page-link go-previous naver" href="#">Pages</a>
                    </li>
                    <c:forEach var="i" begin="1" end="${nb_page/10}" step="1" >
                        <c:choose>
                            <c:when test="${i==1}">
                                <li class="page-item active val-${i}" value="${i}">
                                    <a class="numb page-link" onClick="Util.paginator({f: 'userListFutureAdmin', offset: 0, btn: this})" value="${i}" href="#">1 <span class="sr-only">(current)</span></a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="page-item val-${i}" value="${1}">
                                    <a onClick="Util.paginator({f: 'userListFutureAdmin', offset:${ ((i-1)*10) }, btn: this})" class="numb page-link" href="#">${i}</a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </ul>
            </nav>
        </div>
    </div>

</div>
<!-- CLASS userListFutureAdmin-->

<script>
    Util.setCheckable('.table-get-all');
    //Util.setPaginable('#userListFutureAdmin');


</script>