package de.devfest.mvpbase;

import android.support.annotation.NonNull;

import de.devfest.model.User;

public interface MvpBase {
    interface View {
        void onError(Throwable error);
    }

    interface AuthView extends View {
        void startLogin();
        void onSuccessfullLogin(@NonNull User user);
    }

    interface Presenter<V extends View> {
        void attachView(V view);
        void detachView();
    }
}
