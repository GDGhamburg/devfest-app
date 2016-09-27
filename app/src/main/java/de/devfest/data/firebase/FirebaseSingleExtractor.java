package de.devfest.data.firebase;

import com.google.firebase.FirebaseException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import rx.SingleSubscriber;

abstract class FirebaseSingleExtractor<T> implements ValueEventListener {
    private final SingleSubscriber<? super T> subscriber;
    private boolean single;

    FirebaseSingleExtractor(SingleSubscriber<? super T> subscriber) {
        this.subscriber = subscriber;
        this.single = ;
    }
    FirebaseSingleExtractor(SingleSubscriber<? super List<T>> subscriber) {
        this.subscriber = subscriber;
        this.single = true;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (!single) {
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                subscriber.onNext(convert(data));
            }
        } else {
            subscriber.onSuccess(convert(dataSnapshot));
        }
    }

    protected abstract T convert(DataSnapshot snapshot);

    @Override
    public void onCancelled(DatabaseError databaseError) {
        subscriber.onError(new FirebaseException(databaseError.getDetails()));
    }
}
