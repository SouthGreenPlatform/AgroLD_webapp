/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var currentPage = 0; // curent page
var result = "";
var sparqljson;

function drawResultTable(data, entityType, keyword, page) {
    var sparqljson = data.data;
    displayResult("result", sparqljson);
    var tableId = $("table.resultsTable").attr("id");
    $("tr.odd").ready(function () {
        var nbResults = data.obj["results"]["bindings"].length;
        var previousBtnId = "previousPage";
        var nextBtnId = "nextPage";
        addNavButtonsADVS(nbResults, page, "pageBtns", previousBtnId, nextBtnId);
        $("#" + previousBtnId).attr("onclick", 'search("' + entityType + '","' + keyword + '",' + (page - 1) + ')');
        $("#" + nextBtnId).attr("onclick", 'search("' + entityType + '","' + keyword + '",' + (page + 1) + ')');
        processHtmlResult(entityType);
    });
}
/*
 * 
 */
function search(entityType, keyword, page) {
    window.swagger = new SwaggerClient({
        url: AGROLDAPIJSONURL,
        success: function () {
            displayHoldMessage("#result");
            switch (entityType) {
                case "gene":
                    swagger.apis.gene.getGenesByKeyWord({format: DEFAULTAPIFORMAT, keyword: keyword, pageSize: pageSize, page: page},
                    {responseContentType: 'application/json'}, function (data) {
                        drawResultTable(data, entityType, keyword, page);
                    });
                    break;
                case "protein":
                    swagger.apis.protein.getProteinsByKeyWord({format: DEFAULTAPIFORMAT, keyword: keyword, pageSize: pageSize, page: page},
                        {responseContentType: 'text/html'}, function (data) {
                            drawResultTable(data, entityType, keyword, page);
                    });
                    break;
                case "qtl":
                    swagger.apis.qtl.getQtlsByKeyWord({format: DEFAULTAPIFORMAT, keyword: keyword, pageSize: pageSize, page: page},
                        {responseContentType: 'text/html'}, function (data) {
                            drawResultTable(data, entityType, keyword, page);
                    });
                    break;
                case "pathway":
                    swagger.apis.pathway.getPathwaysByKeyWord({format: DEFAULTAPIFORMAT, keyword: keyword, pageSize: pageSize, page: page},
                        {responseContentType: 'text/html'}, function (data) {
                            drawResultTable(data, entityType, keyword, page);
                    });
                    break;
                case "ontology":
                    swagger.apis.ontologies.getOntologyTermsByKeyWord({format: DEFAULTAPIFORMAT, keyword: keyword, pageSize: pageSize, page: page},
                        {responseContentType: 'text/html'}, function (data) {
                           drawResultTable(data, entityType, keyword, page);
                    });
                    break;
                default:
                    $("#result").html("nothing found");
            }
        }
    });
}