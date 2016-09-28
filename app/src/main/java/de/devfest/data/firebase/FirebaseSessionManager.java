package de.devfest.data.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import java.util.List;

import dagger.Lazy;
import de.devfest.data.SessionManager;
import de.devfest.data.SpeakerManager;
import de.devfest.data.StageManager;
import de.devfest.data.TrackManager;
import de.devfest.model.EventPart;
import de.devfest.model.Session;
import de.devfest.model.Track;
import rx.Observable;
import rx.Single;
import rx.Subscriber;

import static org.threeten.bp.temporal.ChronoUnit.SECONDS;

public final class FirebaseSessionManager implements SessionManager {

    private static final String FIREBASE_CHILD_SESSIONS = "sessions";
    private static final String FIREBASE_CHILD_TRACK = "track";

    private final Lazy<SpeakerManager> speakerManager;
    private final Lazy<StageManager> stageManager;
    private final Lazy<TrackManager> trackManager;
    private final DatabaseReference reference;

    public FirebaseSessionManager(FirebaseDatabase database, Lazy<SpeakerManager> speakerManager,
                                  Lazy<StageManager> stageManager, Lazy<TrackManager> trackManager) {
        this.reference = database.getReference(FIREBASE_CHILD_SESSIONS);
        this.speakerManager = speakerManager;
        this.stageManager = stageManager;
        this.trackManager = trackManager;
    }

    @Override
    public Single<Session> getSessionById(String id) {
        return toSession(Observable.create(subscriber -> {
            reference.child(id).addListenerForSingleValueEvent(new SessionExtractor(subscriber, true));
        })).toSingle();
    }

    @Override
    public Single<List<Session>> getSessions() {
        return toSession(Observable.create(subscriber -> {
            reference.addListenerForSingleValueEvent(new SessionExtractor(subscriber, false));
        })).toList().toSingle();
    }

    @Override
    public Single<List<Session>> getSessions(Track track, EventPart eventPart) {
        return toSession(Observable.create(subscriber -> {
            long fromTime = eventPart.startTime.withZoneSameInstant(ZoneId.of("UTC")).toEpochSecond();
            long toTime = eventPart.endTime.withZoneSameInstant(ZoneId.of("UTC")).toEpochSecond();
            reference
                    .startAt(fromTime)
                    .endAt(toTime)
                    .orderByChild(FIREBASE_CHILD_TRACK)
                    .equalTo(track.id)
                    .addListenerForSingleValueEvent(new SessionExtractor(subscriber, false));
        })).toList().toSingle();
    }

    private Observable<Session> toSession(Observable<FirebaseSession> observable) {
        return observable.flatMap(session -> Observable.zip(
                Observable.just(session),
                stageManager.get().getStage(session.stage).toObservable(),
                trackManager.get().getTrack(session.track).toObservable(),
                Observable.from(session.speakers.keySet()).flatMap(id -> speakerManager.get().getSpeaker(id).toObservable()).toList(),
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
