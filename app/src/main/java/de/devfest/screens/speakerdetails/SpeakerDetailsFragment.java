package de.devfest.screens.speakerdetails;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import javax.inject.Inject;

import de.devfest.R;
import de.devfest.databinding.FragmentSpeakerDetailsBinding;
import de.devfest.injection.ApplicationComponent;
import de.devfest.model.Speaker;
import de.devfest.mvpbase.BaseFragment;
import de.devfest.ui.UiUtils;

public class SpeakerDetailsFragment extends BaseFragment<SpeakerDetailsView, SpeakerDetailsPresenter>
        implements SpeakerDetailsView {

    public static final String TAG = SpeakerDetailsFragment.class.toString();

    @Inject
    SpeakerDetailsPresenter presenter;

    private FragmentSpeakerDetailsBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_speaker_details, container, false);
        return binding.getRoot();
    }

    @Override
    protected SpeakerDetailsPresenter inject(ApplicationComponent component) {
        component.inject(this);
        return presenter;
    }

    @Override
    public String getSpeakerId() {
        return getActivity().getIntent().getExtras().getString(SpeakerDetailsActivity.EXTRA_SPEAKER_ID);
    }

    @Override
    public void onSpeakerAvailable(Speaker speaker) {
        int colorResId = UiUtils.getTrackColor(speaker);
        int pixels = getResources().getDisplayMetrics().widthPixels;
        Glide.with(getContext()).load(speaker.photoUrl)
                .override(pixels, pixels).diskCacheStrategy(DiskCacheStrategy.ALL).dontTransform()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception ex, String model, Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        getActivity().supportStartPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        getActivity().supportStartPostponedEnterTransition();
                        return false;
                    }
                })
                .into(binding.imageSpeaker);
        binding.toolbar.setTitle(speaker.name);
        binding.textSpeakerName.setText(speaker.name);
    }

    @Override
    public void onError(Throwable error) {
        Snackbar.make(binding.getRoot(), R.string.error_default, Snackbar.LENGTH_LONG).show();
    }
}
