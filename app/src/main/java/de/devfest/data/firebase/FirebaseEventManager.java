package de.devfest.data.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import dagger.Lazy;
import de.devfest.data.EventManager;
import de.devfest.data.TrackManager;
import de.devfest.model.EventPart;
import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;


public class FirebaseEventManager implements EventManager {

    private static final String FIREBASE_CHILD_DETAILS = "eventdetail";

    private final DatabaseReference reference;
    private final Lazy<TrackManager> trackManager;

    public FirebaseEventManager(FirebaseDatabase database, Lazy<TrackManager> trackManager) {
        this.reference = database.getReference(FIREBASE_CHILD_DETAILS);
        this.trackManager = trackManager;
    }

    @Override
    public Observable<EventPart> getEventParts() {
        return toEventPart(Observable.create(subscriber -> {
            ValueEventListener listener = new EventPartExtractor(subscriber, false);
            subscriber.add(Subscriptions.create(() ->
                    reference.child("eventPart").removeEventListener(listener)));
            reference.child("eventPart").addValueEventListener(listener);
        }));
    }

    @Override
    public Observable<EventPart> getEventPart(String id) {
        return toEventPart(Observable.create(subscriber -> {
            DatabaseReference databaseReference = reference.child("eventPart").child(id);
            ValueEventListener listener = new EventPartExtractor(subscriber, true);
            subscriber.add(Subscriptions.create(() ->
                    databaseReference.removeEventListener(listener)));
            databaseReference.addValueEventListener(listener);
        }));
    }

    @Override
    public Single<Boolean> isActive() {
        return Single.create(new Single.OnSubscribe<Boolean>() {
            @Override
            public void call(SingleSubscriber<? super Boolean> singleSubscriber) {
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ZonedDateTime startTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(dataSnapshot.child("startTime").getValue(Long.class)), ZoneId.of("UTC"));
                        ZonedDateTime endTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(dataSnapshot.child("endTime").getValue(Long.class)), ZoneId.of("UTC"));
                        ZonedDateTime now = ZonedDateTime.now();
                        singleSubscriber.onSuccess(
                                now.isAfter(startTime)
                                        && now.isBefore(endTime)
                        );
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Single.error(databaseError.toException());
                    }
                });
            }
        });
    }

    private Observable<EventPart> toEventPart(Observable<FirebaseEventPart> partObservable) {
        return partObservable.flatMap(part -> Observable.zip(
                Observable.just(part),
                Observable.from(part.tracks.keySet()).filter(item -> part.tracks.get(item))
                        .flatMap(trackId -> trackManager.get().getTrack(trackId).first())
                        .toList(),
                (part1, tracks) -> {
                    ZonedDateTime startTime = ZonedDateTime
                            .ofInstant(Instant.ofEpochSecond(part1.startTime), ZoneId.of("UTC"));
                    ZonedDateTime endTime = ZonedDateTime
                            .ofInstant(Instant.ofEpochSecond(part1.endTime), ZoneId.of("UTC"));
                    return EventPart.newBuilder()
                            .id(part1.id)
                            .tracks(tracks)
                            .name(part1.name)
                            .startTime(startTime)
                            .endTime(endTime)
                            .build();
                }
        ));
    }

    private static class EventPartExtractor extends FirebaseExtractor<FirebaseEventPart> {

        EventPartExtractor(Subscriber<? super FirebaseEventPart> subscriber, boolean single) {
            super(subscriber, single);
        }

        @Override
        protected FirebaseEventPart convert(DataSnapshot snapshot) {
            FirebaseEventPart part = snapshot.getValue(FirebaseEventPart.class);
            part.id = snapshot.getKey();
            return part;
        }
    }
}
