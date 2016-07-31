package de.devfest.data.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.devfest.data.TrackManager;
import de.devfest.model.Track;
import rx.Observable;
import rx.Subscriber;

public final class FirebaseTrackManager implements TrackManager {

    private static final String FIREBASE_CHILD_TRACKS = "tracks";
    private final DatabaseReference reference;


    public FirebaseTrackManager() {
        this.reference = FirebaseDatabase.getInstance().getReference(FIREBASE_CHILD_TRACKS);
    }

    @Override
    public Observable<Track> getTrack(String trackId) {
        return Observable.create(new Observable.OnSubscribe<Track>() {
            @Override
            public void call(Subscriber<? super Track> subscriber) {
                reference.child(trackId).addListenerForSingleValueEvent(new TrackExctractor(subscriber, true));
            }
        });
    }

    static final class TrackExctractor extends FirebaseExtractor<Track> {

        TrackExctractor(Subscriber<? super Track> subscriber, boolean single) {
            super(subscriber, single);
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
