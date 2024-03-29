
//Fuses all parameters into a url
function buildUrl(...url) {
    return url.reduce((acc, val) => {
        if (acc.endsWith('/') && val.startsWith('/'))
            return acc + val.substring(1);
        else if (acc.endsWith('/') || val.startsWith('/'))
            return acc + val;
        else return acc + '/' + val;
    })
}

//WEBAPPURL="http://agrold.southgreen.fr/agrold";
CONTEXT= system_context ?? "aldp";
WEBAPPURL= buildUrl(( system_baseurl ?? "http://127.0.0.1:8080/" ),  CONTEXT);

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
FACETEDURL="http://agrold.southgreen.fr/fct"; 

//AGROLDAPIJSONURL=WEBAPPURL + "/config/agrold-api.json";
//AGROLDAPIJSONURL=WEBAPPURL + "/api/agrold-api-specification.json";
AGROLDAPIJSONURL= buildUrl(WEBAPPURL, "api/webservices");


// Advanced search default format to query the web services
DEFAULTAPIFORMAT = ".jsonld";