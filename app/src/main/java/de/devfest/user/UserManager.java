package de.devfest.user;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import de.devfest.model.User;
import rx.Observable;

public interface UserManager {
    User getCurrentUser();
    Observable<User> authenticate(GoogleSignInAccount account);
}
