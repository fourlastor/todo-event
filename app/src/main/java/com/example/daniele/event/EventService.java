package com.example.daniele.event;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import com.example.daniele.db.BriteDatabaseSingleton;
import com.example.daniele.db.DB;
import com.example.daniele.db.DB.Event;
import com.example.daniele.proto.todo.InsertTodo;
import com.example.daniele.todoevent.BuildConfig;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.Date;
import java.util.UUID;

public class EventService extends IntentService {
    private static final String ACTION_ADD_TODO = BuildConfig.APPLICATION_ID + ".event.action.ADD_TODO";

    private static final String EXTRA_TODO_NAME = "com.example.daniele.event.extra.TODO_NAME";

    public EventService() {
        super("EventService");
    }

    public static void addTodo(Context context, String todoName) {
        Intent intent = new Intent(context, EventService.class);
        intent.setAction(ACTION_ADD_TODO);
        intent.putExtra(EXTRA_TODO_NAME, todoName);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_ADD_TODO.equals(action)) {
                final String todoName = intent.getStringExtra(EXTRA_TODO_NAME);
                handleAddTodo(todoName);
            }
        }
    }

    private void handleAddTodo(String todoName) {
        BriteDatabase db = BriteDatabaseSingleton.getInstance(this);

        String todoId = UUID.randomUUID().toString();

        InsertTodo insertTodo = new InsertTodo(todoId, todoName);

        String eventId = UUID.randomUUID().toString();
        long time = new Date().getTime();

        ContentValues values = new Event(eventId, EventType.ADD_TODO, time, InsertTodo.ADAPTER.encode(insertTodo))
                .toContentValues();

        db.insert(DB.Tables.Event, values);
    }
}
