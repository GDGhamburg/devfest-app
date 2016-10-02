package de.devfest.model;

public final class Track {
    public final String id;
    public final String name;

    private Track(Builder builder) {
        id = builder.id;
        name = builder.name;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private String id;
        private String name;

        private Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Track build() {
            return new Track(this);
        }
    }
}
