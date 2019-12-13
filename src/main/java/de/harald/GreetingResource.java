package de.harald;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

@Path("/hello")
@ApplicationScoped
public class GreetingResource {

    @Inject
    GreetingInteractiveQuery iq;

    // WORKS BUT PRODUCES EXCEPTIONS IN THE LOG-FILE
    @GET
    @Path("/jax-rs")
    @Produces(MediaType.TEXT_PLAIN)
    public String withJaxRsClient() {
        Client restClient = ClientBuilder.newClient();
        String result = restClient
            .target("http://localhost:8080")
            .path("/hello/one/100")
            .request(MediaType.TEXT_PLAIN)
            .get(String.class);
        return "hello " + result;
    }

    // WORKS WITHOUT SIDE-EFFECTS
    @GET
    @Path("/mp-rest-client")
    @Produces(MediaType.TEXT_PLAIN) 
    public String withMpRestClient() {
        return iq.getOneFromRemote(200l);
    }

    @GET
    @Path("/one/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getOne(@PathParam("id") Long id) {
        return "one " + id;
    }

    @GET
    @Path("/all")
    @Produces(MediaType.TEXT_PLAIN)
    public List<String> getAll() {
        return Arrays.asList("a", "b", "c");
    }

}