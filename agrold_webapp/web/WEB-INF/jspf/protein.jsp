<%-- 
    Document   : gene
    Created on : Sep 7, 2015, 3:04:42 PM
    Author     : tagny
--%>

<div id="proteinPage">
    <div id="header"></div>
    <br><div id="geneContainer"><b style="font-size:13pt">is encoded by</b>
        <span id="genePageBtns" class="pageNavBtns"><a href="javascript:void(0)" id="gene"> + </a></span>
        <div id="geneResult"></div>            
    </div>    
    <br><div id="qtlContainer"><b style="font-size:13pt">QTL associations</b>
        <span id="qtlPageBtns" class="pageNavBtns"><a href="javascript:void(0)" id="qtl"> + </a></span>
        <div id="qtlResult"></div>
    </div>
    <br><div id="ontologyContainer"><b style="font-size:13pt">Ontology associations</b>
        <span id="ontologyPageBtns" class="pageNavBtns"><a href="javascript:void(0)" id="ontology"> + </a></span>
        <div id="ontologyResult"></div>
    </div>
    <!--br><div id="publicationContainer"><b style="font-size:13pt">Publication</b>
        <span id="publicationPageBtns" class="pageNavBtns"><a href="javascript:void(0)" id="publication"> + </a></span>
        <div id="publicationResult"></div>            
    </div-->
</div>
<script type="text/javascript">
    var proteinUri = <% out.println("'" + request.getParameter("uri") + "'");%>;
    var proteinId = "";
    function getProteinDescription(uri) {
        sparql = 'PREFIX agrold:<http://www.southgreen.fr/agrold/vocabulary/> \
PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> \
SELECT distinct  ?Id ?Name ?Description (?entity AS ?Uri) \
WHERE { \
  VALUES ?entity{ \
    <' + uri + '> \
  } \
?entity agrold:description ?Description .  \
OPTIONAL{?entity rdfs:label ?Name .} \
BIND(REPLACE(str(?entity), \'^.*(#|/)\', "") AS ?Id) \
}';

        query = sparqlEndpoint + "?query=" + encodeURIComponent(sparql) + "&format=application/json";
        $.get(query, function (json) {
            count = json["results"]["bindings"].length;
            if (count > 0) {
                //console.log(count);
                dispalyHeader("protein", json, "header");                
                proteinId = json["results"]["bindings"][0]["Id"]["value"];
                addEvents(proteinId);
            } else {
                $("#proteinPage").html('<span style="text-transform: capitalize">'+type + '</span> " <b>' + uri + ' </b>" not found.');
            }
        });
    }
    $(document).ready(function () {
        //displayHoldMessage("header");
        getProteinDescription(proteinUri);
    });
    function  addEvents(proteinId) {
        console.log(proteinId);
        $("#gene").attr("onclick", 'searchGenesEncodingProtein(\'' + proteinId + '\', ' + currentGenePage + ')');
        $("#qtl").attr("onclick", 'searchQtlsAssociatedWithProtein(\'' + proteinId + '\',' + currentQtlPage + ')');
        $("#ontology").attr("onclick", 'searchOntologyTermsAssociatedWithProtein(\'' + proteinId + '\',' + currentOntologyPage + ')');
        $("#publication").attr("onclick", 'searchPublications(\'' + proteinId + '\')');
    }

    window.swagger = new SwaggerClient({
        url: url,
        success: function () {
            console.log("API definition well loaded");
        }
    });
    var currentGenePage = 0;
    var currentNeighborPage = 0;
    var currentQtlPage = 0;
    var currentOntologyPage = 0;
    function searchGenesEncodingProtein(proteinId, page) {
        currentGenePage = page;
        type = "gene";
        containerId = "geneContainer";
        displayHoldMessage(type + "Result");
        swagger.apis.gene.getGenesEncodingProteins({_format: ".sparql-json", proteinId: proteinId, _pageSize: pageSize, _page: currentGenePage},
        {responseContentType: 'application/json'}, function (data) {
            sparqljson = data.data;
            resultId = type + "Result";
            displayResult(resultId, sparqljson);
            $("tr.odd").ready(function () {
                pageBtnsId = type + "PageBtns";
                displayInformation(data, page, containerId, pageBtnsId, "searchGenesEncodingProtein");
                processHtmlResult(type);
            });
        });
    }
    function searchQtlsAssociatedWithProtein(proteinId, page) {
        currentGenePage = page;
        type = "qtl";
        containerId = "qtlContainer";
        displayHoldMessage(type + "Result");
        swagger.apis.qtl.getQtlsAssociatedWithProteinId({_format: ".sparql-json", proteinId: proteinId, _pageSize: pageSize, _page: currentGenePage},
        {responseContentType: 'application/json'}, function (data) {
            sparqljson = data.data;
            resultId = type + "Result";
            displayResult(resultId, sparqljson);
            $("tr.odd").ready(function () {
                pageBtnsId = type + "PageBtns";
                displayInformation(data, page, containerId, pageBtnsId, "searchQtlsAssociatedWithProtein");
                processHtmlResult(type);
            });
        });
    }
    function searchOntologyTermsAssociatedWithProtein(proteinId, page) {
        currentGenePage = page;
        type = "ontology";
        containerId = "ontologyContainer";
        displayHoldMessage(type + "Result");
        swagger.apis.ontologies.getOntoTermsAssociatedWithProtein({_format: ".sparql-json", proteinId: proteinId, _pageSize: pageSize, _page: currentGenePage},
        {responseContentType: 'application/json'}, function (data) {
            sparqljson = data.data;
            resultId = type + "Result";
            displayResult(resultId, sparqljson);
            $("tr.odd").ready(function () {
                pageBtnsId = type + "PageBtns";
                displayInformation(data, page, containerId, pageBtnsId, "searchOntologyTermsAssociatedWithProtein");
                processHtmlResult(type);
            });
        });
    }
    function searchPublications(proteinId) {
        displayHoldMessage("publicationResult");
        // get PubMed Id from G-link web service
        swagger.apis.protein.getPublicationsOfProteinById({proteinId: proteinId},
        {responseContentType: 'application/json'}, function (data) {
            //console.log(data.data);            
            json = data.obj;
            displayPublications(json, "publicationResult");
            /*console.log(json);
            if (json.length > 0) {
                $("#publicationResult").append("<ol></ol>");
                for (i = 0; i < json.length; i++) {
                    $("#publication").html("");
                    url = json[i]["URL"];
                    $("#publicationResult ol").append('<li id="paper'+i+'"><span>'+json[i]["Authors"]+', " <b>'+json[i]["Title"]+'</b> ", <i>'+json[i]["Journal"]+'</i>, '+json[i]["Year"]+'</span></li>');
                    $("#publicationResult ol li#paper"+i).append('<br><span>More at: <a href="' + url + '" target="_blank">' + url + '</a></span>');                    
                }
            } else {                
                $("#publicationResult").append("No publication found.")
            }*/
        });
    }
    function displayInformation(data, page, type, pageBtnsId, functionName) {
        nbResults = data.obj["results"]["bindings"].length;
        previousBtnId = "previousPage";
        nextBtnId = "nextPage";
        addNavButtons(nbResults, page, pageBtnsId, previousBtnId, nextBtnId);
        $("#" + type + " #" + previousBtnId).attr("onclick", functionName + '("' + proteinId + '",' + (page - 1) + ')');
        $("#" + type + " #" + nextBtnId).attr("onclick", functionName + '("' + proteinId + '",' + (page + 1) + ')');
    }

</script>