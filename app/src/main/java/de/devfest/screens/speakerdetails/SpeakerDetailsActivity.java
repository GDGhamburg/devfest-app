package de.devfest.screens.speakerdetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import de.devfest.R;
import de.devfest.mvpbase.GoogleApiActivity;

public class SpeakerDetailsActivity extends GoogleApiActivity {

    public final static String EXTRA_SPEAKER_ID = "EXTRA_SPEAKER_ID";

    public static Intent createIntent(Context context, String speakerId) {
        Intent intent = new Intent(context, SpeakerDetailsActivity.class);
        intent.putExtra(EXTRA_SPEAKER_ID, speakerId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker_details);
        ActivityCompat.postponeEnterTransition(this);
    }
}
