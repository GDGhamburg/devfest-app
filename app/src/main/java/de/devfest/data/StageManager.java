package de.devfest.data;

import de.devfest.model.Stage;
import rx.Single;

public interface StageManager {
    Single<Stage> getStage(String stageId);
}
