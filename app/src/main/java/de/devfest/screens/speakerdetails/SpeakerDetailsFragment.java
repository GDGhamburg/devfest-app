package de.devfest.screens.speakerdetails;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import javax.inject.Inject;

import de.devfest.R;
import de.devfest.databinding.FragmentSpeakerDetailsBinding;
import de.devfest.injection.ApplicationComponent;
import de.devfest.model.Session;
import de.devfest.model.SocialLink;
import de.devfest.model.Speaker;
import de.devfest.mvpbase.BaseFragment;
import de.devfest.ui.UiUtils;

public class SpeakerDetailsFragment extends BaseFragment<SpeakerDetailsView, SpeakerDetailsPresenter>
        implements SpeakerDetailsView, View.OnClickListener {

    public static final String TAG = SpeakerDetailsFragment.class.toString();

    @Inject
    SpeakerDetailsPresenter presenter;

    private FragmentSpeakerDetailsBinding binding;
    private SocialLinksAdapter socialLinksAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_speaker_details, container, false);

        binding.statusBarBackground.getLayoutParams().height = UiUtils.getStatusBarHeight(getContext());
        binding.gridSocialButtons.setLayoutManager(
                new GridLayoutManager(getContext(), UiUtils.getDefaultGridColumnCount(getContext())));
        socialLinksAdapter = new SocialLinksAdapter(this);
        binding.gridSocialButtons.setAdapter(socialLinksAdapter);
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
    public void onSpeakerAvailable(Speaker speaker, List<Session> sessions) {
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
        binding.textSpeakerDesc.setText(speaker.description);
        binding.textSpeakerJobTitle.setText(speaker.jobTitle);
        binding.textSpeakerCompany.setText(speaker.company);
        binding.collapsingToolbarLayout.setContentScrimResource(colorResId);
        binding.collapsingToolbarLayout.setStatusBarScrimResource(colorResId);
        socialLinksAdapter.setSocialLinks(speaker.socialLinks);
        binding.getRoot().requestLayout();
    }

    @Override
    public void onError(Throwable error) {
        Snackbar.make(binding.getRoot(), R.string.error_default, Snackbar.LENGTH_LONG).show();
//        error.printStackTrace();
    }

    @Override
    public void openLink(String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        int index = binding.gridSocialButtons.getChildAdapterPosition(view);
        SocialLink link = socialLinksAdapter.getItem(index);
        presenter.onLinkClick(link);
    }
}
