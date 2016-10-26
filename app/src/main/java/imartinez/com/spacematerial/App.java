package imartinez.com.spacematerial;

import android.app.Application;

import imartinez.com.spacematerial.AppComponent.AppModule;

/**
 * TODO: Add class description.
 *
 * Created on 26/10/16.
 */
public class App extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Dagger application component
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
