<%-- 
    Document   : gene
    Created on : Sep 7, 2015, 3:04:42 PM
    Author     : tagny
--%>
<!--div id="header"></div-->
<script type="text/javascript">
    var gene = {
        currentProteinPage: 0,
        currentPathwayPage: 0,
        currentPublicationPage: 0,

        getDescription: function (uri) {
            console.log("getGeneDescription(" + uri + ")");
            var sparql = 'PREFIX agrold:<http://www.southgreen.fr/agrold/vocabulary/> \
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
            var query = sparqlEndpoint + "?query=" + encodeURIComponent(sparql) + "&format=application/json";
            var tthis = this;
            $.get(query, function (json) {
                DBG = json;
                WaitingModal(1);
                dispalyHeader("gene", json);
                if (typeof (json["results"]["bindings"][0]) !== 'undefined')
                    ModalContext.id = json["results"]["bindings"][0]["Id"]["value"];
                else
                    ModalContext.notFound();
                console.log(json);
                tthis.addEvents();
                console.log('Chargement Terminé');
                $('#result-modal #protein').click();
            });
        },
        addEvents: function () {
            $("#protein").attr("onclick", "invoke('searchProteinsEncodedByGene'," + this.currentProteinPage + ")");
            $("#pathway").attr("onclick", "invoke('searchPathwayInWhichParticipatesGene'," + this.currentPathwayPage + ")");
            $("#publication").attr("onclick", "invoke('searchPublications'," + this.currentPublicationPage + ")");
        },
        searchProteinsEncodedByGene: function (page) {
            console.log('searchProteinsEncodedByGene:' + page)
            this.currentProteinPage = page; //var containerId = "proteinContainer";var tthis = this;
            console.log(ModalContext.uri);
            var type = "protein";
            displayHoldMessage(type + "Result");
            var tthis = this;
            swagger.apis.protein.getProteinsEncodedByGene({_format: ".sparql-json", geneId: ModalContext.id, _pageSize: pageSize, _page: page}, {
                responseContentType: 'application/json'
            }, function (data) {
                sparqljson = data.data;
                var resultId = type + "Result";
                displayResult(resultId, data.data);
                $("tr.odd").ready(function () {
                    pageBtnsId = type + "PageBtns";
                    tthis.displayInformation(data, page, resultId, pageBtnsId, "searchProteinsEncodedByGene");
                    // processHtmlResult(resultId);
                    processHtmlResult(type);
                });
            });
        },
        searchPathwayInWhichParticipatesGene: function (page) {
            this.currentPathwayPage = page; //            var containerId = "pathwayContainer";
            var type = "pathway";
            displayHoldMessage(type + "Result");
            console.log('searchPathwayInWhichParticipatesGene:(' + ModalContext.id + ', ' + '');
            var tthis = this;
            swagger.apis.pathway.getPathwaysInWhichParticipatesGene({_format: ".sparql-json", geneId: ModalContext.id, _pageSize: pageSize, _page: page}, {responseContentType: 'application/json'},
                    function (data) {
                        sparqljson = data.data;
                        var resultId = type + "Result";
                        displayResult(resultId, sparqljson);
                        tables = $("table.resultsTable");
                        //var tableClass = type;
                        $(tables[tables.length - 1]).addClass(type); //console.log($("table.resultsTable"));            
                        $("tr.odd").ready(function () {
                            // pageBtnsId = "" + "PageBtns";
                            tthis.displayInformation(data, page, resultId, pageBtnsId, "searchPathwayInWhichParticipatesGene");
                            processHtmlResult(type);
                        });
                    });
        },
        searchPublications: function (geneId) {
            displayHoldMessage("publicationResult");
            // get PubMed Id from G-link web service
            swagger.apis.gene.getPublicationsOfGeneById({
                geneId: ModalContext.id
            }, {
                responseContentType: 'application/json'
            }, function (data) {
                var json = data.obj;
                displayPublications(json, "publicationResult");
            });
        },
        displayInformation: function (data, page, where, pageBtnsId, functionName) {
            nbResults = data.obj["results"]["bindings"].length;
            previousBtnId = "previousPage";
            nextBtnId = "nextPage";
            addNavButtons(nbResults, page, where, previousBtnId, nextBtnId, String(functionName));
        },
        html: '<nav id="genePage" class="nav nav-tabs flex-column flex-sm-row advs">\
                   <div id="proteinContainer">\
                       <span id="proteinPageBtns" class="pageNavBtns"><a class="flex-sm-fill text-sm-center nav-link active o-proteinResult" href="javascript:void(0)" id="protein"> Proteins </a></span>\
                   </div>\
                   <div id="pathwayContainer">\
                       <span id="pathwayPageBtns" class="pageNavBtns"><a class="flex-sm-fill text-sm-center nav-link o-pathwayResult" href="javascript:void(0)" id="pathway"> Pathways </a></span>\
                   </div>\
                   <div id="publicationContainer">\
                       <span id="publicationPageBtns" class="pageNavBtns"><a class="flex-sm-fill text-sm-center nav-link o-publicationResult" href="javascript:void(0)" id="publication"> Publications </a></span>\
                   </div>\
               </nav>\
               <div class="o-panel o-active" id="proteinResult"></div>\
               <div class="o-panel" id="pathwayResult"></div>\
               <div class="o-panel" id="publicationResult"></div>'
    };
</script>