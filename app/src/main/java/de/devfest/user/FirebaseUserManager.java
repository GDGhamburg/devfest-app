package de.devfest.user;

import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import de.devfest.model.User;
import rx.Observable;
import rx.Subscriber;

public class FirebaseUserManager implements UserManager {

    private final FirebaseAuth firebaseAuth;

    public FirebaseUserManager() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public User getCurrentUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        User user;
        if (firebaseUser != null) {
            return transformUser(firebaseUser);
        }
        return null;
    }

    private User transformUser(FirebaseUser firebaseUser) {
        return new User.Builder()
                .userId(firebaseUser.getUid())
                .email(firebaseUser.getEmail())
                .photoUrl((firebaseUser.getPhotoUrl() != null)
                        ? firebaseUser.getPhotoUrl().toString() : null)
                .build();
    }

    @Override
    public Observable<User> authenticate(@NonNull GoogleSignInAccount account) {
        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> subscriber) {
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener(task -> {
                            if (subscriber.isUnsubscribed()) return;
                            if (!task.isSuccessful()) {
                                subscriber.onError(task.getException());
                                subscriber.onCompleted();
                                return;
                            }
                            subscriber.onNext(transformUser(task.getResult().getUser()));
                            subscriber.onCompleted();
                        });
            }
        });
    }
}
