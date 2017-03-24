/* 
 * General definitions for the advanced search
 */
var url = "http://volvestre.cirad.fr:8080/agrold/swagger/agrold.json";
//var url = "http://localhost:8084/agrold/swagger/agrold.json";
var pageSize = 30; // limit number of results per page
//var sparqlEndpoint = "http://volvestre.cirad.fr:8890/sparql";
//var sparqlEndpoint = "http://volvestre.cirad.fr:3128/sparql";
//var sparqlEndpoint = "http://volvestre.cirad.fr/sparql";
var sparqlEndpoint = "http://volvestre.cirad.fr:8890/sparql";

var holdMessage = '<center id="holdMessage"><img src="images/wait_animated.gif" alt="Please Wait!"/></center>';

function displayHoldMessage(targetId) {
    $("#" + targetId).html(holdMessage);
    $(".pageNavBtns").hide();
    //$("#overlay").show();
}
function removeHoldMessage(targetId) {
    //$("#overlay").hide();
    $(".pageNavBtns").show();
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

function displayPublications(pubjson, targetId) {
    removeHoldMessage("publicationResult");
    $("#publicationPageBtns").remove();
    if (json.length > 0) {
        $("#" + targetId).html("<ol></ol>");
        for (i = 0; i < json.length; i++) {
            url = json[i]["URL"];
            authors = json[i]["Authors"];
            maxAuthorsLength = 100;
            if (authors.length > maxAuthorsLength) {
                authors = authors.substring(1, maxAuthorsLength) + ' ...';
            }
            $("#" + targetId + " ol").append('<li id="paper' + i + '"><span>' + authors + ', " <b>' + json[i]["Title"] + '</b> ", <i>' + json[i]["Journal"] + '</i>, ' + json[i]["Year"] + '</span></li>');
            $("#" + targetId + " ol li#paper" + i).append('<br><span>More at: <a href="' + url + '" target="_blank">' + url + '</a></span>');
            //$("#publicationResult ol").append('<li><a href="' + url + '" target="_blank">' + url + "</a></li>");
        }
    } else {
        $("#" + targetId).html("No publication found.")
    }
}

// first information
function dispalyHeader(eltType, json, divId) {
    elt = typeof json["results"]["bindings"][0] !== 'undefined' ? json["results"]["bindings"][0] : "";
    if (elt === "") {
        return false;
    }
    div = $("#" + divId);
    name = typeof elt["Name"] !== 'undefined' ? " / " + elt["Name"]["value"] : "";
    description = typeof elt["Description"] !== 'undefined' ? elt["Description"]["value"] : "";
    div.html('<span style="font-size:16pt;" id="id"><b style="text-transform: uppercase" id="id">' + eltType + ' :</b><b> ' + elt["Id"]["value"] + name + "</b></span>");
    if (description !== "") {
        div.append('<br><big>' + description + '</big>');
    }
    div.append('<br><br> <b> URI: </b>  <a href="' + elt["Uri"]["value"] + '" target="_blank"><i>' + elt["Uri"]["value"] + '</i></a><br>');
    div.attr("class", "set");
    return true;
}
function processHtmlResult(entitiesType) {
    tables = $("table.resultsTable");
    tableClass = entitiesType;
    //$(tables[tables.length - 1]).addClass(tableClass); //console.log($("table.resultsTable"));
    table = "";
    currentTable = "";
    for (i = 0; i < tables.length; i++) {
        if (!$(tables[i]).hasClass("complete"))
            currentTable = $(tables[i]);
    }
    table = $(currentTable).children("tbody")[0];
    //console.log($(table).html());
    trs = $(table).children("tr");
    tdsVars = $(trs[0]).children("th");
    for (i = 0; i < trs.length; i++) {
        tds = $(trs[i]).children("td");
        if (entitiesType !== "") {
            // create the json elt with all result values
            var entity = {};
            for (k = 0; k < tds.length; k++) {
                entity[$(tdsVars[k]).text()] = $(tds[k]).text();
            }
            //availableElts.push(entity);            
        }
        a = "";
        for (j = 1; j < tds.length; j++) {
            div = $(tds[j]).children("div")[0];
            a = $(div).children("a.uri")[0];
            $(a).attr("target", "_blank");
            sparqlLink = 'sparqleditor.jsp?query=SELECT * \nWHERE{\n\tGRAPH ?graph{\n\t\t<' + $(a).text() + '> ?property ?object.\n\t}\n}';
            $(a).after('<a href="' + encodeURI(sparqlLink) + '" target="_blank" style="text-decoration: none; color:#00B5AD; font-weight:bold"> (in Sparql) </a>');
        }
        $(tds[1]).append('<a href="advancedSearch.jsp?type=' + entitiesType + '&uri=' + encodeURIComponent($(a).text()) + '" style="text-decoration: none; color:#00B5AD; font-weight:bold"> (display) </a>');
    }
    $(currentTable).addClass("complete");
}

function addNavButtons(nbResults, currentPage, divId, previousBtnId, nextBtnId) {
    div = $("#" + divId);
    div.html("");
    if (currentPage > 0) {
        div.html('<button class="yasrbtn" id="' + previousBtnId + '"><< Previous page</button>');
    }
    if (pageSize == nbResults) {
        div.append('<button class="yasrbtn" id="' + nextBtnId + '" style="position: relative;right: 0px;">Next page>></button>');
    }
}