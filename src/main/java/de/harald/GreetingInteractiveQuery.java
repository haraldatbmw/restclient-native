package de.harald;

import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.rest.client.RestClientBuilder;

@ApplicationScoped
public class GreetingInteractiveQuery 
    extends InteractiveQueryBase<Long, Greeting, GreetingService> {

    // function returning the concrete rest-client
    private static final Function<URL, GreetingService> GET_REST_CLIENT = (url) -> {
        return RestClientBuilder.newBuilder()
            .baseUrl(url)
            .build(GreetingService.class);
    };

    // function for fetching one message by the key
    private static final BiFunction<GreetingService, Long, Greeting> GET_ONE = (restClient, id) -> {
        return restClient.getOne(id);
    };

    // function for fetching all messages from the local state-store
    private static final Function<GreetingService, CompletionStage<List<Greeting>>> GET_ALL = (restClient) -> {
        return restClient.getAll();
    };

    public GreetingInteractiveQuery() {
        super("greetings-statestore", GET_REST_CLIENT, GET_ONE, GET_ALL);
    }

}