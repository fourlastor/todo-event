package com.example.daniele.debugger;

import android.support.annotation.ColorInt;

public interface DebuggerEvent {
    String id();
    String message();
    @ColorInt int color();
}
