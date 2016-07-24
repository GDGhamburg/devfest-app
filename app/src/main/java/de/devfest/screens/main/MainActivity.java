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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import de.devfest.R;
import de.devfest.databinding.ActivityMainBinding;
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
            showFragment(ScheduleFragment.TAG);
            binding.navView.setCheckedItem(R.id.nav_schedule);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_schedule:
                showFragment(ScheduleFragment.TAG);
                break;
            case R.id.nav_sessions:
                showFragment(SessionsFragment.TAG);
                break;
            case R.id.nav_speakers:
                showFragment(SpeakersFragment.TAG);
                break;
            case R.id.nav_social:
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
        if(fragment != null) {
            return fragment;
        }

        if (SpeakersFragment.TAG.equals(tag)) {
            return new SpeakersFragment();
        } else if (SessionsFragment.TAG.equals(tag)) {
            return new SessionsFragment();
        } else if (ScheduleFragment.TAG.equals(tag)) {
            return new ScheduleFragment();
        } else if (SocialFragment.TAG.equals(tag)) {
            return new SocialFragment();
        }

        throw new IllegalStateException("No start fragment found");
    }

    @Override
    public void onBackPressed() {
        FragmentManager.BackStackEntry backStackEntry = getSupportFragmentManager().getBackStackEntryAt(0);
        if (!getSupportFragmentManager().popBackStackImmediate(backStackEntry.getId(), 0)) {
            finish();
        }
    }
}
