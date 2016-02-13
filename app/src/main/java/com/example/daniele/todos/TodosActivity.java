package com.example.daniele.todos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.daniele.db.BriteDatabaseSingleton;
import com.example.daniele.event.EventRepository;
import com.example.daniele.event.EventService;
import com.example.daniele.todoevent.R;

import rx.functions.Action1;

public class TodosActivity extends AppCompatActivity {

    private EventRepository eventRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todos);

        eventRepository = new EventRepository(BriteDatabaseSingleton.getInstance(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        eventRepository.getEventCount()
                .subscribe(onNewCountAvailable());

        findViewById(R.id.add_todo).setOnClickListener(addTodo());
    }

    private View.OnClickListener addTodo() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventService.addTodo(TodosActivity.this, "todo");
            }
        };
    }

    @NonNull
    private Action1<Integer> onNewCountAvailable() {
        return new Action1<Integer>() {
            @Override
            public void call(Integer count) {
                displayCount(count);
            }
        };
    }

    private void displayCount(Integer count) {
        Toast.makeText(this, String.format("%d events in the database", count), Toast.LENGTH_SHORT).show();
    }
}
