package de.devfest.screens.schedule;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import de.devfest.R;
import de.devfest.databinding.FragmentScheduleBinding;
import de.devfest.injection.ApplicationComponent;
import de.devfest.mvpbase.BaseFragment;

public class ScheduleFragment extends BaseFragment<ScheduleView, SchedulePresenter>
        implements ScheduleView {

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

}
