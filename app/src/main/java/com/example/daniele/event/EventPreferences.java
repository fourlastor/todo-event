package com.example.daniele.event;

import android.content.Context;
import android.content.SharedPreferences;

import com.f2prateek.rx.preferences.RxSharedPreferences;

import rx.Observable;

public class EventPreferences {
    private static final java.lang.String KEY_PAUSE_STATE = "paused_state";
    private static final String KEY_PLAYHEAD_ID = "playhead_id";
    private static final String NO_PLAYHEAD = "";
    private static final Boolean NOT_PAUSED = false;
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

    public Observable<String> observePlayhead() {
        return rxSharedPreferences.getString(KEY_PLAYHEAD_ID, NO_PLAYHEAD)
                .asObservable();
    }

    public void togglePausedState() {
        boolean paused = preferences.getBoolean(KEY_PAUSE_STATE, NOT_PAUSED);

        preferences.edit()
                .putBoolean(KEY_PAUSE_STATE, !paused)
                .apply();
    }

    public Observable<Boolean> observabePauseState() {
        return rxSharedPreferences.getBoolean(KEY_PAUSE_STATE, NOT_PAUSED)
                .asObservable();
    }

    public boolean isInPausedState() {
        return preferences.getBoolean(KEY_PAUSE_STATE, NOT_PAUSED);
    }
}
