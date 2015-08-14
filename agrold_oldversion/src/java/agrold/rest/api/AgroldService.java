/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrold.rest.api;

import java.util.Date;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author tagny
 */
@Path("/res")
public class AgroldService {

    PredicateDAO predicateDAO = new PredicateDAO();
    GeneDAO geneDAO = new GeneDAO();

    @GET
    @Path("/graphs.json")
    @Produces({MediaType.APPLICATION_JSON})
    public String listGraphs(){
        return APILib.listGraph(APILib.JSON);
    }
    
    @GET
    @Path("/description.json")
    @Produces({MediaType.APPLICATION_JSON})
    public String getDescription(@QueryParam("uri")String resourceURI){
        return APILib.getResourceDescription(resourceURI, APILib.JSON);
    }
    
    @GET
    @Path("/qtls.json")
    @Produces({MediaType.APPLICATION_JSON})
    public String getQtls(){
        return QtlDAO.getAllQtlURI(APILib.JSON);
    }
    
    @GET
    @Path("/proteins.json")
    @Produces({MediaType.APPLICATION_JSON})
    public String getProteins(){
        return ProteinDAO.getAllProteinsURI(APILib.JSON);
    }
    
    @GET
    @Path("/genes.json")
    @Produces({MediaType.APPLICATION_JSON})
    public String getGenes(){
        return GeneDAO.getAllGenesURI(APILib.JSON);
    }
    
    @GET
    @Path("/genes.xml")
    @Produces({MediaType.APPLICATION_XML})
    public String getGenesXML(){
        return GeneDAO.getAllGenesURI(APILib.XML);
    }
    
    @GET
    @Path("/predicates.xml")
    @Produces({MediaType.APPLICATION_XML})
    public List<Predicate> getPredicates() {
        return predicateDAO.getAllPredicates();
    }
    @GET
    @Path("/predicates.json")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Predicate> getPredicatesJson() {
        return predicateDAO.getAllPredicates();
    }

    @GET
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
