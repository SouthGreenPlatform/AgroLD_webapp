/*
 * Web services on QTLs
 */
package agrold.rest.api.sparqlaccess;

/**
 *
 * @author tagny
 */
public class QtlDAO {

    public static String QTL_TYPE_URI = "http://purl.obolibrary.org/obo/SO_0000771";

    // return URIs and agrold_vocabulary:description of all genes in Agrold
    public static String getQtls(int page, int pageSize, String resultFormat) {
        String sparqlQuery = "prefix	agrold:<http://www.southgreen.fr/agrold/vocabulary/> \n"
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
                + "SELECT distinct ?qtlId ?qtlName ?qtlDescription (?qtl AS ?URI)\n"
                + "WHERE {\n"
                + "    ?qtl rdfs:label ?qtlName;\n"
                + "          agrold:description ?qtlDescription;          \n"
                + "          rdfs:subClassOf <" + QTL_TYPE_URI + ">.\n"
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
                + "SELECT DISTINCT ?qtlId (?qtl AS ?URI)\n"
                + "WHERE\n"
                + "{\n"
                + "  { \n"
                + "    SELECT ?ontoElt\n"
                + "    FROM <http://www.southgreen.fr/agrold/so>\n"
                + "    FROM <http://www.southgreen.fr/agrold/go>\n"
                + "    FROM <http://www.southgreen.fr/agrold/eco>\n"
                + "    FROM <http://www.southgreen.fr/agrold/eo>	\n"
                + "    FROM <http://www.southgreen.fr/agrold/pato>\n"
                + "    FROM <http://www.southgreen.fr/agrold/po>\n"
                + "    FROM <http://www.southgreen.fr/agrold/to>"
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
                + "}";

        sparqlQuery = APILib.addLimitAndOffset(sparqlQuery, pageSize, page);
        System.out.println(sparqlQuery);

        String result = APILib.executeSparqlQuery(sparqlQuery, APILib.sparqlEndpointURL, resultFormat);
        return result;
    }

    public static String getQtlsAssociatedWithProteinId(String proteinId, int page, int pageSize, String resultFormat) {
        String sparqlQuery = "BASE <http://www.southgreen.fr/agrold/>\n"
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX vocab:<vocabulary/>\n"
                + "PREFIX graph1:<protein.annotations>\n"
                + "PREFIX graph2:<qtl.annotations>\n"
                + "PREFIX protein: <http://purl.uniprot.org/uniprot/" + proteinId + ">\n"
                + "\n"
                + "SELECT distinct ?Id ?Name (?qtl AS ?URI) \n"
                + "WHERE {\n"
                + " GRAPH graph1: {\n"
                + "  protein: vocab:has_trait ?to.    \n"
                + " }\n"
                + " GRAPH graph2: {\n"
                + "  ?qtl vocab:has_trait ?to.\n"
                + "  ?qtl rdfs:label ?Name.\n"
                + "  BIND(REPLACE(str(?qtl), '^.*(#|/)', \"\") AS ?Id) .\n"
                + " }\n"
                + "}\n"
                + "ORDER BY ?Name";

        sparqlQuery = APILib.addLimitAndOffset(sparqlQuery, pageSize, page);
        System.out.println(sparqlQuery);

        String result = APILib.executeSparqlQuery(sparqlQuery, APILib.sparqlEndpointURL, resultFormat);
        return result;
    }

    public static void main(String[] args) {
        //System.out.println(getQtlsAssociatedWithProteinId("Q9LL45", 0, 5, APILib.TSV));
        //System.out.println(getQtlsByKeyWord("root", 0, -1, APILib.TSV));
    }
}
