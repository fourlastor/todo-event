package com.example.daniele.event;

import android.database.Cursor;

import com.example.daniele.db.DB;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.example.daniele.db.DB.Tables.Event;

public class EventRepository {

    private final BriteDatabase db;
    private final EventPreferences preferences;

    public EventRepository(BriteDatabase db, EventPreferences preferences) {
        this.db = db;
        this.preferences = preferences;
    }

    public Observable<List<DB.Event>> getEvents() {
        return getAllEvents()
                .map(limitToPlayhead());
    }

    private Func1<List<DB.Event>, List<DB.Event>> limitToPlayhead() {
        return new Func1<List<DB.Event>, List<DB.Event>>() {
            @Override
            public List<DB.Event> call(List<DB.Event> events) {
                String playhead = preferences.getPlayhead();
                if (playhead.isEmpty()) {
                    return events;
                }

                List<DB.Event> limitedEvents = new ArrayList<>();

                for (DB.Event event : events) {
                    limitedEvents.add(event);
                    if (event.getEventId().equals(playhead)) {
                        break;
                    }
                }

                return limitedEvents;
            }
        };
    }

    public Observable<List<DB.Event>> getAllEvents() {
        QueryObservable query = db.createQuery(Event, "SELECT * FROM " + Event + " ORDER BY " + DB.Columns.Event.CreatedAt + " ASC");

        return query.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(toEventList());
    }

    private Func1<SqlBrite.Query, List<DB.Event>> toEventList() {
        return new Func1<SqlBrite.Query, List<DB.Event>>() {
            @Override
            public List<DB.Event> call(SqlBrite.Query query) {
                try (Cursor cursor = query.run()) {
                    List<DB.Event> events = new ArrayList<>(cursor.getCount());

                    while (cursor.moveToNext()) {
                       events.add(DB.Event.fromCursor(cursor));
                   }

                    return events;
                }
            }
        };
    }
}
