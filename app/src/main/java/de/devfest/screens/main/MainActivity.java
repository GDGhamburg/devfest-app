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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import de.devfest.R;
import de.devfest.databinding.ContentMainBinding;
import de.devfest.injection.ApplicationComponent;
import de.devfest.model.User;
import de.devfest.mvpbase.BaseActivity;

public class MainActivity extends BaseActivity<MainView, MainPresenter> implements MainView,
        GoogleApiClient.OnConnectionFailedListener, OnNavigationItemSelectedListener {

    private static final int RC_SIGN_IN = 1234;
    @Inject
    MainPresenter presenter;
    private ContentMainBinding binding;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View root = findViewById(R.id.content_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.nav_drawer_open,R.string.nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        binding = DataBindingUtil.bind(root);
        binding.signInButton.setOnClickListener(view -> presenter.loginRequested());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
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
        binding.imageUser.setVisibility(View.VISIBLE);
        binding.textUser.setVisibility(View.VISIBLE);
        binding.signInButton.setVisibility(View.INVISIBLE);
        binding.textUser.setText(user.email);
        if (user.photoUrl != null) {
            Picasso.with(this).load(user.photoUrl).into(binding.imageUser);
        }
        presenter.test();
    }

    @Override
    public void offerLogin() {
        binding.imageUser.setVisibility(View.INVISIBLE);
        binding.textUser.setVisibility(View.INVISIBLE);
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
                presenter.googleSigninSuccessful(account);
            } else {
                presenter.authenticationFailed();
            }
        }
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
