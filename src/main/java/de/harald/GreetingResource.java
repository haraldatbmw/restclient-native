package de.harald;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        Client restClient = ClientBuilder.newClient();
        String result = restClient.target("http://localhost:8080")
                .path("/hello/echo")
                .request(MediaType.TEXT_PLAIN)
                .get(String.class);
        return "hello " + result;
    }

    @GET
    @Path("/echo")
    @Produces(MediaType.TEXT_PLAIN)
    public String echo() {
        return "echo";
    }
}