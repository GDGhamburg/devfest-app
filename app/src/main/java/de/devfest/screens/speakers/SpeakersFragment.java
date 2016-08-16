package de.devfest.screens.speakers;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.FirebaseException;

import javax.inject.Inject;

import de.devfest.R;
import de.devfest.databinding.FragmentSpeakersBinding;
import de.devfest.injection.ApplicationComponent;
import de.devfest.model.Speaker;
import de.devfest.mvpbase.BaseFragment;
import de.devfest.screens.speakerdetails.SpeakerDetailsActivity;
import de.devfest.ui.UiUtils;

public class SpeakersFragment extends BaseFragment<SpeakersView, SpeakersPresenter> implements SpeakersView,
        View.OnClickListener {

    public static final String TAG = SpeakersFragment.class.toString();

    @Inject
    SpeakersPresenter presenter;

    private FragmentSpeakersBinding binding;
    private SpeakerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_speakers, container, false);
        binding.listSpeakers.setLayoutManager(
                new GridLayoutManager(getContext(), UiUtils.getDefaultGridColumnCount(getContext())));
        adapter = new SpeakerAdapter(this);
        binding.listSpeakers.setAdapter(adapter);

        return binding.getRoot();
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
        String speakerId = adapter.getSpeakerId(binding.listSpeakers.getChildAdapterPosition(view));
        SpeakerDetailsActivity.start(getActivity(), speakerId, view.findViewById(R.id.imageSpeaker));
    }
}
