package com.example.daniele.debugger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.daniele.Projector;
import com.example.daniele.db.BriteDatabaseSingleton;
import com.example.daniele.db.DB;
import com.example.daniele.event.EventRepository;
import com.example.daniele.event.EventType;
import com.example.daniele.proto.todo.CreateTodo;
import com.example.daniele.proto.todo.DeleteTodo;
import com.example.daniele.proto.todo.UpdateTodo;
import com.example.daniele.todoevent.R;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class DebuggerActivity extends AppCompatActivity {

    @Bind(R.id.event_list)
    RecyclerView eventList;

    private EventRepository eventRepository;
    private DebuggerEventsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debugger);
        ButterKnife.bind(this);

        eventList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DebuggerEventsAdapter();
        eventList.setAdapter(adapter);

        eventRepository = new EventRepository(BriteDatabaseSingleton.getInstance(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        eventRepository.getEvents()
                .map(toEvents())
                .subscribe(onNewEventsAvailable());
    }

    private Projector<DebuggerEvents> toEvents() {
        return new Projector<DebuggerEvents>() {
            @Override
            public DebuggerEvents call(List<DB.Event> events) {
                DebuggerEvents debuggerEvents = new InMemoryDebuggerEvents(events.size());

                for (DB.Event event : events) {
                    debuggerEvents.add(debuggerEventFrom(event));
                }

                return debuggerEvents;
            }

            private DebuggerEvent debuggerEventFrom(DB.Event event) {
                switch (event.getEventType()) {
                    case EventType.CREATE_TODO:
                        try {
                            CreateTodo insert = CreateTodo.ADAPTER.decode(event.getData());
                            return new TodoCreated(insert.id, insert.name);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    case EventType.UPDATE_TODO:
                        try {
                            UpdateTodo update = UpdateTodo.ADAPTER.decode(event.getData());
                            return new TodoUpdated(update.id, update.name);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    case EventType.DELETE_TODO:
                        try {
                            DeleteTodo delete = DeleteTodo.ADAPTER.decode(event.getData());
                            return new TodoDeleted(delete.id);
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