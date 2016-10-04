<%-- 
    Document   : gene
    Created on : Sep 7, 2015, 3:04:42 PM
    Author     : tagny
--%>

<div id="genePage">
    <div id="header"></div>
    <!--br><div id="chromosomeContainer"><b style="font-size:13pt">is located on</b>
        <span id="chromosomePageBtns"><a href="javascript:void(0)" id="chromosome"> + </a></span>
        <div id="chromosomeResult"></div>            
    </div-->

    <br><div id="proteinContainer"><b style="font-size:13pt">encodes proteins</b>
        <span id="proteinPageBtns" class="pageNavBtns"><a href="javascript:void(0)" id="proteins"> + </a></span>
        <div id="proteinResult"></div>            
    </div>

    <br><div id="pathwayContainer"><b style="font-size:13pt">Pathways</b>
        <span id="pathwayPageBtns" class="pageNavBtns"><a href="javascript:void(0)" id="pathways"> + </a></span>
        <div id="pathwayResult"></div>
    </div>
    <!--br><div id="publicationContainer"><b style="font-size:13pt">Publication</b>
        <span id="publicationPageBtns" class="pageNavBtns"><a href="javascript:void(0)" id="publication"> + </a></span>
        <div id="publicationResult"></div>            
    </div-->
</div>
<script type="text/javascript">
    var geneUri = <% out.println("'" + request.getParameter("uri") + "'");%>;    
    
    function getGeneDescription(uri) {
        //err.println("uri="+ uri);
        sparql = 'PREFIX agrold:<http://www.southgreen.fr/agrold/vocabulary/> \
PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> \
SELECT distinct  ?Id ?Name ?Description (?entity AS ?Uri) \
WHERE { \
  VALUES ?entity{ \
    <' + uri + '> \
  } \
{?entity agrold:description ?Description}UNION{BIND("" as ?Description)}   \
OPTIONAL{?entity rdfs:label ?Name .} \
BIND(REPLACE(str(?entity), \'^.*(#|/)\', "") AS ?Id) \
}';

        query = sparqlEndpoint + "?query=" + encodeURIComponent(sparql) + "&format=application/json";
        $.get(query, function (json) {
            dispalyHeader("gene", json, "header");
            geneId = json["results"]["bindings"][0]["Id"]["value"];
            //console.log(geneId);        
            addEvents(geneId);
        });
    }
    $(document).ready(function () {
        getGeneDescription(geneUri);
    });
    function  addEvents(geneId) {
        //$("#chromosome").attr("onclick", '');
        $("#proteins").attr("onclick", 'searchProteinsEncodedByGene(\'' + geneId + '\', ' + currentProteinPage + ')');
        $("#pathways").attr("onclick", 'searchPathwayInWhichParticipatesGene(\'' + geneId + '\',' + currentPathwayPage + ')');
        $("#publication").attr("onclick", 'searchPublications(\'' + geneId + '\')');
    }
    ;
    //alert(geneUri)
    console.log(geneUri);
    window.swagger = new SwaggerClient({
        url: url,
        success: function () {
            console.log("API definition well loaded");
        }
    });
    var currentProteinPage = 0;
    var currentPathwayPage = 0;
    function searchProteinsEncodedByGene(geneId, page) {
        currentProteinPage = page;
        type = "protein";
        containerId = "proteinContainer";
        displayHoldMessage(type + "Result");
        console.log(geneId);
        swagger.apis.protein.getProteinsEncodedByGene({_format: ".sparql-json", geneId: geneId, _pageSize: pageSize, _page: currentProteinPage},
        {responseContentType: 'application/json'}, function (data) {
            sparqljson = data.data;
            resultId = type + "Result";
            displayResult(resultId, sparqljson);            
            $("tr.odd").ready(function () {
                pageBtnsId = type + "PageBtns";
                displayInformation(data, page, containerId, pageBtnsId, "searchProteinsEncodedByGene");
                processHtmlResult(type);
            });
        });
    }
    function searchPathwayInWhichParticipatesGene(geneId, page) {
        currentPathwayPage = page;
        type = "pathway";
        containerId = "pathwayContainer";
        displayHoldMessage(type + "Result");
        console.log(geneId);
        swagger.apis.pathway.getPathwaysInWhichParticipatesGene({_format: ".sparql-json", geneId: geneId, _pageSize: pageSize, _page: currentPathwayPage},
        {responseContentType: 'application/json'}, function (data) {
            sparqljson = data.data;
            resultId = type + "Result";
            displayResult(resultId, sparqljson);
            tables = $("table.resultsTable");
            var tableClass = type;
            $(tables[tables.length - 1]).addClass(tableClass); //console.log($("table.resultsTable"));            
            $("tr.odd").ready(function () {
                pageBtnsId = type + "PageBtns";
                displayInformation(data, page, containerId, pageBtnsId, "searchPathwayInWhichParticipatesGene");
                processHtmlResult(tableClass, type);
            });
        });
    }
    function searchPublications(geneId) {
        displayHoldMessage("publicationResult");
        // get PubMed Id from G-link web service
        swagger.apis.gene.getPublicationsOfGeneById({geneId: geneId},
        {responseContentType: 'application/json'}, function (data) {            
            //$("#publicationPageBtns").remove();
            json = data.obj;
            displayPublications(json, "publicationResult");            
        });
    }
    function displayInformation(data, page, containerId, pageBtnsId, functionName) {
        nbResults = data.obj["results"]["bindings"].length;
        previousBtnId = "previousPage";
        nextBtnId = "nextPage";
        addNavButtons(nbResults, page, pageBtnsId, previousBtnId, nextBtnId);
        $("#" + containerId + " #" + previousBtnId).attr("onclick", functionName + '("' + geneId + '",' + (page - 1) + ')');
        $("#" + containerId + " #" + nextBtnId).attr("onclick", functionName + '("' + geneId + '",' + (page + 1) + ')');
    }

</script>