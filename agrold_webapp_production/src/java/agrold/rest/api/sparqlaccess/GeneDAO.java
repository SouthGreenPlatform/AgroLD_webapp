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
public class GeneDAO {

    public static String GENE_TYPE_URI = "http://purl.obolibrary.org/obo/SO_0000704";

    // return URIs and agrold_vocabulary:description of all genes in Agrold
    public static String getGenes(int page, int pageSize, String resultFormat) {
        //return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<genes> Hello Jersey" + "</genes>";
        String genes = "";

        String sparqlQuery = "prefix	agrold:<http://www.southgreen.fr/agrold/vocabulary/> \n"
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
                + "SELECT distinct ?gene ?geneId ?geneName ?geneDescription\n"
                + "WHERE {\n"
                + "    ?gene rdfs:label ?geneName;\n"
                + "          agrold:description ?geneDescription;          \n"
                + "          rdfs:subClassOf <" + GENE_TYPE_URI + ">.\n"
                + "    BIND(REPLACE(str(?gene), '^.*(#|/)', \"\") AS ?geneId) .\n"
                + "}";
        sparqlQuery = APILib.addLimitAndOffset(sparqlQuery, pageSize, page);
        System.out.println(sparqlQuery);

        genes = APILib.executeSparqlQuery(sparqlQuery, APILib.sparqlEndpointURL, resultFormat);
        return genes;
    }

    // return genes participating in a pathway given its local name (Id)
    public static String getGenesByPathwaysID(String pathwayId, int page, int pageSize, String resultFormat) {
        String pathways = "";

        String sparqlQuery = "BASE <http://www.southgreen.fr/agrold/>\n"
                + "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX vocab:<vocabulary/>\n"
                + "PREFIX graph:<gramene.cyc>\n"
                + "PREFIX taxonGraph:<ncbitaxon>\n"
                + "PREFIX pathway:<biocyc.pathway/" + pathwayId + ">\n"
                + "SELECT DISTINCT ?geneId ?gene_name ?taxon (str(?taxon_name) AS ?taxon_name) (?gene AS ?URI)\n"
                + "WHERE {\n"
                + "  GRAPH graph: {\n"
                + "    ?gene vocab:is_agent_in pathway:.\n"
                + "    ?gene rdfs:label ?gene_name.\n"
                + "    ?gene vocab:taxon ?taxon.\n"
                + "    BIND(REPLACE(str(?gene), '^.*(#|/)', \"\") AS ?geneId) .\n"
                + "  }\n"
                + "  GRAPH taxonGraph:{\n"
                + "    ?taxon rdfs:label ?taxon_name.\n"
                + "  }\n"
                + "}";
        sparqlQuery = APILib.addLimitAndOffset(sparqlQuery, pageSize, page);
        System.out.println(sparqlQuery);

        pathways = APILib.executeSparqlQuery(sparqlQuery, APILib.sparqlEndpointURL, resultFormat);
        return pathways;
    }

    public static String getGenesEncodingProteins(String proteinId, int page, int pageSize, String resultFormat) {
        String sparqlQuery = "BASE <http://www.southgreen.fr/agrold/>\n"
                + "PREFIX vocab: <vocabulary/>\n"
                + "PREFIX protein: <http://purl.uniprot.org/uniprot/" + proteinId + ">\n"
                + "SELECT ?Id ?Name ?Description (?gene AS ?URI)\n"
                + "WHERE{\n"
                + "  ?gene vocab:encodes protein:.\n"
                + "  OPTIONAL{?gene rdfs:label ?Name.}\n"
                + "  ?gene vocab:description ?Description.\n"
                + "  BIND(REPLACE(str(?gene), '^.*(#|/)', \"\") AS ?Id) .\n"
                + "}";

        sparqlQuery = APILib.addLimitAndOffset(sparqlQuery, pageSize, page);
        System.out.println(sparqlQuery);

        String result = APILib.executeSparqlQuery(sparqlQuery, APILib.sparqlEndpointURL, resultFormat);
        return result;
    }

    public static void main(String[] args) {
        System.out.println(getGenesByPathwaysID("CALVIN-PWY", 0, 10, ".json"));
    }
}
