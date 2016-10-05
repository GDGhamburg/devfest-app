package de.devfest.injection;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import de.devfest.ApplicationModule;
import de.devfest.data.SessionManager;
import de.devfest.data.SpeakerManager;
import de.devfest.data.StageManager;
import de.devfest.data.TrackManager;
import de.devfest.data.UserManager;
import de.devfest.screens.dashboard.DashboardFragment;
import de.devfest.screens.eventpart.EventPartFragment;
import de.devfest.screens.main.MainActivity;
import de.devfest.screens.notstarted.EventNotStartedFragment;
import de.devfest.screens.schedule.ScheduleFragment;
import de.devfest.screens.sessiondetails.SessionDetailsFragment;
import de.devfest.screens.social.SocialFragment;
import de.devfest.screens.speakerdetails.SpeakerDetailsFragment;
import de.devfest.screens.speakers.SpeakersFragment;
import de.devfest.screens.user.UserFragment;

@Singleton
@Component(modules = ApplicationModule.class)
public abstract class ApplicationComponent {
    public static ApplicationComponent init(Application application) {
        return DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(application)).build();
    }

    public abstract void inject(MainActivity activity);
    public abstract void inject(UserFragment fragment);
    public abstract void inject(ScheduleFragment fragment);
    public abstract void inject(SessionDetailsFragment fragment);
    public abstract void inject(DashboardFragment fragment);
    public abstract void inject(SpeakersFragment fragment);
    public abstract void inject(SpeakerDetailsFragment fragment);
    public abstract void inject(SocialFragment fragment);
    public abstract void inject(EventPartFragment fragment);
    public abstract void inject(EventNotStartedFragment fragment);

    abstract SessionManager sessionManager();
    abstract SpeakerManager speakerManager();
    abstract StageManager stageManager();
    abstract TrackManager trackManager();
    abstract UserManager userManager();


}
