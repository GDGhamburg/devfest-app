package de.devfest.screens.schedule;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import de.devfest.R;
import de.devfest.databinding.FragmentScheduleBinding;
import de.devfest.injection.ApplicationComponent;
import de.devfest.model.EventPart;
import de.devfest.model.Track;
import de.devfest.mvpbase.BaseFragment;
import de.devfest.screens.eventpart.EventPartFragment;
import de.devfest.screens.eventpart.SmartFragmentStatePagerAdapter;

public class ScheduleFragment extends BaseFragment<ScheduleView, SchedulePresenter> implements ScheduleView {

    public static final String TAG = ScheduleFragment.class.toString();

    @Inject
    SchedulePresenter presenter;

    private FragmentScheduleBinding binding;

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false);
        binding.pagerTracks.setAdapter(pagerAdapter);
        return binding.getRoot();
    }

    @Override
    protected SchedulePresenter inject(ApplicationComponent component) {
        component.inject(this);
        return presenter;
    }

    @Override
    public void onError(Throwable error) {
        Snackbar.make(binding.getRoot(), "Error: " + error.getMessage(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onEventPartReceived(EventPart eventPart) {
        Log.d("SCHEDULE" , "onEventPartReceived: " + eventPart.name);
        pagerAdapter.addEventPart(eventPart);
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private static class EventTrackPagerAdapter extends SmartFragmentStatePagerAdapter<EventPartFragment> {

        private final SparseArray<Pair<String, String>> trackList = new SparseArray<>();


        public EventTrackPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
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

        @Override
        public Parcelable saveState() {
            for (int i = 0; i < getCount(); i++) {
                EventPartFragment fragment = getRegisteredFragment(i);
                fragment.onPause();
            }
            return super.saveState();
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
            for (int i = 0; i < getCount(); i++) {
                EventPartFragment fragment = getRegisteredFragment(i);
                fragment.onResume();
            }
            super.restoreState(state, loader);
        }
    }
}
