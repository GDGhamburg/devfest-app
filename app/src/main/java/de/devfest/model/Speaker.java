package de.devfest.model;

import java.util.List;

public final class Speaker {

    public final static String TAG_ANDROID = "android";
    public final static String TAG_WEB = "web";
    public final static String TAG_CLOUD = "cloud";
    public final static String TAG_GDE = "gde";
    public final static String TAG_GDG = "gdg";

    public final String speakerId;
    public final String name;
    public final String photoUrl;
    public final String description;
    public final String jobTitle;
    public final String company;
    public final String companyLogo;
    public final List<String> tags;
    public final List<SocialLink> socialLinks;
    public final List<String> sessions;

    private Speaker(Builder builder) {
        speakerId = builder.speakerId;
        name = builder.name;
        photoUrl = builder.photoUrl;
        description = builder.description;
        company = builder.company;
        companyLogo = builder.companyLogo;
        jobTitle = builder.jobTitle;
        tags = builder.tags;
        socialLinks = builder.socialLinks;
        sessions = builder.sessions;
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
        if (!description.equals(speaker.description)) return false;
        if (company != null ? !company.equals(speaker.company) : speaker.company != null) return false;
        if (companyLogo != null ? !companyLogo.equals(speaker.companyLogo) : speaker.companyLogo != null) return false;
        if (jobTitle != null ? !jobTitle.equals(speaker.jobTitle) : speaker.jobTitle != null) return false;
        return tags != null ? tags.equals(speaker.tags) : speaker.tags == null;

    }

    @Override
    public int hashCode() {
        int result = speakerId.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (photoUrl != null ? photoUrl.hashCode() : 0);
        result = 31 * result + description.hashCode();
        result = 31 * result + (company != null ? company.hashCode() : 0);
        result = 31 * result + (companyLogo != null ? companyLogo.hashCode() : 0);
        result = 31 * result + (jobTitle != null ? jobTitle.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (socialLinks != null ? socialLinks.hashCode() : 0);
        return result;
    }

    public static final class Builder {
        private List<String> tags;
        private List<SocialLink> socialLinks;
        private String speakerId;
        private String name;
        private String photoUrl;
        private String description;
        private String company;
        private String companyLogo;
        private String jobTitle;
        private List<String> sessions;

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

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder company(String val) {
            company = val;
            return this;
        }

        public Builder companyLogo(String val) {
            companyLogo = val;
            return this;
        }

        public Builder jobTitle(String val) {
            jobTitle = val;
            return this;
        }

        public Builder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }

        public Builder socialLinks(List<SocialLink> socialLinks) {
            this.socialLinks = socialLinks;
            return this;
        }

        public Builder sessions(List<String> sessions) {
            this.sessions = sessions;
            return this;
        }

        public Speaker build() {
            return new Speaker(this);
        }
    }

}
