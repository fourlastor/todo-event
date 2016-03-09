package com.example.daniele.todos;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Todos {
    private final List<Todo> todos = new CopyOnWriteArrayList<>();

    public void add(Todo todo) {
        todos.add(todo);
    }

    public int count() {
        return todos.size();
    }

    public Todo get(int index) {
        return todos.get(index);
    }
}
