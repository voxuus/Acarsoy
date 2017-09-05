package ua.in.zeusapps.acarsoy.services.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by oleg on 16.08.2017.
 */

public interface AcarsoyApiService {

    @GET("api/?operation=login")
    Call<TokenResponse> getToken(@Query("email") String email, @Query("password") String password);

    @GET("api/?operation=mapdata")
    Call<List<PlantResponse>> getMapData(@Query("token") String token);

    @GET("api/?operation=turbinedata")
    Call<TurbineResponse> getTurbineData(@Query("plant") String plant, @Query("turbine") String turbine, @Query("token") String token);

    @GET("api/?operation=trendlerdata")
    Call<TrendlerResponse> getTrendlerData(@Query("plant") String plant, @Query("token") String token);
}
