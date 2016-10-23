package imartinez.com.spacematerial.isslocation;

import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class IssLocationRetrofitNetworkController implements IssLocationNetworkController {

    private final IssLocationService service;

    public IssLocationRetrofitNetworkController() {

        // TODO: 23/10/16 Extract and inject all this Retrofit creation
        // Logging interceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        // Gson converter factory
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(
                new GsonBuilder()
                        .registerTypeAdapterFactory(IssLocationServiceAdapterFactory.create())
                        .create());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.open-notify.org/")
                .client(client)
                .addConverterFactory(gsonConverterFactory)
                .build();

        service = retrofit.create(IssLocationService.class);
    }

    @Override
    public IssLocation fetchIssLocation() throws IOException {
        // TODO: 23/10/16 Control network error
        IssLocationServiceResponse serviceResponse = service.fetchIssLocation().execute().body();
        return IssLocation.builder()
                .setTimestamp(serviceResponse.timestamp())
                .setLatitude(serviceResponse.issPosition().latitude())
                .setLongitude(serviceResponse.issPosition().longitude())
                .build();
    }

}
