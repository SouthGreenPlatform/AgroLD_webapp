/* 
 * General definitions for the advanced search
 */
//var url = "http://localhost:8084/agrold/swagger/agrold.json";
//var sparqlEndpoint = "http://volvestre.cirad.fr:8890/sparql";
//var sparqlEndpoint = "http://volvestre.cirad.fr:3128/sparql";
//var sparqlEndpoint = "http://volvestre.cirad.fr/sparql";
var url = "http://volvestre.cirad.fr:8080/agrold/swagger/agrold.json";
var maxAuthorsLength = 100;
var pageSize = 30; // limit number of results per page
var sparqlEndpoint = "http://volvestre.cirad.fr:8890/sparql";
var holdMessage = '<center id="holdMessage"><img src="images/wait_animated.gif" alt="Please Wait!"/></center>';


function displayHoldMessage(targetId) {
    console.log('++++++++++++++----displayHoldMessage(' + targetId);
    $("#" + targetId).html(holdMessage);
    //$(".pageNavBtns").hide();
    //$("#overlay").show();
}
function removeHoldMessage(targetId) {
    //$("#overlay").hide();
    console.log("removeHoldMessage" + "(" + targetId + ")");
    // $(".pageNavBtns").show();
    $("#" + targetId).html("");
}
function displayResult(targetId, sparqljsonResult) {
    console.log("displayResult(" + targetId + sparqljsonResult + ")");
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
function displayPublications(json, targetId) {
    console.log("displayPublications(" + json + "," + targetId + ")");
    removeHoldMessage("publicationResult");
    if (json.length > 0) {
        $("#" + targetId).html("<ol></ol>");
        for (var i = 0; i < json.length; i++) {
            console.log('-');
            var urlv = json[i]["URL"];
            var authors = json[i]["Authors"];
            if (authors.length > maxAuthorsLength) {
                authors = authors.substring(1, maxAuthorsLength) + ' ...';
            }
            $("#" + targetId + " ol").append('<li id="paper' + i + '"><span>' + authors + ', " <b>' + json[i]["Title"] + '</b> ", <i>' + json[i]["Journal"] + '</i>, ' + json[i]["Year"] + '</span></li>');
            $("#" + targetId + " ol li#paper" + i).append('<br><span>More at: <a href="' + urlv + '" target="_blank">' + urlv + '</a></span>');
            //$("#publicationResult ol").append('<li><a href="' + urlv + '" target="_blank">' + urlv + "</a></li>");
        }
    } else {
        $("#" + targetId).html("No publication found.")
        console.log("No publication found.");
    }
}
function dispalyHeader(eltType, json) {
    console.log("dispalyHeader(" + eltType + ", " + json + ")");
    elt = typeof json["results"]["bindings"][0] !== 'undefined' ? json["results"]["bindings"][0] : "";
    if (elt === "") {
        return false;
    }
    var htmlTitle = $('.modal-title');
    var htmlSubTitle = $('.subtitle');
    var htmlDescription = $('.desc');
    var htmlUri = $('.uri-md');

    //////////////////////////////////
    // div = $("#" + divId);
    var name = typeof elt["Name"] !== 'undefined' ? elt["Name"]["value"] : "";
    var description = typeof elt["Description"] !== 'undefined' ? elt["Description"]["value"] : "";
    //////////////////////////////////
    htmlTitle.html('<b style="text-transform: uppercase" id="id">' + eltType + ' > ' + elt["Id"]["value"] + '</b>');
    htmlSubTitle.html('<b> ' + (name != null ? name.charAt(0).toUpperCase() + name.slice(1) : '') + '</b>');
    if (description !== ""){
        htmlDescription.html('<br><big>' + description + '</big>');
    }
        graphicalInitialisation();
    htmlUri.html('<a href="' + elt["Uri"]["value"] + '"');
    // div.append('<br><br> <b> URI: </b>  <a href="' + elt["Uri"]["value"] + ' target="_blank"><i>' + elt["Uri"]["value"] + '</i></a><br>');
    // div.attr("class", "set");
    //////////////////////////////////
    // div.html('<span style="font-size:16pt;" id="id"><b style="text-transform: uppercase" id="id">' + eltType + ' :</b><b> ' + elt["Id"]["value"] + name + "</b></span>");
    // div.append('<br><big>' + description + '</big>');
    return true;
}
function processHtmlResult(entitiesType) {
    console.log("processHtmlResult(" + entitiesType + ")");
    var tables = $("table.resultsTable");
    var tableClass = entitiesType;
    //$(tables[tables.length - 1]).addClass(tableClass); //console.log($("table.resultsTable"));
    var table = "";
    var currentTable = "";
    for (i = 0; i < tables.length; i++) {
        if (!$(tables[i]).hasClass("complete"))
            currentTable = $(tables[i]);
    }
    table = $(currentTable).children("tbody")[0];
    //console.log($(table).html());
    var trs = $(table).children("tr");
    var tdsVars = $(trs[0]).children("th");
    for (i = 0; i < trs.length; i++) {
        var tds = $(trs[i]).children("td");
        if (entitiesType !== "") {
            // create the json elt with all result values
            var entity = {};
            for (k = 0; k < tds.length; k++) {
                entity[$(tdsVars[k]).text()] = $(tds[k]).text();
            }
            //availableElts.push(entity);            
        }
        a = "";
        for (var j = 1; j < tds.length; j++) {
            div = $(tds[j]).children("div")[0];
            a = $(div).children("a.uri")[0];
            $(a).attr("target", "_blank");
            sparqlLink = 'sparqleditor.jsp?query=SELECT * \nWHERE{\n\tGRAPH ?graph{\n\t\t<' + $(a).text() + '> ?property ?object.\n\t}\n}';
            $(a).after('<a href="' + encodeURI(sparqlLink) + '" target="_blank" style="text-decoration: none; color:#00B5AD; font-weight:bold"> (in Sparql) </a>');
        }
        $(tds[1]).append('<a id="' + encodeURIComponent($(a).text()) + '" name="' + entitiesType + '" class="mdpre" href="#advancedSearch.jsp?type=' + entitiesType + '&uri=' + encodeURIComponent($(a).text()) + '" style="text-decoration: none; color:#00B5AD; font-weight:bold"> (display) </a>');
    }
    $(currentTable).addClass("complete");
}
/**
 * Gestion de la pagination au niveau de l'outil de recherche.
 * @param {type} nbResults
 * @param {type} currentPage
 * @param {type} divId
 * @param {type} previousBtnId
 * @param {type} nextBtnId
 * @returns {undefined}
 */
function addNavButtonsADVS(nbResults, currentPage, divId, previousBtnId, nextBtnId) {
    console.log("addNavButtons(" + nbResults + ", " + currentPage + ", " + divId + ", " + previousBtnId + ", " + nextBtnId + ")");
//    div = $("#" + divId);
//    div.html("");
//    if (currentPage > 0) {
//        div.html('<button class="btn btn-secondary o-secondary" id="' + previousBtnId + '"><i class="fa fa-angle-left"></i>&nbsp;&nbsp; Previous page</button>');
//    }
//    if (pageSize == nbResults) {
//        div.append('<button class="btn btn-secondary o-secondary" id="' + nextBtnId + '" style="position: relative;right: 0px;">Next page &nbsp;&nbsp;<i class="fa fa-angle-right"></i></button>');
//    }
    var nav = "";
    if (currentPage > 0) {
        nav = '<button class="btn btn-secondary o-secondary" id="' + previousBtnId + '"><i class="fa fa-angle-left"></i>&nbsp;&nbsp; Previous page</button>';
    }
    if (pageSize == nbResults) {
        nav += ('<button class="btn btn-secondary o-secondary" id="' + nextBtnId + '" style="position: relative;right: 0px;">Next page &nbsp;&nbsp;<i class="fa fa-angle-right"></i></button>');
    }
    $("body" + " .yasr_header").prepend(nav);
}
/**
 * Gestion de la génération des boutons de pagination à l'intérieur du modal.
 * @param {type} nbResults
 * @param {type} currentPageo
 * @param {type} divIdo
 * @param {type} previousBtnId
 * @param {type} nextBtnId
 * @param {type} functionName
 * @returns {undefined}
 */
function addNavButtons(nbResults, currentPageo, divIdo, previousBtnId, nextBtnId, functionName) {
    console.log("addNavButtons(" + nbResults + ", " + currentPageo + ", " + divIdo + ", " + previousBtnId + ", " + nextBtnId + ")");
    var pager = $("#result-modal #" + divIdo);
    console.log('-----------------------------------------------------------------------------------');
    console.log('Interprétation Précédante ? : currentPageo > 0 : (' + String(currentPageo > 0)+')');
    console.log(000 + "Page suivante : " + "#CurentPage:"+currentPageo); 
    console.log('-----------------------------------------------------------------------------------');
    console.log('Interprétation Suivante ? : pageSize < nbResults : (' +  String(pageSize < nbResults) +')');
    console.log(000 + "Page précédante : " + "#pageSize:"+pageSize + ', #nbResults : ' + nbResults); 
    console.log('-----------------------------------------------------------------------------------');
    if (currentPageo > 0) {
        pager.prepend('<button class="btn btn-secondary o-secondary" onclick="invoke(\''+functionName+'\','+(currentPageo-1)+')" id="' + previousBtnId + '-top"><i class="fa fa-angle-left"></i>&nbsp;&nbsp; Previous page</button>');
        pager.append('<button class="btn btn-secondary o-secondary" onclick="invoke(\''+functionName+'\','+(currentPageo-1)+')" id="' + previousBtnId + '-top'+ '"><i class="fa fa-angle-left"></i>&nbsp;&nbsp; Previous page</button>');
    }
    if (pageSize == nbResults){
        pager.prepend('<button class="btn btn-secondary o-secondary" onclick="invoke(\''+functionName+'\','+(currentPageo+1)+')" id="' + nextBtnId + '-bottom'+ '" style="position: relative;right: 0px;">Next page &nbsp;&nbsp;<i class="fa fa-angle-right"></i></button>');
        pager.append('<button class="btn btn-secondary o-secondary" onclick="invoke(\''+functionName+'\','+(currentPageo+1)+')" id="' + nextBtnId + '-bottom'+ '" style="position: relative;right: 0px;">Next page &nbsp;&nbsp;<i class="fa fa-angle-right"></i></button>');
    }
}