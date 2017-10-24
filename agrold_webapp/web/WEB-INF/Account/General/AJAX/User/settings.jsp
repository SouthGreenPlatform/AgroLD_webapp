<%-- 
    Document   : settings.jsp
    Created on : 16 août 2017, 16:04:51
    Author     : Jc
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="agrold.ogust.beans.UserInfo"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="o-frame">
    <h1>User informations</h1>
    <table class="table">
        <c:forEach items="${inner_getUserInfo}" var="userInfo">
            <tr><td><label>Name : <span class="f-i" style="">${userInfo.getNom()}</span></label></td><td><input id="name" class="info-user" style="display:none" type="text" value="${userInfo.getNom()}" placeholder="name"/></td></tr>
            <tr><td><label>First name : <span class="f-i" style="">${userInfo.getPrenom()}</span></label></td><td><input id="fname" class="info-user" style="display:none" type="text" value="${userInfo.getPrenom()}" placeholder="first name"/></td></tr>
            <tr><td><label>Service : <span class="f-i" style="">${userInfo.getService()}</span></label></td><td><input id="service" class="info-user" style="display:none" type="text" value="${userInfo.getService()}" placeholder="service"/></td></tr>
            <tr><td><label>Town : <span class="f-i" style="">${userInfo.getVille()}</span></label></td><td><input id="town" class="info-user" style="display:none" type="text" value="${userInfo.getVille()}" placeholder="town"/></td></tr>
            <tr><td><label>Job : <span class="f-i" style="">${userInfo.getJob()}</span></label></td><td><input id="job" class="info-user" style="display:none" type="text" value="${userInfo.getJob()}" placeholder="job"/></td></tr>
            <!--<tr><td><label>Mail : <span class="f-i" style="">$--{userInfo.getMail()}</span></label></td><td><input id="mail" class="info-user" style="display:none" type="text" value="$--{userInfo.getMail()}" placeholder="mail@company.com"/></td></tr>-->
        </c:forEach>
    </table>
    <div>
        <button id="modify-info"><i class="fa fa-pencil"></i> Modify informations</button>
        <button id="cancel-modify-info" style="display:none"><i class="fa fa-close"></i> Cancel</button>
        <button id="settingsUserInfo" class="o-mi" style="display:none"><i class="fa fa-check"></i> Validate</button>
    </div>
</div>

<script>
    $(document).ready(function(){
        $('body #modify-info').click(function(){
            $('body .f-i').each(function(t){
                $(this).hide();
            });
            $('body .info-user').each(function(t){
                $(this).show();
            });
            $(this).hide();
            $('body #cancel-modify-info').show();
            $('body #settingsUserInfo').show();
        });
        $('body #cancel-modify-info').click(function(){
            $(this).hide();
            $('body #settingsUserInfo').hide();
            $('body #modify-info').show();
            $('body .f-i').each(function(t){
                $(this).show();
            });
            $('body .info-user').each(function(t){
                $(this).hide();
            });
        });
        $('body #settingsUserInfo').click(function(){
            
        });
    });
</script>