/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var result = "";
var spar_ql_json = "";
var numberOfTables = 0;

function displayResult(targetId, sparqljsonResult) {
    removeHoldMessage(targetId);
    yasr = YASR(document.getElementById(targetId), {
        //this way, the URLs in the results are prettified using the defined prefixes in the query
        //getUsedPrefixes: yasqe.getPrefixesFromQuery,
        useGoogleCharts: false,
        drawOutputSelector: false,
        //drawDownloadIcon: false,
        persistency: {
            prefix: false
        }
    });

    yasr.setResponse(sparqljsonResult);
}

function drawResultTable(data, entityType, keyword, page) {
    var spar_ql_json = data.data;
    displayResult("as-result", spar_ql_json);
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
        url:  A_GRO_LD_API_JSON_URL, // window.location.origin+
    }).then(
        client => {
            displayHoldMessage("#as-result");
            switch (entityType) {
                case "gene":
                    client.execute({ 
                        operationId: 'getGenesByKeyWord', 
                        parameters: { format: DEFAULT_API_FORMAT, keyword: keyword, pageSize: DEFAULT_PAGE_SIZE, page },
                    }).then(
                        data => drawResultTable(data, entityType, keyword, page)
                    )
                    break;
                case "protein":
                    client.execute({ 
                        operationId: 'getProteinsByKeyWord', 
                        parameters: { format: DEFAULT_API_FORMAT, keyword: keyword, pageSize: DEFAULT_PAGE_SIZE, page },
                    }).then(
                        data => drawResultTable(data, entityType, keyword, page)
                    )
                    break;
                case "qtl":
                    client.execute({ 
                        operationId: 'getQtlsByKeyWord', 
                        parameters: { format: DEFAULT_API_FORMAT, keyword: keyword, pageSize: DEFAULT_PAGE_SIZE, page },
                    }).then(
                        data => drawResultTable(data, entityType, keyword, page)
                    )
                    break;
                case "pathway":
                    client.execute({ 
                        operationId: 'getPathwaysByKeyWord', 
                        parameters: { format: DEFAULT_API_FORMAT, keyword: keyword, pageSize: DEFAULT_PAGE_SIZE, page },
                    }).then(
                        data => drawResultTable(data, entityType, keyword, page)
                    )
                    break;
                case "ontology":
                    client.execute({ 
                        operationId: 'getOntologyTermsByKeyWord', 
                        parameters: { format: DEFAULT_API_FORMAT, keyword: keyword, pageSize: DEFAULT_PAGE_SIZE, page },
                    }).then(
                        data => drawResultTable(data, entityType, keyword, page)
                    )
                    break;
                default:
                    $("#as-result").html("nothing found");
            }
        });
}