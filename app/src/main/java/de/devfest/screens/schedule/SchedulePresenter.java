package de.devfest.screens.schedule;

import javax.inject.Inject;

import dagger.Lazy;
import de.devfest.data.SessionManager;
import de.devfest.data.UserManager;
import de.devfest.model.Session;
import de.devfest.mvpbase.BasePresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SchedulePresenter extends BasePresenter<ScheduleView> {

    private final Lazy<SessionManager> sessionManager;
    private final Lazy<UserManager> userManager;

    @Inject
    public SchedulePresenter(Lazy<SessionManager> sessionManager, Lazy<UserManager> userManager) {
        this.sessionManager = sessionManager;
        this.userManager = userManager;
    }

    @Override
    public void onResume() {
        super.onResume();
        userManager.get().getCurrentUser()
                .map(user -> user.schedule)
                .flatMap(ids -> sessionManager.get().getSessions(ids))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        session -> getView().onScheduleSessionReceived(session),
                        error -> getView().onError(error)
                );
    }

    public void removeFromSchedule(Session session) {
        userManager.get().addToSchedule(session, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {
                    // don't do anything for now
                }, error -> getView().onError(error));
    }

}
