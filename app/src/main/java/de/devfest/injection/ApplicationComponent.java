package de.devfest.injection;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import de.devfest.ApplicationModule;
import de.devfest.data.SessionManager;
import de.devfest.data.SpeakerManager;
import de.devfest.data.StageManager;
import de.devfest.data.TrackManager;
import de.devfest.screens.main.MainActivity;
import de.devfest.screens.schedule.ScheduleFragment;
import de.devfest.screens.sessions.SessionsFragment;
import de.devfest.screens.social.SocialFragment;
import de.devfest.screens.speakerdetails.SpeakerDetailsFragment;
import de.devfest.screens.speakers.SpeakersFragment;
import de.devfest.screens.user.UserFragment;
import de.devfest.data.UserManager;

@Singleton
@Component(modules = ApplicationModule.class)
public abstract class ApplicationComponent {
    public static ApplicationComponent init(Application application) {
        return DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(application)).build();
    }

    public abstract void inject(MainActivity activity);
    public abstract void inject(UserFragment fragment);
    public abstract void inject(SessionsFragment fragment);
    public abstract void inject(ScheduleFragment fragment);
    public abstract void inject(SpeakersFragment fragment);
    public abstract void inject(SpeakerDetailsFragment fragment);
    public abstract void inject(SocialFragment fragment);

    abstract SessionManager sessionManager();
    abstract SpeakerManager speakerManager();
    abstract StageManager stageManager();
    abstract TrackManager trackManager();
    abstract UserManager userManager();
}
