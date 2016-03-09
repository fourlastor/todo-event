package com.example.daniele.event;

import android.content.Context;
import android.content.SharedPreferences;

public class EventPreferences {
    private static final String KEY_PLAYHEAD_ID = "playhead_id";
    private final SharedPreferences preferences;

    public EventPreferences(Context context) {
        preferences = context.getApplicationContext()
                .getSharedPreferences("event_prefs", Context.MODE_PRIVATE);
    }

    public void storePlayhead(String eventId) {
        preferences.edit()
                .putString(KEY_PLAYHEAD_ID, eventId)
                .apply();
    }

    public String getPlayhead() {
        return preferences.getString(KEY_PLAYHEAD_ID, "");
    }
}
