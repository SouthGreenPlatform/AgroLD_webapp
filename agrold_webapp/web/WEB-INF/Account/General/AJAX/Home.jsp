<%-- 
    Document   : Home
    Created on : 24 août 2017, 11:05:46
    Author     : Jc
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "util" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="row">
    <div class="t-center col-md-4 col-sm-12 col-lg-4">
        <div class="o-frame m">

            <h1 class="o-w-user-title"><i class="fa fa-user-circle"></i></h1>

            <h6 class="del-if-empty user-w-name">${userInfo['nom']==''?'No name':userInfo['nom']}</h6>
            <h6 class="del-if-empty user-w-name">${userInfo['prenom']==''?'':userInfo['prenom']}</h6>
            <h6><i class="fa fa-calendar"></i>&nbsp;Last connection ${user.getLast()}.</h6>
            <h6><i class="fa fa-calendar-check-o"></i>&nbsp;registered since ${user.getDateInscription()}.</h6>
            <h6><i class="fa fa-at"></i>&nbsp;&nbsp;${user.getEmail()}</h6>


            <h6 class="del-if-empty">${userInfo['service']==''?'':userInfo['service']}</h6>
            <h6 class="del-if-empty">${userInfo['ville']==''?'':userInfo['ville']}</h6>
            <h6 class="del-if-empty">${userInfo['job']==''?'':userInfo['job']}</h6>
            <h6 style="text-align:center;margin-top:10px;padding:20px;"><a class="btn btn-info" href="javascript:void(0)" onClick="$('.o-settings-parent').click(); $('.o-mi#getUserInfo').click()"><i class="fa fa-pen"></i>&nbsp;Edit info</a></h6>
        </div>
    </div>

    <div class="col-md-8 col-sm-12 col-lg-8">
        <div class="o-frame">

                <div id="o-donuts" class="">
                    <div class="titled-donuts">
                        <h1>Statistics</h1>
                    </div>
                    <div class="row">
                        <div style="width:100%;height:50%">
                            <canvas id="donuts" width="100%" height="50%"></canvas>
                        </div>
                    </div>
            </div>
        </div>
    </div>
</div>
<div class="o-user-tool row">
    <div class="col-md-4 col-sm-12 m">
        <div class="o-tool o-one">
            <div class="o-tool-title">Quick Search </div>
            <div class="o-info-count"><span class="o-number">${stat_count_request['quick_search']}</span><span>&nbsp;Request</span></div>
            <span>Status </span><span class="o-statut">${settingsQuickSearch==true ?"activated":"desactivated"}</span>

            <h6><a href="javascript:void(0)" onClick="$('.o-history-parent').click(); $('.o-mi#getQuickSearch').click()"><i class="fa fa-history"></i>&nbsp;Show history</a></h6>
            <h6><a href="javascript:void(0)" onClick="$('.o-settings-parent').click(); $('.o-mi#getHistorySettings').click()"><i class="fa fa-cog"></i>&nbsp;Change settings</a></h6>
        </div>
    </div>
    <div class="col-md-4 col-sm-12 m">
        <div class="o-tool o-two">
            <div class="o-tool-title">Advanced Search </div>
            <div class="o-info-count"><span class="o-number">${stat_count_request['advanced_search']}</span><span>&nbsp;Request</span></div>
            <span>Status </span><span class="o-statut">${settingsAdvancedSearch==true ?"activated":"desactivated"}</span>

            <h6><a href="javascript:void(0)" onClick="$('.o-history-parent').click(); $('.o-mi#getAdvancedSearch').click()"><i class="fa fa-history"></i>&nbsp;Show history</a></h6>
            <h6><a href="javascript:void(0)" onClick="$('.o-settings-parent').click(); $('.o-mi#getHistorySettings').click()"><i class="fa fa-cog"></i>&nbsp;Change settings</a></h6>
        </div>
    </div>
    <div class="col-md-4 col-sm-12 m">
        <div class="o-tool  o-three">
            <div class="o-tool-title">SparQL Editor </div>
            <div class="o-info-count"><span class="o-number">${stat_count_request['sparql_editor']}</span><span>&nbsp;Request</span></div>
            <span>Status </span><span class="o-statut">${settingsSparqlEditor==true ?"activated":"desactivated"}</span>
            <h6><a href="javascript:void(0)" onClick="$('.o-history-parent').click(); $('.o-mi#getSparqlEditor').click()"><i class="fa fa-history"></i>&nbsp;Show history</a></h6>
            <h6><a href="javascript:void(0)" onClick="$('.o-settings-parent').click(); $('.o-mi#getHistorySettings').click()"><i class="fa fa-cog"></i>&nbsp;Change settings</a></h6>
        </div>
    </div>
</div>
<hr>


<script>

    var ctx = $('body #donuts');
    var myDoughnutChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ["Advanced Search", "Quick Search", "Sparql editor"],
            datasets: [
                {
                    label: "Population (millions)",
                    backgroundColor: ["#1ab394", "#23c6c8", "#f8ac59"],
                    data: [${stat_count_request['advanced_search']},${stat_count_request['quick_search']},${stat_count_request['sparql_editor']}]
                }
            ]
        },
        options: {
            legend: {
                display: true,
                position: 'right',
            }
        }
    });
    var UserInfo = {
        name: {
            html: '<span class="col o-u-name">html</span>',
            value: '${fn:substring(userInfo['nom'],0,15)}'
        },
        fname: {
            html: '<span class="col o-u-fname">html</span>',
            value: '${fn:substring(userInfo['prenom'],0,15)}'
        },
        mail: {
            html: '<span class="col o-u-mail">html</span>',
            value: '${fn:substring(user.getEmail(),0,15)}'
        }
    };

    //    $(ducument).ready(function () {
    //        var del = document.getElementsByClassName('del-if-empty');
    //        for (var i = 0; i < del.length; i++)
    //            if (element.innerText == '' || element.textContent == '')
    //                $(del[i]).remove();
    //    });
    function putMenuInfo() {

        var info = '';
        if (UserInfo['fname']['value'] !== '')
            info = UserInfo['fname']['value'];
        else if (UserInfo['name']['value'] !== '') {
            info = UserInfo['name']['value'];
        } else
            info = UserInfo['mail']['value'];
        $('body .o-u-info').html(info);
    }
    function putWidgetUserInfo() {}
    putMenuInfo();
</script>