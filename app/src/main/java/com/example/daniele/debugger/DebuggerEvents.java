package com.example.daniele.debugger;

public interface DebuggerEvents {
    String playhead();

    int count();

    void add(DebuggerEvent debuggerEvent);

    DebuggerEvent get(int position);
}
