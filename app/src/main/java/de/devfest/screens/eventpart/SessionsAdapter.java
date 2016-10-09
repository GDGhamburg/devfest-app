package de.devfest.screens.eventpart;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.rohitarya.glide.facedetection.transformation.FaceCenterCrop;

import org.threeten.bp.format.DateTimeFormatter;

import de.devfest.R;
import de.devfest.databinding.ItemSessionBinding;
import de.devfest.model.ScheduleSession;
import de.devfest.model.Session;
import de.devfest.model.Speaker;
import de.devfest.ui.SessionsListAdapterCallback;
import de.devfest.ui.UiUtils;

public class SessionsAdapter extends RecyclerView.Adapter<SessionsAdapter.SessionViewHolder> {

    private final SortedList<ScheduleSession> sessions;
    private final DateTimeFormatter sessionStartFormat;
    private final EventPartPresenter presenter;
    private final View.OnClickListener addClickListener;

    public SessionsAdapter(EventPartPresenter presenter) {
        this.presenter = presenter;
        sessionStartFormat = UiUtils.getSessionStartFormat();
        sessions = new SortedList<>(ScheduleSession.class, new SessionsListAdapterCallback(this));
        addClickListener = view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) ((ImageButton) view).getDrawable();
                drawable.start();
            }
            presenter.addToSchedule(getItem((String) view.getTag()).session);
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
        ScheduleSession session = sessions.get(position);
        Speaker speaker = session.session.speaker.get(0);
        holder.binding.textSessionTitle.setText(session.session.title);
        holder.binding.imageSession.setImageDrawable(
                UiUtils.getCircledTrackIcon(holder.itemView.getContext(), speaker.tags, false));
        holder.binding.textSessionTitle.setText(session.session.title);
        holder.binding.textSessionSub.setText(session.session.startTime.format(sessionStartFormat));
        int overlayColor = ContextCompat.getColor(holder.itemView.getContext(), UiUtils.getTagOverlayColor(speaker.tags));
        holder.binding.containerSessionForeground.setBackgroundColor(overlayColor);
        holder.binding.buttonAdd.setTag(session.session.id);
        UiUtils.setAddDrawable(session.isScheduled, holder.binding.buttonAdd, Color.WHITE);
        Glide.with(holder.itemView.getContext())
                .load(speaker.photoUrl)
                .transform(new FaceCenterCrop())
                .into(holder.binding.imageSessionBackground);
    }

    @Override
    public long getItemId(int position) {
        return sessions.get(position).session.id.hashCode();
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    public ScheduleSession getItem(int position) {
        return sessions.get(position);
    }

    public ScheduleSession getItem(String id) {
        for (int i = 0; i < sessions.size(); i++) {
            ScheduleSession session = sessions.get(i);
            if (session.session.id.equals(id)) return session;
        }
        return null;
    }

    public void addSession(Session session, boolean isScheduled) {
        sessions.add(new ScheduleSession(session, isScheduled));
    }

    public static class SessionViewHolder extends RecyclerView.ViewHolder {

        public final ItemSessionBinding binding;

        public SessionViewHolder(ItemSessionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
