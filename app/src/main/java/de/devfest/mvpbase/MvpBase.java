package de.devfest.mvpbase;

public interface MvpBase {
    interface View {
        void onLoginRequired();
    }

    interface Presenter<V extends View> {
        void attachView(V view);
        void detachView();
    }
}
