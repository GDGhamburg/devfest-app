package de.devfest.data;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import de.devfest.model.Session;
import de.devfest.model.User;
import rx.Observable;
import rx.Single;

public interface UserManager {
    /**
     * @return the current logged in user.
     * @throws java.util.NoSuchElementException if the user is not logged in
     */
    Single<User> getCurrentUser();

    /**
     * Authenticates the user using google login
     *
     * @param account sign in account by the GoogleApiClient
     * @return a user item once
     */
    Single<User> authenticateWithGoogle(GoogleSignInAccount account);

    /**
     * @param session session to add to the schedule
     * @return <code>true</code> on success
     */
    Single<Boolean> addToSchedule(Session session);

    /**
     * removes a session from the users schedule
     *
     * @param session session to remove
     * @return <code>true</code> on success
     */
    Single<Boolean> removeFromSchedule(Session session);

    Observable<Boolean> loggedInState();

    void logout();
}
