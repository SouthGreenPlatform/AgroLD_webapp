var defaultID = ((readCookie('useModel')!= null && readCookie('useModel')!='') ? (readCookie('useModel')) : 11111);
//var defaultLoc = 'data/CancerChemoPrevention.owl';
//var defaultLoc = 'reveald/data/CancerChemopreventionOntology_light_hypo.owl';
var defaultLoc = 'reveald/data/agrold_test2.owl';
//var defaultLoc = 'data/obi.owl';
console.log(defaultLoc);
var resourceURL = ''; // For ChemSpider API Calls - this is working on the public prototype, but requires a Proxy Server to make request in pure JavaScript.
//var uriStr = "http://srvgal78.deri.ie:8080/sparql"; /// put in your SPARQL Endpoint here
//var uriStr = "http://rdf.ncbi.nlm.nih.gov/pubchem/endpoint/"; /// put in your SPARQL Endpoint here
//var uriStr = "http://volvestre.cirad.fr:3128/sparql"; /// put in your SPARQL Endpoint here
var uriStr = "http://volvestre.cirad.fr:8890/sparql"; /// put in your SPARQL Endpoint here
//var uriStr = "http://localhost:8890/sparql"; /// put in your SPARQL Endpoint here