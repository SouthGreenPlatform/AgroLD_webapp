package agrold.rest.api;

import agrold.rest.api.sparqlaccess.APILib;
import agrold.rest.api.sparqlaccess.OntologyDAO;
import agrold.rest.api.sparqlaccess.QtlDAO;
import agrold.rest.api.sparqlaccess.GraphDAO;
import agrold.rest.api.sparqlaccess.ProteinDAO;
import agrold.rest.api.sparqlaccess.GeneDAO;
import agrold.rest.api.filter.CORSResponseFilter;
import agrold.rest.api.sparqlaccess.ExternalServices;
import agrold.rest.api.sparqlaccess.PathwayDAO;
import agrold.rest.api.sparqlaccess.PredicateDAO;
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
    final static String DEFAULT_PAGE_SIZE = "10";
    final static String DEFAULT_PAGE = "0";
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
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getOntologyTermsByKeyWord(@PathParam("_format") String format, @QueryParam("keyword") String keyword,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
        return OntologyDAO.getOntologyTermsByKeyWord(keyword, page, pageSize, format);
    }

    @POST
    @Path("/ontologies/terms/ancestors/byId{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getAncestorById(@PathParam("_format") String format, @QueryParam("id") String id, @QueryParam("level") int level,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
        return OntologyDAO.getAncestorById(id, level, page, pageSize, format);
    }

    @POST
    @Path("/ontologies/terms/parents/byId{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getParentById(@PathParam("_format") String format, @QueryParam("id") String id,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
        // The parent is the ancestor at level 1
        return OntologyDAO.getAncestorById(id, 1, page, pageSize, format);
    }

    @POST
    @Path("/ontologies/terms/descendants/byId{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getDescendantsById(@PathParam("_format") String format, @QueryParam("id") String id, @QueryParam("level") int level,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
        return OntologyDAO.getDescendantsById(id, level, page, pageSize, format);
    }

    @POST
    @Path("/ontologies/terms/children/byId{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getChildrenById(@PathParam("_format") String format, @QueryParam("id") String id,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
        // The parent is the ancestor at level 1
        return OntologyDAO.getDescendantsById(id, 1, page, pageSize, format);
    }

    @POST
    @Path("/ontologies/ids/byterm{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getIdByOntoTerm(@PathParam("_format") String format, @QueryParam("ontoTerm") String ontoTerm,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
        return OntologyDAO.getIdByOntoTerm(ontoTerm, page, pageSize, format);
    }

    @POST
    @Path("/ontologies/terms/byId{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getOntoTermById(@PathParam("_format") String format, @QueryParam("id") String id,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
        return OntologyDAO.getOntoTermById(id, page, pageSize, format);
    }

    @POST
    @Path("/ontologies/terms/associatedWithQtl{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getOntoTermsAssociatedWithQtl(@PathParam("_format") String format, @QueryParam("qtlId") String qtlId,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
        return OntologyDAO.getOntoTermsAssociatedWithQtl(qtlId, page, pageSize, format);
    }

    @POST
    @Path("/ontologies/terms/associatedWithProtein{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getOntoTermsAssociatedWithProtein(@PathParam("_format") String format, @QueryParam("proteinId") String proteinId,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
        return OntologyDAO.getOntoTermsAssociatedWithProtein(proteinId, page, pageSize, format);
    }

    // graphs
    @POST
     @Path("/graphs{_format}")
     @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,APILib.TSV,APILib.CSV,MediaType.TEXT_XML,APILib.RDF_XML,APILib.TTL})
     public String listGraphs(@PathParam("_format") String format, 
     @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
     return GraphDAO.listGraph( page, pageSize,format);
     }
    /*@POST
    @Path("/graphs{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public Response listGraphs(@PathParam("_format") String format, @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, 
            @DefaultValue("-1") @QueryParam("_pageSize") int pageSize, @Context Request req) {
        //the key of a good cache control technique, is to : be quick in order to determine if present or not in cache, 
        //and to try to avoid the maximum data processing in order to retrieve fromthe cache (example avoid performing getPlaylistSong  under cache
        int TTL_CACHE = 10000; //in ms
        String tag = "CacheService";

           //is under cache ?
           Response r = HttpCacheRizze.getCachedResponseMilliseconds(req, tag, TTL_CACHE);
        if (r != null) {
            // under cache
            return r;
        }

           // cache is not present or need to be refreshed
        ArrayList songList = new ArrayList();
        //songList = UserService.getPlaylistSongs(userId);
        int status = 200;

        //catch here errors .... empty....       

        r = HttpCacheRizze.getCacheInvalidatedResponse(status, GraphDAO.listGraph(page, pageSize, format), tag, TTL_CACHE);

        return r;

        //return GraphDAO.listGraph(page, pageSize, format);
    }*/

    @POST
    @Path("/graphs/resource/description{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getDescription(@PathParam("_format") String format, @QueryParam("uri") String resourceURI,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
        return GraphDAO.getResourceDescription(resourceURI, page, pageSize, format);
    }

    @POST
    @Path("/graphs/predicates{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getGraphPredicates(@PathParam("_format") String format, @QueryParam("graphLocalName") String graphLocalName,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
        System.out.println("GRAPH:" + graphLocalName);
        return PredicateDAO.getPredicates(graphLocalName, page, pageSize, format);
    }

    // QTLs
    @POST
    @Path("/qtls{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getQtls(@PathParam("_format") String format,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
        return QtlDAO.getQtls(page, pageSize, format);
    }

    @POST
    @Path("/qtls/id/associatedWithOntoId{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getQtlsIdAssociatedWithOntoId(@PathParam("_format") String format, @QueryParam("ontoId") String ontoId,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
        return QtlDAO.getQtlIdAssociatedWithOntoId(ontoId, page, pageSize, format);
    }

    @POST
    @Path("/qtls/byKeyword{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getQtlsByKeyWord(@PathParam("_format") String format, @QueryParam("keyword") String keyword,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
        return APILib.getEntitiesByKeyWord(keyword, QtlDAO.QTL_TYPE_URI, page, pageSize, format);
    }

    @POST
    @Path("/qtls/associatedWithProteinId{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getQtlsAssociatedWithProteinId(@PathParam("_format") String format, @QueryParam("proteinId") String proteinId,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
        return QtlDAO.getQtlsAssociatedWithProteinId(proteinId, page, pageSize, format);
    }

    // Proteins
    @POST
    @Path("/proteins{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getProteins(@PathParam("_format") String format,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
        return ProteinDAO.getProteins(page, pageSize, format);
    }

    @POST
    @Path("/proteins/byKeyword{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getProteinsByKeyWord(@PathParam("_format") String format, @QueryParam("keyword") String keyword,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
        return APILib.getEntitiesByKeyWord(keyword, ProteinDAO.PROTEIN_TYPE_URI, page, pageSize, format);
    }

    @POST
    @Path("/proteins/id/associatedWithOntoId{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getProteinsIdAssociatedWithOntoId(@PathParam("_format") String format, @QueryParam("ontoId") String ontoId,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
        return ProteinDAO.getProteinsIdAssociatedWithOntoId(ontoId, page, pageSize, format);
    }

    @POST
    @Path("/proteins/associatedWithQTL{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getProteinsAssociatedWithQtl(@PathParam("_format") String format, @QueryParam("qtlId") String qtlId,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
        return ProteinDAO.getProteinsAssociatedWithQTL(qtlId, page, pageSize, format);
    }

    @POST
    @Path("/proteins/EncodedByGene{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getProteinsEncodedByGene(@PathParam("_format") String format, @QueryParam("geneId") String geneId,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
        return ProteinDAO.getProteinsEncodedByGene(geneId, page, pageSize, format);
    }

    // External services : publications

    @POST
    @Path("/proteins/publications/byId")
    @Produces({MediaType.APPLICATION_JSON})
    public String getPublicationsOfProteinById(@QueryParam("proteinId") String proteinId) {
        return ExternalServices.getPublicationsOfGeneOrProteinById(proteinId);
    }

    // Genes
    @POST
    @Path("/genes{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getGenes(@PathParam("_format") String format,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
        return GeneDAO.getGenes(page, pageSize, format);
    }

    @POST
    @Path("/genes/byKeyword{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getGenesByKeyWord(@PathParam("_format") String format, @QueryParam("keyword") String keyword,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
        return APILib.getEntitiesByKeyWord(keyword, GeneDAO.GENE_TYPE_URI, page, pageSize, format);
    }

    @POST
    @Path("/genes/encodingProtein{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getGenesEncodingProteins(@PathParam("_format") String format, @QueryParam("proteinId") String proteinId,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
        return GeneDAO.getGenesEncodingProteins(proteinId, page, pageSize, format);
    }

    @POST
    @Path("/genes/participatingInPathway{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getGenesByPathways(@PathParam("_format") String format, @QueryParam("pathwayId") String pathwayId,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
        return GeneDAO.getGenesByPathwaysID(pathwayId, page, pageSize, format);
    }
    
    @POST
    @Path("/genes/NumberOfCDS{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getCDSGene(@PathParam("_format") String format,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
        return GeneDAO.getCDSGene(page, pageSize, format);
    }

    
    // External services : publications

    @POST
    @Path("/genes/publications/byId")
    @Produces({MediaType.APPLICATION_JSON})
    public String getPublicationsOfGeneById(@QueryParam("geneId") String geneId) {
        return ExternalServices.getPublicationsOfGeneOrProteinById(geneId);
    }

    // Pathways

    @POST
    @Path("/pathways/inWhichParticipatesGene/byId{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getPathwaysOfGeneId(@PathParam("_format") String format, @QueryParam("geneId") String geneId,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
        return PathwayDAO.getPathwaysInWhichParticipatesGene(geneId, page, pageSize, format);
    }

    @POST
    @Path("/pathways/byKeyword{_format}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, APILib.TSV, APILib.CSV, MediaType.TEXT_XML, APILib.RDF_XML, APILib.TTL})
    public String getPathwaysByKeyWord(@PathParam("_format") String format, @QueryParam("keyword") String keyword,
            @DefaultValue(DEFAULT_PAGE) @QueryParam("_page") int page, @DefaultValue(DEFAULT_PAGE_SIZE) @QueryParam("_pageSize") int pageSize) {
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
    public static void main(String[] args) {
        AgroldService api = new AgroldService();
        System.out.println(api.getPublicationsOfProteinById("P0C127"));
    }
}
