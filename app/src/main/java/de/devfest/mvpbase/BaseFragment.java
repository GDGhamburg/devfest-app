package de.devfest.mvpbase;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        presenter = inject(((DevFestApplication) getActivity().getApplication()).getComponent());
        presenter.onCreate();
    }

    @Nullable
    @Override
    @CallSuper
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        presenter.attachView((V) this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    protected abstract P inject(ApplicationComponent component);
}
