package de.devfest.screens.dashboard;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import de.devfest.R;
import de.devfest.databinding.FragmentDashboardBinding;
import de.devfest.injection.ApplicationComponent;
import de.devfest.model.Session;
import de.devfest.mvpbase.AuthFragment;
import timber.log.Timber;

public class DashboardFragment extends AuthFragment<DashboardView, DashboardPresenter> implements DashboardView {

    public static final String TAG = DashboardFragment.class.toString();

    @Inject
    DashboardPresenter presenter;
    private FragmentDashboardBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        return binding.getRoot();
    }

    @Override
    protected DashboardPresenter inject(ApplicationComponent component) {
        component.inject(this);
        return presenter;
    }

    @Override
    public void onScheduledSessionReceived(Session session) {
        Timber.e("scheduled session: %s", session.title);
    }

    @Override
    public void onRunningSessionReceived(Session session, boolean scheduled) {
        Timber.e("running session: %s", session.title);
    }

    @Override
    public void onError(Throwable error) {
        Timber.e(error);
    }

    @Override
    public void showLogin() {

    }

    @NonNull
    @Override
    protected DashboardPresenter getPresenter() {
        return presenter;
    }
}
