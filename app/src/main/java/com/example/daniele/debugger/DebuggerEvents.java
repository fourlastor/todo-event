package com.example.daniele.debugger;

public interface DebuggerEvents {
    int count();

    void add(DebuggerEvent debuggerEvent);

    DebuggerEvent get(int position);
}
