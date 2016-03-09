package com.example.daniele.debugger;

public class EmptyDebuggerEvents implements DebuggerEvents {
    @Override
    public String playhead() {
        throw new UnsupportedOperationException("No playhead in EmptyDebuggerEvents");
    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public void add(DebuggerEvent debuggerEvent) {

    }

    @Override
    public DebuggerEvent get(int position) {
        throw new IndexOutOfBoundsException("EmptyDebuggerEvents doesn't contain events");
    }
}
