package de.devfest.screens.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import javax.inject.Inject;

import de.devfest.DevFestApplication;
import de.devfest.R;
import de.devfest.databinding.ActivityMainBinding;
import de.devfest.injection.ApplicationComponent;
import de.devfest.mvpbase.BaseActivity;
import de.devfest.screens.dashboard.DashboardFragment;
import de.devfest.screens.notstarted.EventNotStartedFragment;
import de.devfest.screens.schedule.ScheduleFragment;
import de.devfest.screens.social.SocialFragment;
import de.devfest.screens.speakers.SpeakersFragment;
import timber.log.Timber;

public class MainActivity extends BaseActivity<MainActivityView, MainActivityPresenter>
        implements OnNavigationItemSelectedListener, MainActivityView {

    private ActivityMainBinding binding;

    @Inject
    MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Toolbar toolbar = binding.appbar.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.event_tag);

        DrawerLayout drawer = binding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this);
        ((DevFestApplication) getApplication()).getComponent()
                .inject(this);

        if (savedInstanceState == null) {
            binding.navView.setCheckedItem(R.id.nav_dashboard);
            showFragment(DashboardFragment.TAG);
        }
    }

    @Override
    public void onEventStarted(boolean started) {

    }

    @Override
    public void onError(Throwable error) {
        Timber.e(error);
    }


    @Override
    protected MainActivityPresenter inject(ApplicationComponent component) {
        component.inject(this);
        return presenter;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_dashboard:
                getSupportActionBar().setTitle(R.string.event_tag);
                showFragment(DashboardFragment.TAG);
                break;
            case R.id.nav_schedule:
                getSupportActionBar().setTitle(R.string.nav_schedule);
                showFragment(ScheduleFragment.TAG);
                break;
            case R.id.nav_speakers:
                getSupportActionBar().setTitle(R.string.nav_speakers);
                showFragment(SpeakersFragment.TAG);
                break;
            case R.id.nav_social:
                getSupportActionBar().setTitle(R.string.nav_social);
                showFragment(SocialFragment.TAG);
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

    private void showFragment(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = getFragment(tag);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragmentManager.findFragmentById(R.id.containerContent) == null) {
            transaction.add(R.id.containerContent, fragment, tag);
        } else {
            transaction.replace(R.id.containerContent, fragment, tag);
        }
        transaction.addToBackStack(tag).commit();
    }

    private Fragment getFragment(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            return fragment;
        }

        if (SpeakersFragment.TAG.equals(tag)) {
            return new SpeakersFragment();
        } else if (ScheduleFragment.TAG.equals(tag)) {
            return new ScheduleFragment();
        } else if (DashboardFragment.TAG.equals(tag)) {
            return new DashboardFragment();
        } else if (SocialFragment.TAG.equals(tag)) {
            return new SocialFragment();
        } else if (EventNotStartedFragment.TAG.equals(tag)) {
            return new EventNotStartedFragment();
        }

        throw new IllegalStateException("No start fragment found");
    }

    @Override
    public void onBackPressed() {
        FragmentManager.BackStackEntry backStackEntry = getSupportFragmentManager().getBackStackEntryAt(0);
        if (!getSupportFragmentManager().popBackStackImmediate(backStackEntry.getId(), 0)) {
            finish();
        }
        binding.navView.setCheckedItem(R.id.nav_dashboard);
    }
}
