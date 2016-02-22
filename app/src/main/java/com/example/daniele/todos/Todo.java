package com.example.daniele.todos;

import java.util.Date;

public class Todo {
    public final String name;
    public final Date createdAt;

    public Todo(String name, Date createdAt) {
        this.name = name;
        this.createdAt = createdAt;
    }
}
