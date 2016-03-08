<%-- 
    Document   : gene
    Created on : Sep 7, 2015, 3:04:42 PM
    Author     : tagny
--%>

<div id="ontologyPage">
    <div id="header"></div>
    <br><div id="parentContainer"><b style="font-size:13pt">Parents</b>
        <span id="parentPageBtns" class="pageNavBtns"><a href="javascript:void(0)" id="parent"> + </a></span>
        <div id="parentResult"></div>            
    </div>
    <br><div id="childrenContainer"><b style="font-size:13pt">Children</b>
        <span id="childrenPageBtns" class="pageNavBtns"><a href="javascript:void(0)" id="children"> + </a></span>
        <div id="childrenResult"></div>            
    </div>
    <br><div id="proteinContainer"><b style="font-size:13pt">Proteins associated</b>
        <span id="proteinPageBtns" class="pageNavBtns"><a href="javascript:void(0)" id="protein"> + </a></span>
        <div id="proteinResult"></div>            
    </div>
    <br><div id="qtlContainer"><b style="font-size:13pt">QTL associated</b>
        <span id="qtlPageBtns" class="pageNavBtns"><a href="javascript:void(0)" id="qtl"> + </a></span>
        <div id="qtlResult"></div>
    </div>
</div>
<script type="text/javascript">
    var ontologyUri = <% out.println("'" + request.getParameter("uri") + "'");%>;    
    
    function getontologyDescription(uri) {
        sparql = 'PREFIX agrold:<http://www.southgreen.fr/agrold/vocabulary/> \
PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> \
PREFIX meaning:<http://purl.obolibrary.org/obo/IAO_0000115> \
SELECT distinct  ?Id ?Name ?Description (?entity AS ?Uri) \
WHERE { \
  VALUES ?entity{ \
    <' + uri + '> \
  } \
{?entity meaning: ?Description}UNION{BIND("" as ?Description)} .  \
{?entity rdfs:label ?Name}UNION{BIND("" as ?Name)} \
BIND(REPLACE(str(?entity), \'^.*(#|/)\', \"\") AS ?localname)\
BIND(REPLACE(?localname, \"_\", \":\") as ?Id).\
}';
        query = sparqlEndpoint + "?query=" + encodeURIComponent(sparql) + "&format=application/json";
        $.get(query, function (json) {
            dispalyHeader("ontology", json, "header");
            ontologyId = json["results"]["bindings"][0]["Id"]["value"];
            //console.log(geneId);        
            addEvents(ontologyId);
        });
    }
    $(document).ready(function () {
        getontologyDescription(ontologyUri);
    });
    function  addEvents(ontologyId) {        
        $("#parent").attr("onclick", 'searchParentById(\'' + ontologyId + '\', ' + currentParentPage + ')');
        $("#children").attr("onclick", 'searchChildrenById(\'' + ontologyId + '\', ' + currentChildrenPage+ ')');        
        $("#protein").attr("onclick", 'searchProteinIdAssociatedWithOntoId(\'' + ontologyId + '\', ' + currentChildrenPage+ ')');        
        $("#qtl").attr("onclick", 'searchQtlsIdAssociatedWithOntoId(\'' + ontologyId + '\', ' + currentChildrenPage+ ')');        
    }
    
    window.swagger = new SwaggerClient({
        url: url,
        success: function () {
            console.log("API definition well loaded");
        }
    });
    var currentParentPage = 0;
    var currentChildrenPage = 0;
    var currentProteinPage = 0;
    var currentQtlPage = 0;
    function searchParentById(ontologyId, page) {
        currentParentPage = page;
        type = "parent";
        containerId = type+"Container";
        displayHoldMessage(type + "Result");        
        swagger.apis.ontologies.getParentById({_format: ".sparql-json", id: ontologyId, _pageSize: pageSize, _page: page},
        {responseContentType: 'application/json'}, function (data) {
            sparqljson = data.data;
            resultId = type + "Result";
            displayResult(resultId, sparqljson);
            $("tr.odd").ready(function () {
                pageBtnsId = type + "PageBtns";
                displayInformation(data, page, containerId, pageBtnsId, "searchParentById");
                processHtmlResult("ontology");
            });
        });
    }
    function searchChildrenById(ontologyId, page) {
        currentChildrenPage = page;
        type = "children";
        containerId = type+"Container";
        displayHoldMessage(type + "Result");        
        swagger.apis.ontologies.getChildrenById({_format: ".sparql-json", id: ontologyId, _pageSize: pageSize, _page: page},
        {responseContentType: 'application/json'}, function (data) {
            sparqljson = data.data;
            resultId = type + "Result";
            displayResult(resultId, sparqljson);
            $("tr.odd").ready(function () {
                pageBtnsId = type + "PageBtns";
                displayInformation(data, page, containerId, pageBtnsId, "searchChildrenById");
                processHtmlResult("ontology");
            });
        });
    }    
    function searchQtlsIdAssociatedWithOntoId(ontologyId, page) {
        currentChildrenPage = page;
        type = "qtl";
        containerId = type+"Container";
        displayHoldMessage(type + "Result");        
        swagger.apis.qtl.getQtlsIdAssociatedWithOntoId({_format: ".sparql-json", ontoId: ontologyId, _pageSize: pageSize, _page: page},
        {responseContentType: 'application/json'}, function (data) {
            sparqljson = data.data;
            resultId = type + "Result";
            displayResult(resultId, sparqljson);
            $("tr.odd").ready(function () {
                pageBtnsId = type + "PageBtns";
                displayInformation(data, page, containerId, pageBtnsId, "searchQtlsIdAssociatedWithOntoId");
                processHtmlResult(type);
            });
        });
    }
    function searchProteinIdAssociatedWithOntoId(ontologyId, page) {
        currentChildrenPage = page;
        type = "protein";
        containerId = type+"Container";
        displayHoldMessage(type + "Result");        
        swagger.apis.protein.getProteinIdAssociatedWithOntoId({_format: ".sparql-json", ontoId: ontologyId, _pageSize: pageSize, _page: page},
        {responseContentType: 'application/json'}, function (data) {
            sparqljson = data.data;
            resultId = type + "Result";
            displayResult(resultId, sparqljson);
            $("tr.odd").ready(function () {
                pageBtnsId = type + "PageBtns";
                displayInformation(data, page, containerId, pageBtnsId, "searchProteinIdAssociatedWithOntoId");
                processHtmlResult(type);
            });
        });
    }
    
    function displayInformation(data, page, type, pageBtnsId, functionName) {
        nbResults = data.obj["results"]["bindings"].length;
        previousBtnId = "previousPage";
        nextBtnId = "nextPage";
        addNavButtons(nbResults, page, pageBtnsId, previousBtnId, nextBtnId);
        $("#" + type + " #" + previousBtnId).attr("onclick", functionName + '("' + ontologyId + '",' + (page - 1) + ')');
        $("#" + type + " #" + nextBtnId).attr("onclick", functionName + '("' + ontologyId + '",' + (page + 1) + ')');
    }

</script>