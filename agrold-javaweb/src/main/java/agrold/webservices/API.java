/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.webservices;

import agrold.webservices.dao.Utils;
import agrold.webservices.dao.Utils.Formats;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

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
import java.util.logging.Logger;
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

    static final Logger logger = Logger.getLogger(API.class.getName());

    static final String formatVar = "format";
    static final String formatInPath = "{" + formatVar + ":([.].+?)?}"; // optional path
    static final String pageNumVar = "page";
    static final String pageSizeVar = "pageSize";

    /**
     * Show the API interactive documentation
     */
    @SuppressWarnings("resource")
    @GET
    @Path("/")
    @Produces({MediaType.TEXT_HTML})
    @Hidden
    public void help() {
        try {
            java.net.URI location = new java.net.URI("../api-doc.jsp");
            throw new WebApplicationException(Response.temporaryRedirect(location).build());
        } catch (URISyntaxException e) {
            logger.severe(e.getMessage());
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

    // graphs
    @GET
    @Path("/graphs" + formatInPath)
    @Operation(
        summary = "list all the graphs of AgroLD",
        tags = { Swagger.GENERAL_TAG }
    )
    public Response listGraphs(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format,
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
    @Operation(
        summary = "Retrieve complete URI of all predicates used in AgroLD",
        tags = { Swagger.GENERAL_TAG }
    )
    public Response getDescription(@DefaultValue("") @Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format, @QueryParam("uri") String resourceURI,
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
    @Operation(
        summary = "Retrieve complete URI of all predicates used in AgroLD in JSON",
        tags = { Swagger.GENERAL_TAG }
    )
    public Response getGraphPredicates(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format, @QueryParam("graphLocalName") String graphLocalName,
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
    @Operation(
        summary = "Run a sparql query against the rdf store",
        tags = { Swagger.GENERAL_TAG }
    )
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
    @Operation(
        summary = "Retrieve complete URI and description of all QTLs from AgroLD",
        tags = { Swagger.QTL_TAG }
    )
    public Response getQtls(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format,
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
    @Operation(
        summary = "Get ids of QTLs associated with an ontological Id",
        tags = { Swagger.QTL_TAG }
    )
    public Response getQtlsIdAssociatedWithOntoId(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format, @QueryParam("ontoId") String ontoId,
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
    @Operation(
        summary = "Retrieve QTLs with URI or name or description containing the given keyword",
        tags = { Swagger.QTL_TAG }
    )
    public Response getQtlsByKeyWord(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format, @QueryParam("keyword") String keyword,
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
    @Operation(
        summary = "Get the list of QTLs associated with an protein Id",
        tags = { Swagger.QTL_TAG }
    )
    public Response getQtlsAssociatedWithProteinId(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format, @QueryParam("proteinId") String proteinId,
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
    @Operation(
        summary = "Retrieve complete URI and description of all proteins from AgroLD in JSON format",
        tags = { Swagger.PROTEIN_TAG }
    )
    public Response getProteins(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format,
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
    @Operation(
        summary = "Retrieve proteins with URI or name or description containing the given keyword",
        tags = { Swagger.PROTEIN_TAG }
    )
    public Response getProteinsByKeyWord(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format, @QueryParam("keyword") String keyword,
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
    @Operation(
        summary = "Get ids of proteins associated with an ontological Id",
        tags = { Swagger.PROTEIN_TAG }
    )
    public Response getProteinsIdAssociatedWithOntoId(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format, @QueryParam("ontoId") String ontoId,
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
    @Operation(
        summary = "Get URIs, ids, and name of proteins associated with a QTL",
        tags = { Swagger.PROTEIN_TAG }
    )
    public Response getProteinsAssociatedWithQtl(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format, @QueryParam("qtlId") String qtlId,
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
    @Operation(
        summary = "Get URIs, ids, and name of proteins encoded by a gene given its ID",
        tags = { Swagger.PROTEIN_TAG }
    )
    public Response getProteinsEncodedByGene(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format, @QueryParam("geneId") String geneId,
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
    @Operation(
        summary = "Get publications about a gene given its ID",
        tags = { Swagger.PROTEIN_TAG }
    )
    public Response getPublicationsOfProteinById(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format, @QueryParam("proteinId") String proteinId) throws IOException {
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
    @Operation(
        summary = "Retrieve complete URI and description of all genes from AgroLD in JSON format",
        tags = { Swagger.GENE_TAG }
    )
    public Response getGenes(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format,
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
    @Operation(
        summary = "Retrieve genes with the URI or the name or the description containing the given keyword",
        tags = { Swagger.GENE_TAG }
    )
    public Response getGenesByKeyWord(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format, @QueryParam("keyword") String keyword,
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
    @Operation(
        summary = "Complete URI of gene's description by pathway",
        tags = { Swagger.GENE_TAG }
    )
    public Response getGenesEncodingProteins(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format, @QueryParam("proteinId") String proteinId,
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
    @Operation(
        summary = "Returns the genes on chromosome chromosomeNum whose start position is between chromosomeStart and chromosomeEnd",
        tags = { Swagger.GENE_TAG }
    )
    public Response getGenesByLocus(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format,
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
    @Operation(
        summary = "Complete URI of gene's description by pathway",
        tags = { Swagger.GENE_TAG }
    )
    public Response getGenesByPathways(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format, @QueryParam("pathwayId") String pathwayId,
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
    @Operation(
        summary = "Retrieve complete URI and description of all genes from AgroLD in JSON format",
        tags = { Swagger.GENE_TAG }
    )
    public Response getCDSGene(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format,
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
    @Operation(
        summary = "Get publications of a gene",
        tags = { Swagger.GENE_TAG }
    )
    public Response getPublicationsOfGeneById(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format,
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
    @Operation(
        summary = "Retrieve the other links refering to this gene",
        tags = { Swagger.GENE_TAG }
    )
    public Response getSeeAlso(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format,
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
    @Operation(
        summary = "Retrieve IRI and name of pathways in which an id-given gene participates",
        tags = { Swagger.PATHWAY_TAG }
    )
    public Response getPathwaysOfGeneId(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format, @QueryParam("geneId") String geneId,
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
    @Operation(
        summary = "Retrieve IRI and name of pathways given a keyword",
        tags = { Swagger.PATHWAY_TAG }
    )
    public Response getPathwaysByKeyWord(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format, @QueryParam("keyword") String keyword,
            @DefaultValue(DEFAULT_PAGE) @QueryParam(pageNumVar) int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam(pageSizeVar) int pageSize) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = PathwayDAO.getPathwaysByKeyWord(keyword, page, pageSize, format);
        return buildResponse(content, contentType);
    }

    // Ontologies
    @GET
    @Path("/ontologies/terms/byKeyword" + formatInPath)
    @Operation(
        summary = "Returns all the IDs corresponding to an ontological term",
        tags = { Swagger.ONTOLOGIES_TAG }
    )
    public Response getOntologyTermsByKeyWord(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format, @QueryParam("keyword") String keyword,
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
    @Operation(
        summary = "Returns all the IDs corresponding to an ontological term",
        tags = { Swagger.ONTOLOGIES_TAG }
    )
    public Response getAncestorById(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format, @QueryParam("id") String id, @QueryParam("level") int level,
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
    @Operation(
        summary = "Returns the parents of an ontological element given its id",
        tags = { Swagger.ONTOLOGIES_TAG }
    )
    public Response getParentById(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format, @QueryParam("id") String id,
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
    @Operation(
        summary = "Returns the descendents of an ontological element given its id",
        tags = { Swagger.ONTOLOGIES_TAG }
    )
    public Response getDescendantsById(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format, @QueryParam("id") String id, @QueryParam("level") int level,
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
    @Operation(
        summary = "Returns the children of an ontological element given its id",
        tags = { Swagger.ONTOLOGIES_TAG }
    )
    public Response getChildrenById(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format, @QueryParam("id") String id,
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
    @Operation(
        summary = "Returns all the IDs corresponding to an ontological term",
        tags = { Swagger.ONTOLOGIES_TAG }
    )
    public Response getIdByOntoTerm(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format, @QueryParam("ontoTerm") String ontoTerm,
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
    @Operation(
        summary = "Returns the name of an ontological element corresponding to its given ID",
        tags = { Swagger.ONTOLOGIES_TAG }
    )
    public Response getOntoTermById(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format, @QueryParam("id") String id,
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
    @Operation(
        summary = "Get the ontological terms associated with the QTL, and the association",
        tags = { Swagger.ONTOLOGIES_TAG }
    )
    public Response getOntoTermsAssociatedWithQtl(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format, @QueryParam("qtlId") String qtlId,
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
    @Operation(
        summary = "Get the ontological terms associated with the Protein, and the association",
        tags = { Swagger.ONTOLOGIES_TAG }
    )
    public Response getOntoTermsAssociatedWithProtein(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format, @QueryParam("proteinId") String proteinId,
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
    @Operation(
        summary = "Get the ontological annotation associated with the Gene",
        tags = { Swagger.ONTOLOGIES_TAG }
    )
    public Response getOntoTermsAssociatedWithGene(@Parameter(name = "format", in = ParameterIn.PATH,required = true, schema = @Schema(implementation = Formats.class)) @PathParam(formatVar) String format, @QueryParam("geneId") String geneId) throws IOException {
        String contentType = Utils.getFormatFullName(format);
        if (contentType == null) {
            return Response.serverError().entity("[AgroLD Web Services] - Format Error: The requested resource is not available in the format \"" + format + "\"").build();
        }
        String content = OntologyDAO.getOntoTermsAssociatedWithGene(geneId);
        return buildResponse(content, contentType);
    }
}
