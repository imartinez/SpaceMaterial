package imartinez.com.spacematerial.isslocation;

import imartinez.com.spacematerial.isslocation.IssLocationApi.IssLocationApiResponse;
import imartinez.com.spacematerial.net.RetrofitFactory;
import javax.inject.Inject;

import java.io.IOException;

interface IssLocationNetworkController {

    IssLocation getCachedIssLocation() throws IOException;
    IssLocation fetchIssLocation() throws IOException;

    class RetrofitImpl implements IssLocationNetworkController {

        private final IssLocationApi networkService;
        private final IssLocationApi cacheService;

        @Inject
        public RetrofitImpl(RetrofitFactory retrofitFactory) {
            networkService = retrofitFactory.create(IssLocationApi.class);
            cacheService = retrofitFactory.createCacheOnly(IssLocationApi.class);
        }

        @Override
        public IssLocation getCachedIssLocation() throws IOException {
            // TODO: 23/10/16 Control network error?
            IssLocationApiResponse serviceResponse = cacheService.fetchIssLocation().execute().body();

            if (serviceResponse == null) {
                throw new IOException("No cached IssLocation available");
            }

            return IssLocation.builder()
                    .setTimestamp(serviceResponse.timestamp())
                    .setLatitude(serviceResponse.issPosition().latitude())
                    .setLongitude(serviceResponse.issPosition().longitude())
                    .build();
        }

        @Override
        public IssLocation fetchIssLocation() throws IOException {
            // TODO: 23/10/16 Control network error?
            IssLocationApiResponse serviceResponse = networkService.fetchIssLocation().execute().body();
            return IssLocation.builder()
                    .setTimestamp(serviceResponse.timestamp())
                    .setLatitude(serviceResponse.issPosition().latitude())
                    .setLongitude(serviceResponse.issPosition().longitude())
                    .build();
        }
    }
}
