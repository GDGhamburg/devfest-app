package de.devfest.notifications;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by andre on 25.06.2016.
 */

public class InstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        FirebaseInstanceId.getInstance().getToken();
    }
}
