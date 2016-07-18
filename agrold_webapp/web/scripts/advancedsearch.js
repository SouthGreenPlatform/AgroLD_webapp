// You can minify to load more fastly

var availableElts = [];
var pageSize = 10;
//var currentPage = 0;
var url= "http://volvestre.cirad.fr:8080/agrold/swagger/agrold.json";
//var url = "http://localhost:8084/agrold/swagger/agrold.json";
//var url = "swagger/agrold.json";

function displayFormSearchResult(currentPages, result, title, pageLocation, typeOfResult, typeOfEntities, keyword) {
    displayResult(pageLocation, title, processHtmlResult(result, typeOfResult));
    nbResults = (countSubstringOccurences(result, "<tr>", 1) - 1);
    //addNavButtons(currentPages, pageLocationId, nbResults, typeOfResult, searchFunctionStr)
    addNavButtons(currentPages,pageLocation, nbResults, typeOfResult, 'getEntities(\'' + typeOfEntities + '\',\'' + keyword + '\', \''+typeOfResult+'\')' );
}

// get the list of the entities
function getEntities(type, keyword, currentPages,  typeOfResult) {
    displayHoldMessage("result");
    // initialize swagger, point to a resource listing
    page = currentPages[typeOfResult];
    window.swagger = new SwaggerClient({
        url: url,
        success: function () {
            console.log("API definition well loaded")
            switch (type) {
                case "gene":
                    swagger.apis.gene.getGenesByKeyWord({_format: ".html", keyword: keyword, _pageSize: pageSize, _page: page}, {responseContentType: 'text/html'}, function (data) {
                        displayFormSearchResult(currentPages, data.data, "Result", "result", typeOfResult, "gene", keyword);
                    });
                    break;
                case "protein":
                    swagger.apis.protein.getProteinsByKeyWord({_format: ".html", keyword: keyword, _pageSize: pageSize, _page: page}, {responseContentType: 'text/html'}, function (data) {
                        displayFormSearchResult(currentPages, data.data, "Result", "result", typeOfResult, "protein", keyword);
                    });
                    break;
                case "qtl":
                    swagger.apis.qtl.getQtlsByKeyWord({_format: ".html", keyword: keyword, _pageSize: pageSize, _page: page}, {responseContentType: 'text/html'}, function (data) {
                        displayFormSearchResult(currentPages, data.data, "Result", "result", typeOfResult, "qtl", keyword);
                    });
                    break;
                default:
                    $("#result").html("");
            }
        }
    });
}

function reinitAvailableElts() {
    while (availableElts.length > 0) { // empty for new possible values in autocompletion
        availableElts.pop();
    }
}
/*
 * Display a web page aggregating information over Genes
 * 
 * @param {a line of the result set of getGenes} gene
 * @returns nothing
 */
function displayGenePage(gene) {
    dispalyHeader("gene", gene, "result");
    $("#result").append('<br><div id="chromosome"><b style="font-size:13pt">is located on<a href="javascript:void(0)" onclick="showProteinsEncodedByGene(\'' + gene["Id"] + '\')"> + </a></b></div>');
    $("#result").append('<br><div id="protein"><b style="font-size:13pt">encodes proteins<a href="javascript:void(0)" onclick="showProteinsEncodedByGene(\'' + gene["Id"] + '\')"> + </a></b></div>');
    $("#result").append('<br><div id="pathway"><b style="font-size:13pt">Pathways<a href="javascript:void(0)" onclick="showPathwayInWhichParticipatesGene(\'' + gene["Id"] + '\')"> + </a></b></div>');
}
function showProteinsEncodedByGene(geneId) {
    displayHoldMessage("protein");
    swagger.apis.protein.getProteinsEncodedByGene({_format: ".html", geneId: geneId}, {responseContentType: 'text/html'}, function (data) {
        displayResult("protein", "encodes proteins", processHtmlResult(data.data, "protein"));
    });
}
function showPathwayInWhichParticipatesGene(geneId) {
    displayHoldMessage("pathway");
    swagger.apis.pathway.getPathwaysInWhichParticipatesGene({_format: ".html", geneId: geneId}, {responseContentType: 'text/html'}, function (data) {
        displayResult("pathway", "Pathways", processHtmlResult(data.data, "pathway"));
    });
}
/*
 * Display a web page aggregating information over Proteins
 * 
 * @param {a line of the result set of getProteins} protein
 * @returns nothing
 */
