package imartinez.com.spacematerial.isslocation;

import java.io.IOException;

import javax.inject.Inject;

import imartinez.com.spacematerial.isslocation.IssLocationApi.IssLocationApiResponse;
import imartinez.com.spacematerial.net.RetrofitFactory;

interface IssLocationNetworkController {

    IssLocation getCachedIssLocation() throws IOException;
    IssLocation fetchIssLocation() throws IOException;

    class RetrofitImpl implements IssLocationNetworkController {

        private final IssLocationApi networkService;
        private final IssLocationApi cacheService;

        @Inject
        RetrofitImpl(RetrofitFactory retrofitFactory) {
            networkService = retrofitFactory.create(IssLocationApi.ISS_LOCATION_API_BASE_URL,
                    IssLocationApi.class);
            cacheService = retrofitFactory.createCacheOnly(IssLocationApi.ISS_LOCATION_API_BASE_URL,
                    IssLocationApi.class);
        }

        @Override
        public IssLocation getCachedIssLocation() throws IOException {
            IssLocationApiResponse serviceResponse = cacheService.fetchIssLocation().execute().body();

            if (serviceResponse == null) {
                throw new IOException("No cached IssLocation available");
            }

            return IssLocation.builder()
                    .setTimestamp(serviceResponse.timestamp())
                    .setLatitude(serviceResponse.latitude())
                    .setLongitude(serviceResponse.longitude())
                    .build();
        }

        @Override
        public IssLocation fetchIssLocation() throws IOException {
            IssLocationApiResponse serviceResponse = networkService.fetchIssLocation().execute().body();
            return IssLocation.builder()
                    .setTimestamp(serviceResponse.timestamp())
                    .setLatitude(serviceResponse.latitude())
                    .setLongitude(serviceResponse.longitude())
                    .build();
        }
    }
}
