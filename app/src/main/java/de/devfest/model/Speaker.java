package de.devfest.model;

import com.google.common.collect.ImmutableList;

import java.util.List;

public final class Speaker {
    public final String speakerId;

    public final String name;
    public final String photoUrl;
    public final String twitter;
    public final String description;
    public final String company;
    public final String github;
    public final String gplus;
    public final String website;
    public final ImmutableList<String> tags;

    private Speaker(Builder builder) {
        speakerId = builder.speakerId;
        name = builder.name;
        photoUrl = builder.photoUrl;
        twitter = builder.twitter;
        description = builder.description;
        company = builder.company;
        github = builder.github;
        gplus = builder.gplus;
        website = builder.website;
        tags = builder.tags;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private ImmutableList<String> tags;
        private String speakerId;
        private String name;
        private String photoUrl;
        private String twitter;
        private String description;
        private String company;
        private String github;
        private String gplus;
        private String website;

        private Builder() {
            tags = ImmutableList.of();
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

        public Builder company(String val) {
            company = val;
            return this;
        }

        public Builder github(String val) {
            github = val;
            return this;
        }

        public Builder gplus(String val) {
            gplus = val;
            return this;
        }

        public Builder website(String val) {
            website = val;
            return this;
        }

        public Builder tags(List<String> tags) {
            this.tags = ImmutableList.copyOf(tags);
            return this;
        }

        public Speaker build() {
            return new Speaker(this);
        }
    }

}
