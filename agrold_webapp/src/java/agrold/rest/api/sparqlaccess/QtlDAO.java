/*
 * Web services on QTLs
 */
package agrold.rest.api.sparqlaccess;

/**
 *
 * @author tagny
 */
public class QtlDAO {

    // return URIs and agrold_vocabulary:description of all genes in Agrold
    public static String getQtls(int page, int pageSize, String resultFormat) {
        String sparqlQuery = "prefix	agrold:<http://www.southgreen.fr/agrold/vocabulary/> \n"
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
                + "SELECT distinct ?qtl ?qtlId ?qtlName ?qtlDescription\n"
                + "WHERE {\n"
                + "    ?qtl rdfs:label ?qtlName;\n"
                + "          agrold:description ?qtlDescription;          \n"
                + "          rdfs:subClassOf <http://purl.obolibrary.org/obo/SO_0000771>.\n"
                + "    BIND(REPLACE(str(?qtl), '^.*(#|/)', \"\") AS ?qtlId) .\n"
                + "}";
        sparqlQuery = APILib.addLimitAndOffset(sparqlQuery, pageSize, page);
        System.out.println(sparqlQuery);
        String result = APILib.executeSparqlQuery(sparqlQuery, APILib.sparqlEndpointURL, resultFormat);
        return result;
    }

    public static String getQtlIdAssociatedWithOntoId(String ontoId, int page, int pageSize, String resultFormat) {
        String sparqlQuery = "PREFIX agrold:<http://www.southgreen.fr/agrold/>\n"
                + "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
                + "\n"
                + "SELECT DISTINCT ?qtlId\n"
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
                + "  GRAPH agrold:qtl.annotations{\n"
                + "    ?qtl ?predicate ?ontoElt .\n"
                + "    ?qtl rdfs:subClassOf <http://purl.obolibrary.org/obo/SO_0000771> .\n"
                + "    BIND(REPLACE(str(?qtl), '^.*(#|/)', \"\") AS ?qtlId) .\n"
                + "  }\n"
                + "}\n";

        sparqlQuery = APILib.addLimitAndOffset(sparqlQuery, pageSize, page);
        System.out.println(sparqlQuery);

        String result = APILib.executeSparqlQuery(sparqlQuery, APILib.sparqlEndpointURL, resultFormat);
        return result;
    }    

    public static void main(String[] args) {
        //System.out.println(getQtlIdAssociatedWithOntoId("EO:0007403", 1, 5, APILib.TSV));        

    }
}
