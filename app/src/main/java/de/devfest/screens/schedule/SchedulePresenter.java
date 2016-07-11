package de.devfest.screens.schedule;

import javax.inject.Inject;

import de.devfest.data.SessionManager;
import de.devfest.mvpbase.BasePresenter;

public class SchedulePresenter extends BasePresenter<ScheduleView> {

    private final SessionManager sessionManager;

    @Inject
    public SchedulePresenter(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
}
