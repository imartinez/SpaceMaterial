package imartinez.com.spacematerial.connectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Inject;

/**
 * Controller that handles device internet connectivity.
 */
public interface ConnectivityController {

    boolean isConnected();

    class Impl implements ConnectivityController {

        private final ConnectivityManager connectivityManager;

        @Inject
        public Impl(Context context) {
            connectivityManager =
                    (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        @Override
        public boolean isConnected() {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
    }

}
