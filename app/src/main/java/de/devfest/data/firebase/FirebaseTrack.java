package de.devfest.data.firebase;

public final class FirebaseTrack {
    public String name;

    private FirebaseTrack(Builder builder) {
        name = builder.name;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String name;

        private Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public FirebaseTrack build() {
            return new FirebaseTrack(this);
        }
    }
}
