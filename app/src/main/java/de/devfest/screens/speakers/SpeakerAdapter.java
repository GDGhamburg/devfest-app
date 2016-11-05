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
import de.devfest.databinding.ItemSpeakerBinding;
import de.devfest.model.Speaker;
import de.devfest.ui.TagHelper;

import static de.devfest.ui.UiUtils.CACHED_SPEAKER_IMAGE_SIZE;

class SpeakerAdapter extends RecyclerView.Adapter<SpeakerAdapter.SpeakerViewHolder> {

    private final SortedList<Speaker> speakers;
    private View.OnClickListener itemClickListener;

    public SpeakerAdapter(View.OnClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        speakers = new SortedList<>(Speaker.class, new SortedListUpdate(this));
    }

    @Override
    public SpeakerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_speaker, parent, false);
        view.setOnClickListener(itemClickListener);
        return new SpeakerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SpeakerViewHolder holder, int position) {
        holder.bind(speakers.get(position));
    }

    @Override
    public int getItemCount() {
        return speakers.size();
    }

    public Speaker getSpeaker(int position) {
        return speakers.get(position);
    }

    public void addSpeaker(@NonNull Speaker speaker) {
        speakers.add(speaker);
    }

    static class SpeakerViewHolder extends RecyclerView.ViewHolder {
        private final ItemSpeakerBinding binding;

        public SpeakerViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        void bind(Speaker speaker) {
            binding.textSpeakerName.setText(speaker.name);
            binding.textSpeakerInfo.setText(speaker.company);

            int colorResId = TagHelper.getTagColor(speaker.tags.get(0));
            if (colorResId != 0) binding.viewTag.setBackgroundResource(colorResId);

            if (!TextUtils.isEmpty(speaker.photoUrl)) {
                Glide.with(binding.imageSpeaker.getContext())
                        .load(speaker.photoUrl)
                        .placeholder(R.drawable.ic_devfesthh_grey)
                        .override(CACHED_SPEAKER_IMAGE_SIZE, CACHED_SPEAKER_IMAGE_SIZE)
                        .into(binding.imageSpeaker);
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
