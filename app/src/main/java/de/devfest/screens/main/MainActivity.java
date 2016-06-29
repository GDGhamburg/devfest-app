package de.devfest.screens.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import de.devfest.R;
import de.devfest.databinding.ActivityMainBinding;
import de.devfest.injection.ApplicationComponent;
import de.devfest.model.User;
import de.devfest.mvpbase.BaseActivity;

public class MainActivity extends BaseActivity<MainView, MainPresenter> implements MainView,
        GoogleApiClient.OnConnectionFailedListener, OnNavigationItemSelectedListener {

    @Inject
    MainPresenter presenter;
    private ActivityMainBinding binding;
    private GoogleApiClient googleApiClient;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Toolbar toolbar = binding.appbar.toolbar;
        setSupportActionBar(toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.nav_drawer_open,R.string.nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);

//        binding.navView.getHeaderView(0).findViewById(R.id.sign_in_button)
//                .setOnClickListener(view -> presenter.loginRequested());
//
//        GoogleSignInOptions gso = new GoogleSignInOptions
//                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        googleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
    }

    @Override
    protected MainPresenter inject(ApplicationComponent component) {
        component.inject(this);
        return presenter;
    }


    @Override
    public void showConnectivity(boolean connected) {
        Snackbar.make(binding.getRoot(), "Internet available: " + connected, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showUser(@NonNull User user) {
//        ImageView userImage = (ImageView) binding.navView.getHeaderView(0)
//                .findViewById(R.id.account_image);
//        userImage.setVisibility(View.VISIBLE);
//        TextView userText = (TextView) binding.navView.getHeaderView(0)
//                .findViewById(R.id.account_name);
//        userText.setText(user.userId);
//        userText.setVisibility(View.VISIBLE);
//        TextView userMail = (TextView) binding.navView.getHeaderView(0)
//                .findViewById(R.id.account_mail);
//        userMail.setText(user.email);
//        userMail.setVisibility(View.VISIBLE);
//        SignInButton signInButton = (SignInButton) binding.navView.getHeaderView(0)
//                .findViewById(R.id.sign_in_button);
//        signInButton.setVisibility(View.GONE);
//        if (user.photoUrl != null) {
//            Picasso.with(this).load(user.photoUrl).into(userImage);
//        }
//        presenter.test();
    }

    @Override
    public void offerLogin() {
//        TextView userText = (TextView) binding.navView.getHeaderView(0)
//                .findViewById(R.id.account_name);
//        userText.setVisibility(View.GONE);
//        TextView userMail = (TextView) binding.navView.getHeaderView(0)
//                .findViewById(R.id.account_mail);
//        userMail.setVisibility(View.GONE);
//        SignInButton signInButton = (SignInButton) binding.navView.getHeaderView(0)
//                .findViewById(R.id.sign_in_button);
//        signInButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void startGoogleLogin() {
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void showAuthenticationFailedError(Throwable error) {
        Snackbar.make(binding.getRoot(), error.getMessage(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            if (result.isSuccess()) {
//                GoogleSignInAccount account = result.getSignInAccount();
//                presenter.googleSigninSuccessful(account);
//            } else {
//                presenter.authenticationFailed();
//            }
//        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // TODO
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_schedule:
                break;
            case R.id.nav_sessions:
                break;
            case R.id.nav_speakers:
                break;
            case R.id.nav_social:
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_about:
                break;
            default:
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
