package de.devfest.screens.speakers;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

import de.devfest.R;
import de.devfest.databinding.ListitemSpeakerBinding;
import de.devfest.model.Speaker;

class SpeakerAdapter extends RecyclerView.Adapter<SpeakerAdapter.SpeakerViewHolder> {

    private final List<Speaker> speakers;

    public SpeakerAdapter() {
        speakers = new LinkedList<>();
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
        // TODO: add some logic to avoid multiple items
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
}
