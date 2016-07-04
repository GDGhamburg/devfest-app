package de.devfest.notifications;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import timber.log.Timber;

public class NotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Timber.d("From: %s", remoteMessage.getFrom());
        Timber.d("Notification Message Body: %s", remoteMessage.getNotification().getBody());
    }
}
