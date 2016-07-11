package de.devfest.screens.speakers;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import de.devfest.R;
import de.devfest.databinding.ListitemSpeakerBinding;
import de.devfest.model.Speaker;

import static de.devfest.model.Speaker.TAG_ANDROID;
import static de.devfest.model.Speaker.TAG_CLOUD;
import static de.devfest.model.Speaker.TAG_WEB;

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
            binding.textSpeakerInfo.setText(speaker.company);

            int colorResId = 0;
            if (speaker.tags.contains(TAG_ANDROID)) colorResId = R.color.tag_android_overlay;
            else if (speaker.tags.contains(TAG_WEB)) colorResId = R.color.tag_web_overlay;
            else if (speaker.tags.contains(TAG_CLOUD)) colorResId = R.color.tag_cloud_overlay;
            if (colorResId != 0) binding.viewTag.setBackgroundResource(colorResId);

            if (!TextUtils.isEmpty(speaker.photoUrl)) {
                Glide.with(binding.imageSpeaker.getContext()).load(speaker.photoUrl).into(binding.imageSpeaker);
            } else {
                Glide.clear(binding.imageSpeaker);
            }
        }
    }

    static class SortedListUpdate extends SortedListAdapterCallback<Speaker> {

        SortedListUpdate(RecyclerView.Adapter adapter) {
            super(adapter);
        }

        @Override
        public int compare(Speaker speaker1, Speaker speaker2) {
            return speaker1.name.compareTo(speaker2.name);
        }

        @Override
        public boolean areContentsTheSame(Speaker oldItem, Speaker newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(Speaker item1, Speaker item2) {
            return item1.speakerId.equals(item2.speakerId);
        }
    }
}
