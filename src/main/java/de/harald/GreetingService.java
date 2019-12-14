package de.harald;

import java.util.List;
import java.util.concurrent.CompletionStage;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/hello")
@RegisterRestClient
@Produces(MediaType.APPLICATION_JSON)
public interface GreetingService {

    @GET
    @Path("/one/{id}")
    public Greeting getOne(@PathParam("id") Long id);

    @GET
    @Path("/all")
    public CompletionStage<List<Greeting>> getAll();
    
}