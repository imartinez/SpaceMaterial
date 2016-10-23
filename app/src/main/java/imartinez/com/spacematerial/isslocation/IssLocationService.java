package imartinez.com.spacematerial.isslocation;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * ISS Location Service used to fetch ISS current location
 * from the network API. Using Retrofit 2.
 */
interface IssLocationService {
    @GET("iss-now.json")
    Call<IssLocationServiceResponse> fetchIssLocation();
}
