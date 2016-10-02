package de.devfest.data;

import de.devfest.model.Track;
import rx.Observable;

public interface TrackManager {
    Observable<Track> getTrack(String trackId);
}
