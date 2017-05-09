package de.devfest.screens.schedule;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.devfest.R;
import de.devfest.databinding.FragmentScheduleBinding;
import de.devfest.injection.ApplicationComponent;
import de.devfest.model.EventPart;
import de.devfest.model.Track;
import de.devfest.mvpbase.BaseFragment;
import de.devfest.screens.eventpart.EventPartFragment;
import de.devfest.screens.eventpart.SmartFragmentStatePagerAdapter;
import de.devfest.screens.main.ActionBarDrawerToggleHelper;

public class ScheduleFragment extends BaseFragment<ScheduleView, SchedulePresenter> implements ScheduleView,
        EventPartFragment.VerticalScrollStateProvider {

    public static final String TAG = ScheduleFragment.class.toString();

    @Inject
    SchedulePresenter presenter;

    private FragmentScheduleBinding binding;
    private ActionBarDrawerToggleHelper toggleHelper;

    private EventPartPagerAdapter pagerAdapter;
    private ArrayList<EventPart> eventParts;
    private List<Integer> pageListenerPositions;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pagerAdapter = new EventPartPagerAdapter(getChildFragmentManager());
        eventParts = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false);
        binding.pagerTracks.setAdapter(pagerAdapter);
        binding.tabsSchedule.addOnTabSelectedListener(createTabListener());
        binding.pagerTracks.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabsSchedule) {

            int lastSelected = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (pageListenerPositions.contains(position)) {
                    super.onPageScrolled(pagePosToTabPos(position), positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                int tabPosition = pagePosToTabPos(position);
                if (pageListenerPositions.contains(position)
                        && binding.tabsSchedule.getSelectedTabPosition() != tabPosition) {
                    super.onPageSelected(tabPosition);
                }
                lastSelected = position;
            }
        });
        binding.pagerTracks.setTag(-1);
        return binding.getRoot();
    }

    private int pagePosToTabPos(int pagePosition) {
        int pageCount = 0;
        for (int i = 0; i < eventParts.size(); i++) {
            pageCount += eventParts.get(i).tracks.size();
            if (pagePosition < pageCount) {
                return i;
            }
        }
        return eventParts.size() - 1;
    }

    private TabLayout.OnTabSelectedListener createTabListener() {
        return new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (!pagerAdapter.trackList.isEmpty()) {
                    int position = binding.tabsSchedule.getSelectedTabPosition();
                    int newPagerPosition = 0;
                    for (int i = 0; i < position; i++) {
                        newPagerPosition += eventParts.get(i).tracks.size();
                    }
                    if (newPagerPosition < pagerAdapter.trackList.size()
                            && position != pagePosToTabPos(binding.pagerTracks.getCurrentItem())) {
                        binding.pagerTracks.setCurrentItem(newPagerPosition);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toggleHelper = new ActionBarDrawerToggleHelper(this);
        pageListenerPositions = new ArrayList<>();
    }

    @Override
    public void onDestroyView() {
        toggleHelper.destroy(this);
        super.onDestroyView();
    }

    @Override
    protected SchedulePresenter inject(ApplicationComponent component) {
        component.inject(this);
        return presenter;
    }

    @Override
    public void onError(Throwable error) {
        Snackbar.make(binding.getRoot(), "Error: " + error.getMessage(), Snackbar.LENGTH_SHORT).show();
        Log.d(TAG, error.getMessage());
    }

    @Override
    public void onEventPartReceived(EventPart eventPart) {
        if (!eventParts.contains(eventPart)) {
            eventParts.add(eventPart);
            if (eventParts.size() > 1) {
                int pageCount = pagerAdapter.trackList.size();
                pageListenerPositions.add(pageCount - 1);
                pageListenerPositions.add(pageCount);
            }

            pagerAdapter.addEventPart(eventPart);

            TabLayout.Tab tab = binding.tabsSchedule.newTab();
            tab.setText(eventPart.name);
            binding.tabsSchedule.addTab(tab);
        }
    }

    public int getScrollY() {
        View view = binding.pagerTracks.getChildAt(0);
        return view != null ? view.findViewById(R.id.trackSessionList).getScrollY() : 0;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private class EventPartPagerAdapter extends SmartFragmentStatePagerAdapter<EventPartFragment> {

        private final List<Pair<String, String>> trackList = new ArrayList<>();

        public EventPartPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public EventPartFragment getItem(int position) {
            Pair<String, String> stringStringPair = trackList.get(position);
            return EventPartFragment.newInstance(stringStringPair.first, stringStringPair.second);
        }

        @Override
        public int getCount() {
            return trackList.size();
        }

        public void addEventPart(EventPart eventPart) {
            for (Track track : eventPart.tracks) {
                trackList.add(Pair.create(eventPart.id, track.id));
            }
            notifyDataSetChanged();
        }

        @Override
        public float getPageWidth(int position) {
            return getContext().getResources().getFraction(R.fraction.event_part_page_width, 1, 1);
        }
    }
}
