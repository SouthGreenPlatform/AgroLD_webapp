<%-- 
    Document   : sidebarUser
    Created on : 23 juil. 2017, 22:13:42
    Author     : Jc
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "util" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="sidebar-wrapper">
    <ul class="sidebar-nav o-user">
        <li class="sidebar-brand user-ban">
            <div class="col-md-12">
                <div class="inner">
                    <div class="col-sm-2"> 
                        <div class="lunch-menu"><a href="javascript:void(0)"><i class="fa fa-bars"></i></a></div>
                    </div>
                    <div class="col-sm-2">
                        <div class="circle hw ">
                            <i class="fa fa-user"></i>
                        </div>
                    </div>
                    <div class="o-u-info col-sm-8">
                        
                    </div>
                </div>
            </div>
        </li>
        <li id="home" class="parent o-mi fkfk">    <a href="#" class="checkMe"> <i class="fa fa-home"></i>&nbsp;&nbsp;Home</a></li>
        <li id="" class="parent o-history-parent">    <a href="#" class="checkMe"> <i class="fa fa-history"></i>&nbsp;&nbsp;History<span><i class="fa fa-caret-down"></i></span></a>                  
            <ul>
                <li id="getAdvancedSearch" class="parent o-mi">    <a href="#" class="checkMe"> <i class="fa fa-search-plus"></i>&nbsp;&nbsp;Advanced search</a>      </li>
                <li id="getSparqlEditor" class="parent o-mi">    <a href="#" class="checkMe"> <i class="fa fa-database"></i>&nbsp;&nbsp;SparQL editor</a>           </li>
                <li id="getQuickSearch" class="parent o-mi">    <a href="#" class="checkMe"> <i class="fa fa-search"></i>&nbsp;&nbsp;Quick search</a>              </li>
            </ul>
        </li>
        <li id="" class="parent o-settings-parent">    <a href="#" class="checkMe"> <i class="fa fa-cog"></i>&nbsp;&nbsp;Settings<span><i class="fa fa-caret-down"></i></span></a>                     
            <ul>
                <li id="getUserInfo" class="parent o-mi">    <a href="#" class="checkMe"> <i class="fa fa-user"></i>&nbsp;&nbsp;User</a>                     </li>
                <li id="getHistorySettings" class="parent o-mi">    <a href="#" class="checkMe"> <i class="fa fa-history"></i>&nbsp;&nbsp;History</a>                     </li>
            </ul>
        </li>
        <c:if test="${sessionScope.sessionUtilisateur.getRight()==1}">
        <li id="getContact" class="parent o-mi">    <a href="#" class="checkMe"> <i class="fa fa-comment-o"></i>&nbsp;&nbsp;Contact</a></li>
        </c:if>
        <c:if test="${sessionScope.sessionUtilisateur.getRight()==0}">
            <jsp:include page="sidebarAdmin.jsp"></jsp:include>
        </c:if>
    </ul>
</div>
<script>
    var CURRENT_ITEM_SELECTED = 0;
    window.onload = function () {

        $('.sidebar-nav li.parent').click(function () {
            $('ul.sidebar-nav li.parent.fkfk').removeClass('fkfk');
            $(this).addClass('fkfk');

        });
    }
</script>