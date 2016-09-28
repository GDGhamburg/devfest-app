package de.devfest.screens.schedule;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import de.devfest.R;
import de.devfest.databinding.FragmentScheduleBinding;
import de.devfest.injection.ApplicationComponent;
import de.devfest.model.Session;
import de.devfest.mvpbase.BaseFragment;
import timber.log.Timber;

public class ScheduleFragment extends BaseFragment<ScheduleView, SchedulePresenter> implements ScheduleView {

    public static final String TAG = ScheduleFragment.class.toString();

    @Inject
    SchedulePresenter presenter;
    private FragmentScheduleBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false);
        return binding.getRoot();
    }

    @Override
    protected SchedulePresenter inject(ApplicationComponent component) {
        component.inject(this);
        return presenter;
    }

    @Override
    public void onSessionRetreived(List<Session> session) {
        // TODO presenter not final yet
        Timber.e("session: %s", session.size());
    }

    @Override
    public void onError(Throwable error) {

    }
}
