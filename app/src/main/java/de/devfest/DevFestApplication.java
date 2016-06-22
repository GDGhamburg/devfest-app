package de.devfest;

import android.app.Application;

import de.devfest.injection.ApplicationComponent;

public class DevFestApplication extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = ApplicationComponent.init(this);
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}
