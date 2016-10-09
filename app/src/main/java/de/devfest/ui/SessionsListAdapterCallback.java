package de.devfest.ui;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;

import de.devfest.model.ScheduleSession;

public class SessionsListAdapterCallback extends SortedListAdapterCallback<ScheduleSession> {

    public SessionsListAdapterCallback(RecyclerView.Adapter adapter) {
        super(adapter);
    }

    @Override
    public int compare(ScheduleSession session1, ScheduleSession session2) {
        return session1.session.startTime.compareTo(session2.session.startTime);
    }

    @Override
    public boolean areContentsTheSame(ScheduleSession oldItem, ScheduleSession newItem) {
        return oldItem.session.id.equals(newItem.session.id);
    }

    @Override
    public boolean areItemsTheSame(ScheduleSession item1, ScheduleSession item2) {
        return item1.session.id.equals(item2.session.id);
    }
}
