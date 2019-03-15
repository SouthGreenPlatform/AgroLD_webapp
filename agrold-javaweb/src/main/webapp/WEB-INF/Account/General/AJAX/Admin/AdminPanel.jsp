<%-- 
    Document   : adminPanel
    Created on : 29 août 2017, 22:40:26
    Author     : Jc
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "util" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="alert alert-info alert-info-2 t-center" role="alert">
    <div class="row">
        <div class="col">
            <h1 class="">${nb_pages_visited}</h1>
        
            <p>Pages views</p>
        </div>
        <div class="col">
            <h1 class="">${nb_account}</h1>
            <p>Accounts</p>
            
        </div>
        <div class="col">
            <h1 class="">${nb_request_tool}</h1>
            <p>Requests on tools</p>
        </div>
    </div>
</div>
</div>
<div class="row">
    <div class="col-sm-12 col-md-12">
        <div class="o-frame m">
            <table class="table table-sm">
                <thead>
                    <tr>
                        <th>pages</th>
                        <th>Anonymous</th>
                        <th>Users</th>
                        <th>Total</th>
                    </tr>
                </thead>
                <tbody>
                    <tr><td>index</td><td>${resume_info['index'][0]}</td><td>${resume_info['index'][1]}</td><td>${resume_info['index'][2]}</td></tr>
                    <tr><td>advancedSearch</td><td>${resume_info['advancedSearch'][0]}</td><td>${resume_info['advancedSearch'][1]}</td><td>${resume_info['advancedSearch'][2]}</td></tr>
                    <tr><td>sparqlEditor</td><td>${resume_info['sparqlEditor'][0]}</td><td>${resume_info['sparqlEditor'][1]}</td><td>${resume_info['sparqlEditor'][2]}</td></tr>
                    <tr><td>quickSearch</td><td>${resume_info['quickSearch'][0]}</td><td>${resume_info['quickSearch'][1]}</td><td>${resume_info['quickSearch'][2]}</td></tr>
                    <tr><td>relfinder</td><td>${resume_info['relfinder'][0]}</td><td>${resume_info['relfinder'][1]}</td><td>${resume_info['relfinder'][2]}</td></tr>
                    <tr><td>agroldApiDoc</td><td>${resume_info['agroldApiDoc'][0]}</td><td>${resume_info['agroldApiDoc'][1]}</td><td>${resume_info['agroldApiDoc'][2]}</td></tr>
                    <tr><td>documentation</td><td>${resume_info['documentation'][0]}</td><td>${resume_info['documentation'][1]}</td><td>${resume_info['documentation'][2]}</td></tr>
                    <tr><td>about</td><td>${resume_info['about'][0]}</td><td>${resume_info['about'][1]}</td><td>${resume_info['about'][2]}</td></tr>
                    <tr><td>survey</td><td>${resume_info['survey'][0]}</td><td>${resume_info['survey'][1]}</td><td>${resume_info['survey'][2]}</td></tr>

                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-md-12 ">
        <div class="o-frame">
            <div id="o-donuts" class="">
                <div class="titled-donuts">
                    <h1>Total visited pages</h1>
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

</div>
<hr>


<script>

    var ctx = $('body #donuts');
    var myDoughnutChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ["about", "Advanced Search", "Agrold ApiDoc", "Documentation", "Index", "Quick Search", "Relfinder", "Sparql Editor", "Survey"],
            datasets: [
                {
                    label: "Population (millions)",
                    backgroundColor: ["#1ab394", "#23c6c8", "#f8ac59", "#fdbd10", "#3c6eb4", "#d65129", "#7ab800", "#8aba56", "#48b8e7"],
                    data: [${resume_info['about'][2]},
    ${resume_info['advancedSearch'][2]},
    ${resume_info['agroldApiDoc'][2]},
    ${resume_info['documentation'][2]},
    ${resume_info['index'][2]},
    ${resume_info['quickSearch'][2]},
    ${resume_info['relfinder'][2]},
    ${resume_info['sparqlEditor'][2]},
    ${resume_info['survey'][2]}]
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
</script>