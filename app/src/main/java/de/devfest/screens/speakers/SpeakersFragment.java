package de.devfest.screens.speakers;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import de.devfest.R;
import de.devfest.databinding.FragmentScheduleBinding;
import de.devfest.injection.ApplicationComponent;
import de.devfest.model.Speaker;
import de.devfest.mvpbase.BaseFragment;

public class SpeakersFragment extends BaseFragment<SpeakersView, SpeakersPresenter> implements SpeakersView {

    @Inject
    SpeakersPresenter presenter;

    private FragmentScheduleBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_speakers, container, false);
        return binding.getRoot();
    }

    @Override
    protected SpeakersPresenter inject(ApplicationComponent component) {
        component.inject(this);
        return presenter;
    }


    @Override
    public void onSpeakerAvailable(@NonNull Speaker speaker) {
        // TODO: add to adapter and show
    }

    @Override
    public void onError(Throwable error) {

    }
}
