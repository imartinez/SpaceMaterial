package imartinez.com.spacematerial.connectivity;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class ConnectivityModule {

    @Provides
    @Singleton
    ConnectivityController providesConnectivityController(
            ConnectivityController.Impl connectivityController) {
        return connectivityController;
    }

}
