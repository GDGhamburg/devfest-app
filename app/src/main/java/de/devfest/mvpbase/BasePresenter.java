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
    private CompositeSubscription attachedViewSubscription;

    @Override
    public void attachView(V mvpView) {
        view = mvpView;
        attachedViewSubscription = new CompositeSubscription();
    }

    @Override
    public void detachView() {
        attachedViewSubscription.unsubscribe();
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

    protected void untilDetach(Subscription subscription) {
        attachedViewSubscription.add(subscription);
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
