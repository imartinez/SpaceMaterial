package imartinez.com.spacematerial.isslocation;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import imartinez.com.spacematerial.AppComponent;
import imartinez.com.spacematerial.isslocation.IssLocationComponent.IssLocationModule;
import imartinez.com.spacematerial.isslocation.IssLocationComponent.IssLocationScope;
import imartinez.com.spacematerial.isslocation.IssLocationNetworkController.RetrofitImpl;
import javax.inject.Scope;

/**
 *
 * TODO: Add class description.
 *
 * Created on 26/10/16.
 */
@IssLocationScope
@Component(dependencies = AppComponent.class, modules = IssLocationModule.class)
interface IssLocationComponent {

    void inject(IssLocationFragment issLocationFragment);

    @Scope
    @interface IssLocationScope {
    }

    @Module
    class IssLocationModule {

        @Provides
        @IssLocationScope
        IssLocationNetworkController providesIssLocationNetworkController(
                RetrofitImpl retrofitNetworkController) {
            return retrofitNetworkController;
        }

    }

}