package de.devfest.screens.user;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import de.devfest.R;
import de.devfest.databinding.FragmentUserBinding;
import de.devfest.injection.ApplicationComponent;
import de.devfest.model.User;
import de.devfest.mvpbase.BaseFragment;

public class UserFragment extends BaseFragment<UserView, UserPresenter>
        implements UserView, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 1234;

    @Inject
    UserPresenter presenter;

    private FragmentUserBinding binding;
    private GoogleApiClient googleApiClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false);
        binding.signInButton.setOnClickListener(view -> presenter.requestLogin());

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        return binding.getRoot();
    }

    @Override
    protected UserPresenter inject(ApplicationComponent component) {
//        component.inject(this);
        return presenter;
    }

    @Override
    public void showUser(@NonNull User user) {
        binding.accountImage.setVisibility(View.VISIBLE);
        binding.accountName.setText(user.userId);
        binding.accountName.setVisibility(View.VISIBLE);
        binding.accountMail.setText(user.email);
        binding.accountMail.setVisibility(View.VISIBLE);
        binding.signInButton.setVisibility(View.GONE);
        if (user.photoUrl != null) {
            Picasso.with(getContext()).load(user.photoUrl).into(binding.accountImage);
        }
    }

    @Override
    public void showLogin() {
        binding.accountName.setVisibility(View.GONE);
        binding.accountMail.setVisibility(View.GONE);
        binding.signInButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void startGoogleLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void showAuthenticationFailedError(Throwable error) {
        Snackbar.make(binding.getRoot(), error.getMessage(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                presenter.onGoogleSignInSuccessful(account);
            } else {
                presenter.onAuthenticationFailed();
            }
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // TODO
    }
}
