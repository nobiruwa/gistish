package javaee7.barcode.server.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

// http://localhost:8080/javaee7-barcode-server/resources
@Path("hello")
public class Hello {
    @GET
    @Produces("text/plain")
    public String getGreeting() {
        return "Hello world.";
    }
}
