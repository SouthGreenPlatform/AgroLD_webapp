/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.webservices;

import agrold.webservices.dao.Utils;

import static agrold.webservices.dao.Utils.DEFAULT_PAGE;
import static agrold.webservices.dao.Utils.DEFAULT_PAGE_SIZE;
import agrold.webservices.dao.GeneDAO;
import agrold.webservices.dao.GeneralServicesDAO;
import agrold.webservices.dao.OntologyDAO;
import agrold.webservices.dao.PathwayDAO;
import agrold.webservices.dao.ProteinDAO;
import agrold.webservices.dao.QtlDAO;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Application;

/**
 *
 * @author zadmin
 */
@Path("/")
public class API extends Application {

    static final String formatVar = "format";
    static final String formatInPath = "{" + formatVar + ":([.].+?)?}"; // optional path
    static final String pageNumVar = "page";
    static final String pageSizeVar = "pageSize";

    /**
     * Show the API interactive documentation
     */
    @GET
    @Path("/")
    @Produces({MediaType.TEXT_HTML})
    public void help() {
        try {
            java.net.URI location = new java.net.URI("../api-doc.jsp");
            throw new WebApplicationException(Response.temporaryRedirect(location).build());
        } catch (URISyntaxException e) {
        }
    }

    Response buildResponse(String content, String contentType) {
        if (content == null) {
            return Response.serverError()
                    .entity("[AgroLD Web Services] - Resource Error: The requested resource is unavailable!")
                    .build();
        }
        return Response.ok(content, contentType).build();
    }

