package com.example.daniele.debugger;

public class EmptyDebuggerEvents implements DebuggerEvents {
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
