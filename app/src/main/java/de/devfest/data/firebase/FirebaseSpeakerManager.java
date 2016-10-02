package de.devfest.data.firebase;

import android.net.Uri;
import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import de.devfest.R;
import de.devfest.data.SpeakerManager;
import de.devfest.model.SocialLink;
import de.devfest.model.Speaker;
import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;

public final class FirebaseSpeakerManager implements SpeakerManager {

    private static final String FIREBASE_CHILD_SPEAKER = "speaker";
    private static final String URL_PREFIX_WWW = "www.";

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

            List<SocialLink> socialLinks = parseSocialLinks(speaker);
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
                    .socialLinks(socialLinks)
                    .build();
        }

        private List<SocialLink> parseSocialLinks(FirebaseSpeaker speaker) {
            List<SocialLink> links = new ArrayList<>();
            if (!TextUtils.isEmpty(speaker.twitter)) {
                String link = speaker.twitter.replace("@", "");
                String name = "@" + Uri.parse(link).getLastPathSegment();
                links.add(new SocialLink(name, link, R.drawable.ic_twitter, R.color.twitter));
            }
            if (!TextUtils.isEmpty(speaker.github)) {
                String name = "/" + Uri.parse(speaker.github).getLastPathSegment();
                links.add(new SocialLink(name, speaker.github, R.drawable.ic_github, R.color.github));
            }
            if (!TextUtils.isEmpty(speaker.gplus)) {
                String name = Uri.parse(speaker.gplus).getLastPathSegment();
                links.add(new SocialLink(name, speaker.gplus, R.drawable.ic_gplus, R.color.gplus));
            }
            if (!TextUtils.isEmpty(speaker.website)) {
                Uri uri = Uri.parse(speaker.website);
                String name = uri.getAuthority();
                if (name.startsWith(URL_PREFIX_WWW)) name = name.replace(URL_PREFIX_WWW, "");
                links.add(new SocialLink(name, speaker.website, R.drawable.ic_link, 0));
            }
            return links;
        }
    }
}
