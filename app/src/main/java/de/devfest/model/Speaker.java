package de.devfest.model;

public final class Speaker {
    public final String speakerId;
    public final String name;
    public final String photoUrl;
    public final String twitter;
    public final String description;

    private Speaker(Builder builder) {
        speakerId = builder.speakerId;
        name = builder.name;
        photoUrl = builder.photoUrl;
        twitter = builder.twitter;
        description = builder.description;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private String speakerId;
        private String name;
        private String photoUrl;
        private String twitter;
        private String description;

        private Builder() {
        }

        public Builder speakerId(String val) {
            speakerId = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder photoUrl(String val) {
            photoUrl = val;
            return this;
        }

        public Builder twitter(String val) {
            twitter = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Speaker build() {
            return new Speaker(this);
        }
    }
}
