<%-- 
    Document   : gene
    Created on : Sep 7, 2015, 3:04:42 PM
    Author     : tagny
--%>

<div id="qtlPage">
    <div id="header"></div>
    <br><div id="proteinContainer"><b style="font-size:13pt">Protein associations</b>
        <span id="proteinPageBtns"><a href="javascript:void(0)" id="protein"> + </a></span>
        <div id="proteinResult"></div>            
    </div>
    <br><div id="ontologyContainer"><b style="font-size:13pt">Ontology associations</b>
        <span id="ontologyPageBtns"><a href="javascript:void(0)" id="ontology"> + </a></span>
        <div id="ontologyResult"></div>            
    </div>
</div>
<script type="text/javascript">
    var qtlUri = <% out.println("'" + request.getParameter("uri") + "'");%>;    
    
    function getQtlDescription(uri) {
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
            dispalyHeader("qtl", json, "header");
            qtlId = json["results"]["bindings"][0]["Id"]["value"];
            //console.log(geneId);        
            addEvents(qtlId);
        });
    }
    $(document).ready(function () {
        getQtlDescription(qtlUri);
    });
    function  addEvents(qtlId) {        
        $("#protein").attr("onclick", 'searchProteinsAssociatedWithQtl(\'' + qtlId + '\', ' + currentProteinPage + ')');
        $("#ontology").attr("onclick", 'searchOntologyTermsAssociatedWithQtl(\'' + qtlId + '\', ' + currentOntologyPage+ ')');        
    }
    
    window.swagger = new SwaggerClient({
        url: url,
        success: function () {
            console.log("API definition well loaded");
        }
    });
    var currentProteinPage = 0;
    var currentOntologyPage = 0;
    function searchProteinsAssociatedWithQtl(qtlId, page) {
        currentGenePage = page;
        type = "protein";
        containerId = type+"Container";
        displayHoldMessage(type + "Result");        
        swagger.apis.protein.getProteinsAssociatedWithQtl({_format: ".sparql-json", qtlId: qtlId, _pageSize: pageSize, _page: page},
        {responseContentType: 'application/json'}, function (data) {
            sparqljson = data.data;
            resultId = type + "Result";
            displayResult(resultId, sparqljson);
            $("tr.odd").ready(function () {
                pageBtnsId = type + "PageBtns";
                displayInformation(data, page, containerId, pageBtnsId, "searchProteinsAssociatedWithQtl");
                processHtmlResult(type);
            });
        });
    }
    function searchOntologyTermsAssociatedWithQtl(proteinId, page) {
        currentGenePage = page;
        type = "ontology";
        containerId = type+"Container";
        displayHoldMessage(type + "Result");        
        swagger.apis.ontologies.getOntoTermsAssociatedWithQtl({_format: ".sparql-json", qtlId: proteinId, _pageSize: pageSize, _page: page},
        {responseContentType: 'application/json'}, function (data) {
            sparqljson = data.data;
            resultId = type + "Result";
            displayResult(resultId, sparqljson);
            $("tr.odd").ready(function () {
                pageBtnsId = type + "PageBtns";
                displayInformation(data, page, containerId, pageBtnsId, "searchOntologyTermsAssociatedWithQtl");
                processHtmlResult(type);
            });
        });
    }
    function displayInformation(data, page, type, pageBtnsId, functionName) {
        nbResults = data.obj["results"]["bindings"].length;
        previousBtnId = "previousPage";
        nextBtnId = "nextPage";
        addNavButtons(nbResults, page, pageBtnsId, previousBtnId, nextBtnId);
        $("#" + type + " #" + previousBtnId).attr("onclick", functionName + '("' + qtlId + '",' + (page - 1) + ')');
        $("#" + type + " #" + nextBtnId).attr("onclick", functionName + '("' + qtlId + '",' + (page + 1) + ')');
    }

</script>