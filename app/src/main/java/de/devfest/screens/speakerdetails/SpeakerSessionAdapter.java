package de.devfest.screens.speakerdetails;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.List;

import de.devfest.R;
import de.devfest.databinding.ItemSessionBinding;
import de.devfest.model.ScheduleSession;
import de.devfest.model.Session;
import de.devfest.ui.SessionsListAdapterCallback;
import de.devfest.ui.UiUtils;

public class SpeakerSessionAdapter extends
        RecyclerView.Adapter<SpeakerSessionAdapter.SpeakerSessionViewHolder> implements View.OnClickListener {

    private final SortedList<ScheduleSession> list;
    private final SessionInteractionListener interactionListener;

    private int addIconColor = -1;

    public SpeakerSessionAdapter(SessionInteractionListener interactionListener) {
        this.list = new SortedList<>(ScheduleSession.class, new SessionsListAdapterCallback(this));
        this.interactionListener = interactionListener;
    }

    @Override
    public SpeakerSessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_session, parent, false);
        ItemSessionBinding binding = DataBindingUtil.bind(view);
        TextViewCompat.setTextAppearance(binding.textSessionTitle, R.style.TextAppearance_DevFest_Card_Title);
        TextViewCompat.setTextAppearance(binding.textSessionSub, R.style.TextAppearance_DevFest_Card_Subtitle);
        if (addIconColor == -1) addIconColor = binding.textSessionTitle.getCurrentTextColor();
        binding.getRoot().setOnClickListener(this);
        binding.buttonAdd.setOnClickListener(this);
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

    public void addSession(Session session, boolean isScheduled) {
        list.add(new ScheduleSession(session, isScheduled));
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

        public void bind(ScheduleSession session) {
            List<String> tags = session.session.speaker.get(0).tags;
            binding.imageSession.setImageDrawable(
                    UiUtils.getCircledTrackIcon(binding.getRoot().getContext(), tags, true));
            binding.textSessionTitle.setText(session.session.title);
            binding.textSessionSub.setText(session.session.startTime.format(UiUtils.getSessionStartFormat()));
            UiUtils.setAddDrawable(session.isScheduled, binding.buttonAdd, binding.textSessionTitle.getCurrentTextColor());
            binding.buttonAdd.setTag(session);
            binding.getRoot().setTag(session);
        }
    }

    public interface SessionInteractionListener {

        void onSessionClick(Session session);

        void onAddSessionClick(Session session);

        void onRemoveSessionClick(Session session);
    }
}
