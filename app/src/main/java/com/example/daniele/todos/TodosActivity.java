package com.example.daniele.todos;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.daniele.Projector;
import com.example.daniele.db.BriteDatabaseSingleton;
import com.example.daniele.db.DB;
import com.example.daniele.event.EventRepository;
import com.example.daniele.event.EventService;
import com.example.daniele.event.EventType;
import com.example.daniele.proto.todo.InsertTodo;
import com.example.daniele.todoevent.R;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class TodosActivity extends AppCompatActivity implements TodoDialog.Listener {

    @Bind(R.id.todo_list)
    RecyclerView todoList;

    private EventRepository eventRepository;
    private TodosAdapter todosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todos);
        ButterKnife.bind(this);

        eventRepository = new EventRepository(BriteDatabaseSingleton.getInstance(this));
        todoList.setLayoutManager(new LinearLayoutManager(this));
        todosAdapter = new TodosAdapter();
        todoList.setAdapter(todosAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        eventRepository.getEvents()
                .map(toTodoList())
                .subscribe(onNewTodosAvailable());

        findViewById(R.id.add_todo).setOnClickListener(addTodo());
    }

    @Override
    public void onCreateTodo(String name) {
        EventService.addTodo(TodosActivity.this, name);
    }

    private Action1<Todos> onNewTodosAvailable() {
        return new Action1<Todos>() {
            @Override
            public void call(Todos todos) {
                todosAdapter.updateWith(todos);
            }
        };
    }

    private Projector<Todos> toTodoList() {
        return new Projector<Todos>() {
            @Override
            public Todos call(List<DB.Event> events) {
                Todos todos = new Todos();

                for (DB.Event event : events) {
                    if (event.getEventType() == EventType.ADD_TODO) {
                        try {
                            InsertTodo insertTodo = InsertTodo.ADAPTER.decode(event.getData());

                            todos.add(new Todo(insertTodo.name, new Date((long) event.getCreatedAt())));
                        } catch (IOException e) {
                            throw new RuntimeException("Marshalling failed");
                        }
                    }
                }

                return todos;
            }
        };
    }

    private View.OnClickListener addTodo() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment todoDialog = TodoDialog.newTodo();

                todoDialog.show(getFragmentManager(), "TodoDialogFragment");
            }
        };
    }
}
