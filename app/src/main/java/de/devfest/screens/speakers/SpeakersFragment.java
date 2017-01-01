package de.devfest.screens.speakers;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import de.devfest.R;
import de.devfest.databinding.FragmentSpeakersBinding;
import de.devfest.injection.ApplicationComponent;
import de.devfest.model.Speaker;
import de.devfest.mvpbase.BaseFragment;
import de.devfest.screens.main.ActionBarDrawerToggleHelper;
import de.devfest.screens.speakerdetails.SpeakerDetailsActivity;
import de.devfest.ui.UiUtils;

public class SpeakersFragment extends BaseFragment<SpeakersView, SpeakersPresenter>
        implements SpeakersView, View.OnClickListener {

    public static final String TAG = SpeakersFragment.class.toString();

    @Inject
    SpeakersPresenter presenter;

    private FragmentSpeakersBinding binding;
    private ActionBarDrawerToggleHelper toggleHelper;

    private SpeakerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_speakers, container, false);
        binding.listSpeakers.setLayoutManager(
                new GridLayoutManager(getContext(), UiUtils.getDefaultGridColumnCount(getContext())));
        adapter = new SpeakerAdapter(this);
        binding.listSpeakers.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toggleHelper = new ActionBarDrawerToggleHelper(this);
    }

    @Override
    public void onDestroyView() {
        toggleHelper.destroy(this);
        super.onDestroyView();
    }

    @Override
    protected SpeakersPresenter inject(ApplicationComponent component) {
        component.inject(this);
        return presenter;
    }

    @Override
    public void onSpeakerAvailable(@NonNull Speaker speaker) {
        adapter.addSpeaker(speaker);
    }

    @Override
    public void onError(Throwable error) {
        Snackbar.make(binding.getRoot(), error.getMessage(), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        String speakerId = adapter.getSpeaker(binding.listSpeakers.getChildAdapterPosition(view)).speakerId;
        Intent intent = SpeakerDetailsActivity.createIntent(getContext(), speakerId);
        View srcView = view.findViewById(R.id.imageSpeaker);
        Pair<View, String> pair = Pair.create(srcView, ViewCompat.getTransitionName(srcView));
        @SuppressWarnings("unchecked")
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), pair);
        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
    }
}
