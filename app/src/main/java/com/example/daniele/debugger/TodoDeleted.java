package com.example.daniele.debugger;

public class TodoDeleted implements DebuggerEvent {
    private final String eventId;
    private final String id;

    public TodoDeleted(String eventId, String id) {
        this.eventId = eventId;
        this.id = id;
    }

    @Override
    public String id() {
        return eventId;
    }

    @Override
    public String message() {
        return String.format("Deleted todo with id %s", id);
    }

    @Override
    public int color() {
        return 0xffff0000;
    }
}
