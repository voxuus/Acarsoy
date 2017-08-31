package ua.in.zeusapps.acarsoy.services.api;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleg on 16.08.2017.
 */

public class Plant implements ClusterItem {

    @SerializedName("id")
    @Expose
    public String Id;

    @SerializedName("name")
    @Expose
    public String Name;

    @SerializedName("type")
    @Expose
    public String Type;

    @SerializedName("lat")
    @Expose
    public double Latitude;

    @SerializedName("lng")
    @Expose
    public double Longitude;

    @SerializedName("center")
    @Expose
    public Plant.Position Position;

    @SerializedName("power")
    @Expose
    public double Power;

    @SerializedName("powerR")
    @Expose
    public double ReactivePower;

    @SerializedName("buildings")
    @Expose
    public List<Turbine> Turbines = new ArrayList<>();

    @SerializedName("wind")
    @Expose
    public double Wind;

    @SerializedName("temperature")
    @Expose
    public double Temperature;

    @Override
    public LatLng getPosition() {
        return new LatLng(Latitude, Longitude);
    }

    @Override
    public String getTitle() {
        return Name;
    }

    @Override
    public String getSnippet() {
        return Type;
    }

    public Plant(String name, String type, double latitude, double longitude, double power, double wind, double temperature) {
        Name = name;
        Type = type;
        Latitude = latitude;
        Longitude = longitude;
        Power = power;
        Wind = wind;
        Temperature = temperature;
    }

    public class Position {

        @SerializedName("lat")
        @Expose
        public double Lat;

        @SerializedName("lng")
        @Expose
        public double Lng;

    }

    public class Turbine implements ClusterItem {

        @SerializedName("a")
        @Expose
        public String Name;

        @SerializedName("b")
        @Expose
        public String Type;

        @SerializedName("c")
        @Expose
        public double Latitude;

        @SerializedName("d")
        @Expose
        public double Longitude;

        @SerializedName("e")
        @Expose
        public double Power;

        @SerializedName("f")
        @Expose
        public double ReactivePower;

        @SerializedName("g")
        @Expose
        public double WindSpeed;

        @SerializedName("h")
        @Expose
        public double WindDirection;

        @SerializedName("i")
        @Expose
        public double Temperature;

        @SerializedName("j")
        @Expose
        public double CurrentU;

        @SerializedName("k")
        @Expose
        public double CurrentV;

        @SerializedName("l")
        @Expose
        public double CurrentW;

        @SerializedName("m")
        @Expose
        public double VoltageU;

        @SerializedName("n")
        @Expose
        public double VoltageV;

        @SerializedName("o")
        @Expose
        public double VoltageW;

        @SerializedName("u")
        @Expose
        public double Frequency;

        @SerializedName("p")
        @Expose
        public Integer ErrorCode;

        @SerializedName("plant")
        @Expose
        public String PlantName;

        @Override
        public LatLng getPosition() {
            return new LatLng(Latitude, Longitude);
        }

        @Override
        public String getTitle() {
            return Name;
        }

        @Override
        public String getSnippet() {
            return Type;
        }
    }
}
