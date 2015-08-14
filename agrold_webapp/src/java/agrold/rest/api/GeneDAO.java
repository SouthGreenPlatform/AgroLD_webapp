/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.rest.api;

/**
 *
 * @author tagny
 */
public class GeneDAO {

    // return URIs and agrold_vocabulary:description of all genes in Agrold
    public static String getAllGenesURI(String resultFormat) {
        //return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<genes> Hello Jersey" + "</genes>";
        String genes = "";

        String sparqlQuery = "prefix	agrold_vocabulary:<http://www.southgreen.fr/agrold/vocabulary/> \n"
                + "prefix	rdf:<http://www.w3.org/2000/01/rdf-schema#> \n"
                + "SELECT distinct ?gene ( CONCAT(?label, \": \", ?desc) AS ?description)\n"
                + "WHERE {\n"
                + "  GRAPH ?g {\n"
                + "    ?gene a <http://www.southgreen.fr/agrold/vocabulary/Gene>.\n"
                + "    ?gene agrold_vocabulary:description ?desc ;\n"
                + "          rdf:label ?label .\n"
                + "  }\n"
                + "FILTER(REGEX(?g, \"http://www.southgreen.fr/agrold/*\"))\n"
                + "}";
        System.out.println(sparqlQuery);

        genes = APILib.executeSparqlQuery(sparqlQuery, APILib.speURL, resultFormat);
        return genes;
    }

    public static void main(String[] args) {
        // URL and charset
        //String speURL = "http://localhost:8890/sparql";
        String speURL = "http://volvestre.cirad.fr:8890/sparql";
        String sparqlQuery = "SELECT DISTINCT ?p\n"
                + "WHERE {\n"
                + "  GRAPH ?g {\n"
                + "    ?s ?p ?o\n"
                + "  }\n"
                + "FILTER(REGEX(?p, \"http://www.southgreen.fr/agrold/*\"))\n"
                + "}";
        sparqlQuery = "SELECT ?property ?value\n"
                + "WHERE {\n"
                + "  GRAPH ?g {\n"
                + "    <http://identifiers.org/ensembl.plant/TRIUR3_00035> ?property ?value\n"
                + "  }\n"
                + "FILTER(REGEX(?property, \"http://www.southgreen.fr/agrold/*\"))\n"
                + "}";
        String resultFormat = APILib.JSON;

        //String result = APILib.executeSparqlQuery(sparqlQuery, speURL, resultFormat);
        //System.out.println(result);
        System.out.println(getAllGenesURI(resultFormat));
        //System.out.println(APILib.getResourceDescription("http://identifiers.org/ensembl.plant/AT3G14450", resultFormat));
    }
}
