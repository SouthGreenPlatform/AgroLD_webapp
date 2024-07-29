package agrold.webservices;

import java.util.Set;
import javax.servlet.ServletConfig;
import javax.ws.rs.core.Application;

import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.info.*;
import io.swagger.v3.oas.annotations.servers.*;

@OpenAPIDefinition(
    info = @Info(
        title = "AgroLD API",
        description = 
            "AgroLD is a RDF knowledge base that consists of data integrated from a variety of plant resources and ontologies. " 
            + "The aim of the Agronomic Linked Data (AgroLD) project is to provide a portal for bioinformatics and domain experts " 
            + "to exploit the homogenized data models towards efficiently generating research hypotheses.",
        license = @License(
            name = "GNU General Public License v3.0",
            url = "https://github.com/SouthGreenPlatform/AgroLD_webapp/blob/master/LICENSE"
        ),
        summary = "The RESTful API of AgroLD allows its users to look into the application's data.",
        contact = @Contact(
            name = "Pierre Larmande",
            email = "pierre.larmande@ird.fr",
            url = "https://sites.google.com/site/larmandepierre"
        )
    ),
    servers = {
        @Server(
            url = "https://v2.agrold.org",
            description = "Production server"
        ),
        @Server(
            url = "https://dev.agrold.org",
            description = "Development version"
        )        
    },
    tags = {
        @Tag(name = Swagger.GENERAL_TAG, description = "General information about AgroLD"),
        @Tag(name = Swagger.GENE_TAG, description = "Genes information"),
        @Tag(name = Swagger.PROTEIN_TAG, description = "Proteins information"),
        @Tag(name = Swagger.QTL_TAG, description = "QTLs information"),
        @Tag(name = Swagger.PATHWAY_TAG, description = "Pathways information"),
        @Tag(name = Swagger.ONTOLOGIES_TAG, description = "Ontologies information")
    }
)
public class Swagger {
    
    public static final String GENERAL_TAG = "general";
    public static final String GENE_TAG = "gene";
    public static final String PROTEIN_TAG = "protein";
    public static final String QTL_TAG = "qtl";
    public static final String PATHWAY_TAG = "pathway";
    public static final String ONTOLOGIES_TAG = "ontologies";

    public static void initialize(ServletConfig servletConfig, Application application) {
        try {
            new JaxrsOpenApiContextBuilder<>()
                .servletConfig(servletConfig)
                .application(application)
                .openApiConfiguration(
                    new SwaggerConfiguration()
                        .openAPI(new OpenAPI())
                        .prettyPrint(true)
                        .resourcePackages(Set.of("agrold.webservices"))
                )
                .buildContext(true);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
