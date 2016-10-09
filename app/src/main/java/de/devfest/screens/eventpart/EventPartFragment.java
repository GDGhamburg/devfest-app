package de.devfest.screens.eventpart;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rohitarya.glide.facedetection.transformation.core.GlideFaceDetector;

import javax.inject.Inject;

import de.devfest.R;
import de.devfest.databinding.FragmentEventPartBinding;
import de.devfest.injection.ApplicationComponent;
import de.devfest.model.Session;
import de.devfest.mvpbase.BaseFragment;
import timber.log.Timber;

public class EventPartFragment extends BaseFragment<EventPartView, EventPartPresenter>
        implements EventPartView {

    private static final String ARGS_TRACK_ID = "track_id";
    private static final String ARGS_PART_ID = "event_part_id";
    @Inject
    EventPartPresenter presenter;
    private SessionsAdapter adapter;
    private String trackId;
    private String eventPartId;
    private FragmentEventPartBinding binding;

    public static EventPartFragment newInstance(String eventPartId, String trackId) {
        EventPartFragment fragment = new EventPartFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARGS_TRACK_ID, trackId);
        arguments.putString(ARGS_PART_ID, eventPartId);
        fragment.setArguments(arguments);
        return fragment;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GlideFaceDetector.initialize(getContext());
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_part, container, false);
        binding.trackSessionList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.trackSessionList.setAdapter(adapter);
        adapter = new SessionsAdapter(presenter);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GlideFaceDetector.releaseDetector();
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
    public void onSessionReceived(Session session, boolean scheduled) {
        Timber.e("Session: \"%s\" was scheduled: %s", session.title, scheduled);
        adapter.addSession(session, scheduled);

        Timber.e("Adapter size: %d", adapter.getItemCount());
    }

    @Override
    public void onError(Throwable error) {
        Timber.e(error);
    }
}
