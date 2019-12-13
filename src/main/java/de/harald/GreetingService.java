package de.harald;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/")
@RegisterRestClient
public interface GreetingService {

    @GET
    @Path("/echo")
    @Produces(MediaType.TEXT_PLAIN)
    public String echo();
    
}