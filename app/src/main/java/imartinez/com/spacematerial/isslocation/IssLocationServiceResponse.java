package imartinez.com.spacematerial.isslocation;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * Auto-value class representing ISS Location Service response.
 */
@AutoValue
abstract class IssLocationServiceResponse {

    abstract String message();
    abstract long timestamp();
    @SerializedName("iss_position") abstract IssPosition issPosition();

    public static TypeAdapter<IssLocationServiceResponse> typeAdapter(Gson gson) {
        return new AutoValue_IssLocationServiceResponse.GsonTypeAdapter(gson);
    }

    @AutoValue
    abstract static class IssPosition {

        public static TypeAdapter<IssPosition> typeAdapter(Gson gson) {
            return new AutoValue_IssLocationServiceResponse_IssPosition.GsonTypeAdapter(gson);
        }

        abstract double latitude();
        abstract double longitude();

    }

}
