package com.example.daniele.debugger;

public class TodoCreated implements DebuggerEvent {
    private final String id;
    private final String name;

    public TodoCreated(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String message() {
        return String.format("%s todo created with id %s", name, id);
    }

    @Override
    public int color() {
        return 0xff00ff00;
    }
}
