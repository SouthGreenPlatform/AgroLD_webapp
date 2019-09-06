<%-- 
    Document   : AdvancedSearch
    Created on : 14 août 2017, 21:14:23
    Author     : Jc
--%>

<%@page import="agrold.ogust.beans.AdvancedSearch"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"       uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"     uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"      uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="o-frame">
    <h1 class="o-frame-title">History from the Advanced Search tool</h1>
    <table class="table table-hover">
        <thead>
            <tr>
                <th>#</th>
                <th><i class="fa fa-calendar"></i>&nbsp;&nbsp;Date</th>
                <th><i class="fa fa-filter"></i>&nbsp;&nbsp;Type</th>
                <th><i class="fa fa-key"></i>&nbsp;&nbsp;Keyword</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${inner_getAdvancedSearch}" var="name">
                <tr id="ads-entry-${name.getId()}">
                    <td>${name.getId()}</td>
                    <td>${name.getDate()}</td>
                    <td>${name.getType()}</td>
                    <td>${name.getKeyword()}</td>
                    <td><div class="btn-group">
                            <button type="button" class="btn btn-info btn-sm dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                Options
                            </button>
                            <div class="dropdown-menu">
                                <a class="dropdown-item show-in-advs" href="#" id="${name.getKeyword()}_${name.getType()}" ><i style="color:grey" class="fa fa-play"></i> Display in tool</a>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item o-mi" href="#" id="deleteAdvancedSearch" value="id:${name.getId()}"><i style="color:grey" class="fa fa-close"></i> Delete</a>
                            </div>
                        </div>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <script>
        var h_AdvancedSearch = {
            showInTool:function(keyword,type){
                var url=WEBAPPURL + "advancedSearch.jsp?ht=" + type + '&hk=' + keyword;
                window.open(url, '_blank');
            }
        };
        $(document).ready(function(){
           $('.show-in-advs').click(function(e){
               var i = e.target.id;var a = i.split('_');var k = a[0], t=a[1];
               h_AdvancedSearch.showInTool(k,t);
           });
           
        });
    </script>
    
</div>


