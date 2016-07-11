package de.devfest.data.firebase;

final class FirebaseStage {
    public String id;
    public String name;

    private FirebaseStage(Builder builder) {
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

        public FirebaseStage build() {
            return new FirebaseStage(this);
        }
    }
}
