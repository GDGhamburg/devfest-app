package de.devfest.screens.schedule;

import javax.inject.Inject;

import dagger.Lazy;
import de.devfest.data.SessionManager;
import de.devfest.mvpbase.BasePresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SchedulePresenter extends BasePresenter<ScheduleView> {

    private final Lazy<SessionManager> sessionManager;

    @Inject
    public SchedulePresenter(Lazy<SessionManager> sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void onResume() {
        super.onResume();
        untilOnPause(sessionManager.get().getSessions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(session -> {
                    getView().onSessionRetreived(session);
                }, error -> {
                    getView().onError(error);
                }));
    }
}
