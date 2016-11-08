package imartinez.com.spacematerial.peopleinspace;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.Date;
import java.util.List;

/**
 * People in Space API. Retrofit 2 annotations.
 *
 * Also contains the response auto-value model.
 */
public interface PeopleInSpaceApi {

    String PEOPLE_IN_SPACE_API_BASE_URL = "http://www.howmanypeopleareinspacerightnow.com/";

    @GET("peopleinspace.json")
    Call<PeopleInSpaceApiResponse> fetchPeopleInSpace();

    @AutoValue
    abstract class PeopleInSpaceApiResponse {

        abstract int number();
        abstract List<Person> people();

        public static TypeAdapter<PeopleInSpaceApiResponse> typeAdapter(Gson gson) {
            return new AutoValue_PeopleInSpaceApi_PeopleInSpaceApiResponse.GsonTypeAdapter(gson);
        }

        @AutoValue
        public static abstract class Person {

            abstract String name();
            abstract String biophoto();
            abstract String countryflag();
            abstract Date launchdate();
            abstract int careerdays();
            abstract String title();
            abstract String location();
            abstract String bio();
            abstract String biolink();

            public static TypeAdapter<Person> typeAdapter(Gson gson) {
                return new AutoValue_PeopleInSpaceApi_PeopleInSpaceApiResponse_Person.GsonTypeAdapter(gson);
            }

        }

    }
}
