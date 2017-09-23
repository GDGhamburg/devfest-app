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
import de.devfest.databinding.IncludeItemSessionBinding;
import de.devfest.model.ScheduleSession;
import de.devfest.model.Session;
import de.devfest.model.Speaker;
import de.devfest.ui.facedetection.FaceCenterCrop;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class SessionAdapter extends
        RecyclerView.Adapter<SessionAdapter.ScheduleSessionViewHolder>
        implements View.OnClickListener {

    private final static int MINUTES_5 = 300;
    private final static int MINUTES_15 = 900;
    public final static float DIPS_PER_MINUTE = 2.7f;
    private final static int ITEM_TYPE_FILL_UP = -1;

    private final SortedList<ScheduleSession> scheduleItems;
    private final SessionInteractionListener interactionListener;
    private final View.OnClickListener itemClickListener;

    private int addIconColor = -1;
    private boolean showImage = true;
    private boolean showBadge = true;
    private boolean showCard = true;
    private boolean showAddButton = true;
    private boolean useDurationAsHeight = true;
    private int itemWidth = MATCH_PARENT;

    public SessionAdapter(SessionInteractionListener interactionListener,
            View.OnClickListener itemClickListener) {
        this.scheduleItems =
                new SortedList<>(ScheduleSession.class, new SessionsListAdapterCallback(this));
        this.interactionListener = interactionListener;
        this.itemClickListener = itemClickListener;
    }

    public void setItemWidth(int width) {
        itemWidth = width;
    }

    public void setShowImage(boolean enabled) {
        showImage = enabled;
    }

    public void setShowBadge(boolean enabled) {
        showBadge = enabled;
    }

    public void setShowCard(boolean enabled) {
        showCard = enabled;
    }

    public void setShowAddButton(boolean enabled) {
        showAddButton = enabled;
    }

    public void setUseDurationAsHeight(boolean enabled) {
        useDurationAsHeight = enabled;
    }

    @Override
    public ScheduleSessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        int layoutId = showCard ? R.layout.item_session_card : R.layout.item_session;
        ViewGroup view = (ViewGroup) inflater.inflate(layoutId, parent, false);
        view.getLayoutParams().width = itemWidth;
        IncludeItemSessionBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.include_item_session, view, false);
        view.addView(binding.getRoot());
        binding.getRoot().setOnClickListener(itemClickListener);
        binding.buttonAdd.setOnClickListener(this);
        if (addIconColor == -1) addIconColor = binding.textSessionTitle.getCurrentTextColor();
        return new SessionViewHolder(view, binding);
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
                UiUtils.onAddButtonClick((ImageButton) view, session, interactionListener,
                        addIconColor);
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

    private class SessionViewHolder extends ScheduleSessionViewHolder {
        private final IncludeItemSessionBinding binding;

        public SessionViewHolder(View view, IncludeItemSessionBinding binding) {
            super(view);
            this.binding = binding;
        }

        @Override
        public void bind(ScheduleSession session) {
            String tag = null;
            Context context = binding.getRoot().getContext();
            if (!session.session.tags.isEmpty()) tag = session.session.tags.get(0);
            if (showBadge) {
                binding.imageSession.setImageDrawable(
                        TagHelper.getCircledTagIcon(context, tag, !showImage));
            }
            binding.textSessionTitle.setText(session.session.title);
            binding.textSessionSub
                    .setText(session.session.startTime.format(UiUtils.getSessionStartFormat()));
            setTextAppearances(session);
            if (session.session.isScheduable && showAddButton) {
                binding.buttonAdd.setVisibility(View.VISIBLE);
                binding.buttonAdd.setTag(session);
                UiUtils.setAddDrawable(session.isScheduled, binding.buttonAdd,
                        binding.textSessionTitle.getCurrentTextColor());
            } else {
                binding.buttonAdd.setVisibility(View.GONE);
            }

            if (useDurationAsHeight) {
                long durationMinutes = TimeUnit.SECONDS.toMinutes(
                        session.session.endTime.toEpochSecond() - session.session.startTime
                                .toEpochSecond());
                binding.getRoot().getLayoutParams().height = UiUtils.dipsToPxls(context,
                        (int) (DIPS_PER_MINUTE * durationMinutes));
            }

            binding.getRoot().setTag(session);

            if (showImage) {
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

        private void setTextAppearances(ScheduleSession session) {
            if (!session.session.isScheduable || !showImage) {
                TextViewCompat.setTextAppearance(binding.textSessionTitle,
                        R.style.TextAppearance_DevFest_Card_Title);
                TextViewCompat.setTextAppearance(binding.textSessionSub,
                        R.style.TextAppearance_DevFest_Card_Subtitle);
            } else {
                TextViewCompat.setTextAppearance(binding.textSessionTitle,
                        R.style.TextAppearance_DevFest_Card_Title_Inverse);
                TextViewCompat.setTextAppearance(binding.textSessionSub,
                        R.style.TextAppearance_DevFest_Card_Subtitle_Inverse);
            }
        }
    }

    public interface SessionInteractionListener {
        void onAddSessionClick(Session session);

        void onRemoveSessionClick(Session session);
    }
}
