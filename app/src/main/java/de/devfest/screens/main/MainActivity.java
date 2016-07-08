package de.devfest.screens.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import javax.inject.Inject;

import de.devfest.R;
import de.devfest.databinding.ActivityMainBinding;
import de.devfest.injection.ApplicationComponent;
import de.devfest.mvpbase.BaseActivity;
import de.devfest.screens.speakers.SpeakersFragment;

public class MainActivity extends BaseActivity<MainView, MainPresenter> implements MainView,
        OnNavigationItemSelectedListener {

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
                R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected MainPresenter inject(ApplicationComponent component) {
        component.inject(this);
        return presenter;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_schedule:
                break;
            case R.id.nav_sessions:
                break;
            case R.id.nav_speakers:
                showFragment(new SpeakersFragment());
                break;
            case R.id.nav_social:
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_about:
                break;
            default:
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.containerContent) == null) {
            fragmentManager.beginTransaction().add(R.id.containerContent, fragment).commitNow();
        } else {
            fragmentManager.beginTransaction().replace(R.id.containerContent, fragment).commitNow();
        }
    }
}
