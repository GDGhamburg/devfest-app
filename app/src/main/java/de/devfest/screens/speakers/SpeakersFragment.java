package de.devfest.screens.speakers;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class SpeakersFragment extends BaseFragment<SpeakersView, SpeakersPresenter> implements SpeakersView {

    @Inject
    SpeakersPresenter presenter;

    private FragmentSpeakersBinding binding;
    private SpeakerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_speakers, container, false);
        binding.listSpeakers.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new SpeakerAdapter();
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

    }
}
