package agrold.webservices;

import java.util.Set;
import javax.servlet.ServletConfig;

import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;
import javax.ws.rs.core.Application;

import agrold.config.PropertiesBean;

public class Swagger {
    public static void initialize(ServletConfig servletConfig, Application application) {
        OpenAPI oas = new OpenAPI().info( new Info()
            .title("AgroLD API")
            .description(
                "AgroLD is a RDF knowledge base that consists of data integrated from a variety of plant resources and ontologies. " 
                + "The aim of the Agronomic Linked Data (AgroLD) project is to provide a portal for bioinformatics and domain experts " 
                + "to exploit the homogenized data models towards efficiently generating research hypotheses."
            )
            .version(PropertiesBean.getAPIVersion() + (
                    PropertiesBean.getInstance() != null ? " " + PropertiesBean.getInstance() : ""
                )
            )
            .license(
                new License().name("GNU General Public License v3.0")
                    .url("https://github.com/SouthGreenPlatform/AgroLD_webapp/blob/master/LICENSE")
            )
            .summary("The RESTful API of AgroLD allows its users to look into the application's data.")
            .contact(
                new Contact()
                    .name("Pierre Larmande")
                    .email("pierre.larmande@ird.fr")
                    .url("https://sites.google.com/site/larmandepierre")
            )
        );

        try {
            new JaxrsOpenApiContextBuilder<>()
                .servletConfig(servletConfig)
                .application(application)
                .openApiConfiguration(
                    new SwaggerConfiguration()
                        .openAPI(oas)
                        .prettyPrint(true)
                        .resourcePackages(Set.of("agrold.webservices"))
                )
                .buildContext(true);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
