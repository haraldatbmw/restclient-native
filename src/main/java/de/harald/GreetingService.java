package de.harald;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/hello")
@RegisterRestClient
public interface GreetingService {

    @GET
    @Path("/one/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getOne(@PathParam("id") Long id);

    @GET
    @Path("/all")
    @Produces(MediaType.TEXT_PLAIN)
    public List<String> getAll();
    
}