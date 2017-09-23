package de.devfest.screens.main;

import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import de.devfest.R;

public class ActionBarDrawerToggleHelper {

    private ActionBarDrawerToggle toggle;

    public ActionBarDrawerToggleHelper(Fragment fragment) {
        @SuppressWarnings("ConstantConditions")
        Toolbar toolbar = fragment.getView().findViewById(R.id.toolbar);
        DrawerLayout drawer = ((NavigationDrawerHost) fragment.getHost()).getNavigationDrawer();

        toggle = new ActionBarDrawerToggle(fragment.getActivity(), drawer, toolbar, R.string.nav_drawer_open,
                R.string.nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    public void destroy(Fragment fragment) {
        DrawerLayout drawer = ((NavigationDrawerHost) fragment.getHost()).getNavigationDrawer();
        drawer.removeDrawerListener(toggle);
    }
}
