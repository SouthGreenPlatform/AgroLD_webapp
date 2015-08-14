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
public class ProteinDAO {

    // return URIs and agrold_vocabulary:description of all genes in Agrold
    public static String getAllProteinsURI(String resultFormat) {
        String sparqlQuery = "prefix	agrold:<http://www.southgreen.fr/agrold/vocabulary/> \n"
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
                + "SELECT distinct ?protein ( CONCAT(?label, \": \", ?desc) AS ?description)\n"
                + "WHERE {\n"
                + "    ?protein agrold:description ?desc; \n"
                + "          rdfs:label ?label ;\n"
                + "          rdfs:subClassOf <http://purl.obolibrary.org/obo/SO_0000104>.\n" 
                + "}";
        System.out.println(sparqlQuery);

        String result = APILib.executeSparqlQuery(sparqlQuery, APILib.speURL, resultFormat);
        return result;
    }

    
    public static String getProteinIdAssociatedWithOntoId(String ontoId, int page, int pageSize, String resultFormat) {
        String sparqlQuery = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
                + "\n"
                + "SELECT DISTINCT ?proteinId\n"
                + "WHERE\n"
                + "{\n"
                + "  { \n"
                + "    SELECT ?ontoElt\n"
                + "    WHERE\n"
                + "    {\n"
                + "    	?ontoElt rdfs:subClassOf ?ontoEltClass.\n"
                + "  		FILTER REGEX(STR(?ontoElt), CONCAT(REPLACE(\"" + ontoId + "\", \":\", \"_\"), \"$\"))\n"
                + "    } limit 1\n"
                + "  }\n"
                + "  ?protein ?predicate ?ontoElt .\n"
                + "  ?protein rdfs:subClassOf <http://purl.obolibrary.org/obo/SO_0000104> .\n"
                + "  BIND(REPLACE(str(?protein), '^.*(#|/)', \"\") AS ?proteinId) .\n"
                + "}\n";
                
        if (page > 0 && pageSize > 0) {
            sparqlQuery += "ORDER BY ?protein\n"
                    + "LIMIT " + pageSize + "\n"
                    + "OFFSET " + page + "\n"
                    + " ";
        }
        System.out.println(sparqlQuery);

        String result = APILib.executeSparqlQuery(sparqlQuery, APILib.speURL, resultFormat);
        return result;
    }

    public static void main(String[] args) {
        //System.out.println(getProteinIdAssociatedWithOntoId("GO:0003824", 1, 5, APILib.TSV));
        System.out.println(getAllProteinsURI(APILib.TSV));
    }
}
