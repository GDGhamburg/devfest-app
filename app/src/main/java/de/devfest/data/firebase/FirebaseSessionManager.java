package de.devfest.data.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.devfest.data.SessionManager;
import de.devfest.data.SpeakerManager;
import de.devfest.data.StageManager;
import de.devfest.data.TrackManager;
import de.devfest.model.Session;
import de.devfest.model.Speaker;
import de.devfest.model.Stage;
import de.devfest.model.Track;
import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;

import static org.threeten.bp.temporal.ChronoUnit.SECONDS;

public final class FirebaseSessionManager implements SessionManager {

    private static final String FIREBASE_CHILD_SESSIONS = "sessions";

    private final FirebaseDatabase database;
    private final SpeakerManager speakerManager;
    private final StageManager stageManager;
    private final TrackManager trackManager;

    public FirebaseSessionManager(FirebaseDatabase database, SpeakerManager speakerManager,
                                  StageManager stageManager, TrackManager trackManager) {
        this.database = database;
        this.speakerManager = speakerManager;
        this.stageManager = stageManager;
        this.trackManager = trackManager;
    }

    @Override
    public Observable<Session> getSessions() {
        return Observable.create(new Observable.OnSubscribe<Session>() {
            @Override
            public void call(Subscriber<? super Session> subscriber) {
                SessionExtractor sessionExtractor =
                        new SessionExtractor(subscriber, speakerManager, stageManager, trackManager, false);
                subscriber.add(Subscriptions.create(() ->
                        database.getReference(FIREBASE_CHILD_SESSIONS).removeEventListener(sessionExtractor)));
                database.getReference(FIREBASE_CHILD_SESSIONS).addValueEventListener(sessionExtractor);
            }
        });
    }

    static final class SessionExtractor extends FirebaseExtractor<Session> {

        private final SpeakerManager speakerManager;
        private final StageManager stageManager;
        private final TrackManager trackManager;

        SessionExtractor(Subscriber<? super Session> subscriber, SpeakerManager speakerManager,
                         StageManager stageManager, TrackManager trackManager, boolean single) {
            super(subscriber, single);
            this.speakerManager = speakerManager;
            this.stageManager = stageManager;
            this.trackManager = trackManager;
        }

        @Override
        protected Session convert(DataSnapshot data) {
            FirebaseSession session = data.getValue(FirebaseSession.class);
            ZonedDateTime startTime = ZonedDateTime
                    .ofInstant(Instant.ofEpochSecond(session.datetime), ZoneId.of("UTC"));
            ZonedDateTime endTime = ZonedDateTime.from(startTime).plus(session.duration, SECONDS);
            List<Speaker> speakers = new LinkedList<>();
            for (Map.Entry<String, String> pair : session.speakers.entrySet()) {
                Speaker speaker = speakerManager.getSpeaker(pair.getKey()).toBlocking().single();
                speakers.add(speaker);
            }
            Stage stage = stageManager.getStage(session.stage).toBlocking().single();
            Track track = trackManager.getTrack(session.track).toBlocking().single();

            return Session.newBuilder()
                    .id(data.getKey())
                    .title(session.title)
                    .description(session.description)
                    .language(session.language)
                    .startTime(startTime)
                    .endTime(endTime)
                    .speaker(speakers)
                    .stage(stage)
                    .track(track)
                    .build();
        }
    }
}
