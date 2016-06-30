package de.devfest.model;

/**
 * Created by andre on 23.06.2016.
 */

public final class User {

    public final String userId;
    public final String email;
    public final String photoUrl;
    public final String displayName;
    public final boolean admin;

    private User(Builder builder) {
        userId = builder.userId;
        email = builder.email;
        photoUrl = builder.photoUrl;
        displayName = builder.displayName;
        admin = builder.admin;
    }

    public static class Builder {
        public boolean admin;
        private String email;
        private String userId;
        private String photoUrl;
        private String displayName;

        public Builder() {
            this.admin = false;
        }

        public Builder admin(boolean admin) {
            this.admin = admin;
            return this;
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

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
