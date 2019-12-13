package de.harald;

import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.RestClientBuilder;

@Path("/hello")
public class GreetingResource {

    // WORKS BUT PRODUCES EXCEPTIONS IN THE LOG-FILE
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        Client restClient = ClientBuilder.newClient();
        String result = restClient
            .target("http://localhost:8080")
            .path("/hello/echo")
            .request(MediaType.TEXT_PLAIN)
            .get(String.class);
        return "hello " + result;
    }

    // WORKS WITHOUT EXCEPTIONS
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/mp-rest-client")
    public String hello2() throws MalformedURLException {
        URL apiUrl = new URL("http://localhost:8080/hello");
        GreetingService srv = RestClientBuilder.newBuilder()
            .baseUrl(apiUrl)
            .build(GreetingService.class);
        return "hello " + srv.echo();
    }

    @GET
    @Path("/echo")
    @Produces(MediaType.TEXT_PLAIN)
    public String echo() {
        return "echo";
    }
}