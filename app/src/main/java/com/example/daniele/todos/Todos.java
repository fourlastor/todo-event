package com.example.daniele.todos;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Todos {
    private final List<Todo> todos = new CopyOnWriteArrayList<>();

    public void add(Todo todo) {
        todos.add(0, todo);
    }

    public int count() {
        return todos.size();
    }

    public Todo get(int index) {
        return todos.get(index);
    }

    public void update(String id, String name) {
        for (int i = 0; i < todos.size(); i++) {
            Todo todo = todos.get(i);

            if (todo.id.equals(id)) {
                todos.set(i, new Todo(id, name, todo.createdAt));

                return;
            }
        }
    }

}
