package imartinez.com.spacematerial;

import android.app.Application;
import android.content.Context;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import imartinez.com.spacematerial.AppComponent.AppModule;
import imartinez.com.spacematerial.connectivity.ConnectivityController;
import imartinez.com.spacematerial.connectivity.ConnectivityModule;
import imartinez.com.spacematerial.net.NetModule;
import imartinez.com.spacematerial.net.RetrofitFactory;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import javax.inject.Singleton;

@Singleton
@Component(modules = {AppModule.class, NetModule.class, ConnectivityModule.class})
public interface AppComponent {
    void inject(MainActivity activity);

    // downstream components need these exposed
    Application application();
    Scheduler uiScheduler();
    RetrofitFactory retrofitFactory();
    ConnectivityController connectivityController();

    @Module
    class AppModule {

        Application application;

        public AppModule(Application application) {
            this.application = application;
        }

        @Provides
        @Singleton
        Application providesApplication() {
            return application;
        }

        @Provides
        @Singleton
        Context providesApplicationContext() {
            return application;
        }

        @Provides
        Scheduler providesUIScheduler() {
            return AndroidSchedulers.mainThread();
        }

    }
}
