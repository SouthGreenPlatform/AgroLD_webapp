WEBAPPURL= "/" + (system_context ?? "aldp")

// still constant in :
// /Users/zadmin/agrold/git/AgroLD/agrold/src/main/webapp/bc_sparqleditor.jsp
// /src/main/webapp/relfinder/config/Config.xml
// /src/main/webapp/sparqleditor.jsp
//SPARQLENDPOINTURL="http://agrold.southgreen.fr/sparql"; 
//SPARQLENDPOINTURL=WEBAPPURL + "/api/sparql"; 
SPARQLENDPOINTURL= system_sparqlendpoint ?? "http://sparql.southgreen.fr"; 
// 
// still in HTML form in :
// WEB-INF/Account/General/AJAX/Admin/_USER_FULL_DATA_LOADER.jsp
// WEB-INF/Account/General/AJAX/History/QuickSearchList.jsp 
// /src/main/webapp/quicksearch.jsp
FACETEDURL="http://agrold.southgreen.fr/fct"; //TODO

//AGROLDAPIJSONURL=WEBAPPURL + "/config/agrold-api.json";
//AGROLDAPIJSONURL=WEBAPPURL + "/api/agrold-api-specification.json";
AGROLDAPIJSONURL= WEBAPPURL + "/api/webservices";


// Advanced search default format to query the web services
DEFAULTAPIFORMAT = ".jsonld";