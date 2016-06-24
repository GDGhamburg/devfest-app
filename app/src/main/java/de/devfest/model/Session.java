package de.devfest.model;

public final class Session {
    public final String title;
    public final String description;
    public final Speaker speaker;

    private Session(Builder builder) {
        title = builder.title;
        description = builder.description;
        speaker = builder.speaker;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String title;
        private String description;
        private Speaker speaker;

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

        public Builder speaker(Speaker val) {
            speaker = val;
            return this;
        }

        public Session build() {
            return new Session(this);
        }
    }
}
