package de.devfest.data;

import de.devfest.model.Track;
import rx.Observable;

/**
 * Created by andre on 06.07.2016.
 */

public interface TrackManager {
    Observable<Track> getTrack(String trackId);
}
