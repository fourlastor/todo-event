package com.example.daniele.todos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.daniele.db.BriteDatabaseSingleton;
import com.example.daniele.db.DB;
import com.example.daniele.event.EventRepository;
import com.example.daniele.event.EventService;
import com.example.daniele.event.EventType;
import com.example.daniele.proto.todo.InsertTodo;
import com.example.daniele.todoevent.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;
import rx.functions.Func1;

public class TodosActivity extends AppCompatActivity {

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

    private Action1<List<Todo>> onNewTodosAvailable() {
        return new Action1<List<Todo>>() {
            @Override
            public void call(List<Todo> todos) {
                 todosAdapter.updateWith(todos);
            }
        };
    }

    private Func1<List<DB.Event>, List<Todo>> toTodoList() {
        return new Func1<List<DB.Event>, List<Todo>>() {
            @Override
            public List<Todo> call(List<DB.Event> events) {
                List<Todo> todos = new ArrayList<>();

                for (DB.Event event : events) {
                    if (event.getEventType() == EventType.ADD_TODO) {
                        try {
                            InsertTodo insertTodo = InsertTodo.ADAPTER.decode(event.getData());

                            todos.add(new Todo(insertTodo.name, new Date(event.getTime())));
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
                EventService.addTodo(TodosActivity.this, "todo");
            }
        };
    }
}
