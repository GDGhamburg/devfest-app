package de.devfest.screens.eventpart;

import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import de.devfest.R;
import de.devfest.databinding.ItemSessionBinding;
import de.devfest.model.Session;
import de.devfest.model.Speaker;
import de.devfest.ui.UiUtils;

public class SessionsAdapter extends RecyclerView.Adapter<SessionsAdapter.SessionViewHolder> {

    private List<Session> sessions = new ArrayList<>(0);
    private DateTimeFormatter sessionStartFormat;

    public SessionsAdapter() {
        sessionStartFormat = UiUtils.getSessionStartFormat();
    }

    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSessionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_session, parent, false);
        return new SessionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(SessionViewHolder holder, int position) {
        Session session = sessions.get(position);
        Speaker speaker = session.speaker.get(0);
        holder.binding.textSessionTitle.setText(session.title);
        holder.binding.imageSession.setImageDrawable(
                UiUtils.getCircledTrackIcon(holder.itemView.getContext(), speaker.tags));
        holder.binding.textSessionTitle.setText(session.title);
        holder.binding.textSessionSub.setText(session.startTime.format(sessionStartFormat));
        int color = ContextCompat.getColor(holder.itemView.getContext(), UiUtils.getTagOverlayColor(speaker.tags));
        holder.binding.containerSessionForeground.setBackgroundColor(color);
        Glide.with(holder.itemView.getContext())
                .load(speaker.photoUrl)
//                .transform(new FaceCenterCrop())
                .into(holder.binding.imageSessionBackground);
    }

    @Override
    public long getItemId(int position) {
        return sessions.get(position).id.hashCode();
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    public void addSession(Session session) {
        sessions.add(session);
        notifyDataSetChanged();
    }

    public static class SessionViewHolder extends RecyclerView.ViewHolder {

        public final ItemSessionBinding binding;

        public SessionViewHolder(ItemSessionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
