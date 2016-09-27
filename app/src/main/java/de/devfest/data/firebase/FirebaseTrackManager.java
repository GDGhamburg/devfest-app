package de.devfest.data.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import de.devfest.data.TrackManager;
import de.devfest.model.Track;
import rx.Single;
import rx.SingleSubscriber;

public final class FirebaseTrackManager implements TrackManager {

    private static final String FIREBASE_CHILD_TRACKS = "tracks";
    private final DatabaseReference reference;


    public FirebaseTrackManager(FirebaseDatabase database) {
        this.reference = database.getReference(FIREBASE_CHILD_TRACKS);
    }

    @Override
    public Single<Track> getTrack(String trackId) {
        return Single.create(new Single.OnSubscribe<Track>() {
            @Override
            public void call(SingleSubscriber<? super Track> singleSubscriber) {
                reference.child(trackId).addListenerForSingleValueEvent(new TrackExctractor(singleSubscriber));
            }
        });
    }

    @Override
    public Single<List<Track>> getTracks() {
        return null;
    }

    static final class TrackExctractor extends FirebaseSingleExtractor<Track> {

        TrackExctractor(SingleSubscriber<? super Track> subscriber) {
            super(subscriber);
        }

        @Override
        protected Track convert(DataSnapshot snapshot) {
            FirebaseTrack track = snapshot.getValue(FirebaseTrack.class);
            return Track.newBuilder()
                    .id(snapshot.getKey())
                    .name(track.name)
                    .build();
        }
    }
}
