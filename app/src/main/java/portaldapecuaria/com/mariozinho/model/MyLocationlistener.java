package portaldapecuaria.com.mariozinho.model;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;

public class MyLocationlistener implements LocationListener {
    public static double latitude;
    public static double longitude;

    @Override
    public void onLocationChanged(Location location){
        MyLocationlistener.latitude = location.getLatitude();
        MyLocationlistener.longitude = location.getLongitude();
    }

    @Override
    public void onProviderDisabled(@NonNull String proovider){

    }

    @Override
    public void onProviderEnabled(@NonNull String provider){

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras){

    }
}
