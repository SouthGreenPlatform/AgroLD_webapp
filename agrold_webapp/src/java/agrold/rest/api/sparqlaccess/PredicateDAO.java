/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.rest.api.sparqlaccess;

/**
 *
 * @author tagny
 */
public class PredicateDAO {

    /**
     * What predicates are in AgroLD?
     *
     * @return the list of URIs of predicates
     */
    public static String getPredicates(String graphLocalName, int page, int pageSize, String resultFormat) {
        String sparqlQuery = "BASE <http://www.southgreen.fr/agrold/>\n"
                + "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX graph:<"+graphLocalName+">\n"
                + "\n"
                + "SELECT distinct ?LocalName (?relation AS ?URI)\n"
                + "WHERE { \n"
                + " GRAPH graph: { \n"
                + "  ?subject ?relation ?object . \n"
                + "    BIND(REPLACE(str(?relation), '^.*(#|/)', \"\") AS ?LocalName)\n"
                + " } \n"
                + "} \n"
                + "ORDER BY ?relation";
        sparqlQuery = APILib.addLimitAndOffset(sparqlQuery, pageSize, page);
        System.out.println(sparqlQuery);
        return APILib.executeSparqlQuery(sparqlQuery, APILib.sparqlEndpointURL, resultFormat);
    }

    public static void main(String[] args) {
        System.out.println(getPredicates("gramene.genes", 0, 10, ".json"));
    }
}
