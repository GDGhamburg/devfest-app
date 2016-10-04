package de.devfest.screens.eventpart;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.rohitarya.glide.facedetection.transformation.FaceCenterCrop;

import org.threeten.bp.format.DateTimeFormatter;

import de.devfest.R;
import de.devfest.databinding.ItemSessionBinding;
import de.devfest.model.Session;
import de.devfest.model.Speaker;
import de.devfest.ui.UiUtils;

public class SessionsAdapter extends RecyclerView.Adapter<SessionsAdapter.SessionViewHolder> {

    private final SortedList<Session> sessions;
    private final DateTimeFormatter sessionStartFormat;
    private final View.OnClickListener addClickListener;

    public SessionsAdapter() {
        sessionStartFormat = UiUtils.getSessionStartFormat();
        sessions = new SortedList<>(Session.class, new SessionsListUpdate(this));
        addClickListener = view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) ((ImageButton) view).getDrawable();
                drawable.start();
            }
        };
    }

    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSessionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_session, parent, false);
        binding.buttonAdd.setOnClickListener(addClickListener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.buttonAdd.setImageResource(R.drawable.avd_add);
        }
        return new SessionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(SessionViewHolder holder, int position) {
        Session session = sessions.get(position);
        Speaker speaker = session.speaker.get(0);
        holder.binding.textSessionTitle.setText(session.title);
        holder.binding.imageSession.setImageDrawable(
                UiUtils.getCircledTrackIcon(holder.itemView.getContext(), speaker.tags, false));
        holder.binding.textSessionTitle.setText(session.title);
        holder.binding.textSessionSub.setText(session.startTime.format(sessionStartFormat));
        int overlayColor = ContextCompat.getColor(holder.itemView.getContext(), UiUtils.getTagOverlayColor(speaker.tags));
        holder.binding.containerSessionForeground.setBackgroundColor(overlayColor);
        int darkColor = ContextCompat.getColor(holder.itemView.getContext(), UiUtils.getTagDarkColor(speaker.tags));
        Glide.with(holder.itemView.getContext())
                .load(speaker.photoUrl)
                .transform(new FaceCenterCrop())
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
    }

    public static class SessionViewHolder extends RecyclerView.ViewHolder {

        public final ItemSessionBinding binding;

        public SessionViewHolder(ItemSessionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private class SessionsListUpdate extends SortedListAdapterCallback<Session> {

        public SessionsListUpdate(RecyclerView.Adapter adapter) {
            super(adapter);
        }

        @Override
        public int compare(Session session1, Session session2) {
            return session1.startTime.compareTo(session2.startTime);
        }

        @Override
        public boolean areContentsTheSame(Session oldItem, Session newItem) {
            return oldItem.id.equals(newItem.id);
        }

        @Override
        public boolean areItemsTheSame(Session item1, Session item2) {
            return item1.id.equals(item2.id);
        }
    }
}
