package agrold.rest.api;

import agrold.rest.api.security.CORSResponseFilter;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
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
// http://stackoverflow.com/questions/9373081/how-to-set-up-jax-rs-application-using-annotations-only-no-web-xml
public class AgroldService extends ResourceConfig {

    /**
     * Register JAX-RS application components.
     */
    public AgroldService() {
        register(CORSResponseFilter.class);
   //other registrations omitted for brevity
    }

    PredicateDAO predicateDAO = new PredicateDAO();
    GeneDAO geneDAO = new GeneDAO();

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
    @Path("/ancestor/byId")
    @Produces({"text/tsv"})
    public String getAncestorById(@QueryParam("id") String id, @QueryParam("level") int level) {
        return OntologyDAO.getAncestorById(id, level, APILib.TSV);
    }
    
    @POST
    @Path("/parent/byId")
    @Produces({"text/tsv"})
    public String getParentById(@QueryParam("id") String id) {
        // The parent is the ancestor at level 1
        return OntologyDAO.getAncestorById(id, 1, APILib.TSV);
    }
    
    @POST
    @Path("/descendent/byId")
    @Produces({"text/tsv"})
    public String getDescendentById(@QueryParam("id") String id, @QueryParam("level") int level) {
        return OntologyDAO.getDescendentById(id, level, APILib.TSV);
    }
    
    @POST
    @Path("/children/byId")
    @Produces({"text/tsv"})
    public String getChildrenById(@QueryParam("id") String id) {
        // The parent is the ancestor at level 1
        return OntologyDAO.getDescendentById(id, 1, APILib.TSV);
    }

    @POST
    @Path("/id/byOntoTerm")
    @Produces({"text/tsv"})
    public String getIdByOntoTerm(@QueryParam("ontoTerm") String ontoTerm) {
        return OntologyDAO.getIdByOntoTerm(ontoTerm, APILib.TSV);
    }
    
    @POST
    @Path("/ontoTerm/byId")
    @Produces({"text/tsv"})
    public String getOntoTermById(@QueryParam("id") String id) {
        return OntologyDAO.getOntoTermById(id, APILib.TSV);
    }

    // graphs
    @POST
    @Path("/graphs.json")
    @Produces({MediaType.APPLICATION_JSON})
    public String listGraphs() {
        return GraphDAO.listGraph(APILib.JSON);
    }

    @POST
    @Path("/description.json")
    @Produces({MediaType.APPLICATION_JSON})
    public String getDescription(@QueryParam("uri") String resourceURI) {
        return GraphDAO.getResourceDescription(resourceURI, APILib.JSON);
    }

    // QTLs
    @POST
    @Path("/qtls.json")
    @Produces({MediaType.APPLICATION_JSON})
    public String getQtls() {
        return QtlDAO.getAllQtlURI(APILib.JSON);
    }

    // Proteins
    @POST
    @Path("/proteins.json")
    @Produces({MediaType.APPLICATION_JSON})
    public String getProteins() {
        return ProteinDAO.getAllProteinsURI(APILib.JSON);
    }
    
    @POST
    @Path("/proteins/id/associatedWithOntoId")
    @Produces({"text/tsv"})
    public String getProteinsIdAssociatedWithOntoId(@QueryParam("ontoId") String ontoId, 
            @QueryParam("_page") int page, @QueryParam("_pageSize") int pageSize) {
        return ProteinDAO.getProteinIdAssociatedWithOntoId(ontoId, page, pageSize, APILib.TSV);
    }

    // Genes
    @POST
    @Path("/genes.json")
    @Produces({MediaType.APPLICATION_JSON})
    public String getGenes() {
        return GeneDAO.getAllGenesURI(APILib.JSON);
    }

    @POST
    @Path("/genes.xml")
    @Produces({MediaType.APPLICATION_XML})
    public String getGenesXML() {
        return GeneDAO.getAllGenesURI(APILib.XML);
    }

    // Predicates
    @POST
    @Path("/predicates.xml")
    @Produces({MediaType.APPLICATION_XML})
    public List<Predicate> getPredicates() {
        return predicateDAO.getAllPredicates();
    }

    @POST
    @Path("/predicates.json")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Predicate> getPredicatesJson() {
        return predicateDAO.getAllPredicates();
    }

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
    }
}
