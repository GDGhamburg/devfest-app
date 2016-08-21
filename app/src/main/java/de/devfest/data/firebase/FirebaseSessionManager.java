package de.devfest.data.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

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

    private final SpeakerManager speakerManager;
    private final StageManager stageManager;
    private final TrackManager trackManager;
    private final DatabaseReference reference;

    public FirebaseSessionManager(SpeakerManager speakerManager,
                                  StageManager stageManager, TrackManager trackManager) {
        this.reference = FirebaseDatabase.getInstance().getReference(FIREBASE_CHILD_SESSIONS);
        this.speakerManager = speakerManager;
        this.stageManager = stageManager;
        this.trackManager = trackManager;
    }

    @Override
    public Observable<Session> getSessionById(String id) {
        return toSession(Observable.create(subscriber -> {
            reference.child(id).addListenerForSingleValueEvent(new SessionExtractor(subscriber, true));
        }));
    }

    @Override
    public Observable<Session> getSessions() {
        return toSession(Observable.create(subscriber -> {
            SessionExtractor sessionExtractor = new SessionExtractor(subscriber, false);
            subscriber.add(Subscriptions.create(() -> reference.removeEventListener(sessionExtractor)));
            reference.addValueEventListener(sessionExtractor);
        }));
    }

    @Override
    public Observable<Session> getSessions(ZonedDateTime from, ZonedDateTime to) {
        return toSession(Observable.create(subscriber -> {
            long fromTime = from.withZoneSameInstant(ZoneId.of("UTC")).toEpochSecond();
            long toTime = to.withZoneSameInstant(ZoneId.of("UTC")).toEpochSecond();
            reference
                    // TODO: missing somthing !!!
                    .startAt(fromTime)
                    .endAt(toTime)
                    .addValueEventListener(new SessionExtractor(subscriber, false));
        }));
    }

    private Observable<Session> toSession(Observable<FirebaseSession> observable) {
        return observable.flatMap(session -> Observable.zip(
                Observable.just(session),
                stageManager.getStage(session.stage),
                trackManager.getTrack(session.track),
                Observable.from(session.speakers.keySet()).flatMap(speakerManager::getSpeaker).toList(),
                (firebaseSession, stage, track, speakers) -> {
                    ZonedDateTime startTime = ZonedDateTime
                            .ofInstant(Instant.ofEpochSecond(session.datetime), ZoneId.of("UTC"));
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
                            .build();
                }

        ));
    }

    static final class SessionExtractor extends FirebaseExtractor<FirebaseSession> {
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
