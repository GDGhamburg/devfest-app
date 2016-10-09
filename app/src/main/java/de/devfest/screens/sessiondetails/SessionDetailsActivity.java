package de.devfest.screens.sessiondetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import de.devfest.R;

public class SessionDetailsActivity extends AppCompatActivity {

    public final static String EXTRA_SESSION_ID = "EXTRA_SESSION_ID";

    public static Intent createIntent(Context context, String sessionId) {
        Intent intent = new Intent(context, SessionDetailsActivity.class);
        intent.putExtra(EXTRA_SESSION_ID, sessionId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_details);
        ActivityCompat.postponeEnterTransition(this);
    }
}
