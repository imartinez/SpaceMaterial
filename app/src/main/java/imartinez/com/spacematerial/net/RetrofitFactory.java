package imartinez.com.spacematerial.net;

import com.google.gson.GsonBuilder;
import javax.inject.Inject;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * TODO: Add class description.
 * TODO ServiceFactory is the proper name?
 *
 * Created on 25/10/16.
 */
public class RetrofitFactory {

    private static final String CACHE_CONTROL = "Cache-Control";
    private static final int FORCED_CACHE_MAX_STALE_DAYS = 7; // tolerate 1-week stale
    private static final int BASIC_CACHE_MAX_AGE_SECONDS = 5; // basic cache seconds

    private final String baseUrl;
    private final GsonConverterFactory gsonConverterFactory;
    private final OkHttpClient baseNetworkClient;
    private final OkHttpClient cacheOnlyNetworkClient;

    private static final Interceptor basicCacheInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());

            CacheControl cacheControl = new CacheControl.Builder()
                    .maxAge(BASIC_CACHE_MAX_AGE_SECONDS, TimeUnit.SECONDS)
                    .build();

            return originalResponse.newBuilder()
                    .header(CACHE_CONTROL, cacheControl.toString())
                    .build();
        }
    };

    private static final Interceptor cacheOnlyInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();

            CacheControl cacheControl = new CacheControl.Builder()
                    .onlyIfCached()
                    .maxStale(FORCED_CACHE_MAX_STALE_DAYS, TimeUnit.DAYS)
                    .build();

            originalRequest = originalRequest.newBuilder()
                    .cacheControl(cacheControl)
                    .build();

            return chain.proceed(originalRequest);
        }
    };

    @Inject
    public RetrofitFactory(Cache cache) {
        // TODO: 25/10/16 Inject the base url
        baseUrl = "http://api.open-notify.org/";
        gsonConverterFactory = createAutoValueGsonConverterFactory();
        baseNetworkClient = createBasicHttpClient(cache);
        cacheOnlyNetworkClient = createCacheOnlyHttpClient(cache);
    }

    /**
     * TODO
     * @param service
     * @param <T>
     * @return
     */
    public <T> T create(final Class<T> service) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(baseNetworkClient)
                .addConverterFactory(gsonConverterFactory)
                .build();

        return retrofit.create(service);
    }

    /**
     * TODO
     * @param service
     * @param <T>
     * @return
     */
    public <T> T createCacheOnly(final Class<T> service) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(cacheOnlyNetworkClient)
                .addConverterFactory(gsonConverterFactory)
                .build();

        return retrofit.create(service);
    }

    /**
     * Create basic OkHttp client with logs and cache.
     * @param cache Cache used to cache results.
     * @return OkHttpClient ready to use client with logs and cache.
     */
    private OkHttpClient createBasicHttpClient(Cache cache) {
        // Logs interceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addNetworkInterceptor(basicCacheInterceptor)
                .cache(cache)
                .build();
    }

    /**
     * Create OkHttp client that is the basic client but only
     * returns results from the cache.
     * @param cache Cache the results are extracted from.
     * @return OkHttpClient ready to use client with lgos and cache. It only
     * returns results from the given cache, never goes to the Internet.
     */
    private OkHttpClient createCacheOnlyHttpClient(Cache cache) {
        // Logs interceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(cacheOnlyInterceptor)
                .cache(cache)
                .build();
    }

    /**
     * Crete GsonConverterFactory handling the conversion of AutoValue objects
     * @return GsonConverterFactory ready to use to convert AutoValue objects
     */
    private GsonConverterFactory createAutoValueGsonConverterFactory() {
        return GsonConverterFactory.create(
                new GsonBuilder()
                        .registerTypeAdapterFactory(AutoValueGsonAdapterFactory.create())
                        .create());
    }

}
