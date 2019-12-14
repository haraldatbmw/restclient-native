package de.harald;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;

public abstract class InteractiveQueryBase<K, V> {

    private static final Logger LOGGER = Logger.getLogger(InteractiveQueryBase.class);

    private String stateStoreName;
    private BiFunction<URL, K, V> getOne;
    private Function<URL, CompletionStage<List<V>>> getAll;

    public InteractiveQueryBase(String stateStoreName, BiFunction<URL, K, V> getOne, Function<URL, CompletionStage<List<V>>> getAll) {
        this.stateStoreName = stateStoreName;
        this.getOne = getOne;
        this.getAll = getAll;
    }

    public V getOneFromRemote(K id) {
        try {
            return this.getOne.apply(new URL("http://localhost:8080"), id);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<V> getAllFromRemote() {
        try {
            LOGGER.info("getAllFromRemote - start");
            List<URL> allServer = Arrays.asList(
                new URL("http://localhost:8080"), 
                new URL("http://localhost:8080"), 
                new URL("http://localhost:8080"));
            List<V> result = allServer
                .parallelStream()
                .map(x -> this.getAll.apply(x).toCompletableFuture())
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
            LOGGER.info("getAllFromRemote - finished");
            return result;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    logic for kafka-streams interactive queries not included for simplicity
    */
}