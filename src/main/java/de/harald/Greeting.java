package de.harald;

public class Greeting {

    public Long id;
    public String text;
    public String name;

    public Greeting() {
    }

    public Greeting(Long id, String text, String name) {
        this.id = id;
        this.text = text;
        this.name = name;
    }
}