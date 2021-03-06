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

import de.devfest.R;
import de.devfest.databinding.ItemSessionBinding;
import de.devfest.model.ScheduleSession;
import de.devfest.model.Session;
import de.devfest.model.Speaker;
import de.devfest.ui.facedetection.FaceCenterCrop;

public class SessionAdapter extends
        RecyclerView.Adapter<SessionAdapter.SpeakerSessionViewHolder> implements View.OnClickListener {

    private final SortedList<ScheduleSession> sessions;
    private final SessionInteractionListener interactionListener;
    private final View.OnClickListener itemClickListener;

    private boolean useSimpleView;
    private int addIconColor = -1;

    public SessionAdapter(SessionInteractionListener interactionListener, View.OnClickListener itemClickListener) {
        this.sessions = new SortedList<>(ScheduleSession.class, new SessionsListAdapterCallback(this));
        this.interactionListener = interactionListener;
        this.itemClickListener = itemClickListener;
    }

    public void setSimpleViewEnabled(boolean enabled) {
        useSimpleView = enabled;
    }

    @Override
    public SpeakerSessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
                UiUtils.onAddButtonClick((ImageButton) view, session, interactionListener, addIconColor);
                break;
            default:
        }
    }

    public Session getSession(int position) {
        return sessions.get(position).session;
    }

    public static class SpeakerSessionViewHolder extends RecyclerView.ViewHolder {
        private final ItemSessionBinding binding;

        public SpeakerSessionViewHolder(ItemSessionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ScheduleSession session, boolean useSimpleView) {
            String tag = null;
            Context context = binding.getRoot().getContext();
            if (!session.session.tags.isEmpty()) tag = session.session.tags.get(0);
            binding.imageSession.setImageDrawable(
                    TagHelper.getCircledTagIcon(context, tag, useSimpleView));
            binding.textSessionTitle.setText(session.session.title);
            binding.textSessionSub.setText(session.session.startTime.format(UiUtils.getSessionStartFormat()));

            if (session.session.isScheduable) {
                binding.getRoot().getLayoutParams().height =
                        context.getResources().getDimensionPixelSize(R.dimen.session_item_height);
                binding.buttonAdd.setVisibility(View.VISIBLE);
                binding.buttonAdd.setTag(session);
                UiUtils.setAddDrawable(session.isScheduled, binding.buttonAdd,
                        binding.textSessionTitle.getCurrentTextColor());
            } else {
                binding.getRoot().getLayoutParams().height = RecyclerView.LayoutParams.WRAP_CONTENT;
                binding.buttonAdd.setVisibility(View.GONE);
            }
            binding.getRoot().setTag(session);

            if (!useSimpleView) {
                int overlayColor = ContextCompat.getColor(binding.getRoot().getContext(),
                        TagHelper.getTagOverlayColor(tag));
                binding.containerSessionForeground.setBackgroundColor(overlayColor);
                if (!session.session.speakers.isEmpty()) {
                    Speaker speaker = session.session.speakers.get(0);
                    Glide.with(binding.imageSessionBackground.getContext())
                            .load(speaker.photoUrl)
                            .transform(new FaceCenterCrop())
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
