package imartinez.com.spacematerial.isslocation;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import imartinez.com.spacematerial.AppComponent;
import imartinez.com.spacematerial.connectivity.ConnectivityController;
import imartinez.com.spacematerial.isslocation.IssLocationComponent.IssLocationModule;
import imartinez.com.spacematerial.isslocation.IssLocationComponent.IssLocationScope;
import imartinez.com.spacematerial.net.RetrofitFactory;
import javax.inject.Scope;
import rx.Scheduler;

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
        IssLocationPresenter providesIssLocationPresenter(
                GetIssLocationInteractor getIssLocationInteractor, Scheduler uiScheduler) {
            return new IssLocationPresenter(getIssLocationInteractor, uiScheduler);
        }

        @Provides
        @IssLocationScope
        GetIssLocationInteractor providesGetIssLocationInteractor(
                ConnectivityController connectivityController,
                IssLocationNetworkController issLocationNetworkController) {
            return new GetIssLocationInteractor.Impl(connectivityController,
                    issLocationNetworkController);
        }

        @Provides
        @IssLocationScope
        IssLocationNetworkController providesIssLocationNetworkController(
                RetrofitFactory retrofitFactory) {
            return new IssLocationNetworkController.RetrofitImpl(retrofitFactory);
        }

    }

}
