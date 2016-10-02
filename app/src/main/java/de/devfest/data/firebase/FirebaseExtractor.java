package de.devfest.data.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import rx.Subscriber;

public abstract class FirebaseExtractor<T> implements ValueEventListener {
    private final Subscriber<? super T> subscriber;
    private final boolean single;
    private final boolean ishot;


    protected FirebaseExtractor(Subscriber<? super T> subscriber, boolean singleValue, boolean ishot) {
        this.subscriber = subscriber;
        this.single = singleValue;
        this.ishot = ishot;
    }

    protected FirebaseExtractor(Subscriber<? super T> subscriber, boolean singleValue) {
        this(subscriber, singleValue, true);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (!single) {
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                subscriber.onNext(convert(data));
            }
        } else {
            subscriber.onNext(convert(dataSnapshot));
        }
        if (!ishot) subscriber.onCompleted();
    }

    protected abstract T convert(DataSnapshot snapshot);

    @Override
    public void onCancelled(DatabaseError databaseError) {
        subscriber.onError(databaseError.toException());
    }
}