    // Ontologies
    @GET
    @Path("/ontologies/terms/byKeyword" + formatInPath)
    public Response getOntologyTermsByKeyWord(@PathParam(formatVar) String format, @QueryParam("keyword") String keyword,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page,
            @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError()
                    .entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"")
                    .build();
        }
        String content = OntologyDAO.getOntologyTermsByKeyWord(keyword, page, pageSize, format);
        return buildResponse(content, contentType);
    }

    @GET
    @Path("/ontologies/terms/ancestors/byId" + formatInPath)
    public Response getAncestorById(@PathParam(formatVar) String format, @QueryParam("id") String id, @QueryParam("level") int level,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page,
            @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError()
                    .entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"")
                    .build();
        }
        String content = OntologyDAO.getAncestorById(id, level, page, pageSize, format);
        return buildResponse(content, contentType);
    }

    @GET
    @Path("/ontologies/terms/parents/byId" + formatInPath)
    public Response getParentById(@PathParam(formatVar) String format, @QueryParam("id") String id,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        // The parent is the ancestor at level 1
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError()
                    .entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"")
                    .build();
        }
        String content = OntologyDAO.getAncestorById(id, 1, page, pageSize, format);
        return buildResponse(content, contentType);
    }

    @GET
    @Path("/ontologies/terms/descendants/byId" + formatInPath)
    public Response getDescendantsById(@PathParam(formatVar) String format, @QueryParam("id") String id, @QueryParam("level") int level,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = OntologyDAO.getDescendantsById(id, level, page, pageSize, format);
        return buildResponse(content, contentType);
    }

    @GET
    @Path("/ontologies/terms/children/byId" + formatInPath)
    public Response getChildrenById(@PathParam(formatVar) String format, @QueryParam("id") String id,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        // The parent is the ancestor at level 1
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = OntologyDAO.getDescendantsById(id, 1, page, pageSize, format);
        return buildResponse(content, contentType);
    }

    @GET
    @Path("/ontologies/ids/byterm" + formatInPath)
    public Response getIdByOntoTerm(@PathParam(formatVar) String format, @QueryParam("ontoTerm") String ontoTerm,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = OntologyDAO.getIdByOntoTerm(ontoTerm, page, pageSize, format);
        return buildResponse(content, contentType);
    }

    @GET
    @Path("/ontologies/terms/byId" + formatInPath)
    public Response getOntoTermById(@PathParam(formatVar) String format, @QueryParam("id") String id,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = OntologyDAO.getOntoTermById(id, page, pageSize, format);
        return buildResponse(content, contentType);
    }

    @GET
    @Path("/ontologies/terms/associatedWithQtl" + formatInPath)
    public Response getOntoTermsAssociatedWithQtl(@PathParam(formatVar) String format, @QueryParam("qtlId") String qtlId,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = OntologyDAO.getOntoTermsAssociatedWithQtl(qtlId, page, pageSize, format);
        return buildResponse(content, contentType);
    }

    @GET
    @Path("/ontologies/terms/associatedWithProtein" + formatInPath)
    public Response getOntoTermsAssociatedWithProtein(@PathParam(formatVar) String format, @QueryParam("proteinId") String proteinId,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = OntologyDAO.getOntoTermsAssociatedWithProtein(proteinId, page, pageSize, format);
        return buildResponse(content, contentType);
    }

    @GET
    @Path("/ontologies/terms/associatedWithGene" + formatInPath)
    @Produces({MediaType.APPLICATION_JSON})
    public Response getOntoTermsAssociatedWithGene(@PathParam(formatVar) String format, @QueryParam("geneId") String geneId) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = OntologyDAO.getOntoTermsAssociatedWithGene(geneId);
        return buildResponse(content, contentType);
    }

    // graphs
    @GET
    @Path("/graphs" + formatInPath)
    public Response listGraphs(@PathParam(formatVar) String format,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = GeneralServicesDAO.listGraph(page, pageSize, format);
        return buildResponse(content, contentType);
    }

    @GET
    @Path("/describe" + formatInPath)
    public Response getDescription(@DefaultValue("") @PathParam(formatVar) String format, @QueryParam("uri") String resourceURI,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = GeneralServicesDAO.getIRIDescription(resourceURI, page, pageSize, format);
        return buildResponse(content, contentType);
    }
    
    @GET
    @Path("/describe4visualization.json")
    public Response getDescriptionForVisualization(@QueryParam("uri") String resourceURI,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {        
        String content = GeneralServicesDAO.getIRIDescription4visualization(resourceURI, page, pageSize);
        return buildResponse(content, Utils.JSON);
    }
    

    @GET
    @Path("/predicates" + formatInPath)
    public Response getGraphPredicates(@PathParam(formatVar) String format, @QueryParam("graphLocalName") String graphLocalName,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        System.out.println("GRAPH:" + graphLocalName);
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = GeneralServicesDAO.getPredicates(graphLocalName, page, pageSize, format);
        return buildResponse(content, contentType);
    }

    /**
     *
     * @param query
     * @param format
     * @return
     * @throws IOException
     */
    @GET
    @Path("/sparql")
    //@Produces({CSV, HTML, JSON, N3, RDF, JSON_LD, TSV, TTL, XML})
    public Response sparql(@QueryParam("query") String query, @QueryParam(formatVar) String format) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = Utils.executeSparqlQuery(query, Utils.sparqlEndpointURL, contentType);
        return buildResponse(content, contentType);
    }

    // QTLs
    @GET
    @Path("/qtls" + formatInPath)
    public Response getQtls(@PathParam(formatVar) String format,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = QtlDAO.getQtls(page, pageSize, format);
        return buildResponse(content, contentType);
    }

    @GET
    @Path("/qtls/id/associatedWithOntoId" + formatInPath)
    public Response getQtlsIdAssociatedWithOntoId(@PathParam(formatVar) String format, @QueryParam("ontoId") String ontoId,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = QtlDAO.getQtlIdAssociatedWithOntoId(ontoId, page, pageSize, format);
        return buildResponse(content, contentType);
    }

    @GET
    @Path("/qtls/byKeyword" + formatInPath)
    public Response getQtlsByKeyWord(@PathParam(formatVar) String format, @QueryParam("keyword") String keyword,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = QtlDAO.getQtlsByKeyword(keyword, page, pageSize, format);
        return buildResponse(content, contentType);
    }

    @GET
    @Path("/qtls/associatedWithProteinId" + formatInPath)
    public Response getQtlsAssociatedWithProteinId(@PathParam(formatVar) String format, @QueryParam("proteinId") String proteinId,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = QtlDAO.getQtlsAssociatedWithProteinId(proteinId, page, pageSize, format);
        return buildResponse(content, contentType);
    }

    // Proteins
    @GET
    @Path("/proteins" + formatInPath)
    public Response getProteins(@PathParam(formatVar) String format,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = ProteinDAO.getProteins(page, pageSize, format);
        return buildResponse(content, contentType);
    }

    @GET
    @Path("/proteins/byKeyword" + formatInPath)
    public Response getProteinsByKeyWord(@PathParam(formatVar) String format, @QueryParam("keyword") String keyword,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = ProteinDAO.getProteinsByKeyword(keyword, page, pageSize, format);
        return buildResponse(content, contentType);
    }

    @GET
    @Path("/proteins/id/associatedWithOntoId" + formatInPath)
    public Response getProteinsIdAssociatedWithOntoId(@PathParam(formatVar) String format, @QueryParam("ontoId") String ontoId,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = ProteinDAO.getProteinsIdAssociatedWithOntoId(ontoId, page, pageSize, format);
        return buildResponse(content, contentType);
    }

    @GET
    @Path("/proteins/associatedWithQTL" + formatInPath)
    public Response getProteinsAssociatedWithQtl(@PathParam(formatVar) String format, @QueryParam("qtlId") String qtlId,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = ProteinDAO.getProteinsAssociatedWithQTL(qtlId, page, pageSize, format);
        return buildResponse(content, contentType);
    }

    @GET
    @Path("/proteins/EncodedByGene" + formatInPath)
    public Response getProteinsEncodedByGene(@PathParam(formatVar) String format, @QueryParam("geneId") String geneId,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = ProteinDAO.getProteinsEncodedByGene(geneId, page, pageSize, format);
        return buildResponse(content, contentType);
    }

    // External services : publications
    @GET
    @Path("/proteins/publications/byId" + formatInPath)
    @Produces({MediaType.APPLICATION_JSON})
    public Response getPublicationsOfProteinById(@PathParam(formatVar) String format, @QueryParam("proteinId") String proteinId) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = ProteinDAO.getPublicationsOfProteinById(proteinId);
        return buildResponse(content, contentType);
    }

    // Genes
    @GET
    @Path("/genes" + formatInPath)
    public Response getGenes(@PathParam(formatVar) String format,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = GeneDAO.getGenes(page, pageSize, format);
        return buildResponse(content, contentType);
    }

    @GET
    @Path("/genes/byKeyword" + formatInPath)
    public Response getGenesByKeyWord(@PathParam(formatVar) String format, @QueryParam("keyword") String keyword,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = GeneDAO.getGenesByKeyword(keyword, page, pageSize, format);
        return buildResponse(content, contentType);
    }

    @GET
    @Path("/genes/encodingProtein" + formatInPath)
    public Response getGenesEncodingProteins(@PathParam(formatVar) String format, @QueryParam("proteinId") String proteinId,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = GeneDAO.getGenesEncodingProtein(proteinId, page, pageSize, format);
        return buildResponse(content, contentType);
    }

    @GET
    @Path("/genes/byLocus" + formatInPath)
    public Response getGenesByLocus(@PathParam(formatVar) String format,
            @QueryParam("chromosomeNum") String chromosomeNum,
            @QueryParam("chromosomeStart") String chromosomeStart,
            @QueryParam("chromosomeEnd") String chromosomeEnd,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = GeneDAO.getGenesByLocus(chromosomeNum, chromosomeStart, chromosomeEnd, page, pageSize, format);
        return buildResponse(content, contentType);
    }

    @GET
    @Path("/genes/participatingInPathway" + formatInPath)
    public Response getGenesByPathways(@PathParam(formatVar) String format, @QueryParam("pathwayId") String pathwayId,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = GeneDAO.getGenesByPathwaysID(pathwayId, page, pageSize, format);
        return buildResponse(content, contentType);
    }

    @GET
    @Path("/genes/NumberOfCDS" + formatInPath)
    public Response getCDSGene(@PathParam(formatVar) String format,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = GeneDAO.getCDSGene(page, pageSize, format);
        return buildResponse(content, contentType);
    }

    // publications
    @GET
    @Path("/genes/publications/byId" + formatInPath)
    //@Produces({MediaType.APPLICATION_JSON})
    public Response getPublicationsOfGeneById(@PathParam(formatVar) String format,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page,
            @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize,
            @QueryParam("geneId") String geneId) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = GeneDAO.getPublicationsOfGeneById(geneId, page, pageSize, format);
        return buildResponse(content, contentType);
    }

    // seeAlso
    @GET
    @Path("/genes/seeAlso" + formatInPath)
    //@Produces({MediaType.APPLICATION_JSON})
    public Response getSeeAlso(@PathParam(formatVar) String format,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page,
            @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize,
            @QueryParam("geneUri") String geneUri) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = GeneDAO.getSeeAlsoByURI(geneUri, page, pageSize, format);
        return buildResponse(content, contentType);
    }

    // Pathways
    @GET
    @Path("/pathways/inWhichParticipatesGene/byId" + formatInPath)
    public Response getPathwaysOfGeneId(@PathParam(formatVar) String format, @QueryParam("geneId") String geneId,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = PathwayDAO.getPathwaysInWhichParticipatesGene(geneId, page, pageSize, format);
        return buildResponse(content, contentType);
    }

    @GET
    @Path("/pathways/byKeyword" + formatInPath)
    public Response getPathwaysByKeyWord(@PathParam(formatVar) String format, @QueryParam("keyword") String keyword,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = PathwayDAO.getPathwaysByKeyWord(keyword, page, pageSize, format);
        return buildResponse(content, contentType);
    }

    @GET
    @Path("/ontologies/terms/byKeywordTEST" + formatInPath)
    public Response getCountInstancesAssociatedWithOntologyId(@PathParam(formatVar) String format, @QueryParam("keyword") String keyword,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = OntologyDAO.getCountInstancesAssociatedWithOntologyId(keyword, page, pageSize, format);
        return buildResponse(content, contentType);
    }
}
