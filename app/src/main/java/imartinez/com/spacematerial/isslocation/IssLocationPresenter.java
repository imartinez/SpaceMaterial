package imartinez.com.spacematerial.isslocation;

import imartinez.com.spacematerial.base.BasePresenter;
import imartinez.com.spacematerial.base.BasePresenterImpl;
import imartinez.com.spacematerial.isslocation.IssLocationPresenter.IssLocationRouter;
import imartinez.com.spacematerial.isslocation.IssLocationPresenter.IssLocationView;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

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
    IssLocationPresenter(GetIssLocationInteractor getIssLocationInteractor,
            Scheduler uiScheduler) {
        this.getIssLocationInteractor = getIssLocationInteractor;
        this.uiScheduler = uiScheduler;
    }

    void onMapReady() {
        getIssLocation();
    }

    void onRetrySelected() {
        getIssLocation();
    }

    private void getIssLocation() {
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
