//package de.devfest.data.firebase;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.ValueEventListener;
//
//import io.reactivex.Emitter;
//import rx.Subscriber;
//import timber.log.Timber;
//
//public abstract class FirebaseExtractor<T> implements ValueEventListener {
//    private final Emitter<? super T> subscriber;
//    private final boolean single;
//    private final boolean ishot;
//
//
//    protected FirebaseExtractor(Emitter<? super T> subscriber, boolean singleValue, boolean ishot) {
//        this.subscriber = subscriber;
//        this.single = singleValue;
//        this.ishot = ishot;
//    }
//
//    protected FirebaseExtractor(Emitter<? super T> subscriber, boolean singleValue) {
//        this(subscriber, singleValue, true);
//    }
//
//    @Override
//    public void onDataChange(DataSnapshot dataSnapshot) {
//        if (!single) {
//            for (DataSnapshot data : dataSnapshot.getChildren()) {
//                try {
//                    subscriber.onNext(convert(data));
//                } catch (Exception e) {
//                    Timber.e(e);
//                }
//            }
//        } else {
//            try {
//                subscriber.onNext(convert(dataSnapshot));
//            } catch (Exception e) {
//                Timber.e(e);
//            }
//        }
//        if (!ishot) subscriber.onCompleted();
//    }
//
//    protected abstract T convert(DataSnapshot snapshot);
//
//    @Override
//    public void onCancelled(DatabaseError databaseError) {
//        subscriber.onError(databaseError.toException());
//    }
//}
