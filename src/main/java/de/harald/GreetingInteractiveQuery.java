package de.harald;

import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.rest.client.RestClientBuilder;

@ApplicationScoped
public class GreetingInteractiveQuery extends InteractiveQueryBase<Long, Greeting> {

    // function for fetching one message by the key
    private static final BiFunction<URL, Long, Greeting> getOne = (url, id) -> {
        GreetingService srv = RestClientBuilder.newBuilder()
            .baseUrl(url)
            .build(GreetingService.class);
        return srv.getOne(id);
    };

    // function for fetching all messages from the local state-store
    private static final Function<URL, CompletionStage<List<Greeting>>> getAll = (url) -> {
        GreetingService srv = RestClientBuilder.newBuilder()
            .baseUrl(url)
            .build(GreetingService.class);
        return srv.getAll();
    };

    public GreetingInteractiveQuery() {
        super("greetings-statestore", getOne, getAll);
    }

}