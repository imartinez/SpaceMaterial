package imartinez.com.spacematerial.peopleinspace;

import imartinez.com.spacematerial.base.BasePresenter;
import imartinez.com.spacematerial.base.BasePresenterImpl;
import imartinez.com.spacematerial.peopleinspace.PeopleInSpacePresenter.PeopleInSpaceRouter;
import imartinez.com.spacematerial.peopleinspace.PeopleInSpacePresenter.PeopleInSpaceView;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

import java.util.List;

class PeopleInSpacePresenter extends BasePresenterImpl<PeopleInSpaceView, PeopleInSpaceRouter>
        implements BasePresenter<PeopleInSpaceView, PeopleInSpaceRouter> {

    interface PeopleInSpaceView {
        void presentPeopleInSpace(List<PersonInSpace> peopleInSpace);
        void presentPeopleInSpaceRetrievalError();
    }

    interface PeopleInSpaceRouter {

    }

    private final GetPeopleInSpaceInteractor getPeopleInSpaceInteractor;
    private final Scheduler uiScheduler;

    @Inject
    public PeopleInSpacePresenter(GetPeopleInSpaceInteractor getPeopleInSpaceInteractor,
            Scheduler uiScheduler) {
        this.getPeopleInSpaceInteractor = getPeopleInSpaceInteractor;
        this.uiScheduler = uiScheduler;
    }

    @Override
    public void onViewReady() {
        super.onViewReady();

        getPeopleInSpace();
    }

    public void onPersonSelected(PersonInSpace personSelected) {

    }

    void onRetrySelected() {
        getPeopleInSpace();
    }

    private void getPeopleInSpace() {
        track(getPeopleInSpaceInteractor.getPeopleInSpace()
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
