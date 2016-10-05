package de.devfest.screens.sessiondetails;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import de.devfest.databinding.FragmentSessionDetailsBinding;
import de.devfest.injection.ApplicationComponent;
import de.devfest.model.Session;
import de.devfest.mvpbase.BaseFragment;

import de.devfest.R;

public class SessionDetailsFragment extends BaseFragment<SessionDetailsView, SessionDetailsPresenter>
        implements SessionDetailsView {

    @Inject
    SessionDetailsPresenter presenter;

    private FragmentSessionDetailsBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_session_details, container, false);
        return binding.getRoot();
    }

    @Override
    protected SessionDetailsPresenter inject(ApplicationComponent component) {
        component.inject(this);
        return presenter;
    }

    @Override
    public String getSessionId() {
        return getActivity().getIntent().getExtras().getString(SessionDetailsActivity.EXTRA_SESSION_ID);
    }

    @Override
    public void onSessionAvailable(Session session) {

    }

    @Override
    public void onError(Throwable error) {

    }
}
