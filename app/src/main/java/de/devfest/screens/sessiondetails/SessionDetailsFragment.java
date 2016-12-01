package de.devfest.screens.sessiondetails;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
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
import de.devfest.screens.speakerdetails.SpeakerDetailsActivity;
import de.devfest.ui.TagHelper;
import de.devfest.ui.UiUtils;

public class SessionDetailsFragment extends BaseFragment<SessionDetailsView, SessionDetailsPresenter>
        implements SessionDetailsView, View.OnClickListener {

    @Inject
    SessionDetailsPresenter presenter;

    private FragmentSessionDetailsBinding binding;
    private SessionSpeakerAdapter speakerAdapter;

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
        int tagColorDarkRes = TagHelper.getTagDarkColor(tag);
        int tagColorRes = TagHelper.getTagColor(tag);

        binding.toolbar.setNavigationOnClickListener(v -> {
            // fine as long as there is no deep linking to speaker details
            ActivityCompat.finishAfterTransition(getActivity());
        });
        ((ViewGroup.MarginLayoutParams) binding.imageTopic.getLayoutParams()).topMargin
                = UiUtils.getStatusBarHeight(getContext())
                + getResources().getDimensionPixelSize(R.dimen.spacing_small);
        binding.imageTopic.setImageDrawable(TagHelper.getTagIcon(getContext(), tag));
        binding.collapsingToolbarLayout.setContentScrimResource(tagColorDarkRes);
        binding.appbar.setBackgroundResource(tagColorDarkRes);
        binding.containerSessionContent.setBackgroundResource(tagColorRes);
        binding.speakerList.setLayoutManager(new LinearLayoutManager(getContext()));

        getActivity().supportStartPostponedEnterTransition();
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
        if (session.stage != null) {
            binding.textLocation.setText(session.stage.name);
            binding.imageSessionLocation.setVisibility(View.VISIBLE);
            binding.textLocation.setVisibility(View.VISIBLE);
        }
        binding.textSessionTitle.setText(session.title);
        binding.textSessionSub.setText(session.startTime.format(UiUtils.getSessionStartFormat()));
        binding.textLanguage.setText(session.language);
        speakerAdapter = new SessionSpeakerAdapter(session.speakers, this);
        binding.speakerList.setAdapter(speakerAdapter);
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

    @Override
    public void onClick(View view) {
        int position = binding.speakerList.getChildAdapterPosition(view);
        Intent intent = SpeakerDetailsActivity.createIntent(getContext(), speakerAdapter.getSpeaker(position).speakerId);
        getContext().startActivity(intent);
    }
}
