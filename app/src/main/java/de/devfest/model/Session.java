package de.devfest.model;

import org.threeten.bp.ZonedDateTime;

import java.util.List;

public final class Session {
    public final String id;
    public final String title;
    public final String description;
    public final String language;
    // likely to be changed
    public final Stage stage;
    public final ZonedDateTime startTime;
    public final ZonedDateTime endTime;
    public final List<Speaker> speaker;
    public final Track track;

    private Session(Builder builder) {
        id = builder.id;
        title = builder.title;
        description = builder.description;
        language = builder.language;
        stage = builder.stage;
        startTime = builder.startTime;
        endTime = builder.endTime;
        speaker = builder.speaker;
        track = builder.track;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String id;
        private String title;
        private String description;
        private String language;
        private Stage stage;
        private ZonedDateTime startTime;
        private ZonedDateTime endTime;
        private List<Speaker> speaker;
        private Track track;

        private Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder language(String val) {
            language = val;
            return this;
        }

        public Builder stage(Stage val) {
            stage = val;
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

        public Builder speaker(List<Speaker> val) {
            speaker = val;
            return this;
        }

        public Builder track(Track val) {
            track = val;
            return this;
        }

        public Session build() {
            return new Session(this);
        }
    }
}
