package ua.in.zeusapps.acarsoy.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import java.text.DecimalFormat;

import ua.in.zeusapps.acarsoy.R;
import ua.in.zeusapps.acarsoy.services.api.Plant;

/**
 * Created by oleg on 17.08.2017.
 */

public class ConvertUtils {

    private static DecimalFormat _df = new DecimalFormat("0.0");

    private Context mContext;

    public ConvertUtils(Context mContext) {
        this.mContext = mContext;
    }

    public Drawable getIcon(String energyType) {
        int iconRes = energyType.equals(Const.ENERGY_TYPE_COAL)
                ? R.drawable.coal
                : R.drawable.wind;

        return ContextCompat.getDrawable(mContext, iconRes);
    }

    public int getIconBackground(String energyType) {
        int colorRes = energyType.equals(Const.ENERGY_TYPE_COAL)
                ? R.color.colorCoal
                : R.color.colorWind;

        return ContextCompat.getColor(mContext, colorRes);
    }

    public String getPowerMWatt(double power) {
        return String.format(
                mContext.getString(R.string.power_format_mwatt),
                _df.format(power));
    }

    public String getWind(double windSpeed) {
        return String.format(
                mContext.getString(R.string.wind_format),
                _df.format(windSpeed)
        );
    }

    public String getTemperature(double temp) {
        return String.format(
                mContext.getString(R.string.temperature),
                _df.format(temp)
        );
    }
}
