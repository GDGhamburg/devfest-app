package de.devfest.screens.speakers;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.devfest.R;
import de.devfest.databinding.ListitemSpeakerBinding;
import de.devfest.model.Speaker;

class SpeakerAdapter extends RecyclerView.Adapter<SpeakerAdapter.SpeakerViewHolder> {

    private final SortedList<Speaker> speakers;

    public SpeakerAdapter() {
        speakers = new SortedList<>(Speaker.class, new SortedListUpdate(this));
    }

    @Override
    public SpeakerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SpeakerViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.listitem_speaker, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(SpeakerViewHolder holder, int position) {
        holder.bind(speakers.get(position));
    }

    @Override
    public int getItemCount() {
        return speakers.size();
    }

    public void addSpeaker(@NonNull Speaker speaker) {
        speakers.add(speaker);
    }

    static class SpeakerViewHolder extends RecyclerView.ViewHolder {
        private final ListitemSpeakerBinding binding;

        public SpeakerViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        void bind(Speaker speaker) {
            binding.textSpeakerName.setText(speaker.name);
            binding.textDescription.setText(speaker.description);
        }
    }

    static class SortedListUpdate extends SortedList.Callback<Speaker> {

        private final SpeakerAdapter adapter;

        SortedListUpdate(SpeakerAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public int compare(Speaker speaker1, Speaker speaker2) {
            // TODO
            return 0;
        }

        @Override
        public void onInserted(int position, int count) {
            adapter.notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            adapter.notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            adapter.notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            adapter.notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(Speaker oldItem, Speaker newItem) {
            // TODO
            return false;
        }

        @Override
        public boolean areItemsTheSame(Speaker item1, Speaker item2) {
            // TODO
            return false;
        }
    }
}
