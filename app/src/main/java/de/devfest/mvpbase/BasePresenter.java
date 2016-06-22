package de.devfest.mvpbase;

import android.support.annotation.CallSuper;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * This is the basic presenter, which should be extended by all Presenters.
 *
 * @param <V> View to handle within this presenter
 */
public abstract class BasePresenter<V extends MvpBase.View> implements MvpBase.Presenter<V> {
    private V view;
    private CompositeSubscription pauseSubscription;

    @Override
    public void attachView(V mvpView) {
        view = mvpView;
    }

    @Override
    public void detachView() {
        view = null;
    }

    public boolean isViewAttached() {
        return view != null;
    }

    public V getView() {
        return view;
    }


    protected void untilOnPause(Subscription subscription) {
        pauseSubscription.add(subscription);
    }

    @CallSuper
    public void onPause() {
        pauseSubscription.unsubscribe();
    }

    @CallSuper
    public void onResume() {
        pauseSubscription = new CompositeSubscription();
    }

    public void destroy() {
    }

}