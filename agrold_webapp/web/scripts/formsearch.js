// You can minify to load more fastly

var availableUri = [];
var availableDescription = [];
var availableElts;

// get the list of the entities
function getEntities(type, json) {
    console.log(json);
    availableElts = json;
    nbResults = Object.keys(json).length;
    for (i = 0; i < nbResults; i++) {
        availableDescription.push({label: json[i][type + "Id"] + " (" + json[i][type + "Name"] + " : " + json[i][type + "Description"] + ")", idx: i});
        availableUri.push(json[i][type]);
    }
    $("#input").attr('placeholder', "Type it's code/name...");
}

$(document).ready(function () {
    // initialize swagger, point to a resource listing
    window.swagger = new SwaggerClient({
        url: "http://volvestre.cirad.fr:8080/aldp/swagger/agrold.json",
        //url: "http://localhost:8084/aldp/swagger/agrold.json",
        success: function () {
        }
    });
    $("#elements").change(function () {
        //var input = document.getElementById("label");
        $("#input").css("display", "inline");
        $("#input").attr('placeholder', "Please wait a bit...");
        while (availableDescription.length > 0) { // empty for new possible values in autocompletion
            availableDescription.pop();
            availableUri.pop();
        }
        document.getElementById("input").value = ""; // empty the textfield for the results of the new query

        switch ($("#elements").val()) {
            case "gene":
                swagger.apis.gene.getGenes({_format: ".json"}, {responseContentType: 'application/json'}, function (data) {
                    getEntities("gene", data.obj);
                });
                break;
            case "protein":
                swagger.apis.protein.getProteins({_format: ".json"}, {responseContentType: 'application/json'}, function (data) {
                    getEntities("protein", data.obj);
                });
                break;
            case "qtl":
                swagger.apis.qtl.getQtls({_format: ".json"}, {responseContentType: 'application/json'}, function (data) {
                    getEntities("qtl", data.obj);
                });
                break;
            default:
                $("#result").html("");
        }
    });
    $("#input").autocomplete({
        source: availableDescription,
        minLength: 1, // minimum number of characters before autocompletion 
        select: function (event, ui) { // lors de la sélection d'une proposition                                  
            switch ($("#elements").val()) {
                case "gene":
                    $("#result").html("<b>Gene : </b>");
                    displayGenePage(availableElts[ui.item.idx]);
                    break;
                case "protein":
                    displayProteinPage(availableElts[ui.item.idx]);
                    break;
                case "qtl":
                    displayQTLPage(availableElts[ui.item.idx]);
                    break;
                default:
                    $("#result").html("");
            }
        }
    });
});
/*
 * Display a web page aggregating information over Genes
 * 
 * @param {a line of the result set of getGenes} gene
 * @returns nothing
 */
