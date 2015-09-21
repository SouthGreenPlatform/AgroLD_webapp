/*
 * API services on graphs
 */
package agrold.rest.api.sparqlaccess;

/**
 * API services on graphs
 *
 * @author tagny
 */
public class GraphDAO {
    // describe a resource
    public static String getResourceDescription(String resourceURI, int page, int pageSize, String resultFormat) {
        String resource;

        String sparqlQuery = "prefix	agrold_vocabulary:<http://www.southgreen.fr/agrold/vocabulary/> \n"
                + "SELECT distinct ?property ?object\n"
                + "WHERE {\n"
                + "  GRAPH ?g {\n"
                + "    <" + resourceURI + "> ?property ?object \n"
                + "  }\n"
                + "FILTER(REGEX(?g, \"http://www.southgreen.fr/agrold/*\"))\n"
                + "}"
                + "ORDER BY ?property";
        sparqlQuery = APILib.addLimitAndOffset(sparqlQuery, pageSize, page);
        System.out.println(sparqlQuery);

        resource = APILib.executeSparqlQuery(sparqlQuery, APILib.sparqlEndpointURL, resultFormat);
        return resource;

    }

    public static String listGraph( int page, int pageSize,String resultFormat) {
        String graphs;

        String sparqlQuery = "SELECT distinct ?graph\n"
                + "WHERE {\n"
                + " GRAPH ?graph {\n"
                + "   ?subject ?predicate ?object.\n"
                + " }\n"
                + " filter(REGEX(?graph, \"http://www.southgreen.fr/agrold/*\"))\n"
                + "}"
                + "ORDER BY str(?graph)";
        //+ "GROUP BY ?property";
        sparqlQuery = APILib.addLimitAndOffset(sparqlQuery, pageSize, page);
        System.out.println(sparqlQuery);

        graphs = APILib.executeSparqlQuery(sparqlQuery, APILib.sparqlEndpointURL, resultFormat);
        return graphs;
    }
    
     public static void main(String[] args) {
         System.out.println(listGraph(0, 0, APILib.JSON));
     }
}
