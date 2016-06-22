package de.devfest.connectivity;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;

import rx.Observable;
import rx.observables.ConnectableObservable;

import static com.github.pwittchen.reactivenetwork.library.ConnectivityStatus.MOBILE_CONNECTED;
import static com.github.pwittchen.reactivenetwork.library.ConnectivityStatus.WIFI_CONNECTED;

/**
 * The implementation of a {@link ConnectivityManager} to tell if you have internet available or not
 */
public final class DevFestConnectivityManager implements ConnectivityManager {

    private final ConnectableObservable<Boolean> observable;


    public DevFestConnectivityManager(@NonNull Context context) {
        ReactiveNetwork network = new ReactiveNetwork();
        observable = network.observeNetworkConnectivity(context)
                // every time network connectivity changes
                .flatMap(state -> {
                    if (MOBILE_CONNECTED.equals(state) || WIFI_CONNECTED.equals(state)) {
                        // check internet if connected to something
                        return network.observeInternetConnectivity().take(1);
                    }
                    return Observable.just(false);
                })
                .distinctUntilChanged()
                .replay(1);
        observable.connect();
    }

    @Override
    public Observable<Boolean> observeInternetConnectivity() {
        return observable;
    }

    @Override
    public boolean hasInternetAccess() {
        return observable.take(1).toBlocking().single();
    }
}