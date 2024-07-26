package agrold;

import javax.ws.rs.core.Application;

import agrold.webservices.API;
import agrold.webservices.Swagger;

import java.util.Set;

public class AgroldApplication extends Application{
    @Override
    public Set<Class<?>> getClasses() {
        return Set.of(
            // Your resource classes, whenever you want to document a class provide it here
            API.class,
            // Swagger resources
            io.swagger.v3.jaxrs2.integration.resources.OpenApiResource.class,
            io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource.class
        );
    }

    public AgroldApplication() {
        Swagger.initialize(null, this);
    }
}
