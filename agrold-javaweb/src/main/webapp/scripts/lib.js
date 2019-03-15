/* 
 * General definitions for the advanced search
 */
var url = AGROLDAPIJSONURL;
var maxAuthorsLength = 100;
var pageSize = 30; // limit number of results per page
var sparqlEndpoint = SPARQLENDPOINTURL;
var holdMessage = '<center id="holdMessage"><img src="images/wait_animated.gif" alt="Please Wait!"/></center>';

function displayHoldMessage(target) {
    $(target).html(holdMessage);
}
function removeHoldMessage(targetId) {
    $("#" + targetId).html("");
}
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

function displayPublications(json, targetId) {
    removeHoldMessage("publicationResult");
    if (json.length > 0) {
        $("#" + targetId).html("<ol></ol>");
        for (var i = 0; i < json.length; i++) {
            var urlv = "http://doi.org/" + json[i]["doi"];
            var authors = json[i]["authorString"];
            if (authors.length > maxAuthorsLength) {
                authors = authors.substring(1, maxAuthorsLength) + ' ...';
            }
            $("#" + targetId + " ol").append('<li id="paper' + i + '"><span>' + authors + ', " <b>' + json[i]["title"] + '</b> ", <i>' + json[i]["journalTitle"] + '</i>, ' + json[i]["pubYear"] + '</span></li>');
            $("#" + targetId + " ol li#paper" + i).append('<br><span>Available at: <a href="' + urlv + '" target="_blank">' + urlv + '</a></span>');
        }
    } else {
        $("#" + targetId).html("No publication found.")
    }
}
function dispalyHeader(eltType, json) {
    elt = typeof json["results"]["bindings"][0] !== 'undefined' ? json["results"]["bindings"][0] : "";
    if (elt === "") {
        return false;
    }
    var htmlTitle = $('.modal-title');
    var htmlSubTitle = $('.subtitle');
    var htmlDescription = $('.desc');
    var htmlUri = $('.uri-md');

    var name = typeof elt["Name"] !== 'undefined' ? elt["Name"]["value"] : "";
    var description = typeof elt["Description"] !== 'undefined' ? elt["Description"]["value"] : "";
    //////////////////////////////////
    htmlTitle.html('<b style="text-transform: uppercase" id="id">' + eltType + ' > ' + elt["Id"]["value"] + '</b>');
    htmlSubTitle.html('<b> ' + (name != null ? name.charAt(0).toUpperCase() + name.slice(1) : '') + '</b>');
    if (description !== "") {
        htmlDescription.html('<br><big>' + description + '</big>');
    }
    graphicalInitialisation();
    htmlUri.html('<a href="' + elt["Uri"]["value"] + '"');
    return true;
}
/**
 * 
 * @param {type} data: JSON Array
 * @param {type} tableId: existing and empty table
 * @returns {undefined}
 */
function simpleDataDisplay(data, tableId) {
    var keys = Object.keys(data[0]);
    var table = $('#' + tableId);
    $(table).append("<thead></thead>");//<></>
    var thead = $(table).children("thead").last();
    $(thead).append("<tr></tr>");
    var row = $(thead).children("tr").last();
    for (i = 0; i < keys.length; i++) {
        $(row).append("<th>" + keys[i] + "</th>");
    }
    $(table).append("<tbody></tbody>");
    var tbody = $(table).children("tbody").last();
    for (i = 0; i < data.length; i++) {
        $(tbody).append("<tr></tr>");
        var row = $(tbody).children().last();
        var elt = data[i];
        for (j = 0; j < keys.length; j++) {
            $(row).append('<td style="word-wrap:break-word;">' + JSON.stringify(elt[keys[j]]).replace(/,/gi, " , ") + "</td>");
        }
    }
    $(table).DataTable();
}

function processHtmlResult(entitiesType) {
    var tables = $("table.resultsTable");
    var table = "";
    var currentTable = "";
    for (i = 0; i < tables.length; i++) {
        if (!$(tables[i]).hasClass("complete"))
            currentTable = $(tables[i]);
    }
    if (entitiesType !== "") {
        var ths = $(currentTable).find("th");
        var uriIdx = 0;
        for (i = 0; i < ths.length; i++) {
            if ($(ths[i]).text() === "URI") {
                uriIdx = i;
                break;
            }
        }
        table = $(currentTable).children("tbody")[0];
        var trs = $(table).children("tr");
        for (i = 0; i < trs.length; i++) {
            var tds = $(trs[i]).children("td");
            var a = $(tds[uriIdx]).find("a.uri");
            sparqlLink = 'sparqleditor.jsp?query=PREFIX uri:<' + $(a).text() + '>\nSELECT ?property ?hasValue ?isValueOf\nWHERE {\n  { uri: ?property ?hasValue }\n  UNION\n  { ?isValueOf ?property uri:}\n}';
            $(a).after('<a href="' + encodeURI(sparqlLink) + '" target="_blank" style="text-decoration: none; color:#00B5AD; font-weight:bold"> (in Sparql) </a>');
            $(a).attr("target", "_blank");            
            $(tds[1]).append('<a id="' + encodeURIComponent($(a).text()) + '" name="' + entitiesType + '" class="mdpre" href="#advancedSearch.jsp?type=' + entitiesType + '&uri=' + encodeURIComponent($(a).text()) + '" style="text-decoration: none; color:#00B5AD; font-weight:bold"> (display) </a>');
            for(j = 0; j < trs.length; j++){
               if($(tds[j]).text().includes("<b>")){
                   $(tds[j]).html($(tds[j]).text());
               } else {
                   console.log($(tds[j]).text());
               }
            }            
        }
    }
    $(currentTable).addClass("complete");
}
//'PREFIX uri:<' + $(a).text() + '>\nSELECT ?property ?hasValue ?isValueOf\nWHERE {\n  { uri: ?property ?hasValue }\n  UNION\n  { ?isValueOf ?property uri:}\n}'
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
    var pager = $("#result-modal #" + divIdo);
    if (currentPageo > 0) {
        pager.prepend('<button class="btn btn-secondary o-secondary" onclick="invoke(\'' + functionName + '\',' + (currentPageo - 1) + ')" id="' + previousBtnId + '-top"><i class="fa fa-angle-left"></i>&nbsp;&nbsp; Previous page</button>');
        pager.append('<button class="btn btn-secondary o-secondary" onclick="invoke(\'' + functionName + '\',' + (currentPageo - 1) + ')" id="' + previousBtnId + '-top' + '"><i class="fa fa-angle-left"></i>&nbsp;&nbsp; Previous page</button>');
    }
    if (pageSize == nbResults) {
        pager.prepend('<button class="btn btn-secondary o-secondary" onclick="invoke(\'' + functionName + '\',' + (currentPageo + 1) + ')" id="' + nextBtnId + '-bottom' + '" style="position: relative;right: 0px;">Next page &nbsp;&nbsp;<i class="fa fa-angle-right"></i></button>');
        pager.append('<button class="btn btn-secondary o-secondary" onclick="invoke(\'' + functionName + '\',' + (currentPageo + 1) + ')" id="' + nextBtnId + '-bottom' + '" style="position: relative;right: 0px;">Next page &nbsp;&nbsp;<i class="fa fa-angle-right"></i></button>');
    }
}