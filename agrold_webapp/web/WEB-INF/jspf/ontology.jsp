<%-- 
    Document   : gene
    Created on : Sep 7, 2015, 3:04:42 PM
    Author     : tagny
--%>

<div id="ontologyPage">
    <div id="header"></div>
    <br><div id="parentContainer"><b style="font-size:13pt">Parents</b>
        <span id="parentPageBtns"><a href="javascript:void(0)" id="parent"> + </a></span>
        <div id="parentResult"></div>            
    </div>
    <br><div id="childrenContainer"><b style="font-size:13pt">Children</b>
        <span id="childrenPageBtns"><a href="javascript:void(0)" id="children"> + </a></span>
        <div id="childrenResult"></div>            
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
?entity meaning: ?Description .  \
OPTIONAL{?entity rdfs:label ?Name .} \
BIND(REPLACE(str(?entity), \'^.*(#|/)\', \"\") AS ?localname)\
BIND(REPLACE(?localname, \"_\", \":\") as ?Id).\
}';
        query = sparqlEndpoint + "?query=" + encodeURIComponent(sparql) + "&format=application/json";
        $.get(query, function (json) {
            if(!dispalyHeader("ontology", json, "header")){
                //$("#ontologyPage").html('" <b>'+uri+'</b> " not found');
                //return;
            }
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
    }
    
    window.swagger = new SwaggerClient({
        url: url,
        success: function () {
            console.log("API definition well loaded");
        }
    });
    var currentParentPage = 0;
    var currentChildrenPage = 0;
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
    
    function displayInformation(data, page, type, pageBtnsId, functionName) {
        nbResults = data.obj["results"]["bindings"].length;
        previousBtnId = "previousPage";
        nextBtnId = "nextPage";
        addNavButtons(nbResults, page, pageBtnsId, previousBtnId, nextBtnId);
        $("#" + type + " #" + previousBtnId).attr("onclick", functionName + '("' + ontologyId + '",' + (page - 1) + ')');
        $("#" + type + " #" + nextBtnId).attr("onclick", functionName + '("' + ontologyId + '",' + (page + 1) + ')');
    }

</script>