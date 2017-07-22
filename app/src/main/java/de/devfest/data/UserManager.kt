package de.devfest.data

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import de.devfest.model.Session
import de.devfest.model.User
import io.reactivex.Observable
import io.reactivex.Single

interface UserManager {
    /**
     * @return the current logged in user.
     * *
     * @throws java.util.NoSuchElementException if the user is not logged in
     */
    fun getCurrentUser(): Observable<User>

    /**
     * Authenticates the user using google login

     * @param account sign in account by the GoogleApiClient
     * *
     * @return a user item once
     */
    fun authenticateWithGoogle(account: GoogleSignInAccount): Single<User>

    /**
     * @param session session to add to the schedule
     * *
     * @return `true` on success
     */
    fun addToSchedule(session: Session): Single<Boolean>

    /**
     * removes a session from the users schedule

     * @param session session to remove
     * *
     * @return `true` on success
     */
    fun removeFromSchedule(session: Session): Single<Boolean>

    fun loggedInState(): Observable<Boolean>

    fun logout()
}