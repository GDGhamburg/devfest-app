package de.devfest.data;

import de.devfest.model.Stage;
import rx.Observable;

public interface StageManager {
    Observable<Stage> getStage(String stageId);
}
