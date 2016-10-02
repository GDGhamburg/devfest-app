package de.devfest.data;

import de.devfest.model.Track;
import rx.Observable;

public interface TrackManager {
    /**
     * @param trackId id of the track you want to observe
     * @return hot observable of the track
     */
    Observable<Track> getTrack(String trackId);
}
