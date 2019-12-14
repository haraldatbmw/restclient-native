package de.harald;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

@Path("/hello")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
public class GreetingResource {

    private static final Logger LOGGER = Logger.getLogger(GreetingResource.class);

    @Inject
    GreetingInteractiveQuery iq;

    // WORKS BUT PRODUCES EXCEPTIONS IN THE LOG-FILE
    @GET
    @Path("/jax-rs/one")
    public Greeting withJaxRsClient() {
        Client restClient = ClientBuilder.newClient();
        Greeting result = restClient
            .target("http://localhost:8080")
            .path("/hello/one/100")
            .request(MediaType.APPLICATION_JSON)
            .get(Greeting.class);
        return result;
    }

    // WORKS BUT PRODUCES EXCEPTIONS IN THE LOG-FILE
    @GET
    @Path("/jax-rs/all")
    public List<Greeting> withJaxRsClientAll() {
        Client restClient = ClientBuilder.newClient();
        List<Greeting> result = restClient
            .target("http://localhost:8080")
            .path("/hello/all")
            .request(MediaType.APPLICATION_JSON)
            .get(new GenericType<List<Greeting>>() {});
        return result;
    }

    // WORKS WITHOUT SIDE-EFFECTS
    @GET
    @Path("/mp-rest-client/one")
    public Greeting withMpRestClient() {
        return iq.getOneFromRemote(200l);
    }

    // WORKS WITHOUT SIDE-EFFECTS
    @GET
    @Path("/mp-rest-client/all")
    public List<Greeting> withMpRestClientAll() {
        return iq.getAllFromRemote();
    }

    @GET
    @Path("/one/{id}")
    public Greeting getOne(@PathParam("id") Long id) {
        return new Greeting(id, "Hallo", "Harald");
    }

    @GET
    @Path("/all")
    public CompletionStage<List<Greeting>> getAll() {
        return CompletableFuture.supplyAsync(() -> {
            LOGGER.info("getAll");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Arrays.asList(
                new Greeting(1l, "hello", "john"), 
                new Greeting(2l, "jippi", "du"), 
                new Greeting(3l, "bye", "sally"));
        });
    }

}