function displayGenePage(gene) {
    dispalyHeader("gene", gene, "result");
    $("#result").append('<br><div id="protein"><b style="font-size:13pt">encodes proteins<a href="#" onclick="showProteinsEncodedByGene(\'' + gene["geneId"] + '\')"> + </a></b></div>');
    $("#result").append('<br><div id="pathway"><b style="font-size:13pt">Pathways<a href="#"> + </a></b></div>');
}
function showProteinsEncodedByGene(geneId) {
    displayHoldMessage("protein");
    swagger.apis.protein.getProteinsEncodedByGene({_format: ".html", geneId: geneId}, {responseContentType: 'text/html'}, function (data) {
        displayResult("protein", "encodes proteins", processHtmlResult(data.data));
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
    $("#result").append('<br><div id="gene"><b style="font-size:13pt">is encoded by<a href="#" onclick="showGenesEncodingProtein(\'' + protein["proteinId"] + '\')"> + </a></b></div>');
    $("#result").append('<br><div id="neighbor"><b style="font-size:13pt">Local neighborhood<a href="#"> + </a></b></div>');
    $("#result").append('<br><div id="qtlAsso"><b style="font-size:13pt">QTL associations<a href="#" onclick="showQtlsAssociatedWithProtein(\'' + protein["proteinId"] + '\')"> + </a></b></div>');
    $("#result").append('<br><div id="ontoAsso"><b style="font-size:13pt">Ontology associations<a href="#" onclick="showOntologyTermsAssociatedWithProtein(\'' + protein["proteinId"] + '\')"> + </a></b></div>');
}
function showGenesEncodingProtein(proteinId) {
    displayHoldMessage("gene");
    swagger.apis.gene.getGenesEncodingProteins({_format: ".html", proteinId: proteinId}, {responseContentType: 'text/html'}, function (data) {
        displayResult("gene", "is encoded by", processHtmlResult(data.data));
    });
}
function showQtlsAssociatedWithProtein(proteinId) {
    displayHoldMessage("qtlAsso");
    swagger.apis.qtl.getQtlsAssociatedWithProteinId({_format: ".html", proteinId: proteinId}, {responseContentType: 'text/html'}, function (data) {
        displayResult("qtlAsso", "QTL associations", processHtmlResult(data.data));
    });
}
function showOntologyTermsAssociatedWithProtein(proteinId) {
    displayHoldMessage("ontoAsso");//.append('Please wait!');
    swagger.apis.ontologies.getOntoTermsAssociatedWithProtein({_format: ".html", proteinId: proteinId}, {responseContentType: 'text/html'}, function (data) {
        displayResult("ontoAsso", "Ontology associations", processHtmlResult(data.data));
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
    $("#result").append('<br><div id="protAsso"><b style="font-size:13pt">Protein associations<a href="#" onclick="showProteinsAssociatedWithQtl(\'' + qtl["qtlId"] + '\')"> + </a></b></div>');
    $("#result").append('<br><div id="ontoAsso"><b style="font-size:13pt">Ontology associations<a href="#" onclick="showOntologyTermsAssociatedWithQtl(\'' + qtl["qtlId"] + '\')"> + </a></b></div>');
    //showProteinsAssociatedWithQtl();
}
function showProteinsAssociatedWithQtl(qtlId) {
    displayHoldMessage("protAsso");//$("#protAsso").append('Please wait!');
    swagger.apis.protein.getProteinsAssociatedWithQtl({_format: ".html", qtlId: qtlId}, {responseContentType: 'text/html'}, function (data) {
        displayResult("protAsso", "Protein associations", processHtmlResult(data.data));
    });
}
function showOntologyTermsAssociatedWithQtl(qtlId) {
    displayHoldMessage("ontoAsso");//.append('Please wait!');
    swagger.apis.ontologies.getOntoTermsAssociatedWithQtl({_format: ".html", qtlId: qtlId}, {responseContentType: 'text/html'}, function (data) {
        displayResult("ontoAsso", "Ontology associations", processHtmlResult(data.data));
    });
}
// first information
function dispalyHeader(eltType, elt, pageLocationId) {
    $("#" + pageLocationId).html('<b style="font-size:16pt;text-transform: uppercase">' + eltType + ' : ' + elt[eltType + "Id"] + " / " + elt[eltType + "Name"] + "</b>");
    $("#" + pageLocationId).append('<br><big>' + elt[eltType + "Description"] + '</big>');
    $("#" + pageLocationId).append('<br><br> <b> IRI: </b>  <a href="' + elt[eltType] + '" target="_blank"><i>' + elt[eltType] + '</i></a><br>');
}
// general message
function displayHoldMessage(pageLocationId) {
    $('#' + pageLocationId).append('Please wait!');
}
function displayResult(pageLocationId, title, result) {
    $('#' + pageLocationId).html('<b style="font-size:13pt">' + title + '</b> ' + showNbResults(result, '.html'));
    $('#' + pageLocationId).append(result);
}
function processHtmlResult(result) {
    iDiv = document.createElement("div");
    $(iDiv).html(result);
    $(iDiv).addClass("resultTemp");
    table = $(iDiv).children(".sparql")[0];
    table = $(table).children("tbody")[0];
    trs = $(table).children("tr");
    for (i = 0; i < trs.length; i++) {
        tds = $(trs[i]).children("td");
        for (j = 0; j < tds.length; j++) {
            a = $(tds[j]).children("a")[0];
            $(a).attr("target", "_blank");
            $(a).after('<a href="sparqleditor.jsp?query=DESCRIBE <'+$(a).text()+'>" target="_blank" style="text-decoration: none; color:green; font-weight:bold"> ( in AgroLD ) </a>');
        }
    }
    return $(iDiv).html();
}
function addDescriptionLinkToTableResult(table) {

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
            return ' ( ' + (nbLines - 1) + ' founded )';
            break;
        default:
            return "??";
    }
}