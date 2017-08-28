//TODO delete

/*
package ua.in.zeusapps.acarsoy.common;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import java.text.DecimalFormat;

import ua.in.zeusapps.acarsoy.R;
import ua.in.zeusapps.acarsoy.models.Plant;
import ua.in.zeusapps.acarsoy.models.PlantType;

public abstract class PlantHolder extends GenericHolder<Plant> {

    private static DecimalFormat _df = new DecimalFormat("0.0");

    public PlantHolder(View itemView) {
        super(itemView);
    }

    protected Drawable getIcon(Plant plant){
        int iconRes = plant.getType() == PlantType.geo
                ? R.drawable.coal
                : R.drawable.wind;

        return ContextCompat.getDrawable(getContext(), iconRes);
    }

    protected int getIconBackground(Plant plant){
        int colorRes = plant.getType() == PlantType.geo
                ? R.color.colorCoal
                : R.color.colorWind;

        return ContextCompat.getColor(getContext(), colorRes);
    }

    protected String getPower(Plant plant){
        return String.format(
                getContext().getString(R.string.power_format),
                _df.format(plant.getPower()));
    }

    protected String getWind(Plant plant){
        return String.format(
                getContext().getString(R.string.wind_format),
                _df.format(plant.getWind())
        );
    }

    protected String getTemperature(Plant plant){
        return String.format(
                getContext().getString(R.string.temperature),
                _df.format(plant.getTemperature())
        );
    }
}
*/