function displayProteinPage(protein) {
    dispalyHeader("protein", protein, "result");
    $("#result").append('<br><div id="gene"><b style="font-size:13pt">is encoded by<a href="javascript:void(0)" onclick="showGenesEncodingProtein(\'' + protein["Id"] + '\')"> + </a></b></div>');
    $("#result").append('<br><div id="neighbor"><b style="font-size:13pt">Local neighborhood<a href="javascript:void(0)"> + </a></b></div>');
    $("#result").append('<br><div id="qtlAsso"><b style="font-size:13pt">QTL associations<a href="javascript:void(0)" onclick="showQtlsAssociatedWithProtein(\'' + protein["Id"] + '\')"> + </a></b></div>');
    $("#result").append('<br><div id="ontoAsso"><b style="font-size:13pt">Ontology associations<a href="javascript:void(0)" onclick="showOntologyTermsAssociatedWithProtein(\'' + protein["Id"] + '\')"> + </a></b></div>');
}
function showGenesEncodingProtein(proteinId) {
    displayHoldMessage("gene");
    swagger.apis.gene.getGenesEncodingProteins({_format: ".html", proteinId: proteinId}, {responseContentType: 'text/html'}, function (data) {
        displayResult("gene", "is encoded by", processHtmlResult(data.data, "gene"));
    });
}
function showQtlsAssociatedWithProtein(proteinId) {
    displayHoldMessage("qtlAsso");
    swagger.apis.qtl.getQtlsAssociatedWithProteinId({_format: ".html", proteinId: proteinId}, {responseContentType: 'text/html'}, function (data) {
        displayResult("qtlAsso", "QTL associations", processHtmlResult(data.data, "qtl"));
    });
}
function showOntologyTermsAssociatedWithProtein(proteinId) {
    displayHoldMessage("ontoAsso");//.append('Please wait!');
    swagger.apis.ontologies.getOntoTermsAssociatedWithProtein({_format: ".html", proteinId: proteinId}, {responseContentType: 'text/html'}, function (data) {
        displayResult("ontoAsso", "Ontology associations", processHtmlResult(data.data, "ontology"));
    });
}
/*
 * Display a web page aggregating information over QTL
 * 
 * @param {a line of the result set of getQtls} qtl
 * @returns nothing
 */
function displayQTLPage(qtl) {
    dispalyHeader("qtl", qtl, "result");
    $("#result").append('<br><div id="protAsso"><b style="font-size:13pt">Protein associations<a href="javascript:void(0)" onclick="showProteinsAssociatedWithQtl(\'' + qtl["Id"] + '\')"> + </a></b></div>');
    $("#result").append('<br><div id="ontoAsso"><b style="font-size:13pt">Ontology associations<a href="javascript:void(0)" onclick="showOntologyTermsAssociatedWithQtl(\'' + qtl["Id"] + '\')"> + </a></b></div>');
//showProteinsAssociatedWithQtl();
}
function showProteinsAssociatedWithQtl(qtlId) {
    displayHoldMessage("protAsso");//$("#protAsso").append('Please wait!');
    swagger.apis.protein.getProteinsAssociatedWithQtl({_format: ".html", qtlId: qtlId}, {responseContentType: 'text/html'}, function (data) {
        displayResult("protAsso", "Protein associations", processHtmlResult(data.data, "protein"));
    });
}
function showOntologyTermsAssociatedWithQtl(qtlId) {
    displayHoldMessage("ontoAsso");//.append('Please wait!');
    swagger.apis.ontologies.getOntoTermsAssociatedWithQtl({_format: ".html", qtlId: qtlId}, {responseContentType: 'text/html'}, function (data) {
        displayResult("ontoAsso", "Ontology associations", processHtmlResult(data.data, "ontology"));
    });
}
// first information
function dispalyHeader(eltType, elt, pageLocationId) {
    $("#" + pageLocationId).html('<b style="font-size:16pt;text-transform: uppercase">' + eltType + ' : ' + elt["Id"] + " / " + elt["Name"] + "</b>");
    $("#" + pageLocationId).append('<br><big>' + elt["Description"] + '</big>');
    $("#" + pageLocationId).append('<br><br> <b> IRI: </b>  <a href="' + elt["IRI"] + '" target="_blank"><i>' + elt["IRI"] + '</i></a><br>');
}
// general message
function displayHoldMessage(pageLocationId) {
    //$('#' + pageLocationId).append('Please wait!');
    $('#' + pageLocationId).html('<div id="fountainG">\n\
	<div id="fountainG_1" class="fountainG"></div>\n\
	<div id="fountainG_2" class="fountainG"></div>\n\
	<div id="fountainG_3" class="fountainG"></div>\n\
	<div id="fountainG_4" class="fountainG"></div>\n\
	<div id="fountainG_5" class="fountainG"></div>\n\
	<div id="fountainG_6" class="fountainG"></div>\n\
	<div id="fountainG_7" class="fountainG"></div>\n\
	<div id="fountainG_8" class="fountainG"></div>\n\
</div>');
}
function displayResult(pageLocationId, title, result) {
    $('#' + pageLocationId).html('<b style="font-size:13pt">' + title + '</b> ' + showNbResults(result, '.html'));
    $('#' + pageLocationId).append(result);
}
function addPreviousButton(currentPages, pageLocationId, typeOfResult, searchFunctionStr) {
    if (currentPages[typeOfResult] > 0) {
        $("#" + pageLocationId).append('<button class="btn" onclick="currentPages[\'' + typeOfResult + '\']--;' + searchFunctionStr + '"><< Previous</button> ');
    } else {
        console.log(typeOfResult + ":"+ currentPages['"'+typeOfResult+'"']);
    }
}
function addNextButton(pageLocationId, nbResults, typeOfResult, searchFunctionStr) {
    if (pageSize == nbResults) {
        $("#" + pageLocationId).append(' <button class="btn" onclick="currentPages[\'' + typeOfResult + '\']++;' + searchFunctionStr + '">Next >></button>');
    }
}
function addNavButtons(currentPages, pageLocationId, nbResults, typeOfResult, searchFunctionStr) {
    addPreviousButton(currentPages, pageLocationId, typeOfResult, searchFunctionStr);
    addNextButton(pageLocationId, nbResults, typeOfResult, searchFunctionStr);
    /*if (currentPage > 0) {
     $("#result").append('<button class="btn" onclick="currentPage--;getEntities(\'' + type + '\',\'' + keyword + '\')"><< Previous</button>');
     }
     if (pageSize == nbResults) {
     $("#result").append('<button class="btn" onclick="currentPage++;getEntities(\'' + type + '\',\'' + keyword + '\')" style="position: relative;right: 0px;">Next>></button>');
     }*/
}

