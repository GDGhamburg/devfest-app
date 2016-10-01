package de.devfest.data;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import de.devfest.model.Session;
import de.devfest.model.User;
import rx.Observable;
import rx.Single;

public interface UserManager {
    Observable<User> getCurrentUser();
    Observable<User> authenticateWithGoogle(GoogleSignInAccount account);
    Single<Boolean> addToSchedule(Session session, boolean insert);
}
