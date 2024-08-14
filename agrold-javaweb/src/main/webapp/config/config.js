WEBAPPURL= "/agrold"

SPARQLENDPOINTURL= system_sparqlendpoint; 
// 
// still in HTML form in :
// WEB-INF/Account/General/AJAX/Admin/_USER_FULL_DATA_LOADER.jsp
// WEB-INF/Account/General/AJAX/History/QuickSearchList.jsp 
// /src/main/webapp/quicksearch.jsp
FACETEDURL = system_faceted_search_url;
FACETEDPAGE = new URL(
    `${(new URL(system_faceted_search_url)).pathname.split("/").map(_ => "..").join("/")}/fct/facet.vsp?cmd=text&sid=231`, 
    system_faceted_search_url
).href;

AGROLDAPIJSONURL= WEBAPPURL + "/api/openapi.json";


// Advanced search default format to query the web services
DEFAULTAPIFORMAT = ".jsonld";