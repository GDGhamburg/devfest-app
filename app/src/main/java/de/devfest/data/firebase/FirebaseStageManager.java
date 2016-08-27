package de.devfest.data.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.devfest.data.StageManager;
import de.devfest.model.Stage;
import rx.Observable;
import rx.Subscriber;

public final class FirebaseStageManager implements StageManager {

    private static final String FIREBASE_CHILD_STAGES = "stages";

    private final DatabaseReference reference;

    public FirebaseStageManager() {
        this.reference = FirebaseDatabase.getInstance().getReference(FIREBASE_CHILD_STAGES);
    }

    @Override
    public Observable<Stage> getStage(String stageId) {
        return Observable.create(new Observable.OnSubscribe<Stage>() {
            @Override
            public void call(Subscriber<? super Stage> subscriber) {
                reference.child(stageId).addListenerForSingleValueEvent(new StageExtractor(subscriber, true));
            }
        });
    }

    static final class StageExtractor extends FirebaseExtractor<Stage> {

        StageExtractor(Subscriber<? super Stage> subscriber, boolean single) {
            super(subscriber, single);
        }

        @Override
        protected Stage convert(DataSnapshot data) {
            FirebaseStage stage = data.getValue(FirebaseStage.class);
            return Stage.newBuilder()
                    .id(data.getKey())
                    .name(stage.name)
                    .description(stage.description)
                    .build();
        }
    }
}
