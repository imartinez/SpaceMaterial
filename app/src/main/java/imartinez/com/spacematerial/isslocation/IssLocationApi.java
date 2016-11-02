package imartinez.com.spacematerial.isslocation;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * ISS Location API used to fetch ISS current location
 * from the network API. Retrofit 2 annotations.
 *
 * Also contains the response auto-value model.
 */
public interface IssLocationApi {

    String ISS_LOCATION_API_BASE_URL = "https://api.wheretheiss.at/v1/";

    @GET("satellites/25544")
    Call<IssLocationApiResponse> fetchIssLocation();

    @AutoValue
    abstract class IssLocationApiResponse {

        abstract double latitude();
        abstract double longitude();
        abstract double altitude();
        abstract double velocity();
        abstract String visibility();
        abstract long timestamp();

        public static TypeAdapter<IssLocationApiResponse> typeAdapter(Gson gson) {
            return new AutoValue_IssLocationApi_IssLocationApiResponse.GsonTypeAdapter(gson);
        }

    }
}
