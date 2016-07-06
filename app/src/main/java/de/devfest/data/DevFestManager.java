package de.devfest.data;

public interface DevFestManager {

    SpeakerManager speaker();

    SessionManager sessions();

    TrackManager tracks();

    StageManager stages();

}
