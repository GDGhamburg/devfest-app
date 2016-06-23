package de.devfest.model;

/**
 * Created by andre on 23.06.2016.
 */

public final class User {

    public final String userId;
    public final String email;
    public final String photoUrl;

    private User(Builder builder) {
        userId = builder.userId;
        email = builder.email;
        photoUrl = builder.photoUrl;
    }

    public static class Builder {
        private String email;
        private String userId;
        private String photoUrl;

        public Builder() {
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder photoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
