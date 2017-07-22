//package de.devfest.data.firebase;
//
//import android.support.annotation.NonNull;
//
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.GoogleAuthProvider;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.GenericTypeIndicator;
//
//import java.util.HashSet;
//import java.util.Map;
//import java.util.NoSuchElementException;
//import java.util.Set;
//
//import de.devfest.model.Session;
//import de.devfest.model.User;
//import rx.Observable;
//import rx.Single;
//import rx.SingleSubscriber;
//import rx.Subscriber;
//import rx.subjects.BehaviorSubject;
//
//public class FirebaseUserManager implements UserManager {
//
//
//    private static final String FIREBASE_USER = "users";
//
//    private final FirebaseAuth firebaseAuth;
//    private final FirebaseDatabase database;
//
//    private final BehaviorSubject<Boolean> loggedInState = BehaviorSubject.create();
//
//    public FirebaseUserManager(FirebaseDatabase database) {
//        firebaseAuth = FirebaseAuth.getInstance();
//        this.database = database;
//        loggedInState.onNext(firebaseAuth.getCurrentUser() != null);
//    }
//
//    @Override
//    public Single<User> getCurrentUser() {
//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//        if (firebaseUser != null) {
//            return transformUser(firebaseUser).toSingle();
//        }
//        return Single.error(new NoSuchElementException("User not logged in!"));
//    }
//
//    private Observable<User> transformUser(FirebaseUser firebaseUser) {
//        return Observable.create(new Observable.OnSubscribe<User>() {
//            @Override
//            public void call(Subscriber<? super User> subscriber) {
//                database.getReference(FIREBASE_USER).child(firebaseUser.getUid())
//                        .addListenerForSingleValueEvent(
//                                new FirebaseUserExtractor(firebaseUser, subscriber, true)
//                        );
//            }
//        });
//    }
//
//    @Override
//    public Single<User> authenticateWithGoogle(@NonNull GoogleSignInAccount account) {
//        return Single.create(new Single.OnSubscribe<FirebaseUser>() {
//            @Override
//            public void call(SingleSubscriber<? super FirebaseUser> subscriber) {
//                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//                firebaseAuth.signInWithCredential(credential)
//                        .addOnCompleteListener(task -> {
//                            if (subscriber.isUnsubscribed()) return;
//                            if (!task.isSuccessful()) {
//                                subscriber.onError(task.getException());
//                                return;
//                            }
//                            subscriber.onSuccess(task.getResult().getUser());
//                        });
//            }
//        }).flatMapObservable(this::transformUser)
//                .doOnNext(user -> loggedInState.onNext(firebaseAuth.getCurrentUser() != null))
//                .toSingle();
//    }
//
//
//
//    @Override
//    public Observable<Boolean> loggedInState() {
//        return loggedInState.distinctUntilChanged();
//    }
//
//    @Override
//    public void logout() {
//        firebaseAuth.signOut();
//        loggedInState.onNext(false);
//    }
//
//    private static class FirebaseUserExtractor extends FirebaseExtractor<User> {
//
//        private final FirebaseUser firebaseUser;
//
//        FirebaseUserExtractor(FirebaseUser user, Subscriber<? super User> subscriber, boolean single) {
//            super(subscriber, single, false);
//            this.firebaseUser = user;
//        }
//
//        @Override
//        protected User convert(DataSnapshot snapshot) {
//            Set<String> schedule = new HashSet<>();
//            boolean isAdmin = false;
//            if (snapshot.getValue() != null) {
//                if (snapshot.child("admin").exists()) {
//                    isAdmin = snapshot.child("admin").getValue(Boolean.class);
//                }
//                if (snapshot.child("schedule").exists()) {
//                    GenericTypeIndicator<Map<String, Boolean>> t =
//                            new GenericTypeIndicator<Map<String, Boolean>>() {
//                            };
//                    Map<String, Boolean> scheduleData = snapshot.child("schedule").getValue(t);
//                    for (String sessionId : scheduleData.keySet()) {
//                        if (scheduleData.get(sessionId))
//                            schedule.add(sessionId);
//                    }
//                }
//            }
//
//            return new User.Builder()
//                    .userId(firebaseUser.getUid())
//                    .admin(isAdmin)
//                    .schedule(schedule)
//                    .email(firebaseUser.getEmail())
//                    .displayName(firebaseUser.getDisplayName())
//                    .photoUrl((firebaseUser.getPhotoUrl() != null)
//                            ? firebaseUser.getPhotoUrl().toString() : null)
//                    .build();
//        }
//    }
//
//}
