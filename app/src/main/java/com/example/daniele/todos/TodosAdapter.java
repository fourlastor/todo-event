package com.example.daniele.todos;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TodosAdapter extends RecyclerView.Adapter<TodosAdapter.TodoViewHolder> {

    private static DateFormat dateFormat = DateFormat.getDateInstance();

    private Todos todos = new Todos();

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);

        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, int position) {
                     holder.bind(todos.get(position));
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

        @Bind(android.R.id.text1)
        TextView todoName;

        public TodoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Todo todo) {
            String text = String.format("%s: %s", todo.name, dateFormat.format(todo.createdAt));
            todoName.setText(text);
        }
    }
}
