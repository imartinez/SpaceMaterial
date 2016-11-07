package imartinez.com.spacematerial.isslocation;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import javax.inject.Inject;
import org.reactivestreams.Publisher;

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
    private final Flowable<IssLocation> getCachedIssLocationFlowable =
            Flowable.fromCallable(new Callable<IssLocation>() {
                @Override
                public IssLocation call() throws Exception {
                    return issLocationNetworkController.getCachedIssLocation();
                }
            });

    /**
     * Fetch ISS location from server.
     */
    private final Flowable<IssLocation> fetchIssLocationFlowable =
            Flowable.fromCallable(new Callable<IssLocation>() {
                @Override
                public IssLocation call() throws Exception {
                    return issLocationNetworkController.fetchIssLocation();
                }
            });

    /**
     * Polling of ISS location (starting immediately). If the server fails a certain number of
     * times,
     * send onError signal and stop polling.
     */
    private final Flowable<IssLocation> pollIssLocationFlowable =
            Flowable.interval(0, LOCATION_POLLING_INTERVAL_SECONDS, TimeUnit.SECONDS)
                    .flatMap(new Function<Long, Publisher<IssLocation>>() {
                        @Override
                        public Publisher<IssLocation> apply(Long aLong) throws Exception {
                            return fetchIssLocationFlowable;
                        }
                    })
                    .retry(MAX_RETRIES_ON_SERVER_FAILURE);

    @Inject
    GetIssLocationInteractor(IssLocationNetworkController issLocationNetworkController) {
        this.issLocationNetworkController = issLocationNetworkController;
    }

    /**
     * First returns cached location. If there is no cached location, tries to fetch
     * from server. If it fails, send onError signal and stop.
     * If there is a cached location or the server responds with valid data,
     * start polling the server.
     */
    Flowable<IssLocation> getIssLocation() {
        return getCachedIssLocationFlowable
                .onErrorResumeNext(fetchIssLocationFlowable)
                .concatWith(pollIssLocationFlowable);
    }
}
