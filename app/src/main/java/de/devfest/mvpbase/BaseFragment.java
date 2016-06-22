package de.devfest.mvpbase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import de.devfest.DevFestApplication;
import de.devfest.injection.ApplicationComponent;

/**
 * Every Fragment should extend the BaseFragment, which provides some mvp base functionalities
 */
public abstract class BaseFragment<V extends MvpBase.View, P extends BasePresenter<V>> extends Fragment {

    private P presenter;

    public BaseFragment() {
        if (!(this instanceof MvpBase.View)) {
            throw new IllegalStateException(
                    String.format("The Fragment \"%s\" must implement a View!", this.getClass().getSimpleName()));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inject(((DevFestApplication) getActivity().getApplication()).getComponent());
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        //noinspection unchecked
        presenter.attachView((V) this);
    }

    @Override
    public void onStop() {
        presenter.detachView();
        super.onStop();
    }

    protected abstract P inject(ApplicationComponent component);
}