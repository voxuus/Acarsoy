package ua.in.zeusapps.acarsoy.services;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.in.zeusapps.acarsoy.common.IAsyncCommand;
import ua.in.zeusapps.acarsoy.services.api.AcarsoyApiService;
import ua.in.zeusapps.acarsoy.services.api.PlantResponse;
import ua.in.zeusapps.acarsoy.services.api.TokenRequest;
import ua.in.zeusapps.acarsoy.services.api.TokenResponse;
import ua.in.zeusapps.acarsoy.services.api.TrendlerResponse;
import ua.in.zeusapps.acarsoy.services.api.TurbineRequest;
import ua.in.zeusapps.acarsoy.services.api.TurbineResponse;

/**
 * Created by oleg on 17.08.2017.
 */

public class AcarsoyService {

    private AcarsoyApiService mAcarsoyService;
    private TokenService mTokenService;

    public AcarsoyService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://acarsoyenerji.robosoftenerji.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mAcarsoyService = retrofit.create(AcarsoyApiService.class);
        mTokenService = TokenService.getInstance();

    }

    public void getPlantsAsync(final IAsyncCommand asyncCommand) {
        mAcarsoyService.getMapData(mTokenService.getToken()).enqueue(new Callback<List<PlantResponse>>() {
            @Override
            public void onResponse(Call<List<PlantResponse>> call, Response<List<PlantResponse>> response) {
                asyncCommand.onComplete(response.body());
            }

            @Override
            public void onFailure(Call<List<PlantResponse>> call, Throwable t) {
                asyncCommand.onError(t.getMessage());
            }
        });
    }

    public void getTokenAsync(final IAsyncCommand<TokenRequest, TokenResponse> asyncCommand) {
        mAcarsoyService.getToken(asyncCommand.getParameters().getEmail(), asyncCommand.getParameters().getPassword())
                .enqueue(new Callback<TokenResponse>() {
                    @Override
                    public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                        asyncCommand.onComplete(response.body());
                    }

                    @Override
                    public void onFailure(Call<TokenResponse> call, Throwable t) {
                        asyncCommand.onError(t.getMessage());
                    }
                });
    }

    public void getTurbineDetailsAsync(final IAsyncCommand<TurbineRequest, TurbineResponse> asyncCommand) {
        mAcarsoyService.getTurbineData(asyncCommand.getParameters().getPlant(), asyncCommand.getParameters().getTurbine(), mTokenService.getToken()).enqueue(new Callback<TurbineResponse>() {
            @Override
            public void onResponse(Call<TurbineResponse> call, Response<TurbineResponse> response) {
                asyncCommand.onComplete(response.body());
            }

            @Override
            public void onFailure(Call<TurbineResponse> call, Throwable t) {
                asyncCommand.onError(t.getMessage());
            }
        });
    }

    public void getTrendlerAsync(final IAsyncCommand<String, TrendlerResponse> asyncCommand) {
        mAcarsoyService.getTrendlerData(asyncCommand.getParameters(), mTokenService.getToken()).enqueue(new Callback<TrendlerResponse>() {
            @Override
            public void onResponse(Call<TrendlerResponse> call, Response<TrendlerResponse> response) {
                asyncCommand.onComplete(response.body());
            }

            @Override
            public void onFailure(Call<TrendlerResponse> call, Throwable t) {
                asyncCommand.onError(t.getMessage());
            }
        });

    }
}
