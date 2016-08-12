package de.devfest.model;

import java.util.List;

public final class Speaker {

    public final static String TAG_ANDROID = "android";
    public final static String TAG_WEB = "web";
    public final static String TAG_CLOUD = "cloud";

    public final String speakerId;
    public final String name;
    public final String photoUrl;
    public final String twitter;
    public final String description;
    public final String jobTitle;
    public final String company;
    public final String github;
    public final String gplus;
    public final String website;
    public final List<String> tags;

    private Speaker(Builder builder) {
        speakerId = builder.speakerId;
        name = builder.name;
        photoUrl = builder.photoUrl;
        twitter = builder.twitter;
        description = builder.description;
        company = builder.company;
        jobTitle = builder.jobTitle;
        github = builder.github;
        gplus = builder.gplus;
        website = builder.website;
        tags = builder.tags;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Speaker speaker = (Speaker) o;

        if (!speakerId.equals(speaker.speakerId)) return false;
        if (!name.equals(speaker.name)) return false;
        if (photoUrl != null ? !photoUrl.equals(speaker.photoUrl) : speaker.photoUrl != null) return false;
        if (twitter != null ? !twitter.equals(speaker.twitter) : speaker.twitter != null) return false;
        if (!description.equals(speaker.description)) return false;
        if (company != null ? !company.equals(speaker.company) : speaker.company != null) return false;
        if (jobTitle != null ? !jobTitle.equals(speaker.jobTitle) : speaker.jobTitle != null) return false;
        if (github != null ? !github.equals(speaker.github) : speaker.github != null) return false;
        if (gplus != null ? !gplus.equals(speaker.gplus) : speaker.gplus != null) return false;
        if (website != null ? !website.equals(speaker.website) : speaker.website != null) return false;
        return tags != null ? tags.equals(speaker.tags) : speaker.tags == null;

    }

    @Override
    public int hashCode() {
        int result = speakerId.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (photoUrl != null ? photoUrl.hashCode() : 0);
        result = 31 * result + (twitter != null ? twitter.hashCode() : 0);
        result = 31 * result + description.hashCode();
        result = 31 * result + (company != null ? company.hashCode() : 0);
        result = 31 * result + (jobTitle != null ? jobTitle.hashCode() : 0);
        result = 31 * result + (github != null ? github.hashCode() : 0);
        result = 31 * result + (gplus != null ? gplus.hashCode() : 0);
        result = 31 * result + (website != null ? website.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }

    public static final class Builder {
        private List<String> tags;
        private String speakerId;
        private String name;
        private String photoUrl;
        private String twitter;
        private String description;
        private String company;
        private String jobTitle;
        private String github;
        private String gplus;
        private String website;

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

        public Builder company(String val) {
            company = val;
            return this;
        }

        public Builder jobTitle(String val) {
            jobTitle = val;
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
            this.tags = tags;
            return this;
        }

        public Speaker build() {
            return new Speaker(this);
        }
    }

}
