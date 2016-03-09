package com.example.daniele.todos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.daniele.todoevent.R;

public class TodoDialog extends DialogFragment {

    private Listener listener;

    public static TodoDialog newTodo() {
        return new TodoDialog();
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

        return builder
                .setTitle("New Todo")
                .setView(view)
                .setPositiveButton("ok", onOkPressed(view))
                .setNegativeButton("cancel", onCancelPressed())
                .create();
    }

    private DialogInterface.OnClickListener onOkPressed(View view) {

        final EditText todoName = (EditText) view.findViewById(R.id.todo_name);

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
    }
}
