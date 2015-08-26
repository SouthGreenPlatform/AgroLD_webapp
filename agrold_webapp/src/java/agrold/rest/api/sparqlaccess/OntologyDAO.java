package agrold.rest.api.sparqlaccess;

/**
 * Class where are defined the SPARQL queries to retrieve informations on the
 * ontologies of AgroLD for the API Web service
 *
 * @author tagny
 *
 */
public class OntologyDAO {

    /**
     * Returns the ID of an ontological element corresponding to its term (name)
     *
     * @param ontoTerm the exact matching name of the element as wrote in the
     * AgroLD KB (e.g. composition)
     * @param resultFormat the format in which the result will be returned (e.g.
     * "text/tab-separated-values")
     * @return the id of the ontological element
     * @see getOntoTermById
     */
    public static String getIdByOntoTerm(String ontoTerm, int page, int pageSize, String resultFormat) {
        System.out.println("format: "+resultFormat);
        String sparqlQuery = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "\n"
                + "SELECT ?id\n"
                + "WHERE { \n"
                + " {\n"
                + "  SELECT ?subject\n"
                + "  WHERE\n"
                + "  {\n"
                + "   ?subject rdfs:label \"" + ontoTerm + "\"^^xsd:string .\n"
                + "   ?subject a owl:Class ."
                + "  }    \n"
                + " }\n"
                + " BIND(REPLACE(str(?subject), '^.*(#|/)', \"\") AS ?localname)\n"
                + " BIND(REPLACE(?localname, \"_\", \":\") as ?id).\n"
                + "} ";
        sparqlQuery = APILib.addLimitAndOffset(sparqlQuery, pageSize, page);
        System.out.println(sparqlQuery);

        String id = APILib.executeSparqlQuery(sparqlQuery, APILib.sparqlEndpointURL, resultFormat);

        return id;
    }

    /**
     * Returns the name of an ontological element corresponding to its given ID
     *
     * @param id the ID of the ontological element
     * @param resultFormat the format in which the result will be returned (e.g.
     * "text/tab-separated-values")
     * @return the name of the ontological element
     * @see getIdByOntoTerm
     */
    public static String getOntoTermById(String id, int page, int pageSize, String resultFormat) {
        String sparqlQuery = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "SELECT ?OntoTerm\n"
                + "WHERE { \n"
                + " {\n"
                + "  SELECT ?subject\n"
                + "  WHERE\n"
                + "  {\n"
                + "   ?subject rdfs:subClassOf ?o.\n"
                + "   FILTER REGEX(STR(?subject), CONCAT(REPLACE(\""+id+"\", \":\", \"_\"), \"$\"))\n"
                + "  }\n"
                + " }\n"
                + " ?subject rdfs:label ?OntoTerm .\n"
                + " ?subject a owl:Class .\n"
                + "}  ";
        sparqlQuery = APILib.addLimitAndOffset(sparqlQuery, pageSize, page);
        System.out.println(sparqlQuery);

        String name = APILib.executeSparqlQuery(sparqlQuery, APILib.sparqlEndpointURL, resultFormat);

        return name;
    }

    public static String getAncestorById(String id, int level, int page, int pageSize, String resultFormat) {
        int i = 1;
        String pattern = "";
        for (; i < level; i++) {
            pattern += "    ?ancestor" + i + " rdfs:subClassOf ?ancestor" + (i + 1) + ".\n";
        }
        String sparqlQuery = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "SELECT ?ancestorId\n"
                + " WHERE \n"
                + " {     \n"
                + "  { \n"
                + "    SELECT ?ancestor1 ?subject\n"
                + "    WHERE\n"
                + "    {\n"
                + "    	?subject rdfs:subClassOf ?ancestor1.          	\n"
                + "  		FILTER REGEX(STR(?subject), CONCAT(REPLACE(\"" + id + "\", \":\", \"_\"), \"$\"))\n"
                + "    }   \n"
                + "  }\n"
                + pattern
                + "  ?ancestor" + i + " a owl:Class .\n"
                + "   BIND(REPLACE(str(?ancestor" + i + "), '^.*(#|/)', \"\") AS ?ancestorLocalname)\n"
                + "   BIND(REPLACE(?ancestorLocalname, \"_\", \":\") as ?ancestorId)\n"
                + "}";
        sparqlQuery = APILib.addLimitAndOffset(sparqlQuery, pageSize, page);
        System.out.println(sparqlQuery);
        String ancestorId = APILib.executeSparqlQuery(sparqlQuery, APILib.sparqlEndpointURL, resultFormat);
        return ancestorId;
    }

    public static String getDescendantsById(String id, int level, int page, int pageSize, String resultFormat) {
        int i = 1;
        String pattern = "";
        for (; i < level; i++) {
            pattern += "    ?descendent" + (i + 1) + " rdfs:subClassOf ?descendent" + i + ".\n";
        }
        String sparqlQuery = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
                + "\n"
                + "SELECT ?descendentId\n"
                + " WHERE \n"
                + " {     \n"
                + "  { \n"
                + "    SELECT ?descendent1 ?subject\n"
                + "    WHERE\n"
                + "    {\n"
                + "    	?descendent1 rdfs:subClassOf ?subject. \n"
                + "  		FILTER REGEX(STR(?subject), CONCAT(REPLACE(\"" + id + "\", \":\", \"_\"), \"$\"))\n"
                + "    }   \n"
                + "  }\n"
                + pattern
                + "  ?descendent" + i + " a owl:Class .\n"
                + "   BIND(REPLACE(str(?descendent" + i + "), '^.*(#|/)', \"\") AS ?descendentLocalname)\n"
                + "   BIND(REPLACE(?descendentLocalname, \"_\", \":\") as ?descendentId)\n"
                + "}";
        sparqlQuery = APILib.addLimitAndOffset(sparqlQuery, pageSize, page);
        System.out.println(sparqlQuery);
        String descendentId = APILib.executeSparqlQuery(sparqlQuery, APILib.sparqlEndpointURL, resultFormat);
        return descendentId;
    }

    public static void main(String[] args) {
        //System.out.println(getIdByOntoTerm("homoaconitate hydratase activity", "text/tab-separated-values"));
        //System.out.println(extractIDfromURI("http://purl.obolibrary.org/obo/BFO_0000051"));
        //System.out.println(getAncestorById("GO:0004409", 3, "text/tab-separated-values"));
        //System.out.println(getDescendantsById("GO:0003824", 2, "text/tab-separated-values"));
        //System.out.println(getOntoTermById("GO:0003824", "text/tab-separated-values"));
    }
}