function processHtmlResult(result, entitiesType) {
    entitiesTypes = typeof entitiesTypes !== 'undefined' ? entitiesTypes : "";
    idColumn = typeof idColumn !== 'undefined' ? idColumn : 1;
    iDiv = document.createElement("div");
    $(iDiv).html(result);
    $(iDiv).addClass("resultTemp");
    table = $(iDiv).children(".sparql")[0];
    table = $(table).children("tbody")[0];
    trs = $(table).children("tr");
    tdsVars = $(trs[0]).children("th");
    for (i = 1; i < trs.length; i++) {
        tds = $(trs[i]).children("td");
        if (entitiesType !== "") {
            // create the json elt with all result values
            var entity = {};
            for (k = 0; k < tds.length; k++) {
                entity[$(tdsVars[k]).text()] = $(tds[k]).text();
            }
            availableElts.push(entity);
            $(tds[0]).append('<a href="javascript:void(0)" onclick="displayPage(\'' + entitiesType + '\',' + (i - 1) + ')" style="text-decoration: none; color:#00B5AD; font-weight:bold"> (display) </a>');
        }
        for (j = 1; j < tds.length; j++) {
            a = $(tds[j]).children("a")[0];
            $(a).attr("target", "_blank");
            $(a).after('<a href="sparqleditor.jsp?query=SELECT * WHERE{<' + $(a).text() + '> ?property ?object.}" target="_blank" style="text-decoration: none; color:#00B5AD; font-weight:bold"> (in Sparql) </a>');
        }
    }
    return $(iDiv).html();
}
function displayPage(entityType, entityNumber) {
    switch (entityType) {
        case "gene":
            displayGenePage(availableElts[entityNumber]);
            break;
        case "protein":
            displayProteinPage(availableElts[entityNumber]);
            break;
        case "qtl":
            displayQTLPage(availableElts[entityNumber]);
            break;
        default:
            $("#result").html("");
    }
    reinitAvailableElts();
}
/** Function count the occurrences of substring in a string;
 * used to know the size of the results set
 * @param {String} string   Required. The string;
 * @param {String} subString    Required. The string to search for;
 * @param {Boolean} allowOverlapping    Optional. Default: false;
 */
function countSubstringOccurences(string, subString, allowOverlapping) {
    string += "";
    subString += "";
    if (subString.length <= 0)
        return string.length + 1;
    var n = 0, pos = 0;
    var step = (allowOverlapping) ? (1) : (subString.length);

    while (true) {
        pos = string.indexOf(subString, pos);
        if (pos >= 0) {
            n++;
            pos += step;
        } else
            break;
    }
    return(n);
}
// count the number of lines in the HTML format result set
function showNbResults(result, format) {
    switch (format) {
        case '.html':
            nbLines = countSubstringOccurences(result, "<tr>", 1);
            if (nbLines == 0)
                return " ( ERROR )";
            return ' ( ' + (nbLines - 1) + ' found )';
            break;
        default:
            return "??";
    }
}