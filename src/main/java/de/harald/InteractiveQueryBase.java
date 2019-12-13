package de.harald;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class InteractiveQueryBase<K, V> {

    private String stateStoreName;
    private BiFunction<URL, K, V> getOne;
    private Function<URL, List<V>> getAll;

    public InteractiveQueryBase(String stateStoreName, BiFunction<URL, K, V> getOne, Function<URL, List<V>> getAll) {
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
            return this.getAll.apply(new URL("http://localhost:8080"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    logic for kafka-streams interactive queries not included for simplicity
    */
}