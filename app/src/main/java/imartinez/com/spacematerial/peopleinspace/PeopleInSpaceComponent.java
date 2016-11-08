package imartinez.com.spacematerial.peopleinspace;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import imartinez.com.spacematerial.AppComponent;
import imartinez.com.spacematerial.peopleinspace.PeopleInSpaceComponent.PeopleInSpaceModule;
import imartinez.com.spacematerial.peopleinspace.PeopleInSpaceComponent.PeopleInSpaceScope;
import imartinez.com.spacematerial.peopleinspace.PeopleInSpaceNetworkController.RetrofitImpl;
import javax.inject.Scope;

@PeopleInSpaceScope
@Component(dependencies = AppComponent.class, modules = PeopleInSpaceModule.class)
interface PeopleInSpaceComponent {

    void inject(PeopleInSpaceFragment peopleInSpaceFragment);

    @Scope
    @interface PeopleInSpaceScope {
    }

    @Module
    class PeopleInSpaceModule {

        @Provides
        @PeopleInSpaceComponent.PeopleInSpaceScope
        PeopleInSpaceNetworkController providesPeopleInSpaceNetworkController(
                RetrofitImpl retrofitNetworkController) {
            return retrofitNetworkController;
        }
    }

}
