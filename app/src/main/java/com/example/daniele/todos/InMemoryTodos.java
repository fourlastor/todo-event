package com.example.daniele.todos;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class InMemoryTodos implements Todos {
    private final List<Todo> todos = new CopyOnWriteArrayList<>();

    @Override
    public void add(Todo todo) {
        todos.add(0, todo);
    }

    @Override
    public int count() {
        return todos.size();
    }

    @Override
    public Todo get(int index) {
        return todos.get(index);
    }

    @Override
    public void update(String id, String name) {
        for (int i = 0; i < todos.size(); i++) {
            Todo todo = todos.get(i);

            if (todo.id.equals(id)) {
                todos.set(i, new Todo(id, name, todo.createdAt));

                return;
            }
        }
    }

    @Override
    public void delete(String id) {
        for (int i = 0; i < todos.size(); i++) {
            Todo todo = todos.get(i);

            if (todo.id.equals(id)) {
                todos.remove(i);

                return;
            }
        }
    }

}
