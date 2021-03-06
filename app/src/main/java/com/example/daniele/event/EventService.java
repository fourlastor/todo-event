package com.example.daniele.event;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.daniele.db.BriteDatabaseSingleton;
import com.example.daniele.db.DB;
import com.example.daniele.db.DB.Event;
import com.example.daniele.proto.todo.CreateTodo;
import com.example.daniele.proto.todo.DeleteTodo;
import com.example.daniele.proto.todo.UpdateTodo;
import com.example.daniele.todoevent.BuildConfig;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.Date;
import java.util.UUID;

public class EventService extends IntentService {
    private static final String ACTION_CREATE_TODO = BuildConfig.APPLICATION_ID + ".event.action.CREATE_TODO";
    private static final String ACTION_UPDATE_TODO = BuildConfig.APPLICATION_ID + ".event.action.UPDATE_TODO";
    private static final String ACTION_DELETE_TODO = BuildConfig.APPLICATION_ID + ".event.action.DELETE_TODO";

    private static final String EXTRA_TODO_ID = "com.example.daniele.event.extra.TODO_ID";
    private static final String EXTRA_TODO_NAME = "com.example.daniele.event.extra.TODO_NAME";
    private BriteDatabase db;
    private EventPreferences preferences;

    public EventService() {
        super("EventService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        db = BriteDatabaseSingleton.getInstance(this);
        preferences = new EventPreferences(this);
    }

    public static void createTodo(Context context, String todoName) {
        Intent intent = new Intent(context, EventService.class);
        intent.setAction(ACTION_CREATE_TODO);
        intent.putExtra(EXTRA_TODO_NAME, todoName);
        context.startService(intent);
    }

    public static void updateTodo(Context context, String id, String name) {
        Intent intent = new Intent(context, EventService.class);
        intent.setAction(ACTION_UPDATE_TODO);
        intent.putExtra(EXTRA_TODO_ID, id);
        intent.putExtra(EXTRA_TODO_NAME, name);
        context.startService(intent);
    }

    public static void deleteTodo(Context context, String id) {
        Intent intent = new Intent(context, EventService.class);
        intent.setAction(ACTION_DELETE_TODO);
        intent.putExtra(EXTRA_TODO_ID, id);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (preferences.isInPausedState()) {
            return;
        }

        if (intent != null) {
            final String action = intent.getAction();

            Bundle args = intent.getExtras();

            switch (action) {
                case ACTION_CREATE_TODO:
                    handleAddTodo(args);
                    break;
                case ACTION_UPDATE_TODO:
                    handleUpdateTodo(args);
                    break;
                case ACTION_DELETE_TODO:
                    handleDeleteTodo(args);
                    break;
            }
        }
    }

    private void handleAddTodo(Bundle args) {
        String id = UUID.randomUUID().toString();
        String name = args.getString(EXTRA_TODO_NAME);
        CreateTodo insertTodo = new CreateTodo(id, name);

        persistEvent(CreateTodo.ADAPTER.encode(insertTodo), EventType.CREATE_TODO);
    }

    private void handleUpdateTodo(Bundle args) {
        String id = args.getString(EXTRA_TODO_ID);
        String name = args.getString(EXTRA_TODO_NAME);
        UpdateTodo insertTodo = new UpdateTodo(id, name);

        persistEvent(UpdateTodo.ADAPTER.encode(insertTodo), EventType.UPDATE_TODO);
    }

    private void handleDeleteTodo(Bundle args) {
        String id = args.getString(EXTRA_TODO_ID);
        DeleteTodo insertTodo = new DeleteTodo(id);

        persistEvent(DeleteTodo.ADAPTER.encode(insertTodo), EventType.DELETE_TODO);
    }

    private void persistEvent(byte[] encodedEvent, int eventType) {
        String eventId = UUID.randomUUID().toString();
        long time = new Date().getTime();

        ContentValues values = new Event(eventId, eventType, time, encodedEvent)
                .toContentValues();

        db.insert(DB.Tables.Event, values);
    }
}
