package de.devfest.mvpbase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import de.devfest.DevFestApplication;
import de.devfest.injection.ApplicationComponent;

public abstract class BaseActivity<V extends MvpBase.View, P extends BasePresenter<V>> extends AppCompatActivity {

    private P presenter;

    public BaseActivity() {
        if (!(this instanceof MvpBase.View)) {
            throw new IllegalStateException(
                    String.format("The Activity \"%s\" must implement a View!", this.getClass().getSimpleName()));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = inject(((DevFestApplication) getApplication()).getComponent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //noinspection unchecked
        presenter.attachView((V) this);
    }

    @Override
    protected void onStop() {
        presenter.detachView();
        super.onStop();
    }

    protected abstract P inject(ApplicationComponent component);
}
