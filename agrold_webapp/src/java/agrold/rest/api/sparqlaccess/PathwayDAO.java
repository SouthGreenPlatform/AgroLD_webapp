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
public class PathwayDAO {

    //public static String METABOLIC_PATHWAY = "http://edamontology.org/data_1157";
    public static String METABOLIC_PATHWAY = "http://www.southgreen.fr/agrold/resource/Pathway_Identifier";
    //public static String PATHWAY_IDENTIFIER = "http://semanticscience.org/resource/SIO_010532";
    public static String PATHWAY_IDENTIFIER = "http://www.southgreen.fr/agrold/resource/Metabolic_Pathway";
    public static String GRAMECYC_GRAPH = "http://www.southgreen.fr/agrold/gramene.cyc";

    public static String getPathwaysByKeyWord(String keyword, int page, int pageSize, String resultFormat) {
        String sparqlQuery = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
                + "SELECT distinct  ?Id ?Name (?entity as ?URI)\n"
                + "WHERE {\n"
                + "  VALUES ?keyword {\n"
                + "    \""+keyword+"\" \n"
                + "  }\n"
                + "  graph <"+GRAMECYC_GRAPH+">{\n"
                + "    {\n"
                + "#     ?entity rdfs:subClassOf <"+METABOLIC_PATHWAY+">.\n"
                + "      ?entity rdf:type <"+METABOLIC_PATHWAY+">.\n"
                + "    }\n"
                + "    UNION\n"
                + "    {\n"
                + "#     ?entity rdfs:subClassOf <"+PATHWAY_IDENTIFIER+"> .\n"
                + "      ?entity rdf:type <"+PATHWAY_IDENTIFIER+"> .\n"
                + "    }\n"
                + "    OPTIONAL{\n"
                + "    ?entity rdfs:label ?Name .\n"
                + "    }  \n"
                + "    BIND(REPLACE(str(?entity), '^.*(#|/)', \"\") AS ?Id) \n"
                + "    BIND ( CONCAT(?Name, ?Id) AS ?text)\n"
                + "    FILTER(REGEX(?text, ?keyword,\"i\"))\n"
                + "  }\n"
                + "}";
        sparqlQuery = APILib.addLimitAndOffset(sparqlQuery, pageSize, page);
        System.out.println(sparqlQuery);
        String result = APILib.executeSparqlQuery(sparqlQuery, APILib.sparqlEndpointURL, resultFormat);
        return result;
    }

    // return URIs and agrold_vocabulary:description of all genes in Agrold
    public static String getPathwaysInWhichParticipatesGene(String geneId, int page, int pageSize, String resultFormat) {
        //return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<genes> Hello Jersey" + "</genes>";
        String genes = "";

        String sparqlQuery = "BASE <http://www.southgreen.fr/agrold/>\n"
                + "PREFIX vocab:<vocabulary/>\n"
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX graph:<gramene.cyc>\n"
                + "PREFIX gene:<http://identifiers.org/ensembl.plant/" + geneId + ">\n"
                + "\n"
                + "SELECT DISTINCT ?Id ?Name (?pathway AS ?IRI)\n"
                + "WHERE {\n"
                + " GRAPH graph: {\n"
                + "  gene: vocab:is_agent_in ?pathway. \n"
                + "  ?pathway rdfs:label ?Name. \n"
                + "  BIND(REPLACE(str(?pathway), '^.*(#|/)', \"\") AS ?Id) .\n"
                + "  }\n"
                + "}\n";
        sparqlQuery = APILib.addLimitAndOffset(sparqlQuery, pageSize, page);
        System.out.println(sparqlQuery);

        genes = APILib.executeSparqlQuery(sparqlQuery, APILib.sparqlEndpointURL, resultFormat);
        return genes;
    }

    public static void main(String[] args) {
        System.out.println(getPathwaysByKeyWord("CALVIN", 0, -1, ".json"));
    }

}
