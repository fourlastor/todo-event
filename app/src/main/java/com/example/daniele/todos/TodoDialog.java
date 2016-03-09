package com.example.daniele.todos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.daniele.todoevent.R;

public class TodoDialog extends DialogFragment {

    private static final String ARG_TODO_ID = "todo.id";
    private static final String ARG_TODO_NAME = "todo.name";
    private static final String ARG_EDIT_MODE = "todo.edit_mode";
    private Listener listener;

    public static TodoDialog newTodo() {
        TodoDialog dialog = new TodoDialog();
        Bundle args = new Bundle();
        args.putBoolean(ARG_EDIT_MODE, false);
        dialog.setArguments(args);

        return dialog;
    }

    public static TodoDialog editTodo(Todo todo) {
        TodoDialog dialog = new TodoDialog();
        Bundle args = new Bundle();
        args.putString(ARG_TODO_ID, todo.id);
        args.putString(ARG_TODO_NAME, todo.name);
        args.putBoolean(ARG_EDIT_MODE, true);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        listener = (Listener) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();

        View view = inflater.inflate(R.layout.todo_dialog, null);

        EditText todoName = (EditText) view.findViewById(R.id.todo_name);

        Bundle arguments = getArguments();


        if (inEditMode(arguments)) {
            todoName.setText(arguments.getString(ARG_TODO_NAME));
        }

        return builder
                .setTitle("New Todo")
                .setView(view)
                .setPositiveButton("ok", onOkPressed(todoName, arguments))
                .setNegativeButton("cancel", onCancelPressed())
                .create();
    }

    private DialogInterface.OnClickListener onOkPressed(final EditText todoName, Bundle arguments) {
        if (inEditMode(arguments)) {
            return updateTodoOnOkPressed(todoName, arguments);
        } else {
            return createTodoOnOkPressed(todoName);
        }
    }

    private boolean inEditMode(Bundle arguments) {
        return arguments.getBoolean(ARG_EDIT_MODE);
    }

    private DialogInterface.OnClickListener updateTodoOnOkPressed(final EditText todoName, Bundle arguments) {
        final String id = arguments.getString(ARG_TODO_ID);

        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onUpdateTodo(id, todoName.getText().toString());
            }
        };
    }

    @NonNull
    private DialogInterface.OnClickListener createTodoOnOkPressed(final EditText todoName) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onCreateTodo(todoName.getText().toString());
            }
        };
    }

    private DialogInterface.OnClickListener onCancelPressed() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TodoDialog.this.getDialog().cancel();
            }
        };
    }

    interface Listener {
        void onCreateTodo(String name);
        void onUpdateTodo(String id, String name);
    }
}
