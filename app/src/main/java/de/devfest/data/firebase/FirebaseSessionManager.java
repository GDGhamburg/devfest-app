package de.devfest.data.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import java.util.Collection;
import java.util.HashSet;

import dagger.Lazy;
import de.devfest.data.EventManager;
import de.devfest.data.SessionManager;
import de.devfest.data.SpeakerManager;
import de.devfest.data.StageManager;
import de.devfest.data.TrackManager;
import de.devfest.model.Session;
import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;

import static org.threeten.bp.temporal.ChronoUnit.SECONDS;

public final class FirebaseSessionManager implements SessionManager {

    private static final String FIREBASE_CHILD_SESSIONS = "sessions";
    private static final String FIREBASE_CHILD_TRACK = "track";

    private final Lazy<SpeakerManager> speakerManager;
    private final Lazy<StageManager> stageManager;
    private final Lazy<TrackManager> trackManager;
    private final DatabaseReference reference;
    private final Lazy<EventManager> eventManager;

    public FirebaseSessionManager(FirebaseDatabase database, Lazy<SpeakerManager> speakerManager,
                                  Lazy<StageManager> stageManager, Lazy<TrackManager> trackManager,
                                  Lazy<EventManager> eventManager) {
        this.reference = database.getReference(FIREBASE_CHILD_SESSIONS);
        this.speakerManager = speakerManager;
        this.stageManager = stageManager;
        this.trackManager = trackManager;
        this.eventManager = eventManager;
    }

    @Override
    public Observable<Session> getSession(String id) {
        return toSession(Observable.create(subscriber -> {
            ValueEventListener listener = new SessionExtractor(subscriber, true);
            subscriber.add(Subscriptions.create(() ->
                    reference.child(id).removeEventListener(listener)));
            reference.child(id).addValueEventListener(listener);
        }));
    }

    @Override
    public Observable<Session> getSessions(Collection<String> ids) {
        return getSessions()
                .filter(session -> ids.contains(session.id));
    }

    @Override
    public Observable<Session> getSessions() {
        return toSession(Observable.create(subscriber -> {
            ValueEventListener listener = new SessionExtractor(subscriber, false);
            subscriber.add(Subscriptions.create(() -> reference.removeEventListener(listener)));
            reference.addValueEventListener(listener);
        }));
    }

    @Override
    public Observable<Session> getEventPartSessions(String ep, String trackId) {
        return eventManager.get().getEventPart(ep).first()
                .flatMap(eventPart -> toSession(Observable.create(subscriber -> {
                            ValueEventListener listener = new SessionExtractor(subscriber, false);
                            subscriber.add(Subscriptions.create(() -> reference.removeEventListener(listener)));
                            reference.orderByChild(FIREBASE_CHILD_TRACK)
                                    .equalTo(trackId)
                                    .addValueEventListener(listener);
                        }))
                                .filter(item -> item.startTime.isAfter(eventPart.startTime)
                                        || item.startTime.isEqual(eventPart.startTime))
                                .filter(item -> item.endTime.isBefore(eventPart.endTime)
                                        || item.endTime.isEqual(eventPart.endTime))
                );
    }

    @Override
    public Observable<Session> getCurrentlyRunningSessions() {
        ZonedDateTime now = ZonedDateTime.now();
        return getSessions()
                .filter(item -> item.startTime.isAfter(now))
                .filter(item -> item.endTime.isBefore(now));
    }


    private Observable<Session> toSession(Observable<FirebaseSession> observable) {
        return observable.flatMap(session -> Observable.zip(
                Observable.just(session),
                stageManager.get().getStage(session.stage).first(),
                trackManager.get().getTrack(session.track).first(),
                Observable.from(session.speakers != null ? session.speakers.keySet() : new HashSet<>())
                        .flatMap(id -> speakerManager.get().getSpeaker(id).first()).toList(),
                (firebaseSession, stage, track, speakers) -> {
                    ZonedDateTime startTime = ZonedDateTime
                            .ofInstant(Instant.ofEpochSecond(session.datetime), ZoneId.systemDefault());
                    ZonedDateTime endTime = ZonedDateTime.from(startTime).plus(session.duration, SECONDS);
                    return Session.newBuilder()
                            .id(firebaseSession.id)
                            .title(firebaseSession.title)
                            .description(firebaseSession.description)
                            .language(firebaseSession.language)
                            .startTime(startTime)
                            .endTime(endTime)
                            .speaker(speakers)
                            .stage(stage)
                            .track(track)
                            .isScheduable(firebaseSession.isScheduable)
                            .tag((firebaseSession.tag))
                            .build();
                }

        ));
    }

    private static final class SessionExtractor extends FirebaseExtractor<FirebaseSession> {
        SessionExtractor(Subscriber<? super FirebaseSession> subscriber, boolean single) {
            super(subscriber, single);
        }

        @Override
        protected FirebaseSession convert(DataSnapshot data) {
            FirebaseSession session = data.getValue(FirebaseSession.class);
            session.id = data.getKey();
            return session;
        }
    }
}
