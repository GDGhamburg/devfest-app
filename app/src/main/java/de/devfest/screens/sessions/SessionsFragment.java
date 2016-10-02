package de.devfest.screens.sessions;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import de.devfest.R;
import de.devfest.databinding.FragmentSessionsBinding;
import de.devfest.injection.ApplicationComponent;
import de.devfest.model.Session;
import de.devfest.mvpbase.BaseFragment;

public class SessionsFragment extends BaseFragment<SessionsView, SessionsPresenter> implements SessionsView {

    public static final String TAG = SessionsFragment.class.toString();

    @Inject
    SessionsPresenter presenter;

    private FragmentSessionsBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sessions, container, false);
        return binding.getRoot();
    }

    @Override
    protected SessionsPresenter inject(ApplicationComponent component) {
        component.inject(this);
        return presenter;
    }

    @Override
    public void finishedInitializaiton(int size) {
        presenter.loadSession(3);
    }

    @Override
    public void onSessionsReceived(int requestedPage, List<Session> sessions) {

    }

    @Override
    public void onError(Throwable error) {

    }
}
