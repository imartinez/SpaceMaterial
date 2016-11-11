package imartinez.com.spacematerial.isslocation;

import imartinez.com.spacematerial.base.BasePresenter;
import imartinez.com.spacematerial.base.BasePresenterImpl;
import imartinez.com.spacematerial.isslocation.IssLocationPresenter.IssLocationRouter;
import imartinez.com.spacematerial.isslocation.IssLocationPresenter.IssLocationView;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

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

    private boolean pollingPaused = false;

    @Inject
    IssLocationPresenter(GetIssLocationInteractor getIssLocationInteractor,
            Scheduler uiScheduler) {
        this.getIssLocationInteractor = getIssLocationInteractor;
        this.uiScheduler = uiScheduler;
    }

    void onMapReady() {
        getIssLocation();
    }

    void onViewVisibilityChanged(boolean isVisible) {
        if (!isVisible) {
            // Stop polling when the view becomes invisible
            disposeTracked();
            pollingPaused = true;
        } else if (pollingPaused) {
            // Resume pollin when the view becomes visible
            getIssLocation();
        }
    }

    void onRetrySelected() {
        getIssLocation();
    }

    private void getIssLocation() {
        // If there is not an ongoing call and the view is ready, start getting iss location
        track(getIssLocationInteractor.getIssLocation()
                .subscribeOn(Schedulers.io())
                .observeOn(uiScheduler)
                .subscribe(new Consumer<IssLocation>() {
                    @Override
                    public void accept(IssLocation issLocation) throws Exception {
                        getView().presentIssLocation(issLocation);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().presentIssRetrievalError();
                    }
                })
        );
    }
}
