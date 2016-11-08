package imartinez.com.spacematerial.peopleinspace;

import io.reactivex.Flowable;
import javax.inject.Inject;

import java.util.List;
import java.util.concurrent.Callable;

class GetPeopleInSpaceInteractor {

    private final PeopleInSpaceNetworkController peopleInSpaceNetworkController;

    /**
     * Get cached people in space data from disk cache.
     */
    private final Flowable<List<PersonInSpace>> getCachedPeopleInSpaceFlowable =
            Flowable.fromCallable(new Callable<List<PersonInSpace>>() {
                @Override
                public List<PersonInSpace> call() throws Exception {
                    return peopleInSpaceNetworkController.getCachedPeopleInSpace();
                }
            });

    /**
     * Fetch people in space data from server.
     */
    private final Flowable<List<PersonInSpace>> fetchPeopleInSpaceFlowable =
            Flowable.fromCallable(new Callable<List<PersonInSpace>>() {
                @Override
                public List<PersonInSpace> call() throws Exception {
                    return peopleInSpaceNetworkController.fetchPeopleInSpace();
                }
            });

    @Inject
    public GetPeopleInSpaceInteractor(
            PeopleInSpaceNetworkController peopleInSpaceNetworkController) {
        this.peopleInSpaceNetworkController = peopleInSpaceNetworkController;
    }

    /**
     * First returns cached data and then fetches data from network.
     * If there is no cached data nor network data, send onError signal.
     * // TODO: 8/11/16 With this implementation, if cache fails and there is network available, two network calls are made. Fix that.
     */
    Flowable<List<PersonInSpace>> getPeopleInSpace() {
        return getCachedPeopleInSpaceFlowable
                .onErrorResumeNext(fetchPeopleInSpaceFlowable)
                .concatWith(fetchPeopleInSpaceFlowable
                        .onErrorResumeNext(Flowable.<List<PersonInSpace>>empty())
                );
    }

}
