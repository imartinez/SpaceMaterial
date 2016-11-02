package imartinez.com.spacematerial.net;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import okhttp3.Cache;

/**
 *
 * TODO: Add class description.
 *
 * Created on 26/10/16.
 */
@Module
public class NetModule {

    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

}
