package de.devfest.data.firebase;

import java.util.Map;

import de.devfest.model.Speaker;

final class FirebaseSpeaker {
    public String name;
    public String photoUrl;
    public String twitter;
    public String description;
    public String company;
    public String companyLogo;
    public String jobTitle;
    public String website;
    public String github;
    public String gplus;
    public String tags;
    public Map<String, String> sessions;

    public FirebaseSpeaker() {

    }

    public FirebaseSpeaker(Speaker speaker) {
        name = speaker.name;
        photoUrl = speaker.photoUrl;
//        twitter = speaker.twitter;
        description = speaker.description;
        company = speaker.company;
        companyLogo = speaker.companyLogo;
        jobTitle = speaker.jobTitle;
//        website = speaker.website;
//        github = speaker.github;
//        gplus = speaker.gplus;
        tags = speaker.tags.toString().replaceAll("[\\s\\[\\]]", "");
    }
}
