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
import com.example.daniele.proto.todo.CreateTodo;
import com.example.daniele.proto.todo.DeleteTodo;
import com.example.daniele.proto.todo.UpdateTodo;
import com.example.daniele.todoevent.R;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class TodosActivity extends AppCompatActivity implements TodoDialog.Listener {

    private static final String TODO_DIALOG_FRAGMENT_TAG = "TodoDialogFragment";
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
        todosAdapter = new TodosAdapter(onTodoClicked());
        todoList.setAdapter(todosAdapter);
    }

    private TodosAdapter.TodoClickListener onTodoClicked() {
        return new TodosAdapter.TodoClickListener() {
            @Override
            public void onEditTodoClicked(Todo todo) {
                DialogFragment todoDialog = TodoDialog.editTodo(todo);
                todoDialog.show(getFragmentManager(), TODO_DIALOG_FRAGMENT_TAG);
            }

            @Override
            public void onDeleteTodoClicked(Todo todo) {
                deleteTodo(todo);
            }
        };
    }

    private void deleteTodo(Todo todo) {
        EventService.deleteTodo(this, todo.id);
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
        EventService.createTodo(TodosActivity.this, name);
    }

    @Override
    public void onUpdateTodo(String id, String name) {
        EventService.updateTodo(TodosActivity.this, id, name);
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
                Todos todos = new InMemoryTodos();

                for (DB.Event event : events) {
                    switch (event.getEventType()) {
                        case EventType.CREATE_TODO:
                            try {
                                CreateTodo insert = CreateTodo.ADAPTER.decode(event.getData());
                                todos.add(new Todo(insert.id, insert.name, new Date((long) event.getCreatedAt())));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case EventType.UPDATE_TODO:
                            try {
                                UpdateTodo update = UpdateTodo.ADAPTER.decode(event.getData());
                                todos.update(update.id, update.name);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case EventType.DELETE_TODO:
                            try {
                                DeleteTodo update = DeleteTodo.ADAPTER.decode(event.getData());
                                todos.delete(update.id);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;
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

                todoDialog.show(getFragmentManager(), TODO_DIALOG_FRAGMENT_TAG);
            }
        };
    }
}
