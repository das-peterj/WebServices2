package com.iths.models;

public class Todo {
    public String fornamn;
    public String efternamn;
    public boolean completed;

    public Todo(String fornamn, String efternamn, boolean completed) {
        this.fornamn = fornamn;
        this.efternamn = efternamn;
        this.completed = completed;
    }
}

