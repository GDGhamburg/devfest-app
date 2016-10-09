package de.devfest.screens.social;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import de.devfest.R;
import de.devfest.databinding.FragmentSocialBinding;
import de.devfest.injection.ApplicationComponent;
import de.devfest.mvpbase.BaseFragment;

public class SocialFragment extends BaseFragment<SocialView, SocialPresenter> implements SocialView {

    public static final String TAG = SocialFragment.class.toString();

    @Inject
    SocialPresenter presenter;

    private FragmentSocialBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_social, container, false);
        return binding.getRoot();
    }

    @Override
    protected SocialPresenter inject(ApplicationComponent component) {
        component.inject(this);
        return presenter;
    }

    @Override
    public void onError(Throwable error) {

    }
}
