package de.devfest.screens.schedule;

import javax.inject.Inject;

import dagger.Lazy;
import de.devfest.data.EventManager;
import de.devfest.mvpbase.BasePresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * This implementation is quite ugly, but since we were a bit short on time,
 * this will do it for now! I would love to have hot observables for data here
 */
public class SchedulePresenter extends BasePresenter<ScheduleView> {

    private final Lazy<EventManager> eventManager;


    @Inject
    public SchedulePresenter(Lazy<EventManager> eventManager) {
        this.eventManager = eventManager;
    }

    @Override
    public void attachView(ScheduleView mvpView) {
        super.attachView(mvpView);
        untilDetach(
                eventManager.get().getEventParts()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(eventPart -> {
                            getView().onEventPartReceived(eventPart);
                        }, error -> {
                            getView().onError(error);
                        })
        );
    }
}
