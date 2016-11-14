package imartinez.com.spacematerial.peopleinspace;

import imartinez.com.spacematerial.base.BasePresenter;
import imartinez.com.spacematerial.base.BasePresenterImpl;
import imartinez.com.spacematerial.connectivity.ConnectivityController;
import imartinez.com.spacematerial.peopleinspace.PeopleInSpacePresenter.PeopleInSpaceRouter;
import imartinez.com.spacematerial.peopleinspace.PeopleInSpacePresenter.PeopleInSpaceView;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

import java.util.List;
import java.util.concurrent.TimeUnit;

class PeopleInSpacePresenter extends BasePresenterImpl<PeopleInSpaceView, PeopleInSpaceRouter>
        implements BasePresenter<PeopleInSpaceView, PeopleInSpaceRouter> {

    interface PeopleInSpaceView {
        void presentPeopleInSpace(List<PersonInSpace> peopleInSpace);
        void presentPeopleInSpaceRetrievalError();
    }

    interface PeopleInSpaceRouter {
        void showPersonInSpaceDetail(PersonInSpace personInSpace);
    }

    private static final int VIEW_UPDATE_MAX_RATE_MILLIS = 200;

    private final ConnectivityController connectivityController;
    private final GetPeopleInSpaceInteractor getPeopleInSpaceInteractor;
    private final Scheduler uiScheduler;

    @Inject
    public PeopleInSpacePresenter(ConnectivityController connectivityController,
            GetPeopleInSpaceInteractor getPeopleInSpaceInteractor,
            Scheduler uiScheduler) {
        this.connectivityController = connectivityController;
        this.getPeopleInSpaceInteractor = getPeopleInSpaceInteractor;
        this.uiScheduler = uiScheduler;
    }

    @Override
    public void onViewReady() {
        super.onViewReady();
        subscribeToConnectivityChanges();
    }

    public void onPersonSelected(PersonInSpace personSelected) {
        getRouter().showPersonInSpaceDetail(personSelected);
    }

    void onViewVisibilityChanged(boolean isVisible) {
        if (isVisible) {
            getPeopleInSpace();
        }
    }

    void onRetrySelected() {
        getPeopleInSpace();
    }

    private void subscribeToConnectivityChanges() {
        // If connectivity changes and there is connectivity, get new data
        track(connectivityController.connectivityChangeFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(uiScheduler)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean hasConnectivity) throws Exception {
                        if (hasConnectivity) {
                            getPeopleInSpace();
                        }
                    }
                }));
    }

    private void getPeopleInSpace() {
        track(getPeopleInSpaceInteractor.getPeopleInSpace()
                .throttleLast(VIEW_UPDATE_MAX_RATE_MILLIS, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(uiScheduler)
                .subscribe(new Consumer<List<PersonInSpace>>() {
                    @Override
                    public void accept(List<PersonInSpace> peopleInSpace) throws Exception {
                        getView().presentPeopleInSpace(peopleInSpace);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().presentPeopleInSpaceRetrievalError();
                    }
                })
        );
    }
}
