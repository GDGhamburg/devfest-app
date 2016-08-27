package de.devfest.screens.speakerdetails;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.devfest.R;

public class SpeakerDetailsActivity extends AppCompatActivity {

    public final static String EXTRA_SPEAKER_ID = "EXTRA_SPEAKER_ID";

    public static Intent createIntent(Context context, String speakerId) {
        Intent intent = new Intent(context, SpeakerDetailsActivity.class);
        intent.putExtra(EXTRA_SPEAKER_ID, speakerId);
        return intent;
    }

    public static void start(Activity activity, String speakerId, View srcView) {
        Pair<View, String> pair = Pair.create(srcView, ViewCompat.getTransitionName(srcView));
        @SuppressWarnings("unchecked")
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pair);
        Intent intent = new Intent(activity, SpeakerDetailsActivity.class);
        intent.putExtra(EXTRA_SPEAKER_ID, speakerId);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker_details);
        ActivityCompat.postponeEnterTransition(this);
    }
}
