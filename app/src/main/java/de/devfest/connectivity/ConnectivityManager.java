package de.devfest.connectivity;

import rx.Observable;

/**
 * The ConnectivityManager is supposed to tell you if you have internet available or not
 */
public interface ConnectivityManager {
    /**
     * Method to observe internet connectivity
     *
     * @return an observable that emits {@code true} if internet access is available, {@code false} if not.
     */
    Observable<Boolean> observeInternetConnectivity();

    /**
     * @return {@code true} if internet is available right now
     */
    boolean hasInternetAccess();
}
