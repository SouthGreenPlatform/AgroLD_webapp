<%-- 
    Document   : gene
    Created on : Sep 7, 2015, 3:04:42 PM
    Author     : tagny
--%>

<div id="proteinPage">
    <div id="header"></div>
    <br><div id="geneContainer"><b style="font-size:13pt">is encoded by</b>
        <span id="genePageBtns"><a href="javascript:void(0)" id="gene"> + </a></span>
        <div id="geneResult"></div>            
    </div>

    <!--br><div id="neighborContainer"><b style="font-size:13pt">Local neighborhood</b>
        <span id="neighborPageBtns"><a href="javascript:void(0)" id="neighbor"> + </a></span>
        <div id="neighborResult"></div>            
    </div-->

    <br><div id="qtlContainer"><b style="font-size:13pt">QTL associations</b>
        <span id="qtlPageBtns"><a href="javascript:void(0)" id="qtl"> + </a></span>
        <div id="qtlResult"></div>
    </div>
    <br><div id="ontologyContainer"><b style="font-size:13pt">Ontology associations</b>
        <span id="ontologyPageBtns"><a href="javascript:void(0)" id="ontology"> + </a></span>
        <div id="ontologyResult"></div>
    </div>
</div>
<script type="text/javascript">
    var proteinUri = <% out.println("'" + request.getParameter("uri") + "'");%>;    
    
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
            dispalyHeader("protein", json, "header");
            proteinId = json["results"]["bindings"][0]["Id"]["value"];                    
            addEvents(proteinId);
        });
    }
    $(document).ready(function () {
        //displayHoldMessage("header");
        getProteinDescription(proteinUri);
    });
    function  addEvents(proteinId) {        
        console.log(proteinId);
        $("#gene").attr("onclick", 'searchGenesEncodingProtein(\'' + proteinId + '\', ' + currentGenePage + ')');
        //$("#neighbor").attr("onclick", 'search(\'' + proteinId + '\', ' + currentNeighborPage+ ')');
        $("#qtl").attr("onclick", 'searchQtlsAssociatedWithProtein(\'' + proteinId + '\',' + currentQtlPage + ')');
        $("#ontology").attr("onclick", 'searchOntologyTermsAssociatedWithProtein(\'' + proteinId + '\',' + currentOntologyPage + ')');
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
    function displayInformation(data, page, type, pageBtnsId, functionName) {
        nbResults = data.obj["results"]["bindings"].length;
        previousBtnId = "previousPage";
        nextBtnId = "nextPage";
        addNavButtons(nbResults, page, pageBtnsId, previousBtnId, nextBtnId);
        $("#" + type + " #" + previousBtnId).attr("onclick", functionName + '("' + proteinId + '",' + (page - 1) + ')');
        $("#" + type + " #" + nextBtnId).attr("onclick", functionName + '("' + proteinId + '",' + (page + 1) + ')');
    }

</script>