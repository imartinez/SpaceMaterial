package imartinez.com.spacematerial.isslocation;

import imartinez.com.spacematerial.connectivity.ConnectivityController;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;

import java.io.IOException;

/**
 * TODO: Add class description.
 *
 * Created on 25/10/16.
 */
interface GetIssLocationInteractor {

    Observable<IssLocation> getIssLocation();

    class Impl implements GetIssLocationInteractor {

        private final ConnectivityController connectivityController;
        private final IssLocationNetworkController issLocationNetworkController;

        public Impl(ConnectivityController connectivityController,
                IssLocationNetworkController issLocationNetworkController) {
            this.connectivityController = connectivityController;
            this.issLocationNetworkController = issLocationNetworkController;
        }

        @Override
        public Observable<IssLocation> getIssLocation() {
            return Observable.create(new IssLocationOnSubscribe());
        }

        /**
         * First returns cached location and then tries
         * to fetch the latest location from network. If no location was
         * returned, onError is called with the proper IOException
         */
        private class IssLocationOnSubscribe implements OnSubscribe<IssLocation> {
            @Override
            public void call(Subscriber<? super IssLocation> subscriber) {
                // TODO: 25/10/16 manage errors somehow
                boolean cachedValueReturned;
                try {
                    subscriber.onNext(issLocationNetworkController.getCachedIssLocation());
                    cachedValueReturned = true;
                } catch (IOException e) {
                    // No cached value
                    cachedValueReturned = false;
                }

                if (connectivityController.isConnected()) {
                    try {
                        subscriber.onNext(issLocationNetworkController.fetchIssLocation());
                    } catch (IOException e) {
                        // No network value
                        // If no cache value was returned either, call onError
                        if (!cachedValueReturned) {
                            subscriber.onError(e);
                        }
                    }
                }

                subscriber.onCompleted();
            }
        }
    }

}
