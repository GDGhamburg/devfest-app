package de.devfest.model;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 24/06/16.
 */

public final class Track {
    public final String name;
    public final LocalDate date;
    public final List<Session> sessions;

    private Track(Builder builder) {
        name = builder.name;
        date = builder.date;
        sessions = builder.sessions;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private LocalDate date;
        private List<Session> sessions;

        private Builder() {
            sessions = new ArrayList<>();
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder date(LocalDate val) {
            date = val;
            return this;
        }

        public Builder sessions(List<Session> val) {
            sessions = val;
            return this;
        }

        public Builder addSession(Session session) {
            sessions.add(session);
            return this;
        }

        public Track build() {
            return new Track(this);
        }
    }
}
