package de.devfest.screens.sessiondetails;

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
import de.devfest.model.Session;

public class SessionDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_SESSION_ID = "EXTRA_SESSION_ID";
    public static final String EXTRA_SESSION_TAG = "EXTRA_SESSION_TAG";

    public static void showWithTransition(Session session, Activity activity, View srcView) {
        Intent intent = SessionDetailsActivity.createIntent(activity, session);
        View titleView = srcView.findViewById(R.id.textSessionTitle);
        Pair<View, String> pair = Pair.create(titleView, ViewCompat.getTransitionName(titleView));
        View subView = srcView.findViewById(R.id.textSessionSub);
        Pair<View, String> pair2 = Pair.create(subView, ViewCompat.getTransitionName(subView));
        @SuppressWarnings("unchecked")
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pair, pair2);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    public static Intent createIntent(Context context, Session session) {
        Intent intent = new Intent(context, SessionDetailsActivity.class);
        intent.putExtra(EXTRA_SESSION_ID, session.id);
        String tag = session.tags == null || session.tags.isEmpty() ? null : session.tags.get(0);
        intent.putExtra(EXTRA_SESSION_TAG, tag);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.postponeEnterTransition(this);
        setContentView(R.layout.activity_session_details);
    }

    @Override
    public void onBackPressed() {
        SessionDetailsFragment fragment = (SessionDetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentSessionDetails);
        if (!fragment.onBackPressed()) {
            super.onBackPressed();
        }
    }
}
