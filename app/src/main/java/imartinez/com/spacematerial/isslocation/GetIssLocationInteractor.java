package imartinez.com.spacematerial.isslocation;

import javax.inject.Inject;
import rx.Observable;
import rx.functions.Func1;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * TODO: Add class description.
 *
 * Created on 25/10/16.
 */
class GetIssLocationInteractor {

    private static final int LOCATION_POLLING_INTERVAL_SECONDS = 5;
    private static final int MAX_RETRIES_ON_SERVER_FAILURE = 3;

    private final IssLocationNetworkController issLocationNetworkController;

    /**
     * Get cached ISS location from disk cache.
     */
    private Observable<IssLocation> getCachedIssLocationObservable =
            Observable.fromCallable(new Callable<IssLocation>() {
                @Override
                public IssLocation call() throws Exception {
                    return issLocationNetworkController.getCachedIssLocation();
                }
            });

    /**
     * Fetch ISS location from server.
     */
    private Observable<IssLocation> fetchIssLocationObservable =
            Observable.fromCallable(new Callable<IssLocation>() {
                @Override
                public IssLocation call() throws Exception {
                    return issLocationNetworkController.fetchIssLocation();
                }
            });

    /**
     * Polling of ISS location. If the server fails a certain number of times,
     * send onError signal and stop polling.
     */
    private Observable<IssLocation> pollIssLocationObservable =
            Observable.interval(LOCATION_POLLING_INTERVAL_SECONDS, TimeUnit.SECONDS)
                    .flatMap(new Func1<Long, Observable<IssLocation>>() {
                        @Override
                        public Observable<IssLocation> call(Long aLong) {
                            return fetchIssLocationObservable;
                        }
                    })
                    .retry(MAX_RETRIES_ON_SERVER_FAILURE);

    @Inject
    public GetIssLocationInteractor(IssLocationNetworkController issLocationNetworkController) {
        this.issLocationNetworkController = issLocationNetworkController;
    }

    /**
     * First returns cached location. If there is no cached location, tries to fetch
     * from server. If it fails, send onError signal and stop.
     * If there is a cached location or the server responds with valid data,
     * start polling the server.
     */
    public Observable<IssLocation> getIssLocation() {
        return getCachedIssLocationObservable
                .onErrorResumeNext(new Func1<Throwable, Observable<IssLocation>>() {
                    @Override
                    public Observable<IssLocation> call(Throwable throwable) {
                        return fetchIssLocationObservable;
                    }
                })
                .concatWith(pollIssLocationObservable);
    }

}
