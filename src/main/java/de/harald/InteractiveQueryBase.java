package de.harald;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;

/**
 * Base class for abstraction of fetching distributed data in case of a scaled out kafka-streams interactive query service.
 * @param <K> Type of the key
 * @param <V> Type of the value
 * @param <R> Type of the rest-client
 */
public abstract class InteractiveQueryBase<K, V, R> {

    private static final Logger LOGGER = Logger.getLogger(InteractiveQueryBase.class);

    private String stateStoreName;
    private Map<String, R> restClientMap = new HashMap<>();
    private BiFunction<R, K, V> getOne;
    private Function<R, CompletionStage<List<V>>> getAll;
    private Function<URL, R> getRestClient;


    public InteractiveQueryBase(
        String stateStoreName, 
        Function<URL, R> getRestClient,
        BiFunction<R, K, V> getOne, 
        Function<R, CompletionStage<List<V>>> getAll) {

        this.stateStoreName = stateStoreName;
        this.getRestClient = getRestClient;
        this.getOne = getOne;
        this.getAll = getAll;
    }

    public V getOneFromRemote(K id) {
        R restClient = getRestClientForUrl("http://localhost:8080");
        return this.getOne.apply(restClient, id);
    }

    public List<V> getAllFromRemote() {
        LOGGER.info("getAllFromRemote - start");

        // simulate that the service is scaled out over 3 pods
        List<R> allRestClients = Arrays.asList(
            getRestClientForUrl("http://localhost:8080"), 
            getRestClientForUrl("http://localhost:8080"), 
            getRestClientForUrl("http://localhost:8080"));

        // trigger all async rest calls
        List<CompletableFuture<List<V>>> allFutures = allRestClients.stream()
            .map(r -> this.getAll.apply(r).toCompletableFuture())
            .collect(Collectors.toList());

        // wait for all results
        List<V> result = allFutures.stream()
            .map(CompletableFuture::join)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        LOGGER.info("getAllFromRemote - finished");
        return result;
    }

    private R getRestClientForUrl(String url) {
        try {
            if (!restClientMap.containsKey(url)) {
                R restClient = this.getRestClient.apply(new URL(url));
                restClientMap.put(url, restClient);
            }
            return restClientMap.get(url);
        } catch (MalformedURLException e) {
            LOGGER.error("malformed URL '" + url + "'", e);
            throw new IllegalArgumentException(e);
        }
    }

    /*
    logic for kafka-streams interactive queries not included for simplicity
    */
}