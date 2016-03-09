package com.example.daniele.event;

import android.content.Context;
import android.content.SharedPreferences;

import com.f2prateek.rx.preferences.RxSharedPreferences;

import rx.Observable;

public class EventPreferences {
    private static final String KEY_PLAYHEAD_ID = "playhead_id";
    private static final String NO_PLAYHEAD = "";
    private final SharedPreferences preferences;
    private final RxSharedPreferences rxSharedPreferences;

    public EventPreferences(Context context) {
        preferences = context.getApplicationContext()
                .getSharedPreferences("event_prefs", Context.MODE_PRIVATE);

        rxSharedPreferences = RxSharedPreferences.create(preferences);
    }

    public void storePlayhead(String eventId) {
        preferences.edit()
                .putString(KEY_PLAYHEAD_ID, eventId)
                .apply();
    }

    public String getPlayhead() {
        return preferences.getString(KEY_PLAYHEAD_ID, NO_PLAYHEAD);
    }

    public Observable<String> observePlayhead() {
        return rxSharedPreferences.getString(KEY_PLAYHEAD_ID, NO_PLAYHEAD)
                .asObservable();
    }
}
