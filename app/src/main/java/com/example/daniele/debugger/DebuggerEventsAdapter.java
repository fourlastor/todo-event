package com.example.daniele.debugger;

import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.daniele.todoevent.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DebuggerEventsAdapter extends RecyclerView.Adapter<DebuggerEventsAdapter.EventViewHolder> {

    private DebuggerEvents debuggerEvents = new EmptyDebuggerEvents();

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.debugger_event_item, parent, false);

        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        holder.bind(debuggerEvents.get(position));
    }

    @Override
    public int getItemCount() {
        return debuggerEvents.count();
    }

    public void updateWith(DebuggerEvents debuggerEvents) {
        this.debuggerEvents = debuggerEvents;
        notifyDataSetChanged();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.event_icon)
        ImageView eventIcon;

        @Bind(R.id.event_message)
        TextView eventMessage;

        public EventViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bind(DebuggerEvent debuggerEvent) {
            eventMessage.setText(debuggerEvent.message());
            DrawableCompat.setTint(eventIcon.getDrawable(), debuggerEvent.color());
        }
    }
}
