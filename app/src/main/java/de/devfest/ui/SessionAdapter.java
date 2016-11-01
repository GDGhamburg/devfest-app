package de.devfest.ui;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.rohitarya.glide.facedetection.transformation.FaceCenterCrop;

import java.util.Collections;
import java.util.List;

import de.devfest.R;
import de.devfest.databinding.ItemSessionBinding;
import de.devfest.model.ScheduleSession;
import de.devfest.model.Session;
import de.devfest.model.Speaker;

public class SessionAdapter extends
        RecyclerView.Adapter<SessionAdapter.SpeakerSessionViewHolder> implements View.OnClickListener {

    private final SortedList<ScheduleSession> sessions;
    private final SessionInteractionListener interactionListener;

    private boolean useSimpleView;
    private int addIconColor = -1;

    public SessionAdapter(SessionInteractionListener interactionListener) {
        this.sessions = new SortedList<>(ScheduleSession.class, new SessionsListAdapterCallback(this));
        this.interactionListener = interactionListener;
    }

    public void setSimpleViewEnabled(boolean enabled) {
        useSimpleView = enabled;
    }

    @Override
    public SpeakerSessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_session, parent, false);
        ItemSessionBinding binding = DataBindingUtil.bind(view);
        binding.getRoot().setOnClickListener(this);
        binding.buttonAdd.setOnClickListener(this);
        if (useSimpleView) {
            TextViewCompat.setTextAppearance(binding.textSessionTitle, R.style.TextAppearance_DevFest_Card_Title);
            TextViewCompat.setTextAppearance(binding.textSessionSub, R.style.TextAppearance_DevFest_Card_Subtitle);
        }
        if (addIconColor == -1) addIconColor = binding.textSessionTitle.getCurrentTextColor();
        return new SpeakerSessionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(SpeakerSessionViewHolder holder, int position) {
        holder.bind(sessions.get(position), useSimpleView);
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    public void addSession(Session session, boolean isScheduled) {
        sessions.add(new ScheduleSession(session, isScheduled));
    }

    @Override
    public void onClick(View view) {
        ScheduleSession session = (ScheduleSession) view.getTag();
        switch (view.getId()) {
            case R.id.buttonAdd:
                ((AnimatedVectorDrawable) ((ImageButton) view).getDrawable()).start();
                view.postDelayed(() -> {
                    if (session.isScheduled) interactionListener.onRemoveSessionClick(session.session);
                    else interactionListener.onAddSessionClick(session.session);
                    session.isScheduled = !session.isScheduled;
                    UiUtils.setAddDrawable(session.isScheduled, (ImageButton) view, addIconColor);
                }, view.getContext().getResources().getInteger(R.integer.add_duration) + 100);
                break;
            default:
                interactionListener.onSessionClick(session.session);
        }
    }

    public static class SpeakerSessionViewHolder extends RecyclerView.ViewHolder {
        private final ItemSessionBinding binding;

        public SpeakerSessionViewHolder(ItemSessionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ScheduleSession session, boolean useSimpleView) {
            List<String> tags = Collections.emptyList();
            if (!session.session.speakers.isEmpty()) tags = session.session.speakers.get(0).tags;
            binding.imageSession.setImageDrawable(
                    UiUtils.getCircledTrackIcon(binding.getRoot().getContext(), tags, useSimpleView));
            binding.textSessionTitle.setText(session.session.title);
            binding.textSessionSub.setText(session.session.startTime.format(UiUtils.getSessionStartFormat()));

            binding.buttonAdd.setTag(session);
            UiUtils.setAddDrawable(session.isScheduled, binding.buttonAdd, binding.textSessionTitle.getCurrentTextColor());
            binding.getRoot().setTag(session);

            if (!useSimpleView) {
                int overlayColor = ContextCompat.getColor(binding.getRoot().getContext(), UiUtils.getTagOverlayColor(tags));
                binding.containerSessionForeground.setBackgroundColor(overlayColor);
                if (!session.session.speakers.isEmpty()) {
                    Speaker speaker = session.session.speakers.get(0);
                    Glide.with(binding.imageSessionBackground.getContext())
                            .load(speaker.photoUrl)
                            .transform(new FaceCenterCrop())
                            .into(binding.imageSessionBackground);
                }
            }
        }
    }

    public interface SessionInteractionListener {
        void onSessionClick(Session session);
        void onAddSessionClick(Session session);
        void onRemoveSessionClick(Session session);
    }
}
