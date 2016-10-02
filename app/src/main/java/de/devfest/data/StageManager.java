package de.devfest.data;

import de.devfest.model.Stage;
import rx.Observable;

public interface StageManager {
    /**
     * @param stageId id of the stage you want to receive
     * @return hot observable which emits a stage by the given id
     */
    Observable<Stage> getStage(String stageId);
}
