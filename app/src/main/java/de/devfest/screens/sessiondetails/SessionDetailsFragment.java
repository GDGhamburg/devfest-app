package de.devfest.screens.sessiondetails;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import de.devfest.R;
import de.devfest.databinding.FragmentSessionDetailsBinding;
import de.devfest.injection.ApplicationComponent;
import de.devfest.model.Session;
import de.devfest.model.User;
import de.devfest.mvpbase.BaseFragment;
import de.devfest.ui.UiUtils;

public class SessionDetailsFragment extends BaseFragment<SessionDetailsView, SessionDetailsPresenter>
        implements SessionDetailsView {

    @Inject
    SessionDetailsPresenter presenter;

    private FragmentSessionDetailsBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_session_details, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String tag = getActivity().getIntent().getStringExtra(SessionDetailsActivity.EXTRA_SESSION_TAG);
        int tagColorDark = UiUtils.getTagDarkColor(tag);
        int tagColor = UiUtils.getTagColor(tag);

        binding.appbar.setBackgroundResource(tagColorDark);
        binding.containerSessionContent.setBackgroundResource(tagColor);
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
    public void onSessionReceived(Session session, boolean isScheduled) {
        binding.textSessionDesc.setText(session.description);
        binding.textLocation.setText(session.stage.name);
    }

    @Override
    public void onError(Throwable error) {

    }

    @Override
    public void startLogin() {

    }

    @Override
    public void onSuccessfullLogin(@NonNull User user) {

    }
}
