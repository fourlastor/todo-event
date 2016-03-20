package com.example.daniele.debugger;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.example.daniele.db.BriteDatabaseSingleton;
import com.example.daniele.db.DB;
import com.example.daniele.event.EventPreferences;
import com.example.daniele.event.EventRepository;
import com.example.daniele.event.EventType;
import com.example.daniele.proto.todo.CreateTodo;
import com.example.daniele.proto.todo.DeleteTodo;
import com.example.daniele.proto.todo.UpdateTodo;
import com.example.daniele.todoevent.R;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func2;

public abstract class DebuggerActivity extends AppCompatActivity {

    private FloatingActionButton playPauseButton;
    private FrameLayout mainContent;

    private EventRepository eventRepository;
    private DebuggerEventsAdapter adapter;
    private EventPreferences eventPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_debugger);

        RecyclerView eventList = (RecyclerView) findViewById(R.id.event_list);
        playPauseButton = (FloatingActionButton) findViewById(R.id.play_pause);
        mainContent = (FrameLayout) findViewById(R.id.main_content);

        eventPreferences = new EventPreferences(this);

        eventList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DebuggerEventsAdapter(onEventClicked());
        eventList.setAdapter(adapter);

        eventRepository = new EventRepository(
                BriteDatabaseSingleton.getInstance(this),
                new EventPreferences(this)
        );

        playPauseButton.setOnClickListener(togglePlayPause());
    }

    @Override
    public void setContentView(int layoutResID) {
        getLayoutInflater().inflate(layoutResID, mainContent, true);
    }

    private View.OnClickListener togglePlayPause() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventPreferences.togglePausedState();
            }
        };
    }

    private DebuggerEventsAdapter.EventClickListener onEventClicked() {
        return new DebuggerEventsAdapter.EventClickListener() {
            @Override
            public void onEventClicked(DebuggerEvent debuggerEvent) {
                storeSelectedEvent(debuggerEvent);
            }
        };
    }

    private void storeSelectedEvent(DebuggerEvent debuggerEvent) {
        eventPreferences.storePlayhead(debuggerEvent.id());
    }

    @Override
    protected void onStart() {
        super.onStart();

        Observable.combineLatest(
                eventRepository.getAllEvents(),
                eventPreferences.observePlayhead(),
                toEvents()
        ).subscribe(onNewEventsAvailable());

        eventPreferences.observabePauseState().subscribe(setPlayPauseButton());
    }

    @NonNull
    private Action1<Boolean> setPlayPauseButton() {
        return new Action1<Boolean>() {
            @Override
            public void call(Boolean paused) {
                int res = paused ? R.drawable.ic_play : R.drawable.ic_pause;
                playPauseButton.setImageResource(res);
            }
        };
    }

    private Func2<List<DB.Event>, String, DebuggerEvents> toEvents() {
        return new Func2<List<DB.Event>, String, DebuggerEvents>() {
            @Override
            public DebuggerEvents call(List<DB.Event> events, String playhead) {
                DebuggerEvents debuggerEvents = new InMemoryDebuggerEvents(playhead, events.size());

                for (DB.Event event : events) {
                    debuggerEvents.add(debuggerEventFrom(event));
                }

                return debuggerEvents;
            }

            private DebuggerEvent debuggerEventFrom(DB.Event event) {
                String eventId = event.getEventId();

                switch (event.getEventType()) {
                    case EventType.CREATE_TODO:
                        try {
                            CreateTodo insert = CreateTodo.ADAPTER.decode(event.getData());
                            return new TodoCreated(eventId, insert.id, insert.name);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    case EventType.UPDATE_TODO:
                        try {
                            UpdateTodo update = UpdateTodo.ADAPTER.decode(event.getData());
                            return new TodoUpdated(eventId, update.id, update.name);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    case EventType.DELETE_TODO:
                        try {
                            DeleteTodo delete = DeleteTodo.ADAPTER.decode(event.getData());
                            return new TodoDeleted(eventId, delete.id);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    default:
                        throw new IllegalArgumentException("Event of type " + event.getEventType() + " not supported");
                }
            }
        };
    }

    private Action1<DebuggerEvents> onNewEventsAvailable() {
        return new Action1<DebuggerEvents>() {
            @Override
            public void call(DebuggerEvents debuggerEvents) {
                adapter.updateWith(debuggerEvents);
            }
        };
    }
}
