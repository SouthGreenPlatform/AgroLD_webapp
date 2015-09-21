/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var currentPage = 0; // curent page
var result = "";
var sparqljson;

function drawResultTable(entityType, data, page) {
    sparqljson = data.data;
    displayResult("result", sparqljson);
    var tableId = $("table.resultsTable").attr("id");
    $("tr.odd").ready(function () {
        nbResults = data.obj["results"]["bindings"].length;
        previousBtnId = "previousPage";
        nextBtnId = "nextPage";
        addNavButtons(nbResults, page, "pageBtns", previousBtnId, nextBtnId);
        $("#" + previousBtnId).attr("onclick", 'search("' + entityType + '","' + keyword + '",' + (page - 1) + ')');
        $("#" + nextBtnId).attr("onclick", 'search("' + entityType + '","' + keyword + '",' + (page + 1) + ')');
        processHtmlResult(type);
    });
}
/*
 * 
 */
function search(entityType, keyword, page) {
    currentPage = page;
    window.swagger = new SwaggerClient({
        url: url,
        success: function () {
            console.log("API definition well loaded");
            displayHoldMessage("result");
            switch (type) {
                case "gene":
                    swagger.apis.gene.getGenesByKeyWord({_format: ".sparql-json", keyword: keyword, _pageSize: pageSize, _page: currentPage},
                    {responseContentType: 'application/json'}, function (data) {
                        drawResultTable(entityType, data, page);
                    });
                    break;
                case "protein":
                    swagger.apis.protein.getProteinsByKeyWord({_format: ".sparql-json", keyword: keyword, _pageSize: pageSize, _page: page}, {responseContentType: 'text/html'}, function (data) {
                        drawResultTable(entityType, data, page);
                    });
                    break;
                case "qtl":
                    swagger.apis.qtl.getQtlsByKeyWord({_format: ".sparql-json", keyword: keyword, _pageSize: pageSize, _page: page}, {responseContentType: 'text/html'}, function (data) {
                        drawResultTable(entityType, data, page);
                    });
                    break;
                case "pathway":
                    swagger.apis.pathway.getPathwaysByKeyWord({_format: ".sparql-json", keyword: keyword, _pageSize: pageSize, _page: page}, {responseContentType: 'text/html'}, function (data) {
                        drawResultTable(entityType, data, page);
                    });
                    break;
                case "ontology":
                    swagger.apis.ontologies.getOntologyTermsByKeyWord({_format: ".sparql-json", keyword: keyword, _pageSize: pageSize, _page: page}, {responseContentType: 'text/html'}, function (data) {
                        drawResultTable(entityType, data, page);
                    });
                    break;
                default:
                    $("#result").html("nothing found");
            }
        }
    });
}