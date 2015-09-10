package agrold.rest.api;

import agrold.rest.api.sparqlaccess.APILib;
import agrold.rest.api.sparqlaccess.OntologyDAO;
import agrold.rest.api.sparqlaccess.QtlDAO;
import agrold.rest.api.sparqlaccess.GraphDAO;
import agrold.rest.api.sparqlaccess.ProteinDAO;
import agrold.rest.api.sparqlaccess.GeneDAO;
import agrold.rest.api.security.CORSResponseFilter;
import agrold.rest.api.sparqlaccess.PathwayDAO;
import java.net.URISyntaxException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author tagny
 */
@Path("/1.0")
public class AgroldService extends ResourceConfig {

    /**
     * Register JAX-RS application components.
     */
    public AgroldService() {
        register(CORSResponseFilter.class);
   //other registrations omitted for brevity
    }    
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
 
    // Ontologies
    @POST
    @Path("/ontologies/terms/byKeyword{_format}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
    public String getOntologyTermsByKeyWord(@PathParam("_format") String format, @QueryParam("keyword") String keyword,
            @DefaultValue("0") @QueryParam("_page") int page, @DefaultValue("10") @QueryParam("_pageSize") int pageSize) {
        return OntologyDAO.getOntologyTermsByKeyWord(keyword, page, pageSize, format);
    }
    @POST
    @Path("/ontologies/terms/ancestors/byId{_format}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
    public String getAncestorById(@PathParam("_format") String format, @QueryParam("id") String id, @QueryParam("level") int level, 
            @DefaultValue("0") @QueryParam("_page") int page, @DefaultValue("10") @QueryParam("_pageSize") int pageSize) {
        return OntologyDAO.getAncestorById(id, level, page, pageSize, format);
    }
    
    @POST
    @Path("/ontologies/terms/parents/byId{_format}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
    public String getParentById(@PathParam("_format") String format, @QueryParam("id") String id, 
            @DefaultValue("0") @QueryParam("_page") int page, @DefaultValue("10") @QueryParam("_pageSize") int pageSize) {
        // The parent is the ancestor at level 1
        return OntologyDAO.getAncestorById(id, 1, page, pageSize, format);
    }
    
    @POST
    @Path("/ontologies/terms/descendants/byId{_format}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
    public String getDescendantsById(@PathParam("_format") String format, @QueryParam("id") String id, @QueryParam("level") int level, 
            @DefaultValue("0") @QueryParam("_page") int page, @DefaultValue("10") @QueryParam("_pageSize") int pageSize) {
        return OntologyDAO.getDescendantsById(id, level, page, pageSize, format);
    }
    
    @POST
    @Path("/ontologies/terms/children/byId{_format}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
    public String getChildrenById(@PathParam("_format") String format, @QueryParam("id") String id, 
            @DefaultValue("0") @QueryParam("_page") int page, @DefaultValue("10") @QueryParam("_pageSize") int pageSize) {
        // The parent is the ancestor at level 1
        return OntologyDAO.getDescendantsById(id, 1, page, pageSize, format);
    }

    @POST
    @Path("/ontologies/ids/byterm{_format}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
    public String getIdByOntoTerm(@PathParam("_format") String format, @QueryParam("ontoTerm") String ontoTerm, 
            @DefaultValue("0") @QueryParam("_page") int page, @DefaultValue("10") @QueryParam("_pageSize") int pageSize) {
        return OntologyDAO.getIdByOntoTerm(ontoTerm, page, pageSize, format);
    }
    
    @POST
    @Path("/ontologies/terms/byId{_format}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
    public String getOntoTermById(@PathParam("_format") String format, @QueryParam("id") String id, 
            @DefaultValue("0") @QueryParam("_page") int page, @DefaultValue("10") @QueryParam("_pageSize") int pageSize) {
        return OntologyDAO.getOntoTermById(id, page, pageSize, format);
    }
    
    @POST
    @Path("/ontologies/terms/associatedWithQtl{_format}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
    public String getOntoTermsAssociatedWithQtl(@PathParam("_format") String format, @QueryParam("qtlId") String qtlId, 
            @DefaultValue("0") @QueryParam("_page") int page, @DefaultValue("10") @QueryParam("_pageSize") int pageSize) {
        return OntologyDAO.getOntoTermsAssociatedWithQtl(qtlId, page, pageSize, format);
    }
    
    @POST
    @Path("/ontologies/terms/associatedWithProtein{_format}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
    public String getOntoTermsAssociatedWithProtein(@PathParam("_format") String format, @QueryParam("proteinId") String proteinId, 
            @DefaultValue("0") @QueryParam("_page") int page, @DefaultValue("10") @QueryParam("_pageSize") int pageSize) {
        return OntologyDAO.getOntoTermsAssociatedWithProtein(proteinId, page, pageSize, format);
    }

    // graphs
    @POST
    @Path("/graphs{_format}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
    public String listGraphs(@PathParam("_format") String format, 
            @DefaultValue("0") @QueryParam("_page") int page, @DefaultValue("10") @QueryParam("_pageSize") int pageSize) {
        return GraphDAO.listGraph( page, pageSize,format);
    }

    @POST
    @Path("/description{_format}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
    public String getDescription(@PathParam("_format") String format, @QueryParam("uri") String resourceURI, 
            @DefaultValue("0") @QueryParam("_page") int page, @DefaultValue("10") @QueryParam("_pageSize") int pageSize) {
        return GraphDAO.getResourceDescription(resourceURI, page, pageSize, format);
    }

    // QTLs
    @POST
    @Path("/qtls{_format}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
    public String getQtls(@PathParam("_format") String format, 
            @DefaultValue("0") @QueryParam("_page") int page, @DefaultValue("10") @QueryParam("_pageSize") int pageSize) {
        return QtlDAO.getQtls( page, pageSize, format);
    }
    @POST
    @Path("/qtls/id/associatedWithOntoId{_format}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
    public String getQtlsIdAssociatedWithOntoId(@PathParam("_format") String format, @QueryParam("ontoId") String ontoId, 
            @DefaultValue("0") @QueryParam("_page") int page, @DefaultValue("10") @QueryParam("_pageSize") int pageSize) {
        return QtlDAO.getQtlIdAssociatedWithOntoId(ontoId, page, pageSize, format);
    }
    @POST
    @Path("/qtls/byKeyword{_format}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
    public String getQtlsByKeyWord(@PathParam("_format") String format, @QueryParam("keyword") String keyword, 
            @DefaultValue("0") @QueryParam("_page") int page, @DefaultValue("10") @QueryParam("_pageSize") int pageSize) {        
        return APILib.getEntitiesByKeyWord(keyword, QtlDAO.QTL_TYPE_URI, page, pageSize, format);
    }
    @POST
    @Path("/qtls/associatedWithProteinId{_format}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
    public String getQtlsAssociatedWithProteinId(@PathParam("_format") String format, @QueryParam("proteinId") String proteinId, 
            @DefaultValue("0") @QueryParam("_page") int page, @DefaultValue("10") @QueryParam("_pageSize") int pageSize) {
        return QtlDAO.getQtlsAssociatedWithProteinId(proteinId, page, pageSize, format);
    }

    // Proteins
    @POST
    @Path("/proteins{_format}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
    public String getProteins(@PathParam("_format") String format, 
            @DefaultValue("0") @QueryParam("_page") int page, @DefaultValue("10") @QueryParam("_pageSize") int pageSize) {
        return ProteinDAO.getProteins(page, pageSize,format);
    }
    @POST
    @Path("/proteins/byKeyword{_format}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
    public String getProteinsByKeyWord(@PathParam("_format") String format, @QueryParam("keyword") String keyword, 
            @DefaultValue("0") @QueryParam("_page") int page, @DefaultValue("10") @QueryParam("_pageSize") int pageSize) {        
        return APILib.getEntitiesByKeyWord(keyword, ProteinDAO.PROTEIN_TYPE_URI, page, pageSize, format);
    }
    @POST
    @Path("/proteins/id/associatedWithOntoId{_format}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
    public String getProteinsIdAssociatedWithOntoId(@PathParam("_format") String format, @QueryParam("ontoId") String ontoId, 
            @DefaultValue("0") @QueryParam("_page") int page, @DefaultValue("10") @QueryParam("_pageSize") int pageSize) {
        return ProteinDAO.getProteinsIdAssociatedWithOntoId(ontoId, page, pageSize, format);
    }
    @POST
    @Path("/proteins/associatedWithQTL{_format}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
    public String getProteinsAssociatedWithQtl(@PathParam("_format") String format, @QueryParam("qtlId") String qtlId, 
            @DefaultValue("0") @QueryParam("_page") int page, @DefaultValue("10") @QueryParam("_pageSize") int pageSize) {
        return ProteinDAO.getProteinsAssociatedWithQTL(qtlId, page, pageSize, format);
    }
    @POST
    @Path("/proteins/EncodedByGene{_format}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
    public String getProteinsEncodedByGene(@PathParam("_format") String format, @QueryParam("geneId") String geneId, 
            @DefaultValue("0") @QueryParam("_page") int page, @DefaultValue("10") @QueryParam("_pageSize") int pageSize) {
        return ProteinDAO.getProteinsEncodedByGene(geneId, page, pageSize, format);
    }
    
    // Genes
    @POST
    @Path("/genes{_format}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
    public String getGenes(@PathParam("_format") String format, 
            @DefaultValue("0") @QueryParam("_page") int page, @DefaultValue("10") @QueryParam("_pageSize") int pageSize) {
        return GeneDAO.getGenes(page, pageSize, format);
    }
    @POST
    @Path("/genes/byKeyword{_format}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
    public String getGenesByKeyWord(@PathParam("_format") String format, @QueryParam("keyword") String keyword, 
            @DefaultValue("0") @QueryParam("_page") int page, @DefaultValue("10") @QueryParam("_pageSize") int pageSize) {        
        return APILib.getEntitiesByKeyWord(keyword, GeneDAO.GENE_TYPE_URI, page, pageSize, format);
    }
    @POST
    @Path("/genes/encodingProtein{_format}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
    public String getGenesEncodingProteins(@PathParam("_format") String format, @QueryParam("proteinId") String proteinId,
            @DefaultValue("0") @QueryParam("_page") int page, @DefaultValue("10") @QueryParam("_pageSize") int pageSize) {
        return GeneDAO.getGenesEncodingProteins(proteinId,page, pageSize, format);
    }
    @POST
    @Path("/genes/participatingInPathway{_format}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
    public String getGenesByPathways(@PathParam("_format") String format, @QueryParam("pathwayId") String pathwayId,
            @DefaultValue("0") @QueryParam("_page") int page, @DefaultValue("10") @QueryParam("_pageSize") int pageSize) {
        return GeneDAO.getGenesByPathwaysID(pathwayId,page, pageSize, format);
    }
    // Pathways
    @POST
    @Path("/pathways/inWhichParticipatesGene/byId{_format}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
    public String getPathwaysOfGeneId(@PathParam("_format") String format, @QueryParam("geneId") String geneId,
            @DefaultValue("0") @QueryParam("_page") int page, @DefaultValue("10") @QueryParam("_pageSize") int pageSize) {
        return PathwayDAO.getPathwaysInWhichParticipatesGene(geneId,page, pageSize, format);
    }
    @POST
    @Path("/pathways/byKeyword{_format}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
    public String getPathwaysByKeyWord(@PathParam("_format") String format, @QueryParam("keyword") String keyword, 
            @DefaultValue("0") @QueryParam("_page") int page, @DefaultValue("10") @QueryParam("_pageSize") int pageSize) {        
        return PathwayDAO.getPathwaysByKeyWord(keyword, page, pageSize, format);
    }
    
    /*// Cache try
    @POST
    @Path("/predicates.cache")
    @Produces(MediaType.APPLICATION_XML)
    public Response getPredicatesCache() {
        Response.ResponseBuilder response = Response.ok(predicateDAO.getAllPredicates());//.type(MediaType.APPLICATION_XML);
        // Expires 3 seconds from now..this would be ideally based
        // of some pre-determined non-functional requirement.        
        Date expirationDate = new Date(System.currentTimeMillis() + 3000);
        response.expires(expirationDate);

        return response.build();
    }*/
}
