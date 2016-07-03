package de.devfest.model;

import org.threeten.bp.ZonedDateTime;

import java.util.List;

public final class Session {
    public final String title;
    public final String description;
    public final String language;
    public final String level;
    public final String presentationLink;
    // likely to be changed
    public final String location;
    public final ZonedDateTime dateTime;
    public final List<Speaker> speaker;

    private Session(Builder builder) {
        title = builder.title;
        description = builder.description;
        language = builder.language;
        level = builder.level;
        presentationLink = builder.presentationLink;
        location = builder.location;
        dateTime = builder.dateTime;
        speaker = builder.speaker;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String title;
        private String description;
        private String language;
        private String level;
        private String presentationLink;
        private String location;
        private ZonedDateTime dateTime;
        private List<Speaker> speaker;

        private Builder() {
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

        public Builder level(String val) {
            level = val;
            return this;
        }

        public Builder presentationLink(String val) {
            presentationLink = val;
            return this;
        }

        public Builder location(String val) {
            location = val;
            return this;
        }

        public Builder dateTime(ZonedDateTime val) {
            dateTime = val;
            return this;
        }

        public Builder speaker(List<Speaker> val) {
            speaker = val;
            return this;
        }

        public Session build() {
            return new Session(this);
        }
    }
}
