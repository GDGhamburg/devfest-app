package de.devfest.screens.schedule;

import javax.inject.Inject;

import de.devfest.data.SessionManager;
import de.devfest.mvpbase.BasePresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SchedulePresenter extends BasePresenter<ScheduleView> {

    private final SessionManager sessionManager;

    @Inject
    public SchedulePresenter(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void onResume() {
        super.onResume();
//        untilOnPause(sessionManager.getSessions()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(session -> {
//                    getView().onSessionRetreived(session);
//                }, error -> {
//                    getView().onError(error);
//                }));
    }
}
