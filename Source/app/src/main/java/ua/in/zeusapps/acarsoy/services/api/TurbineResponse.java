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

        @SerializedName("POWER")
        @Expose
        public TurbineDetailItem Power;

        @SerializedName("WIND SPEED")
        @Expose
        public TurbineDetailItem WindSpeed;

        @SerializedName("TEMPERATURE")
        @Expose
        public TurbineDetailItem Temperature;

        @SerializedName("ENERGY TOTAL")
        @Expose
        public TurbineDetailItem EnergyTotal;

        @SerializedName("CONSUMPTION TOTAL")
        @Expose
        public TurbineDetailItem ConsumptionTotal;

        @SerializedName("REACTIVE POWER")
        @Expose
        public TurbineDetailItem ReactivePower;

        @SerializedName("ERROR CODE")
        @Expose
        public TurbineDetailItem ErrorCode;

        @SerializedName("CURRENT U")
        @Expose
        public TurbineDetailItem CurrentU;

        @SerializedName("CURRENT V")
        @Expose
        public TurbineDetailItem CurrentV;

        @SerializedName("CURRENT W")
        @Expose
        public TurbineDetailItem CurrentW;

        @SerializedName("VOLTAGE U")
        @Expose
        public TurbineDetailItem VoltageU;

        @SerializedName("VOLTAGE V")
        @Expose
        public TurbineDetailItem VoltageV;

        @SerializedName("VOLTAGE W")
        @Expose
        public TurbineDetailItem VoltageW;

        @SerializedName("WIND DIRECTION")
        @Expose
        public TurbineDetailItem WindDirection;

        @SerializedName("FREQUENCY")
        @Expose
        public TurbineDetailItem Frequency;

        @SerializedName("VOLTAGE L1")
        @Expose
        public TurbineDetailItem VoltageL1;

        @SerializedName("VOLTAGE L2")
        @Expose
        public TurbineDetailItem VoltageL2;

        @SerializedName("VOLTAGE L3")
        @Expose
        public TurbineDetailItem VoltageL3;

        @SerializedName("DATALOGGER")
        @Expose
        public TurbineDetailItem DataLogger;

        public class TurbineDetailItem {

            @SerializedName("value")
            @Expose
            public Object Value;

            @SerializedName("units")
            @Expose
            public Object Units;

            @Override
            public String toString() {
                return String.valueOf(Value) + " "+ String.valueOf(Units);
            }
        }
    }
}
