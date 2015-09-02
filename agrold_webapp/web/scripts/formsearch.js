var availableUri = [];
var availableDescription = [];
var availableElts;
// initialize swagger, point to a resource listing

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
    window.swagger = new SwaggerClient({
        url: "http://volvestre.cirad.fr:8080/aldp/swagger/agrold.json",
        //url: "http://localhost:8084/aldp/swagger/agrold.json",
        success: function () { //console.log("joh");
            // upon connect, fetch a pet and set contents to element "mydata"
            //swagger.apis.gene.getgenes({_format: "json"}, {responseContentType: 'application/json'}, function (data) {
            //document.getElementById("mydata").innerHTML = JSON.stringify(data.obj);
            //});
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

function displayGenePage(gene) {
    $("#result").html('<b style="font-size:16pt">Gene : ' + gene["geneId"] + " / " + gene["geneName"] + "</b>");
    $("#result").append('<br>' + gene["geneDescription"]);
    $("#result").append('<br><a href="' + gene["gene"] + '" target="_blank"><i>Source</i></a>');
    $("#result").append('<div id="pathway"><b style="font-size:13pt">Pathways<a href="#"> + </a></b></div>');
}
function displayProteinPage(protein) {
    $("#result").html('<b style="font-size:16pt">Protein : ' + protein["proteinId"] + " / " + protein["proteinName"] + "</b>");
    $("#result").append('<br>' + protein["proteinDescription"]);
    $("#result").append('<br><a href="' + protein["protein"] + '" target="_blank"><i>Source</i></a>');
    $("#result").append('<div id="neighbor"><b style="font-size:13pt">Local neighborhood<a href="#"> + </a></b></div>');
    $("#result").append('<div id="qtlAsso"><b style="font-size:13pt">QTL associations<a href="#"> + </a></b></div>');
    $("#result").append('<div id="ontoAsso"><b style="font-size:13pt">Ontological associations<a href="#"> + </a></b></div>');
}
function displayQTLPage(qtl) {
    $("#result").html('<b style="font-size:16pt">QTL : ' + qtl["qtlId"] + " / " + qtl["qtlName"] + "</b>");
    $("#result").append('<br>' + qtl["qtlDescription"]);
    $("#result").append('<br><a href="' + qtl["qtl"] + '" target="_blank"><i>Source</i></a>');
    $("#result").append('<div id="protAsso"><b style="font-size:13pt">Protein associations<a href="#" onclick="showProteinsAssociatedWithQtl(\'' + qtl["qtlId"] + '\')"> + </a></b></div>');
    $("#result").append('<div id="ontoAsso"><b style="font-size:13pt">Ontological associations<a href="#"> + </a></b></div>');
}

function showProteinsAssociatedWithQtl(qtlId) {
    swagger.apis.protein.getProteinsAssociatedWithQtl({_format: ".html", qtlId: qtlId}, {responseContentType: 'text/html'}, function (data) {
        $("#protAsso").html('<b style="font-size:13pt">Protein associations</b>');
        $("#protAsso").append(data.data);
        console.log(data.data);
    });
}
function showAssociatedWithQtl(qtlId) {
    swagger.apis.protein.getProteinsAssociatedWithQtl({_format: ".html", qtlId: qtlId}, {responseContentType: 'text/html'}, function (data) {
        $("#protAsso").append(data.data);
        console.log(data.data);
    });
}