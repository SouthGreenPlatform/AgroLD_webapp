<%-- 
    Document   : _USER_FULL_DATA_LOADER
    Created on : 30 août 2017, 22:33:30
    Author     : Jc
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "util" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div style="display:none" class="resume-user-info-inject-modal">
    <div class="row">
        <div class="col">
            <h1 class="o-w-user-title"><i class="fa fa-user-circle"></i></h1>
        </div>
        <div class="col">
            <h6 class="del-if-empty user-w-name">${userInfo['nom']==''?'No name':userInfo['nom']}</h6>
            <h6 class="del-if-empty user-w-name">${userInfo['prenom']==''?'':userInfo['prenom']}</h6>
            <h6><i class="fa fa-calendar"></i>&nbsp;Last connection ${user.getLast()}.</h6>
            <h6><i class="fa fa-calendar-check-o"></i>&nbsp;registered since ${user.getDateInscription()}.</h6>
            <h6><i class="fa fa-at"></i>&nbsp;&nbsp;${user.getEmail()}</h6>
        </div>
        <div class="col">
            <h6 class="del-if-empty">${userInfo['service']==''?'':userInfo['service']}</h6>
            <h6 class="del-if-empty">${userInfo['ville']==''?'':userInfo['ville']}</h6>
            <h6 class="del-if-empty">${userInfo['job']==''?'':userInfo['job']}</h6>
        </div>
    </div>
</div>

