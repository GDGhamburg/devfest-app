package de.devfest.screens.notstarted;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import de.devfest.R;
import de.devfest.databinding.FragmentEventNotStartedBinding;
import de.devfest.injection.ApplicationComponent;
import de.devfest.mvpbase.BaseFragment;
import timber.log.Timber;

public class EventNotStartedFragment extends
        BaseFragment<EventNotStartedView, EventNotStartedPresenter> implements EventNotStartedView {

    public static final String TAG = EventNotStartedFragment.class.getSimpleName();
    @Inject
    EventNotStartedPresenter presenter;
    private FragmentEventNotStartedBinding binding;

    @Override
    protected EventNotStartedPresenter inject(ApplicationComponent component) {
        component.inject(this);
        return presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_not_started, container, false);
        // TODO: show a count down or smth similar
        return binding.getRoot();
    }

    @Override
    public void onError(Throwable error) {
        Timber.e(error);
    }
}
