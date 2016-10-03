package de.devfest.screens.sessions;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import de.devfest.R;
import de.devfest.databinding.FragmentSessionsBinding;
import de.devfest.injection.ApplicationComponent;
import de.devfest.model.EventPart;
import de.devfest.model.Track;
import de.devfest.mvpbase.BaseFragment;
import de.devfest.screens.eventpart.EventPartFragment;
import de.devfest.screens.eventpart.SmartFragmentStatePagerAdapter;

public class SessionsFragment extends BaseFragment<SessionsView, SessionsPresenter> implements SessionsView {

    public static final String TAG = SessionsFragment.class.toString();

    @Inject
    SessionsPresenter presenter;

    private FragmentSessionsBinding binding;

    private EventTrackPagerAdapter pagerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pagerAdapter = new EventTrackPagerAdapter(getFragmentManager());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sessions, container, false);
        binding.tracksPager.setAdapter(pagerAdapter);
        return binding.getRoot();
    }

    @Override
    protected SessionsPresenter inject(ApplicationComponent component) {
        component.inject(this);
        return presenter;
    }


    @Override
    public void onError(Throwable error) {
        Snackbar.make(binding.getRoot(), "Error: " + error.getMessage(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onEventPartReceived(EventPart eventPart) {
        pagerAdapter.addEventPart(eventPart);
        pagerAdapter.notifyDataSetChanged();
    }

    private static class EventTrackPagerAdapter extends SmartFragmentStatePagerAdapter<EventPartFragment> {

        private SparseArray<Pair<String, String>> trackList;


        public EventTrackPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            trackList = new SparseArray<>();
        }

        @Override
        public EventPartFragment getItem(int position) {
            Pair<String, String> stringStringPair = trackList.valueAt(position);
            return EventPartFragment.newInstance(stringStringPair.first, stringStringPair.second);
        }

        @Override
        public int getCount() {
            return trackList.size();
        }

        public void addEventPart(EventPart eventPart) {
            for (Track track : eventPart.tracks) {
                trackList.put((eventPart.id + track.id).hashCode(), Pair.create(eventPart.id, track.id));
            }
        }
    }
}
