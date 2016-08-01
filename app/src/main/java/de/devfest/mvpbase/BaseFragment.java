package de.devfest.mvpbase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

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
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        presenter.attachView((V) this);
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
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
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    protected abstract P inject(ApplicationComponent component);
}
