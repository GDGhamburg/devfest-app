package de.devfest.model;

import org.threeten.bp.ZonedDateTime;

import java.util.List;

public final class EventPart {
    public final String id;
    public final ZonedDateTime startTime;
    public final ZonedDateTime endTime;
    public final String name;
    public final List<Track> tracks;

    private EventPart(Builder builder) {
        id = builder.id;
        startTime = builder.startTime;
        endTime = builder.endTime;
        name = builder.name;
        tracks = builder.tracks;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private String id;
        private ZonedDateTime startTime;
        private ZonedDateTime endTime;
        private List<Track> tracks;
        private String name;

        public Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder startTime(ZonedDateTime val) {
            startTime = val;
            return this;
        }

        public Builder endTime(ZonedDateTime val) {
            endTime = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder tracks(List<Track> tracks) {
            this.tracks = tracks;
            return this;
        }

        public EventPart build() {
            return new EventPart(this);
        }
    }
}
