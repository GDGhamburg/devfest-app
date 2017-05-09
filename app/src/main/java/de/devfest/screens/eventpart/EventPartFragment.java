package de.devfest.screens.eventpart;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import de.devfest.R;
import de.devfest.databinding.FragmentEventPartBinding;
import de.devfest.injection.ApplicationComponent;
import de.devfest.model.EventPart;
import de.devfest.model.ScheduleSession;
import de.devfest.model.Session;
import de.devfest.mvpbase.AuthFragment;
import de.devfest.screens.sessiondetails.SessionDetailsActivity;
import de.devfest.ui.SessionAdapter;
import de.devfest.ui.facedetection.GlideFaceDetector;
import timber.log.Timber;

public class EventPartFragment extends AuthFragment<EventPartView, EventPartPresenter>
        implements EventPartView, View.OnClickListener {

    private static final String ARGS_TRACK_ID = "track_id";
    private static final String ARGS_PART_ID = "event_part_id";
    @Inject
    EventPartPresenter presenter;
    private SessionAdapter adapter;
    private String trackId;
    private String eventPartId;
    private FragmentEventPartBinding binding;
    private VerticalScrollStateProvider scrollStateProvider;

    public static EventPartFragment newInstance(String eventPartId, String trackId) {
        EventPartFragment fragment = new EventPartFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARGS_TRACK_ID, trackId);
        arguments.putString(ARGS_PART_ID, eventPartId);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        scrollStateProvider = (VerticalScrollStateProvider) getParentFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments == null || !arguments.containsKey(ARGS_TRACK_ID)
                || !arguments.containsKey(ARGS_PART_ID)) {
            throw new NullPointerException(
                    String.format("Create this fragment using %s.newInstance",
                            EventPartFragment.class.getSimpleName()));
        }

        trackId = arguments.getString(ARGS_TRACK_ID);
        eventPartId = arguments.getString(ARGS_PART_ID);
    }

    @NonNull
    @Override
    protected EventPartPresenter getPresenter() {
        return presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        GlideFaceDetector.initialize(getContext());
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_part, container, false);
        binding.trackSessionList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.trackSessionList.setTag(true);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GlideFaceDetector.releaseDetector();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        scrollStateProvider = null;
    }

    @Override
    protected EventPartPresenter inject(ApplicationComponent component) {
        component.inject(this);
        return presenter;
    }

    @Override
    public String getTrackId() {
        return trackId;
    }

    @Override
    public String getEventPartId() {
        return eventPartId;
    }

    @Override
    public void onEventPartReceived(EventPart eventPart) {
        adapter = new SessionAdapter(presenter, this);
        binding.trackSessionList.addItemDecoration(new EventPartFillUpDecoration(eventPart));
        binding.trackSessionList.setAdapter(adapter);
    }

    @Override
    public void onSessionReceived(Session session, boolean scheduled) {
        Timber.e("Session: \"%s\" was scheduled: %s", session.title, scheduled);
        adapter.addSession(session, scheduled);
//        int scrollY = scrollStateProvider.getScrollY();
//        Log.d("SYNCSCROLL", "scrollY: " + scrollY);
//        binding.trackSessionList.setScrollY(scrollY);

        int scrollOffset = (int) ((View) binding.trackSessionList.getParent()).getTag();
        if (scrollOffset != -1) {
            binding.trackSessionList.scrollBy(0, scrollOffset);
        }

        Timber.e("Adapter size: %d", adapter.getItemCount());
    }

    @Override
    public void onError(Throwable error) {
        Timber.e(error);
    }

    @Override
    public void onClick(View view) {
        ScheduleSession sessionItem = adapter.getItem(
                binding.trackSessionList.getChildAdapterPosition(view));
        SessionDetailsActivity.showWithTransition(sessionItem.session, getActivity(), view);
    }

    public interface VerticalScrollStateProvider {
        int getScrollY();
    }
}
