/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var result = "";
var sparqljson;
var numberOfTables = 0;

function drawResultTable(data, entityType, keyword, page) {
    var sparqljson = data.data;
    displayResult("result", sparqljson);
    $("tr.odd").ready(function () {
        var nbResults = data.obj["results"]["bindings"].length;
        var previousBtnId = "previousPage" + numberOfTables;
        var nextBtnId = "nextPage" + numberOfTables;
        numberOfTables++;
        addNavButtonsADVS(nbResults, page, previousBtnId, nextBtnId);
        $("#" + previousBtnId).attr("onclick", 'search("' + entityType + '","' + keyword + '",' + (page - 1) + ')');
        console.log("drawResultTable currentPage 2: " + page);
        //$("#" + previousBtnId).attr("title", '"search("' + entityType + '","' + keyword + '",' + (page - 1) + ')"');
        $("#" + nextBtnId).attr("onclick", 'search("' + entityType + '","' + keyword + '",' + (page + 1) + ')');
        $("#" + nextBtnId).attr("adja", 'gloc');
        processHtmlResult(entityType);
    });
}
/*
 * 
 */
async function search(entityType, keyword, page) {
    window.swagger = new SwaggerClient({
        url:  window.location.origin+AGROLDAPIJSONURL
    }).then(
        client => {
            displayHoldMessage("#result");
            switch (entityType) {
                case "gene":
                    client.execute({ 
                        operationId: 'getGenesByKeyWord', 
                        parameters: { format: DEFAULTAPIFORMAT, keyword: keyword, pageSize: DEFAULT_PAGE_SIZE, page },
                    }).then(
                        data => drawResultTable(data, entityType, keyword, page)
                    )
                    break;
                case "protein":
                    client.execute({ 
                        operationId: 'getProteinsByKeyWord', 
                        parameters: { format: DEFAULTAPIFORMAT, keyword: keyword, pageSize: DEFAULT_PAGE_SIZE, page },
                    }).then(
                        data => drawResultTable(data, entityType, keyword, page)
                    )
                    break;
                case "qtl":
                    client.execute({ 
                        operationId: 'getQtlsByKeyWord', 
                        parameters: { format: DEFAULTAPIFORMAT, keyword: keyword, pageSize: DEFAULT_PAGE_SIZE, page },
                    }).then(
                        data => drawResultTable(data, entityType, keyword, page)
                    )
                    break;
                case "pathway":
                    client.execute({ 
                        operationId: 'getPathwaysByKeyWord', 
                        parameters: { format: DEFAULTAPIFORMAT, keyword: keyword, pageSize: DEFAULT_PAGE_SIZE, page },
                    }).then(
                        data => drawResultTable(data, entityType, keyword, page)
                    )
                    break;
                case "ontology":
                    client.execute({ 
                        operationId: 'getOntologyTermsByKeyWord', 
                        parameters: { format: DEFAULTAPIFORMAT, keyword: keyword, pageSize: DEFAULT_PAGE_SIZE, page },
                    }).then(
                        data => drawResultTable(data, entityType, keyword, page)
                    )
                    break;
                default:
                    $("#result").html("nothing found");
            }
        });
}