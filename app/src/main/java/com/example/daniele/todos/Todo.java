package com.example.daniele.todos;

import java.util.Date;

public class Todo {
    public final String id;
    public final String name;
    public final Date createdAt;

    public Todo(String id, String name, Date createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }
}
