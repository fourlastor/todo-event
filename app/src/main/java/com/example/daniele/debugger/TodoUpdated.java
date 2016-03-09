package com.example.daniele.debugger;

public class TodoUpdated implements DebuggerEvent {
    private final String id;
    private final String name;

    public TodoUpdated(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String message() {
        return String.format("Todo with id %s updated to %s", id, name);
    }

    @Override
    public int color() {
        return 0xff0000ff;
    }
}