<div class="tab-content">
    <hr>
    <div class="tab-pane active" id="o-global-info" role="tabpanel">
        <div class="row t-center">
            <div class="col">
                <h4 class="o-tool-title">Quick Search </h4>
                <div class="o-info-count"><span class="o-number">${stat_count_request['quick_search']}</span><span>&nbsp;Request</span></div>
                <span>Status </span><span class="o-statut">${settingsQuickSearch==true ?"activated":"desactivated"}</span>
            </div>
            <div class="col">
                <h4 class="o-tool-title">Advanced Search </h4>
                <div class="o-info-count"><span class="o-number">${stat_count_request['advanced_search']}</span><span>&nbsp;Request</span></div>
                <span>Status </span><span class="o-statut">${settingsAdvancedSearch==true ?"activated":"desactivated"}</span>
            </div>
            <div class="col">
                <h4 class="o-tool-title">SparQL Editor </h4>
                <div class="o-info-count"><span class="o-number">${stat_count_request['sparql_editor']}</span><span>&nbsp;Request</span></div>
                <span>Status </span><span class="o-statut">${settingsSparqlEditor==true ?"activated":"desactivated"}</span>
            </div>
        </div>
        <hr>
        <div id="o-donuts" class="">
            <div class="titled-donuts">
                <h1>Statistics</h1>
            </div>
            <div class="row puthere">
                <div style="width:100%;height:50%">
                    <canvas id="donuts" width="100%" height="50%"></canvas>
                </div>
            </div>
        </div>
    </div>
    <div class="tab-pane" id="o-quick-search" role="tabpanel">
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
                                </div>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="tab-pane" id="o-advanced-search" role="tabpanel">
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
                                </div>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <script>
            var h_AdvancedSearch = {
                showInTool: function (keyword, type) {
                    var url = WEBAPPURL + '/advancedSearch.jsp?ht=" + type + '&hk=' + keyword;
                    window.open(url, '_blank');
                }
            };
            $(document).ready(function () {
                $('.show-in-advs').click(function (e) {
                    var i = e.target.id;
                    var a = i.split('_');
                    var k = a[0], t = a[1];
                    h_AdvancedSearch.showInTool(k, t);
                });

            });
        </script>

    </div>
    <div class="tab-pane" id="o-sparql-editor" role="tabpanel">
        <style>
            .spql-req-td .preview-req{ background: -webkit-linear-gradient(0deg, #FF9F65, rgba(255,255,255,0));
                                       -webkit-background-clip: text;
                                       -webkit-text-fill-color: transparent;
            }
            .modal-hidden textarea {width :100%;}
        </style>

        <div class="modal">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">SparQL Request</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <p>Error from template, please send us a report</p>
                    </div>
                    <div class="modal-hidden" style="display:none">
                        <textarea id="sel-s"></textarea>
                    </div>
                    <div class="modal-footer">
                        <span class="info-modal"></span>
                        <button type="button" class="btn btn-primary genlink-spql"><i class="fa fa-share-alt"></i></button>
                        <button type="button" class="btn btn-primary show-in-editor"><i class="fa fa-play"></i></button>
                        <a href="javascript:void(0);" class="btn btn-primary copy-spql"><i class="fa fa-copy"></i></a>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <h1 class="o-frame-title">History from the SparQL Editor </h1>
        <table class="table table-hover">
            <thead>
                <tr>
                    <th>#</th>
                    <th><i class="fa fa-calendar"></i>&nbsp;&nbsp;Date</th>
                    <th><i class="fa fa-database"></i>&nbsp;&nbsp;Request</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${inner_getSparqlEditor}" var="name">
                    <tr id="sp-entry-${name.getId()}">
                        <td>${name.getId()}</td>
                        <td class="spql-date">${name.getDate()}</td>
                        <td class="spql-req-td">
                            <span class="preview-req">${fn:substring(name.getRequest(), 0, 150)}</span>
                            <span class="show-more-spql">
                                <p class="full-req-spql" id="show-${name.getId()}" style="display:none">${fn:escapeXml(name.getRequest())}</p>
                                <a id="${name.getId()}" class="btn btn-sm btn-warning">
                                    <i style="color:rgba(0,0,0,.7)" class="fa fa-eye"></i>
                                </a>
                            </span>
                        </td>
                        <td>
                            <div class="btn-group">
                                <button type="button" class="btn btn-info btn-sm dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                                    Options
                                </button>
                                <div class="dropdown-menu">
                                    <a class="dropdown-item show-in-editor" href="#"><i style="color:grey" class="fa fa-play"></i> Display in tool</a>
                                </div>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>

        </table>

        <script>
            $(document).ready(function () {
                var h_SparqlEditor = {
                    request: '',
                    clear: function () {
                        $('#sel-s').hide();
                        $('.modal-body').show();
                        this.infoModal('');
                    },
                    showInsideModal: function (title) {
                        alert(this.request);
                        $('.modal .modal-body').html(this.request);
                        $('.modal .modal-title').text('SparQL Request : ' + title);
                        $('.modal').modal();
                    },
                    copyRequest: function () {
                        var req = $('body .modal .modal-body').text();
                        $('#sel-s').val(req)
                        $('.modal-hidden textarea').attr('rows', parseInt($('.modal .modal-body').height()) / 15);
                        $('.modal-hidden').show();
                        $('.modal-body').hide();
                        $('#sel-s').focus().select();
                        // Copy
                        document.execCommand('copy');
                        this.infoModal('Request copied');
                    },
                    shareRequest: function () {
                        var req = WEBAPPURL+'/sparqleditor.jsp?query=' + this.getUriSparqlEditor($('body .modal .modal-body').text());
                        $('#sel-s').val(req)
                        $('.modal-hidden textarea').attr('rows', (parseInt($('.modal .modal-body').height()) / 15));
                        $('.modal-hidden').show();
                        $('.modal-body').hide();
                        $('#sel-s').focus().select();
                        // Copy
                        document.execCommand('copy');
                        this.infoModal('Url copied');
                    },
                    playRequest: function (req) {
                        var url = this.getUriSparqlEditor(req);
                        window.open(url, '_blank');
                    },
                    getUriSparqlEditor: function (req) {
                        var full_Request = encodeURIComponent(req);
                        return WEBAPPURL + '/sparqleditor.jsp?query=' + full_Request;
                    },
                    infoModal: function (text) {
                        $('.modal .info-modal').text(text);
                    }
                };
                $('.modal').on('hidden.bs.modal', function () {
                    h_SparqlEditor.clear();

                });
                // Luch modal to see the full SPARQL request
                $('body .show-more-spql a').click(function (e) {
                    var request = $(this).siblings('p').html();
                    var date = $(this).closest('tr').find('td.spql-date').text();
                    h_SparqlEditor.request = request;
                    h_SparqlEditor.showInsideModal(date);

                });
                // Open a new tab to use the request in the sparql editor tool
                $('body table tr td .show-in-editor').click(function () {
                    var req = $(this).closest('tr').find('.full-req-spql').text();
                    h_SparqlEditor.playRequest(req);
                });
                // Same in modal
                $('body .modal .show-in-editor').click(function () {
                    var req = $('.modal .modal-body').text();
                    h_SparqlEditor.playRequest(req);
                });
                // Allow user to copy the url of the request for sharing
                $('.copy-spql').click(function () {
                    h_SparqlEditor.copyRequest();
                });
                // Allow user to copy the request
                $('.genlink-spql').click(function () {
                    h_SparqlEditor.shareRequest();
                });
            });
        </script>
    </div>
</div>
<script>


    $(document).ready(function () {
    
        var info_user = $('body .resume-user-info-inject-modal').html();
        $('body .resume-user-info-inject-modal').remove();
        $('body #user-modal-info .mban .desc').html(info_user);
        
        var ctx2 = $('body #donuts');
        window.chart = new Chart(ctx2, {
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
        
    });
</script>
