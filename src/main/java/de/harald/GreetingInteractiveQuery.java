package de.harald;

import java.net.URL;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.rest.client.RestClientBuilder;

@ApplicationScoped
public class GreetingInteractiveQuery extends InteractiveQueryBase<Long, String> {

    static final BiFunction<URL, Long, String> getOne = (url, id) -> {
        GreetingService srv = RestClientBuilder.newBuilder()
            .baseUrl(url)
            .build(GreetingService.class);
        return srv.getOne(id);
    };

    static final Function<URL, List<String>> getAll = (url) -> {
        GreetingService srv = RestClientBuilder.newBuilder()
            .baseUrl(url)
            .build(GreetingService.class);
        return srv.getAll();
    };

    public GreetingInteractiveQuery() {
        super("greetings-statestore", getOne, getAll);
    }

}