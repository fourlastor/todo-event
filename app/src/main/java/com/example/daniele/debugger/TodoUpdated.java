package com.example.daniele.debugger;

public class TodoUpdated implements DebuggerEvent {
    private final String eventId;
    private final String id;
    private final String name;

    public TodoUpdated(String eventId, String id, String name) {
        this.eventId = eventId;
        this.id = id;
        this.name = name;
    }

    @Override
    public String id() {
        return eventId;
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
