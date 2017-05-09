package de.devfest.data.firebase;

import android.net.Uri;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.devfest.R;
import de.devfest.data.SpeakerManager;
import de.devfest.data.UriUtils;
import de.devfest.model.SocialLink;
import de.devfest.model.Speaker;
import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;

public final class FirebaseSpeakerManager implements SpeakerManager {

    private static final String FIREBASE_CHILD_SPEAKER = "speaker";

    private static final String SOCIAL_TWITTER = "twitter";
    private static final String SOCIAL_GITHUB = "github";
    private static final String SOCIAL_GPLUS = "gplus";
    private static final String SOCIAL_WEBSITE = "website";

    private final DatabaseReference reference;

    public FirebaseSpeakerManager(FirebaseDatabase database) {
        this.reference = database.getReference(FIREBASE_CHILD_SPEAKER);
    }

    @Override
    public Observable<Speaker> getSpeakers() {
        return Observable.create(new Observable.OnSubscribe<Speaker>() {
            @Override
            public void call(Subscriber<? super Speaker> subscriber) {
                ValueEventListener listener = new SpeakerExtractor(subscriber, false);
                subscriber.add(Subscriptions.create(() -> reference.removeEventListener(listener)));
                reference.addValueEventListener(listener);
            }
        });
    }

    @Override
    public Observable<Speaker> getSpeaker(String uid) {
        return Observable.create(new Observable.OnSubscribe<Speaker>() {
            @Override
            public void call(Subscriber<? super Speaker> subscriber) {
                SpeakerExtractor speakerExtractor = new SpeakerExtractor(subscriber, true);
                DatabaseReference db = reference.child(uid);
                subscriber.add(Subscriptions.create(() -> db.removeEventListener(speakerExtractor)));
                db.addValueEventListener(new SpeakerExtractor(subscriber, true));
            }
        });
    }

    private static final class SpeakerExtractor extends FirebaseExtractor<Speaker> {
        private SpeakerExtractor(Subscriber<? super Speaker> subscriber, boolean single) {
            super(subscriber, single);
        }

        @Override
        public Speaker convert(DataSnapshot data) {
            FirebaseSpeaker speaker = data.getValue(FirebaseSpeaker.class);

            List<String> sessions = new LinkedList<>();
            if (speaker.sessions != null)
                sessions.addAll(speaker.sessions.keySet());

            return Speaker.newBuilder()
                    .speakerId(data.getKey())
                    .name(speaker.name)
                    .photoUrl(speaker.photoUrl)
                    .description(speaker.description)
                    .company(speaker.company)
                    .companyLogo(speaker.companyLogo)
                    .jobTitle(speaker.jobTitle)
                    .sessions(sessions)
                    .tags(Arrays.asList(speaker.tags.split(",")))
                    .socialLinks(parseSocialLinks(speaker.social))
                    .build();
        }

        private List<SocialLink> parseSocialLinks(Map<String, String> social) {
            List<SocialLink> links = new ArrayList<>();
            if (social != null) {
                if (social.containsKey(SOCIAL_TWITTER)) {
                    String link = social.get(SOCIAL_TWITTER).replace("@", "");
                    String name = "@" + Uri.parse(link).getLastPathSegment();
                    links.add(new SocialLink(name, link, R.drawable.ic_twitter, R.color.twitter));
                }
                if (social.containsKey(SOCIAL_GITHUB)) {
                    String github = social.get(SOCIAL_GITHUB);
                    String name = "/" + Uri.parse(github).getLastPathSegment();
                    links.add(new SocialLink(name, github, R.drawable.ic_github, R.color.github));
                }
                if (social.containsKey(SOCIAL_GPLUS)) {
                    String gplus = social.get(SOCIAL_GPLUS);
                    String name = Uri.parse(gplus).getLastPathSegment();
                    links.add(new SocialLink(name, gplus, R.drawable.ic_gplus, R.color.gplus));
                }
                if (social.containsKey(SOCIAL_WEBSITE)) {
                    String website = UriUtils.ensureScheme(social.get(SOCIAL_WEBSITE));
                    Uri uri = Uri.parse(website);
                    String name = UriUtils.stripWWW(uri.getAuthority());
                    links.add(new SocialLink(name, website, R.drawable.ic_link, 0));
                }
            }
            return links;
        }
    }
}
