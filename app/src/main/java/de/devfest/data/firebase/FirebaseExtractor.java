package de.devfest.data.firebase;

import com.google.firebase.FirebaseException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import rx.Subscriber;

abstract class FirebaseExtractor<T> implements ValueEventListener {
    private final Subscriber<? super T> subscriber;
    private final boolean single;

    FirebaseExtractor(Subscriber<? super T> subscriber, boolean single) {
        this.subscriber = subscriber;
        this.single = single;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (!single) {
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                subscriber.onNext(convert(data));
            }
        } else {
            subscriber.onNext(convert(dataSnapshot));
            subscriber.onCompleted();
        }
    }

    protected abstract T convert(DataSnapshot snapshot);

    @Override
    public void onCancelled(DatabaseError databaseError) {
        subscriber.onError(new FirebaseException(databaseError.getDetails()));
    }
}
