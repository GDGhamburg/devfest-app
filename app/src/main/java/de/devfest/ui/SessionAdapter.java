package de.devfest.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;

import java.util.concurrent.TimeUnit;

import de.devfest.R;
import de.devfest.databinding.ItemSessionBinding;
import de.devfest.model.ScheduleSession;
import de.devfest.model.Session;
import de.devfest.model.Speaker;
import de.devfest.ui.facedetection.FaceCenterCrop;

public class SessionAdapter extends
        RecyclerView.Adapter<SessionAdapter.ScheduleSessionViewHolder> implements View.OnClickListener {

    private final static int MINUTES_5 = 300;
    private final static int MINUTES_15 = 900;
    public final static float DIPS_PER_MINUTE = 2.7f;
    private final static int ITEM_TYPE_FILL_UP = -1;

    private final SortedList<ScheduleSession> scheduleItems;
    private final SessionInteractionListener interactionListener;
    private final View.OnClickListener itemClickListener;

    private boolean useSimpleView;
    private int addIconColor = -1;

    public SessionAdapter(SessionInteractionListener interactionListener,
                          View.OnClickListener itemClickListener) {
        this.scheduleItems = new SortedList<>(ScheduleSession.class, new SessionsListAdapterCallback(this));
        this.interactionListener = interactionListener;
        this.itemClickListener = itemClickListener;
    }

    public void setSimpleViewEnabled(boolean enabled) {
        useSimpleView = enabled;
    }

    @Override
    public ScheduleSessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_session, parent, false);
        ItemSessionBinding binding = DataBindingUtil.bind(view);
        binding.getRoot().setOnClickListener(itemClickListener);
        binding.buttonAdd.setOnClickListener(this);
        if (useSimpleView) {
            TextViewCompat.setTextAppearance(binding.textSessionTitle, R.style.TextAppearance_DevFest_Card_Title);
            TextViewCompat.setTextAppearance(binding.textSessionSub, R.style.TextAppearance_DevFest_Card_Subtitle);
        }
        if (addIconColor == -1) addIconColor = binding.textSessionTitle.getCurrentTextColor();
        return new SessionViewHolder(binding, useSimpleView);
    }

    @Override
    public void onBindViewHolder(ScheduleSessionViewHolder holder, int position) {
        holder.bind(scheduleItems.get(position));
    }

    @Override
    public int getItemCount() {
        return scheduleItems.size();
    }

    public void addSession(Session session, boolean isScheduled) {
        int index = scheduleItems.add(new ScheduleSession(session, isScheduled));
    }

    @Override
    public void onClick(View view) {
        ScheduleSession session = (ScheduleSession) view.getTag();
        switch (view.getId()) {
            case R.id.buttonAdd:
                UiUtils.onAddButtonClick((ImageButton) view, session, interactionListener, addIconColor);
                break;
            default:
        }
    }

    public ScheduleSession getItem(int position) {
        return scheduleItems.get(position);
    }

    public static abstract class ScheduleSessionViewHolder extends RecyclerView.ViewHolder {

        public ScheduleSessionViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bind(ScheduleSession item);
    }

    public static class SessionViewHolder extends ScheduleSessionViewHolder {
        private final ItemSessionBinding binding;
        private final boolean simpleMode;

        public SessionViewHolder(ItemSessionBinding binding, boolean simpleMode) {
            super(binding.getRoot());
            this.binding = binding;
            this.simpleMode = simpleMode;
        }

        @Override
        public void bind(ScheduleSession session) {
            String tag = null;
            Context context = binding.getRoot().getContext();
            if (!session.session.tags.isEmpty()) tag = session.session.tags.get(0);
            binding.imageSession.setImageDrawable(
                    TagHelper.getCircledTagIcon(context, tag, simpleMode));
            binding.textSessionTitle.setText(session.session.title);
            binding.textSessionSub.setText(session.session.startTime.format(UiUtils.getSessionStartFormat()));

            if (session.session.isScheduable) {
                binding.buttonAdd.setVisibility(View.VISIBLE);
                binding.buttonAdd.setTag(session);
                UiUtils.setAddDrawable(session.isScheduled, binding.buttonAdd,
                        binding.textSessionTitle.getCurrentTextColor());
            } else {
                binding.buttonAdd.setVisibility(View.GONE);
            }

            long durationMinutes  = TimeUnit.SECONDS.toMinutes(
                    session.session.endTime.toEpochSecond() - session.session.startTime.toEpochSecond());
            binding.getRoot().getLayoutParams().height = UiUtils.dipsToPxls(context,
                    (int) (DIPS_PER_MINUTE * durationMinutes));

            binding.getRoot().setTag(session);

            if (!simpleMode) {
                int overlayColor = ContextCompat.getColor(binding.getRoot().getContext(),
                        TagHelper.getTagOverlayColor(tag));
                binding.containerSessionForeground.setBackgroundColor(overlayColor);
                if (!session.session.speakers.isEmpty()) {
                    Speaker speaker = session.session.speakers.get(0);
                    Glide.with(binding.imageSessionBackground.getContext())
                            .load(speaker.photoUrl)
                            .transform(new FaceCenterCrop(binding.imageSessionBackground.getContext()))
                            .into(binding.imageSessionBackground);
                } else {
                    binding.imageSessionBackground.setImageDrawable(null);
                }
            }
        }
    }

    public interface SessionInteractionListener {
        void onAddSessionClick(Session session);
        void onRemoveSessionClick(Session session);
    }
}
