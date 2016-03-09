package com.example.daniele.todos;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.daniele.todoevent.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TodosAdapter extends RecyclerView.Adapter<TodosAdapter.TodoViewHolder> {

    private final TodoClickListener listener;

    private Todos todos = new InMemoryTodos();

    public TodosAdapter(TodoClickListener listener) {
        this.listener = listener;
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.todo_item, parent, false);

        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, int position) {
        holder.bind(todos.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return todos.count();
    }

    public void updateWith(Todos todos) {
        this.todos = todos;
        notifyDataSetChanged();
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.todo_name)
        TextView todoName;

        @Bind(R.id.edit_todo)
        ImageButton editTodo;

        @Bind(R.id.delete_todo)
        ImageButton deleteTodo;

        public TodoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Todo todo, final TodoClickListener listener) {
            todoName.setText(todo.name);
            editTodo.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onEditTodoClicked(todo);
                        }
                    }
            );
            deleteTodo.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onDeleteTodoClicked(todo);
                        }
                    }
            );
        }
    }

    interface TodoClickListener {
        void onEditTodoClicked(Todo todo);
        void onDeleteTodoClicked(Todo todo);
    }
}
