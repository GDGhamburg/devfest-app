package de.devfest.mvpbase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import de.devfest.model.User;

public abstract class AuthFragment<V extends MvpBase.AuthView, P extends AuthPresenter<V>> extends BaseFragment<V, P> {

    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1234;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        googleApiClient = ((GoogleApiActivity) getActivity()).getGoogleLoginClient();
    }

    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();
    }

    public void startLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                getPresenter().onGoogleSignInSuccessful(account);
            } else {
                getPresenter().onAuthenticationFailed(result.getStatus().getStatusMessage());
            }
        }
    }

    @NonNull
    protected abstract P getPresenter();

    public void onSuccessfullLogin(@NonNull User user) {
    }
}
