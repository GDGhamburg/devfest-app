package de.devfest.screens.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import de.devfest.R;
import de.devfest.databinding.ActivityMainBinding;
import de.devfest.screens.schedule.ScheduleFragment;
import de.devfest.screens.schedule.ScheduleFragment;
import de.devfest.screens.sessions.SessionsFragment;
import de.devfest.screens.social.SocialFragment;
import de.devfest.screens.speakers.SpeakersFragment;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

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

        if (savedInstanceState == null) {
            showFragment(new ScheduleFragment(), ScheduleFragment.TAG);
            binding.navView.setCheckedItem(R.id.nav_schedule);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_schedule:
                showFragment(new ScheduleFragment(), ScheduleFragment.TAG);
                break;
            case R.id.nav_sessions:
                // TODO: not working yet
                // showFragment(FRAGMENT_SESSION);
                showFragment(new SessionsFragment(), SessionsFragment.TAG);
                break;
            case R.id.nav_speakers:
                showFragment(FRAGMENT_SPEAKER);
                showFragment(new SpeakersFragment(), SpeakersFragment.TAG);
                break;
            case R.id.nav_social:
                showFragment(new SocialFragment(), SocialFragment.TAG);
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

    private static final String FRAGMENT_SPEAKER = "speaker-fragment";
    private static final String FRAGMENT_SESSION = "session-fragment";

    private void showFragment(String fragmentToStart) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = getFragment(fragmentToStart);
        if (fragmentManager.findFragmentById(R.id.containerContent) == null) {
            fragmentManager.beginTransaction().add(R.id.containerContent, fragment, fragmentToStart).commitNow();
        } else {
            fragmentManager.beginTransaction().replace(R.id.containerContent, fragment, fragmentToStart).commitNow();
        }
    }

    private Fragment getFragment(String fragmentToStart) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentToStart);
        if(fragment != null) {
            return fragment;
        }

        if(FRAGMENT_SPEAKER.equals(fragmentToStart)) {
            return SpeakersFragment.newInstance();
        } else if(FRAGMENT_SESSION.equals(fragmentToStart)) {
            return ScheduleFragment.newInstance();
        }

        throw new IllegalStateException("No start fragment found");
    }
}
