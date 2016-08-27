package de.devfest.model;

/**
 * Created by andre on 06.07.2016.
 */

public final class Stage {
    public final String id;
    public final String name;
    public final String description;

    private Stage(Builder builder) {
        id = builder.id;
        name = builder.name;
        description = builder.description;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String id;
        private String name;
        private String description;

        private Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Stage build() {
            return new Stage(this);
        }
    }
}
