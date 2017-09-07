package ua.in.zeusapps.acarsoy.services.api;

/**
 * Created by worker on 04.09.2017.
 */

public class TurbineRequest {

    private String mPlant;

    private String mTurbine;

    public TurbineRequest(String plant, String turbine) {
        mPlant = plant;
        mTurbine = turbine;
    }

    public String getPlant() {
        return mPlant;
    }

    public String getTurbine() {
        return mTurbine;
    }
}
