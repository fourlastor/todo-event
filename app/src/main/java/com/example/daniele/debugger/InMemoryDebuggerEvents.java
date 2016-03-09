package com.example.daniele.debugger;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDebuggerEvents implements DebuggerEvents {

    private final List<DebuggerEvent> events;

    public InMemoryDebuggerEvents(int initialCapacity) {
        events = new ArrayList<>(initialCapacity);
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
