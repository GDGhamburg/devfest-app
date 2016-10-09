package de.devfest.model;

public class ScheduleSession {

    public final Session session;
    public boolean isScheduled;

    public ScheduleSession(Session session, boolean isScheduled) {
        this.session = session;
        this.isScheduled = isScheduled;
    }
}
