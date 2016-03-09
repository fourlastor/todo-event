package com.example.daniele.debugger;

public class TodoCreated implements DebuggerEvent {
    private final String eventId;
    private final String id;
    private final String name;

    public TodoCreated(String eventId, String id, String name) {
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
        return String.format("%s todo created with id %s", name, id);
    }

}
