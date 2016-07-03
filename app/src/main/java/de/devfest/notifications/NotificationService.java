package de.devfest.notifications;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import timber.log.Timber;

/**
 * Created by andre on 25.06.2016.
 */

public class NotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Timber.d("From: " + remoteMessage.getFrom());
        Timber.d("Notification Message Body: " + remoteMessage.getNotification().getBody());
    }
}
