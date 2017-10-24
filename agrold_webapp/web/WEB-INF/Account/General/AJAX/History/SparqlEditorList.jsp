<%-- 
    Document   : SparqlEditorList
    Created on : 14 août 2017, 21:35:03
    Author     : Jc
--%>

<%@page import="agrold.ogust.beans.SparqlEditor"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "util" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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

<div class="o-frame">
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
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item o-mi" href="#" id="deleteSparqleditor" value="id:${name.getId()}"><i style="color:grey" class="fa fa-close"></i> Delete</a>
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
                    var req = 'http://volvestre.cirad:8080/agrold/sparqleditor.jsp?query=' + this.getUriSparqlEditor($('body .modal .modal-body').text());
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
                    return '/agrold/sparqleditor.jsp?query=' + full_Request;
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