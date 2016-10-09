package de.devfest.screens.user;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import de.devfest.R;
import de.devfest.databinding.FragmentUserBinding;
import de.devfest.injection.ApplicationComponent;
import de.devfest.model.User;
import de.devfest.mvpbase.AuthFragment;
import de.devfest.ui.UiUtils;

public class UserFragment extends AuthFragment<UserView, UserPresenter>
        implements UserView {

    @Inject
    UserPresenter presenter;

    private FragmentUserBinding binding;

    @NonNull
    @Override
    protected UserPresenter getPresenter() {
        return presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false);
        int statusBarHeight = UiUtils.getStatusBarHeight(getContext());
        binding.getRoot().setPadding(0, statusBarHeight, 0, 0);

        binding.buttonSignIn.setOnClickListener(view -> presenter.requestLogin());

        binding.buttonSignOut.setOnClickListener(view -> presenter.requestLogout());

        return binding.getRoot();
    }

    @Override
    protected UserPresenter inject(ApplicationComponent component) {
        component.inject(this);
        return presenter;
    }

    @Override
    public void showUser(@NonNull User user) {
        binding.imageUser.setVisibility(View.VISIBLE);
        binding.textUserName.setText(user.displayName);
        binding.textUserName.setVisibility(View.VISIBLE);
        binding.textUserMail.setText(user.email);
        binding.textUserMail.setVisibility(View.VISIBLE);
        binding.buttonSignIn.setVisibility(View.GONE);
        binding.buttonSignOut.setVisibility(View.VISIBLE);
        if (user.photoUrl != null) {
            Glide.with(this).load(user.photoUrl).into(binding.imageUser);
        }
    }

    @Override
    public void showLogin() {
        binding.textUserName.setVisibility(View.GONE);
        binding.textUserMail.setVisibility(View.GONE);
        binding.imageUser.setImageResource(R.mipmap.ic_launcher);
        binding.buttonSignIn.setVisibility(View.VISIBLE);
        binding.buttonSignOut.setVisibility(View.GONE);

    }

    @Override
    public void onError(Throwable error) {
        Snackbar.make(binding.getRoot(), error.getMessage(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessfullLogin(@NonNull User user) {
        showUser(user);
    }
}
