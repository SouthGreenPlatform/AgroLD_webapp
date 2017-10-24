<%-- 
    Document   : UserManager
    Created on : 30 août 2017, 11:10:53
    Author     : Jc
--%>

<%@page import="agrold.ogust.beans.HistorySettings"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="modal fade" id="user-modal-info">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><i class="fa fa-pie-chart"></i>&nbsp;&nbsp;User informations</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="mban adv">
                <div class="desc"></div>
                <div class="mnvi">
                    <!-- Nav tabs -->
                    <ul class="nav nav-tabs" role="tablist">
                        <li class="nav-item">
                            <a class="nav-link active mee" data-toggle="tab" href="#o-global-info" role="tab">Global info</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" data-toggle="tab" href="#o-quick-search" role="tab">Quick Search</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" data-toggle="tab" href="#o-advanced-search" role="tab">Advanced Search</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" data-toggle="tab" href="#o-sparql-editor" role="tab">Sparql Editor</a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="modal-body">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<div class="tab-content">
    <div class="tab-pane active" id="o-global-info" role="tabpanel">

    </div>
    <div class="tab-pane" id="o-quick-search" role="tabpanel">
        
    </div>
    <div class="tab-pane" id="o-advanced-search" role="tabpanel">
        
    </div>
    <div class="tab-pane" id="o-sparql-editor" role="tabpanel">
        
    </div>
</div>

<div class="o-frame">
    <h1 class="o-frame-title">User manager</h1>
    <table class="table table-hover">
        <thead>
            <tr>
                <th>User ID</th>
                <th>Since</th>
                <th>Last</th>
                <th>Adv.S.</th>
                <th>Q.S.</th>
                <th>SPQL. E.</th>
            </tr>
        </thead>
        <tbody id="userListManager">
            <c:forEach items="${table_user_list}" var="entry" varStatus="loop">
                <tr>
                    <td>
                        <a class="btn btn-outline-info o-mi" id="getCompleteUserData" href="javascript:void(0)">${entry['mail']}</a>
                    </td>
                    <td>${entry['user'][0]}</td>
                    <td>${entry['user'][1]}</td>
                    <td>${entry['user'][2]}</td>
                    <td>${entry['user'][3]}</td>
                    <td>${entry['user'][4]}</td>
                </tr>
            </c:forEach>

        </tbody>
    </table>

    <div class="row">

        <div class="col">
            <nav aria-label="...">
                <ul class="pagination" value="${(nb_page/10)%1}">
                    <li class="page-item disabled">
                        <a value="0" onClick="Util.paginator({f: 'userListManager', offset: $(this).attr(value), btn: this})" class="page-link go-previous naver" href="#">Pages</a>
                    </li>
                    <c:forEach var="i" begin="1" end="${nb_page/10}" step="1" >
                        <c:choose>
                            <c:when test="${i==1}">
                                <li class="page-item active val-${i}" value="${i}">
                                    <a class="numb page-link" onClick="Util.paginator({f: 'userListManager', offset: 0, btn: this})" value="${i}" href="#">1 <span class="sr-only">(current)</span></a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="page-item val-${i}" value="${1}">
                                    <a onClick="Util.paginator({f: 'userListManager', offset:${ ((i-1)*10) }, btn: this})" class="numb page-link" href="#">${i}</a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </ul>
            </nav>
        </div>
    </div>

</div>