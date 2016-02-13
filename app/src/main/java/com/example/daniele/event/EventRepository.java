package com.example.daniele.event;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;
import com.squareup.sqlbrite.SqlBrite;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.example.daniele.db.DB.Tables.Event;

public class EventRepository {

    private final BriteDatabase db;

    public EventRepository(BriteDatabase db) {
        this.db = db;
    }

    public Observable<Integer> getEventCount() {
        QueryObservable query = db.createQuery(Event, "SELECT COUNT(*) FROM " + Event);

        return query.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(marshallToInt());
    }

    @NonNull
    private Func1<SqlBrite.Query, Integer> marshallToInt() {
        return new Func1<SqlBrite.Query, Integer>() {
            @Override
            public Integer call(SqlBrite.Query query) {
                try (Cursor c = query.run()) {
                    c.moveToFirst();
                    return c.getInt(0);
                }
            }
        };
    }
}
