<%-- 
    Document   : gene
    Created on : Sep 7, 2015, 3:04:42 PM
    Author     : tagny
--%>

<div id="pathwayPage">
    <div id="header"></div>
    <br><div id="geneContainer"><b style="font-size:13pt">Participating genes</b>
        <span id="genePageBtns"><a href="javascript:void(0)" id="gene"> + </a></span>
        <div id="geneResult"></div>            
    </div>    
</div>
<script type="text/javascript">
    var pathwayUri = <% out.println("'" + request.getParameter("uri") + "'");%>;    
    
    function getPathwayDescription(uri) {
        sparql = 'PREFIX agrold:<http://www.southgreen.fr/agrold/vocabulary/> \
PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> \
PREFIX meaning:<http://purl.obolibrary.org/obo/IAO_0000115> \
SELECT distinct  ?Id ?Name (?entity AS ?Uri) \
WHERE { \
  VALUES ?entity{ \
    <' + uri + '> \
  } \
OPTIONAL{?entity rdfs:label ?Name .} \
BIND(REPLACE(str(?entity), \'^.*(#|/)\', \"\") AS ?Id)\
}';
        console.log(sparql);
        query = sparqlEndpoint + "?query=" + encodeURIComponent(sparql) + "&format=application/json";        
        $.get(query, function (json) {
            if(!dispalyHeader("pathway", json, "header")){
                //$("#ontologyPage").html('" <b>'+uri+'</b> " not found');
                //return;
            }
            pathwayId = json["results"]["bindings"][0]["Id"]["value"];
            //console.log(geneId);        
            addEvents(pathwayId);
        });
    }
    $(document).ready(function () {
        getPathwayDescription(pathwayUri);
    });
    function  addEvents(ontologyId) {        
        $("#gene").attr("onclick", 'searchParticipatingGenes(\'' + pathwayId + '\', ' + currentGenePage + ')');        
    }
    
    window.swagger = new SwaggerClient({
        url: url,
        success: function () {
            console.log("API definition well loaded");
        }
    });
    var currentGenePage = 0;
    function searchParticipatingGenes(pathwayId, page) {
        currentGenePage = page;
        type = "gene";
        containerId = type+"Container";
        displayHoldMessage(type + "Result");        
        swagger.apis.gene.getGenesByPathways({_format: ".sparql-json", pathwayId: pathwayId, _pageSize: pageSize, _page: page},
        {responseContentType: 'application/json'}, function (data) {
            sparqljson = data.data;
            resultId = type + "Result";
            displayResult(resultId, sparqljson);
            $("tr.odd").ready(function () {
                pageBtnsId = type + "PageBtns";
                displayInformation(data, page, containerId, pageBtnsId, "searchParticipatingGenes");
                processHtmlResult(type);
            });
        });
    }
    
    function displayInformation(data, page, type, pageBtnsId, functionName) {
        nbResults = data.obj["results"]["bindings"].length;
        previousBtnId = "previousPage";
        nextBtnId = "nextPage";
        addNavButtons(nbResults, page, pageBtnsId, previousBtnId, nextBtnId);
        $("#" + type + " #" + previousBtnId).attr("onclick", functionName + '("' + pathwayId + '",' + (page - 1) + ')');
        $("#" + type + " #" + nextBtnId).attr("onclick", functionName + '("' + pathwayId + '",' + (page + 1) + ')');
    }

</script>