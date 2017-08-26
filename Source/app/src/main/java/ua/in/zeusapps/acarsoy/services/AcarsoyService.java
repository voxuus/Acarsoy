package ua.in.zeusapps.acarsoy.services;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.in.zeusapps.acarsoy.common.IAsyncCommand;
import ua.in.zeusapps.acarsoy.services.api.AcarsoyApiService;
import ua.in.zeusapps.acarsoy.services.api.Plant;

/**
 * Created by oleg on 17.08.2017.
 */

public class AcarsoyService {

    private AcarsoyApiService mAcarsoyService;

    public AcarsoyService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://acarsoyenerji.robosoftenerji.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mAcarsoyService = retrofit.create(AcarsoyApiService.class);

    }

    public void getPlantsAsync(final IAsyncCommand asyncCommand) {
        mAcarsoyService.getMapData().enqueue(new Callback<List<Plant>>() {
            @Override
            public void onResponse(Call<List<Plant>> call, Response<List<Plant>> response) {
                asyncCommand.onComplete(response.body());
            }

            @Override
            public void onFailure(Call<List<Plant>> call, Throwable t) {
                asyncCommand.onError(t.getMessage());
            }
        });
    }
}
