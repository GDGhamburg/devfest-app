package de.devfest.screens.speakerdetails;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import de.devfest.model.Session;
import de.devfest.model.SocialLink;
import de.devfest.model.Speaker;
import de.devfest.mvpbase.AuthFragment;
import de.devfest.screens.sessiondetails.SessionDetailsActivity;
import de.devfest.ui.UiUtils;
import timber.log.Timber;

public class SpeakerDetailsFragment extends AuthFragment<SpeakerDetailsView, SpeakerDetailsPresenter>
        implements SpeakerDetailsView, View.OnClickListener {

    public static final String TAG = SpeakerDetailsFragment.class.toString();

    @Inject
    SpeakerDetailsPresenter presenter;

    private FragmentSpeakerDetailsBinding binding;
    private SocialLinksAdapter socialLinksAdapter;
    private SpeakerSessionAdapter speakerSessionAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_speaker_details, container, false);
        binding.toolbar.setNavigationOnClickListener(v -> {
            // fine as long as there is no deep linking to speaker details
            ActivityCompat.finishAfterTransition(getActivity());
        });
        if (!UiUtils.isLandscape(getContext())) {
            binding.statusBarBackground.getLayoutParams().height = UiUtils.getStatusBarHeight(getContext());
        }
        binding.gridSocialButtons.setLayoutManager(
                new GridLayoutManager(getContext(), UiUtils.getDefaultGridColumnCount(getContext())));
        socialLinksAdapter = new SocialLinksAdapter(this);
        binding.gridSocialButtons.setAdapter(socialLinksAdapter);
        binding.sessionList.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false));

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
        setDetails(speaker);
        setImage(speaker);
        speakerSessionAdapter = new SpeakerSessionAdapter(presenter);
        binding.sessionList.setAdapter(speakerSessionAdapter);
    }

    @Override
    public void onSessionReceived(Session session, boolean scheduled) {
        speakerSessionAdapter.addSession(session, scheduled);
    }

    private void setDetails(Speaker speaker) {
        binding.toolbar.setTitle(speaker.name);
        binding.textSpeakerDesc.setText(speaker.description);
        binding.textSpeakerJobTitle.setText(speaker.jobTitle);
        binding.textSpeakerCompany.setText(speaker.company);
        int colorResId = UiUtils.getTagOverlayColor(speaker.tags);
        if (UiUtils.isLandscape(getContext())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int statusBarColor = ContextCompat.getColor(getContext(), UiUtils.getTagDarkColor(speaker.tags));
                getActivity().getWindow().setStatusBarColor(statusBarColor);
            }
            binding.toolbar.setBackgroundResource(colorResId);
        } else {
            binding.collapsingToolbarLayout.setContentScrimResource(colorResId);
            binding.collapsingToolbarLayout.setStatusBarScrimResource(colorResId);
        }
    }

    private void setImage(Speaker speaker) {
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
    }

    @Override
    public void onError(Throwable error) {
        Snackbar.make(binding.getRoot(), R.string.error_default, Snackbar.LENGTH_LONG).show();
        Timber.e(error);
    }

    @Override
    public void openLink(String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        startActivity(intent);
    }

    @Override
    public void showSessionDetails(String sessionId) {
        Intent intent = SessionDetailsActivity.createIntent(getContext(), sessionId);
        getContext().startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        int index = binding.gridSocialButtons.getChildAdapterPosition(view);
        SocialLink link = socialLinksAdapter.getItem(index);
        presenter.onLinkClick(link);
    }

    @NonNull
    @Override
    protected SpeakerDetailsPresenter getPresenter() {
        return presenter;
    }
}
