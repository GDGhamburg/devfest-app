package de.devfest.screens.sessiondetails;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import de.devfest.R;
import de.devfest.databinding.ItemSessionSpeakerBinding;
import de.devfest.model.Speaker;

public class SessionSpeakerAdapter extends RecyclerView.Adapter<SessionSpeakerAdapter.ViewHolder> {

    private View.OnClickListener itemClickerListener;
    private List<Speaker> speakers;

    public SessionSpeakerAdapter(List<Speaker> speakers, View.OnClickListener itemClickListener) {
        this.itemClickerListener = itemClickListener;
        this.speakers = speakers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_session_speaker, parent, false);
        view.setOnClickListener(itemClickerListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(speakers.get(position));
    }

    @Override
    public int getItemCount() {
        return speakers.size();
    }

    public Speaker getSpeaker(int position) {
        return speakers.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ItemSessionSpeakerBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void bind(Speaker speaker) {
            Glide.with(binding.getRoot().getContext()).load(speaker.photoUrl).placeholder(R.drawable.ic_person)
                    .into(binding.imageSpeaker);
            binding.textSpeakerName.setText(speaker.name);
            binding.textSpeakerCompany.setText(speaker.company);
        }
    }
}
