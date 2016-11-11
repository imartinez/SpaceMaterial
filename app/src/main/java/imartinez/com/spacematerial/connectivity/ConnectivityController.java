package imartinez.com.spacematerial.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import io.reactivex.Flowable;
import io.reactivex.processors.PublishProcessor;
import javax.inject.Inject;

/**
 * Controller that handles device internet connectivity.
 */
public interface ConnectivityController {

    Flowable<Boolean> connectivityChangeFlowable();

    class Impl implements ConnectivityController {

        private final PublishProcessor<Boolean> connectivityChangePublishProcessor =
                PublishProcessor.create();
        private final BroadcastReceiver connectivityChangeBroadcastReceiver;

        @Inject
        public Impl(Context context) {
            // Register connectivity change broadcast receiver
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

            connectivityChangeBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    onConnectivityChanged(context);
                }
            };
            context.registerReceiver(connectivityChangeBroadcastReceiver, filter);
            // TODO: 11/11/16 Is it necessary to unregister the receiver? Dagger manages this as a singleton
        }

        @Override
        public Flowable<Boolean> connectivityChangeFlowable() {
            return connectivityChangePublishProcessor;
        }

        private void onConnectivityChanged(Context context) {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityChangePublishProcessor.onNext(isThereConnectivity(connectivityManager));
        }

        private boolean isThereConnectivity(ConnectivityManager connectivityManager) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
    }
}
