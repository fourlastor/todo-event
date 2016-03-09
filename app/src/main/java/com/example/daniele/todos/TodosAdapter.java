package com.example.daniele.todos;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.daniele.todoevent.R;

import java.text.DateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TodosAdapter extends RecyclerView.Adapter<TodosAdapter.TodoViewHolder> {

    private static DateFormat dateFormat = DateFormat.getDateInstance();

    private final TodoClickListener listener;

    private Todos todos = new Todos();

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

        public TodoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Todo todo, final TodoClickListener listener) {
            String text = String.format("%s: %s", todo.name, dateFormat.format(todo.createdAt));
            todoName.setText(text);
            editTodo.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onTodoClicked(todo);
                        }
                    }
            );
        }
    }

    interface TodoClickListener {
        void onTodoClicked(Todo todo);
    }
}
