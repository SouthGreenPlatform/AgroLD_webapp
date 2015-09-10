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
    static public String PROTEIN_TYPE_URI = "http://purl.obolibrary.org/obo/SO_0000104";

    // return URIs and agrold_vocabulary:description of all genes in Agrold
    public static String getProteins(int page, int pageSize, String resultFormat) {
        String sparqlQuery = "prefix	agrold:<http://www.southgreen.fr/agrold/vocabulary/> \n"
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
                + "SELECT distinct ?protein ?proteinId ?proteinName ?proteinDescription\n"
                + "WHERE {\n"
                + "    ?protein rdfs:label ?proteinName;\n"
                + "          agrold:description ?proteinDescription;          \n"
                + "          rdfs:subClassOf <"+PROTEIN_TYPE_URI+">.\n"
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
                + "PREFIX qtl: <http://www.identifiers.org/gramene.qtl/" + qtlId + "> # DTHD \n"
                + "\n"
                + "SELECT distinct ?Id ?Name (?protein AS ?IRI) \n"
                + "WHERE {\n"
                + " GRAPH graph1: {\n"
                + "  ?protein vocab:has_trait ?to.\n"
                + "  ?protein rdfs:label ?Name.\n"
                + "    BIND(REPLACE(str(?protein), '^.*(#|/)', \"\") AS ?Id) .\n"
                + " }\n"
                + " GRAPH graph2: {\n"
                + "  qtl: vocab:has_trait ?to.\n"
                + " }\n"
                + "}\n"
                + "ORDER BY ?Name";

        sparqlQuery = APILib.addLimitAndOffset(sparqlQuery, pageSize, page);
        System.out.println(sparqlQuery);

        String result = APILib.executeSparqlQuery(sparqlQuery, APILib.sparqlEndpointURL, resultFormat);
        return result;
    }

    public static String getProteinsEncodedByGene(String geneId, int page, int pageSize, String resultFormat) {
        String sparqlQuery = "BASE <http://www.southgreen.fr/agrold/>\n"
                + "PREFIX vocab: <vocabulary/>\n"
                + "PREFIX gene: <http://identifiers.org/ensembl.plant/"+geneId+">\n"
                + "SELECT DISTINCT ?Id ?Name ?Description (?protein AS ?IRI)\n"
                + "WHERE{\n"
                + "  gene: vocab:encodes ?protein.\n"
                + "  ?protein rdfs:label ?Name.\n"
                + "  ?protein vocab:description ?Description.\n"
                + "  BIND(REPLACE(str(?protein), '^.*(#|/)', \"\") AS ?Id) .\n"
                + "}";

        sparqlQuery = APILib.addLimitAndOffset(sparqlQuery, pageSize, page);
        System.out.println(sparqlQuery);

        String result = APILib.executeSparqlQuery(sparqlQuery, APILib.sparqlEndpointURL, resultFormat);
        return result;
    }    
    public static void main(String[] args) {
        //System.out.println(getProteinsIdAssociatedWithOntoId("GO:0003824", 1, 5, APILib.TSV));
        //System.out.println(getProteins(0, 2, ".tsv"));
        System.out.println(getProteinsEncodedByGene("BGIOSGA000339", 0, 1, ".html"));
    }
}
