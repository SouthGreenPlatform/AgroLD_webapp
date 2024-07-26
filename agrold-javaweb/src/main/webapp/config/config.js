WEBAPPURL= "/agrold"

SPARQLENDPOINTURL= system_sparqlendpoint ?? "http://sparql.southgreen.fr"; 
// 
// still in HTML form in :
// WEB-INF/Account/General/AJAX/Admin/_USER_FULL_DATA_LOADER.jsp
// WEB-INF/Account/General/AJAX/History/QuickSearchList.jsp 
// /src/main/webapp/quicksearch.jsp
FACETEDURL="http://agrold.southgreen.fr/fct"; //TODO

AGROLDAPIJSONURL= WEBAPPURL + "/api/openapi.json";


// Advanced search default format to query the web services
DEFAULTAPIFORMAT = ".jsonld";