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
                + "          rdfs:subClassOf <http://purl.obolibrary.org/obo/SO_0000704>.\n"
                + "    BIND(REPLACE(str(?gene), '^.*(#|/)', \"\") AS ?geneId) .\n"
                + "}";
        sparqlQuery = APILib.addLimitAndOffset(sparqlQuery, pageSize, page);
        System.out.println(sparqlQuery);

        genes = APILib.executeSparqlQuery(sparqlQuery, APILib.sparqlEndpointURL, resultFormat);
        return genes;
    }

    // return pathway in which a gene participates
    public static String getGenePathwaysByID(String geneId, int page, int pageSize, String resultFormat) {
        String pathways = "";

        String sparqlQuery = "prefix	agrold:<http://www.southgreen.fr/agrold/vocabulary/> \n"
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
                + "SELECT distinct ?gene ?geneId ?label ?description\n"
                + "WHERE {\n"
                + "    ?gene rdfs:label ?label;\n"
                + "          agrold:description ?description;          \n"
                + "          rdfs:subClassOf <http://purl.obolibrary.org/obo/SO_0000704>.\n"
                + "    BIND(REPLACE(str(?gene), '^.*(#|/)', \"\") AS ?geneId) .\n"
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
                + "SELECT ?Id ?Name ?Description (?gene AS ?IRI)\n"
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
        System.out.println(getGenesEncodingProteins("A2WXV8",0, -1, ".json"));
    }
}
