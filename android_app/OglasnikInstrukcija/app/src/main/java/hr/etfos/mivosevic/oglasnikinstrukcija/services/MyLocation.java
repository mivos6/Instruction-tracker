package hr.etfos.mivosevic.oglasnikinstrukcija.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Constants;

/**
 * Created by admin on 1.7.2016..
 */
public class MyLocation extends Service
        implements LocationListener {
    LocationManager locationManager;
    Location location;
    String provider;
    Criteria criteria;
    Geocoder geocoder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyLocationBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        this.criteria = new Criteria();
        this.criteria.setAccuracy(Criteria.ACCURACY_FINE);
        this.provider = locationManager.getBestProvider(criteria, true);
        if (Geocoder.isPresent())
            this.geocoder = new Geocoder(this, Locale.getDefault());
        this.location = new Location(provider);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, Constants.MIN_TIME, Constants.MIN_DIST, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    public ArrayList<String> getCurrentLocation() {
        ArrayList<String> currentLoc = new ArrayList<String>();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return currentLoc;
        }
        this.location = locationManager.getLastKnownLocation(provider);
        if (location == null) {
            return currentLoc;
        }

        if (this.geocoder != null) {
            Log.d("MILAN", "geocoder");
            try {
                List<Address> addr = this.geocoder.getFromLocation(this.location.getLatitude(),
                        this.location.getLongitude(), 1);
                if (!addr.isEmpty()) {
                    Log.d("MILAN", "address");
                    currentLoc.add(addr.get(0).getLocality());
                    String line = addr.get(0).getAddressLine(0);
                    String[] parts = line.split(" ");
                    line = "";
                    for (int i = 0; i < parts.length - 1; i++) {
                        line += parts[i] + " ";
                    }

                    currentLoc.add(line);
                    currentLoc.add(parts[parts.length - 1]);
                }
            } catch (IOException e) {
                Log.d("MILAN", e.getMessage());
                e.printStackTrace();
            }
        }

        return currentLoc;
    }

    public class MyLocationBinder extends Binder {
        public MyLocation getService() {
            return MyLocation.this;
        }
    }
}
