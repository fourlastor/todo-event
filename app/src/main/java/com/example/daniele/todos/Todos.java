package com.example.daniele.todos;

public interface Todos {
    void add(Todo todo);

    int count();

    Todo get(int index);

    void update(String id, String name);
}
