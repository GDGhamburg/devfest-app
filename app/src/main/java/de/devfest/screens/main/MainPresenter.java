package de.devfest.screens.main;

import javax.inject.Inject;

import de.devfest.connectivity.ConnectivityManager;
import de.devfest.mvpbase.BasePresenter;
import rx.android.schedulers.AndroidSchedulers;

public class MainPresenter extends BasePresenter<MainView> {

    private final ConnectivityManager connectivityManager;

    @Inject
    public MainPresenter(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    @Override
    public void onResume() {
        super.onResume();
        untilOnPause(connectivityManager
                .observeInternetConnectivity()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(connected -> {
                    getView().showConnectivity(connected);
                }));
    }
}
