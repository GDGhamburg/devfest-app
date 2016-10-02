package de.devfest.data;

import java.util.List;

import de.devfest.model.Track;
import rx.Single;

public interface TrackManager {
    Single<Track> getTrack(String trackId);
    Single<List<Track>> getTracks();
}
