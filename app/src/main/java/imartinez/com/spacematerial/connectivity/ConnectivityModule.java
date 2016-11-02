package imartinez.com.spacematerial.connectivity;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Copyright (c) 2016 DigitasLBi. All rights reserved.
 *
 * TODO: Add class description.
 *
 * Created on 2/11/16.
 */
@Module
public class ConnectivityModule {

    @Provides
    @Singleton
    ConnectivityController providesConnectivityController(
            ConnectivityController.Impl connectivityController) {
        return connectivityController;
    }

}
