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
import de.devfest.ui.SessionAdapter;
import de.devfest.ui.TagHelper;
import de.devfest.ui.UiUtils;
import timber.log.Timber;

import static de.devfest.screens.speakerdetails.SpeakerDetailsActivity.EXTRA_SPEAKER_ID;
import static de.devfest.ui.UiUtils.CACHED_SPEAKER_IMAGE_SIZE;

public class SpeakerDetailsFragment extends AuthFragment<SpeakerDetailsView, SpeakerDetailsPresenter>
        implements SpeakerDetailsView, View.OnClickListener {

    public static final String TAG = SpeakerDetailsFragment.class.toString();

    @Inject
    SpeakerDetailsPresenter presenter;

    private FragmentSpeakerDetailsBinding binding;
    private SocialLinksAdapter socialLinksAdapter;
    private SessionAdapter sessionAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
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
        if (!getActivity().getIntent().hasExtra(EXTRA_SPEAKER_ID)) {
            throw new IllegalStateException("Intent extras must provide: " + EXTRA_SPEAKER_ID);
        }
        return getActivity().getIntent().getExtras().getString(EXTRA_SPEAKER_ID);
    }

    @Override
    public void onSpeakerAvailable(Speaker speaker) {
        setDetails(speaker);
        setImage(speaker);
        sessionAdapter = new SessionAdapter(presenter, this);
        sessionAdapter.setSimpleViewEnabled(true);
        binding.sessionList.setAdapter(sessionAdapter);
    }

    @Override
    public void onSessionReceived(Session session, boolean scheduled) {
        sessionAdapter.addSession(session, scheduled);
    }

    private void setDetails(Speaker speaker) {
        binding.toolbar.setTitle(speaker.name);
        binding.textSpeakerDesc.setText(speaker.description);
        binding.textSpeakerJobTitle.setText(speaker.jobTitle);
        binding.textSpeakerCompany.setText(speaker.company);
        socialLinksAdapter.setSocialLinks(speaker.socialLinks);
        int colorResId = TagHelper.getTagOverlayColor(speaker.tags.get(0));
        if (UiUtils.isLandscape(getContext())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int statusBarColor = ContextCompat.getColor(getContext(),
                        TagHelper.getTagDarkColor(speaker.tags.get(0)));
                getActivity().getWindow().setStatusBarColor(statusBarColor);
            }
            binding.toolbar.setBackgroundResource(colorResId);
        } else {
            binding.collapsingToolbarLayout.setContentScrimResource(colorResId);
            binding.collapsingToolbarLayout.setStatusBarScrimResource(colorResId);
        }
    }

    private void setImage(Speaker speaker) {
        Glide.with(getContext()).load(speaker.photoUrl)
                .override(CACHED_SPEAKER_IMAGE_SIZE, CACHED_SPEAKER_IMAGE_SIZE)
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSocial:
                int index = binding.gridSocialButtons.getChildAdapterPosition(view);
                SocialLink link = socialLinksAdapter.getItem(index);
                presenter.onLinkClick(link);
                break;
            case R.id.cardSession:
                showSessionDetails(view);
                break;
            default:
        }
    }

    private void showSessionDetails(View view) {
        Session session = sessionAdapter.getSession(binding.sessionList.getChildAdapterPosition(view));
        SessionDetailsActivity.showWithTransition(session, getActivity(), view);
    }

    @NonNull
    @Override
    protected SpeakerDetailsPresenter getPresenter() {
        return presenter;
    }
}
