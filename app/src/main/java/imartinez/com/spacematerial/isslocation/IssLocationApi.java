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
    @GET("iss-now.json")
    Call<IssLocationApiResponse> fetchIssLocation();

    @AutoValue
    abstract class IssLocationApiResponse {

        abstract String message();
        abstract long timestamp();
        @SerializedName("iss_position") abstract IssLocationApiResponse.IssPosition issPosition();

        public static TypeAdapter<IssLocationApiResponse> typeAdapter(Gson gson) {
            return new AutoValue_IssLocationApi_IssLocationApiResponse.GsonTypeAdapter(gson);
        }

        @AutoValue
        public abstract static class IssPosition {

            public static TypeAdapter<IssPosition> typeAdapter(Gson gson) {
                return new AutoValue_IssLocationApi_IssLocationApiResponse_IssPosition.GsonTypeAdapter(gson);
            }

            abstract double latitude();
            abstract double longitude();

        }
    }
}
