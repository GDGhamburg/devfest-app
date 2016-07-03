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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

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
//        presenter.test();
    }

    @Override
    public void offerLogin() {

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
