package ua.in.zeusapps.acarsoy.services.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by worker on 04.09.2017.
 */

public class TurbineResponse {

    @SerializedName("error")
    @Expose
    public int ErrorCode;

    @SerializedName("message")
    @Expose
    public String Message;

    @SerializedName("turbine")
    @Expose
    public Turbine Turbine;

    public class Turbine {

        @SerializedName("Power")
        @Expose
        public TurbineDetailItem Power;

        @SerializedName("Wind Speed")
        @Expose
        public TurbineDetailItem WindSpeed;

        @SerializedName("Temperature")
        @Expose
        public TurbineDetailItem Temperature;

        @SerializedName("Energy Total")
        @Expose
        public TurbineDetailItem EnergyTotal;

        @SerializedName("Consumption Total")
        @Expose
        public TurbineDetailItem ConsumptionTotal;

        @SerializedName("Reactive Power")
        @Expose
        public TurbineDetailItem ReactivePower;

        @SerializedName("Error Code")
        @Expose
        public TurbineDetailItem ErrorCode;

        @SerializedName("Current U")
        @Expose
        public TurbineDetailItem CurrentU;

        @SerializedName("Current V")
        @Expose
        public TurbineDetailItem CurrentV;

        @SerializedName("Current W")
        @Expose
        public TurbineDetailItem CurrentW;

        @SerializedName("Voltage U")
        @Expose
        public TurbineDetailItem VoltageU;

        @SerializedName("Voltage V")
        @Expose
        public TurbineDetailItem VoltageV;

        @SerializedName("Voltage W")
        @Expose
        public TurbineDetailItem VoltageW;

        @SerializedName("Wind Direction")
        @Expose
        public TurbineDetailItem WindDirection;

        @SerializedName("Frequency")
        @Expose
        public TurbineDetailItem Frequency;

        @SerializedName("Voltage L1")
        @Expose
        public TurbineDetailItem VoltageL1;

        @SerializedName("Voltage L2")
        @Expose
        public TurbineDetailItem VoltageL2;

        @SerializedName("Voltage L3")
        @Expose
        public TurbineDetailItem VoltageL3;

        @SerializedName("DataLogger")
        @Expose
        public TurbineDetailItem DataLogger;

        public class TurbineDetailItem {

            @SerializedName("value")
            @Expose
            public Object Value;

            @SerializedName("units")
            @Expose
            public String Units;

            @Override
            public String toString() {
                return Value + " " + Units;
            }
        }
    }
}
