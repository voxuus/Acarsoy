package ua.in.zeusapps.acarsoy.services.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by worker on 05.09.2017.
 */

public class TrendlerResponse {

    @SerializedName("error")
    @Expose
    public int ErrorCode;

    @SerializedName("message")
    @Expose
    public String Message;

    @SerializedName("result")
    @Expose
    public int Result;

    @SerializedName("turbines")
    @Expose
    public List<Turbine> Turbines = new ArrayList<>();

    public class Turbine {

        @SerializedName("name")
        @Expose
        public String Name;

        @SerializedName("powerToday")
        @Expose
        public double PowerToday;

        @SerializedName("powerWeek")
        @Expose
        public double PowerWeek;

        @SerializedName("powerMonth")
        @Expose
        public double PowerMonth;

        @SerializedName("powerYear")
        @Expose
        public double PowerYear;
    }
}
