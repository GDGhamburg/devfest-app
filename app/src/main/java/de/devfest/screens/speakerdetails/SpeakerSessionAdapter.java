package de.devfest.screens.speakerdetails;

import android.databinding.DataBindingUtil;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;

import de.devfest.R;
import de.devfest.databinding.ItemSessionBinding;
import de.devfest.model.Session;
import de.devfest.ui.UiUtils;

public class SpeakerSessionAdapter extends
        RecyclerView.Adapter<SpeakerSessionAdapter.SpeakerSessionViewHolder> {

    private final SortedList<Session> list;

    public SpeakerSessionAdapter() {
        this.list = new SortedList<>(Session.class, new SessionSorter(this));
    }


    @Override
    public SpeakerSessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_session, parent, false);
        ItemSessionBinding binding = DataBindingUtil.bind(view);
        return new SpeakerSessionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(SpeakerSessionViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addSession(Session session) {
        list.add(session);
    }

    public static class SpeakerSessionViewHolder extends RecyclerView.ViewHolder {
        private final ItemSessionBinding binding;

        public SpeakerSessionViewHolder(ItemSessionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Session session) {
            binding.imageSession.setImageDrawable(
                    UiUtils.getCircledTrackIcon(binding.getRoot().getContext(),
                            Collections.singletonList(session.track.id)));
            binding.textSessionTitle.setText(session.title);
            binding.textSessionSub.setText(
                    session.startTime.format(UiUtils.getSessionStartFormat()));
        }
    }

    private static class SessionSorter extends SortedListAdapterCallback<Session> {
        public SessionSorter(RecyclerView.Adapter adapter) {
            super(adapter);
        }

        @Override
        public int compare(Session o1, Session o2) {
            return o1.startTime.compareTo(o2.startTime);
        }

        @Override
        public boolean areContentsTheSame(Session oldItem, Session newItem) {
            // TODO
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(Session item1, Session item2) {
            return item1.id.equals(item2.id);
        }
    }
}
