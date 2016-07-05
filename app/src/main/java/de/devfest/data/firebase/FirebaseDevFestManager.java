package de.devfest.data.firebase;

import com.google.firebase.FirebaseException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Iterator;

import de.devfest.data.DevFestManager;
import de.devfest.model.Session;
import de.devfest.model.Speaker;
import de.devfest.model.Track;
import rx.Observable;
import rx.Subscriber;

public final class FirebaseDevFestManager implements DevFestManager {

    private static final String FIREBASE_CHILD_SPEAKER = "speaker";


    private final FirebaseDatabase database;

    public FirebaseDevFestManager() {
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public Observable<Speaker> getSpeakers() {
        return Observable.create(new Observable.OnSubscribe<Speaker>() {
            @Override
            public void call(Subscriber<? super Speaker> subscriber) {
                //noinspection InnerClassTooDeeplyNested
                database.getReference(FIREBASE_CHILD_SPEAKER).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                        DataSnapshot data;
                        FirebaseSpeaker speaker;
                        while (iterator.hasNext()) {
                            data = iterator.next();
                            speaker = data.getValue(FirebaseSpeaker.class);
                            subscriber.onNext(Speaker.newBuilder()
                                    .speakerId(data.getKey())
                                    .name(speaker.name)
                                    .photoUrl(speaker.photoUrl)
                                    .description(speaker.description)
                                    .company(speaker.company)
                                    .twitter(speaker.twitter)
                                    .website(speaker.website)
                                    .github(speaker.github)
                                    .gplus(speaker.gplus)
                                    .tags(Arrays.asList(speaker.tags.split(",")))
                                    .build()
                            );
                        }
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        subscriber.onError(new FirebaseException(databaseError.getDetails()));
                    }
                });
            }
        });
    }

    @Override
    public Observable<Speaker> getSpeaker(String uid) {
        return null;
    }

    @Override
    public Observable<Session> getSessions() {
        return null;
    }

    @Override
    public Observable<Session> getSession(String id) {
        return null;
    }

    @Override
    public Observable<Track> getTracks() {
        return null;
    }

    @Override
    public Observable<Track> getTrack(String id) {
        return null;
    }

    @Override
    public Observable<Speaker> insertOrUpdate(Speaker speaker) {
        DatabaseReference databaseReference = database.getReference(FIREBASE_CHILD_SPEAKER);
        String speakerId = (speaker.speakerId == null) ? databaseReference.push().getKey() : speaker.speakerId;
        FirebaseSpeaker firebaseSpeaker = new FirebaseSpeaker(speaker);
        databaseReference.child(speakerId).setValue(firebaseSpeaker);
        return getSpeaker(speakerId);
    }
}
