package de.devfest.user;

import android.support.annotation.NonNull;
import android.util.NoSuchPropertyException;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.devfest.model.User;
import rx.Observable;
import rx.Subscriber;

public class FirebaseUserManager implements UserManager {

    private final FirebaseAuth firebaseAuth;
    private final FirebaseDatabase database;

    public FirebaseUserManager() {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public Observable<User> getCurrentUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        User user;
        if (firebaseUser != null) {
            return transformUser(firebaseUser);
        }
        return Observable.error(new NoSuchPropertyException("User not logged in!"));
    }

    private Observable<User> transformUser(FirebaseUser firebaseUser) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                boolean admin = false;
                //noinspection InnerClassTooDeeplyNested
                database.getReference("admins").child(firebaseUser.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    subscriber.onNext(dataSnapshot.getValue(Boolean.class));
                                } else {
                                    subscriber.onNext(false);
                                }

                                subscriber.onCompleted();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onNext(false);
                                subscriber.onCompleted();
                            }
                        });

            }
        }).map(isAdmin -> new User.Builder()
                .userId(firebaseUser.getUid())
                .admin(isAdmin)
                .email(firebaseUser.getEmail())
                .photoUrl((firebaseUser.getPhotoUrl() != null)
                        ? firebaseUser.getPhotoUrl().toString() : null)
                .build());

    }

    @Override
    public Observable<User> authenticateWithGoogle(@NonNull GoogleSignInAccount account) {
        return Observable.create(new Observable.OnSubscribe<FirebaseUser>() {
            @Override
            public void call(Subscriber<? super FirebaseUser> subscriber) {
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener(task -> {
                            if (subscriber.isUnsubscribed()) return;
                            if (!task.isSuccessful()) {
                                subscriber.onError(task.getException());
                                return;
                            }
                            subscriber.onNext(task.getResult().getUser());
                            subscriber.onCompleted();
                        });
            }
        }).flatMap(this::transformUser);
    }

}
