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
public class ProteinDAO {

    // return URIs and agrold_vocabulary:description of all genes in Agrold
    public static String getProteins(int page, int pageSize, String resultFormat) {
        String sparqlQuery = "prefix	agrold:<http://www.southgreen.fr/agrold/vocabulary/> \n"
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
                + "SELECT distinct ?protein ?proteinId ?proteinName ?proteinDescription\n"
                + "WHERE {\n"
                + "    ?protein rdfs:label ?proteinName;\n"
                + "          agrold:description ?proteinDescription;          \n"
                + "          rdfs:subClassOf <http://purl.obolibrary.org/obo/SO_0000104>.\n"
                + "    BIND(REPLACE(str(?protein), '^.*(#|/)', \"\") AS ?proteinId) .\n"
                + "}";
        sparqlQuery = APILib.addLimitAndOffset(sparqlQuery, pageSize, page);
        System.out.println(sparqlQuery);

        String result = APILib.executeSparqlQuery(sparqlQuery, APILib.sparqlEndpointURL, resultFormat);
        return result;
    }

    public static String getProteinsIdAssociatedWithOntoId(String ontoId, int page, int pageSize, String resultFormat) {
        String sparqlQuery = "PREFIX agrold:<http://www.southgreen.fr/agrold/>\n"
                + "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
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
                + "  GRAPH agrold:protein.annotations{"
                + "    ?protein ?predicate ?ontoElt .\n"
                + "    ?protein rdfs:subClassOf <http://purl.obolibrary.org/obo/SO_0000104> .\n"
                + "    BIND(REPLACE(str(?protein), '^.*(#|/)', \"\") AS ?proteinId) .\n"
                + "  }"
                + "}\n ORDER BY ?proteinId";

        /*if (page > 0 && pageSize > 0) {
         sparqlQuery += "ORDER BY ?protein\n"
         + "LIMIT " + pageSize + "\n"
         + "OFFSET " + page + "\n"
         + " ";
         }*/
        sparqlQuery = APILib.addLimitAndOffset(sparqlQuery, pageSize, page);
        System.out.println(sparqlQuery);

        String result = APILib.executeSparqlQuery(sparqlQuery, APILib.sparqlEndpointURL, resultFormat);
        return result;
    }
    
    public static String getProteinsAssociatedWithQTL(String qtlId, int page, int pageSize, String resultFormat) {
        String sparqlQuery = "BASE <http://www.southgreen.fr/agrold/>\n"
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX vocab:<vocabulary/>\n"
                + "PREFIX graph1:<protein.annotations>\n"
                + "PREFIX graph2:<qtl.annotations>\n"
                + "PREFIX qtl: <http://www.identifiers.org/gramene.qtl/"+qtlId+"> # DTHD \n"
                + "\n"
                + "SELECT distinct ?protein ?proteinId ?proteinName \n"
                + "WHERE {\n"
                + " GRAPH graph1: {\n"
                + "  ?protein vocab:has_trait ?to.\n"
                + "  ?protein rdfs:label ?proteinName.\n" 
                + "    BIND(REPLACE(str(?protein), '^.*(#|/)', \"\") AS ?proteinId) .\n"
                + " }\n"
                + " GRAPH graph2: {\n"
                + "  qtl: vocab:has_trait ?to.\n"
                + " }\n"
                + "}\n"
                + "ORDER BY ?name";

        sparqlQuery = APILib.addLimitAndOffset(sparqlQuery, pageSize, page);
        System.out.println(sparqlQuery);

        String result = APILib.executeSparqlQuery(sparqlQuery, APILib.sparqlEndpointURL, resultFormat);
        return result;
    }
    /*
     public static String getOntoIdAssociatedWithProteinId(String proteinId, int page, int pageSize, String resultFormat) {
     String sparqlQuery = "PREFIX agrold:<http://www.southgreen.fr/agrold/>\n"
     + "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
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
     + "  GRAPH agrold:protein.annotations{"
     + "    ?protein ?predicate ?ontoElt .\n"
     + "    ?protein rdfs:subClassOf <http://purl.obolibrary.org/obo/SO_0000104> .\n"
     + "    BIND(REPLACE(str(?protein), '^.*(#|/)', \"\") AS ?proteinId) .\n"
     + "  }"
     + "}\n";
                
     if (page > 0 && pageSize > 0) {
     sparqlQuery += "ORDER BY ?protein\n"
     + "LIMIT " + pageSize + "\n"
     + "OFFSET " + page + "\n"
     + " ";
     }
     System.out.println(sparqlQuery);

     String result = APILib.executeSparqlQuery(sparqlQuery, APILib.sparqlEndpointURL, resultFormat);
     return result;
     }
     */

    public static void main(String[] args) {
        //System.out.println(getProteinsIdAssociatedWithOntoId("GO:0003824", 1, 5, APILib.TSV));
        //System.out.println(getProteins(0, 2, ".tsv"));
        System.out.println(getProteinsAssociatedWithQTL("AQAT004", 0, -1, ".html"));
    }
}
