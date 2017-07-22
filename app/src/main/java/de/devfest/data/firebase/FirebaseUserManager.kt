package de.devfest.data.firebase

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.devfest.data.UserManager
import de.devfest.model.Session
import de.devfest.model.User
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class FirebaseUserManager @Inject constructor(
        val database: FirebaseDatabase,
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
        val loggedInState: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(firebaseAuth.currentUser != null)

) : UserManager {


    override fun getCurrentUser(): Observable<User> {
        return transformUser(firebaseAuth.currentUser)
    }

    fun transformUser(firebaseUser: FirebaseUser?): Observable<User> {
        return Observable.create { emitter ->
            if (firebaseUser != null) {
                val userRef = database.getReference("users").child(firebaseUser.uid)
                val listener = object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError?) {
                        if (emitter.isDisposed) return
                        emitter.onError(error?.toException() ?: UnknownError())
                    }

                    override fun onDataChange(data: DataSnapshot?) {
                        if (emitter.isDisposed) return
                        if (data != null) {
                            val isAdmin = data.child("admin").getValue(Boolean::class.java) ?: false
                            emitter.onNext(User(firebaseUser.uid,
                                    firebaseUser.email,
                                    firebaseUser.photoUrl.toString(),
                                    firebaseUser.displayName,
                                    isAdmin
                            ))
                            return
                        }
                        emitter.onError(NoSuchElementException("No user logged in!"))
                    }
                }
                userRef.addValueEventListener(listener)
                emitter.setCancellable { userRef.removeEventListener(listener) }
            }
        }
    }

    override fun authenticateWithGoogle(account: GoogleSignInAccount): Single<User> {
        return Single.create<FirebaseUser> { emitter ->
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->
                        if (emitter.isDisposed) return@OnCompleteListener
                        if (!task.isSuccessful) {
                            emitter.onError(task.exception ?: UnknownError())
                            return@OnCompleteListener
                        }
                        val user = task.result?.user!!
                        loggedInState.onNext(true)
                        emitter.onSuccess(user)
                    })
        }
                .flatMapObservable { user -> transformUser(user) }
                .firstOrError()
    }


    /**
     *     @Override
    public Single<Boolean> addToSchedule(Session sessionToStore) {
    return getCurrentUser()
    .flatMap(user -> Single.create(new Single.OnSubscribe<Boolean>() {
    @Override
    public void call(SingleSubscriber<? super Boolean> singleSubscriber) {
    database.getReference(FIREBASE_USER).child(user.userId)
    .child("schedule").child(sessionToStore.id)
    .setValue(true,
    (databaseError, databaseReference) -> singleSubscriber.onSuccess(true));
    }
    })).onErrorResumeNext(throwable -> Single.just(false));
    }

    @Override
    public Single<Boolean> removeFromSchedule(Session sessionToRemove) {
    return getCurrentUser()
    .flatMap(user -> Single.create(new Single.OnSubscribe<Boolean>() {
    @Override
    public void call(SingleSubscriber<? super Boolean> singleSubscriber) {
    database.getReference(FIREBASE_USER).child(user.userId)
    .child("schedule").child(sessionToRemove.id)
    .setValue(false,
    (databaseError, databaseReference) -> singleSubscriber.onSuccess(true));
    }
    })).onErrorResumeNext(throwable -> Single.just(false));
    }
     */
    override fun addToSchedule(session: Session): Single<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeFromSchedule(session: Session): Single<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loggedInState(): Observable<Boolean> {
        return loggedInState.hide()
    }

    override fun logout() {
        firebaseAuth.signOut()
        loggedInState.onNext(false)
    }
}