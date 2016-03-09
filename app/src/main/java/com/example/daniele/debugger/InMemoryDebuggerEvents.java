package com.example.daniele.debugger;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDebuggerEvents implements DebuggerEvents {

    private final String playhead;
    private final List<DebuggerEvent> events;

    public InMemoryDebuggerEvents(String playhead, int initialCapacity) {
        this.playhead = playhead;
        this.events = new ArrayList<>(initialCapacity);
    }

    @Override
    public String playhead() {
        return playhead;
    }

    @Override
    public int count() {
        return events.size();
    }

    @Override
    public void add(DebuggerEvent debuggerEvent) {
        events.add(0, debuggerEvent);
    }

    @Override
    public DebuggerEvent get(int position) {
        return events.get(position);
    }
}
