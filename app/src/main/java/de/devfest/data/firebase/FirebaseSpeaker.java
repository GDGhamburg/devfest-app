package de.devfest.data.firebase;

import java.util.HashMap;
import java.util.Map;

import de.devfest.model.SocialLink;
import de.devfest.model.Speaker;

final class FirebaseSpeaker {
    public String name;
    public String photoUrl;
    public String description;
    public String company;
    public String companyLogo;
    public String jobTitle;
    public String tags;
    public Map<String, String> sessions;
    public Map<String, String> social;

    public FirebaseSpeaker() {

    }

    public FirebaseSpeaker(Speaker speaker) {
        name = speaker.name;
        photoUrl = speaker.photoUrl;
        description = speaker.description;
        company = speaker.company;
        companyLogo = speaker.companyLogo;
        jobTitle = speaker.jobTitle;
        if (speaker.socialLinks != null && !speaker.socialLinks.isEmpty()) {
            social = new HashMap<>(speaker.socialLinks.size());
            for (SocialLink link : speaker.socialLinks) {
                social.put(link.name, link.link);
            }
        }
        tags = speaker.tags.toString().replaceAll("[\\s\\[\\]]", "");
    }
}
