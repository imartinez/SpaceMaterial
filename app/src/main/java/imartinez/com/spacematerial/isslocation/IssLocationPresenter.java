package imartinez.com.spacematerial.isslocation;

import imartinez.com.spacematerial.base.BasePresenter;
import imartinez.com.spacematerial.base.BasePresenterImpl;
import imartinez.com.spacematerial.isslocation.IssLocationPresenter.IssLocationRouter;
import imartinez.com.spacematerial.isslocation.IssLocationPresenter.IssLocationView;
import javax.inject.Inject;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 *
 * TODO: Add class description.
 *
 * Created on 26/10/16.
 */
class IssLocationPresenter extends BasePresenterImpl<IssLocationView, IssLocationRouter> implements
        BasePresenter<IssLocationView, IssLocationRouter> {

    interface IssLocationView {
        // TODO: 26/10/16 Use viewModel? Current model is immutable
        void presentIssLocation(IssLocation issLocation);
        void presentIssRetrievalError();
    }

    interface IssLocationRouter {

    }

    private final GetIssLocationInteractor getIssLocationInteractor;
    private final Scheduler uiScheduler;

    @Inject
    public IssLocationPresenter(GetIssLocationInteractor getIssLocationInteractor,
            Scheduler uiScheduler) {
        this.getIssLocationInteractor = getIssLocationInteractor;
        this.uiScheduler = uiScheduler;
    }

    public void onMapReady() {
        getIssLocation();
    }

    public void onRetrySelected() {
        getIssLocation();
    }

    private void getIssLocation() {
        track(getIssLocationInteractor.getIssLocation()
                .subscribeOn(Schedulers.io())
                .observeOn(uiScheduler)
                .subscribe(new Subscriber<IssLocation>() {
                    // TODO: 2/11/16 Is there any subscriber with an empty default implementation of onCompleted?
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        getView().presentIssRetrievalError();
                    }

                    @Override
                    public void onNext(IssLocation issLocation) {
                        getView().presentIssLocation(issLocation);
                    }
                })
        );
    }
}
