package de.devfest.screens.speakerdetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import de.devfest.R;

public class SpeakerDetailsActivity extends AppCompatActivity {

    private static String EXTRA_SPEAKER_ID = "EXTRA_SPEAKER_ID";

    public static void start(Activity activity, String speakerId) {
        Intent intent = new Intent(activity, SpeakerDetailsActivity.class);
        intent.putExtra(EXTRA_SPEAKER_ID, speakerId);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_speaker_details);
    }
}
