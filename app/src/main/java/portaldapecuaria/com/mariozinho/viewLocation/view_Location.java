package portaldapecuaria.com.mariozinho.viewLocation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import portaldapecuaria.com.mariozinho.R;
import portaldapecuaria.com.mariozinho.model.MyLocationlistener;

public class view_Location extends AppCompatActivity {
    //Obtendo localização

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_seach_location);
        seachLocation();
    }
    public void seachInfGPS(View v){
        seachLocation();
    }
    public void seachLocation(){
            if (ActivityCompat.checkSelfPermission(view_Location.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(view_Location.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(view_Location.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                ActivityCompat.requestPermissions(view_Location.this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                ActivityCompat.requestPermissions(view_Location.this, new String[] {Manifest.permission.ACCESS_NETWORK_STATE}, 1);
                return;
            }

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new MyLocationlistener();
            //locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 0, locationListener);

            if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                int latitude = (int) Math.round(MyLocationlistener.latitude);
                int longetude = (int) Math.round(MyLocationlistener.latitude);

                if(Math.abs(latitude) > 0 || Math.abs(longetude) > 0){
                    String texto = "Latitude: " + MyLocationlistener.latitude + "\n" +
                            "Longitude: " + MyLocationlistener.longitude + "\n";
                    Toast.makeText(view_Location.this, texto, Toast.LENGTH_LONG).show();
                    this.showLocationGoogleMaps(MyLocationlistener.latitude, MyLocationlistener.longitude);
                }
            }else {
                Toast.makeText(view_Location.this, "GPS desabilitado.", Toast.LENGTH_LONG).show();
            }
    }

    public void showLocationGoogleMaps(double latitude, double longitude){
        WebView w_v = findViewById(R.id.view_web);
        w_v.getSettings().setJavaScriptEnabled(true);
        w_v.loadUrl("https://www.google.com/maps/search/?api=1&query="+latitude+","+longitude);
    }
}