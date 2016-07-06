package hr.etfos.mivosevic.oglasnikinstrukcija.activities;

import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import hr.etfos.mivosevic.oglasnikinstrukcija.R;
import hr.etfos.mivosevic.oglasnikinstrukcija.data.User;
import hr.etfos.mivosevic.oglasnikinstrukcija.services.MyLocation;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Constants;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Utility;

public class MapActivity extends AppCompatActivity
    implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener {
    private MapFragment mapFragment;
    private GoogleMap googleMap;
    private Geocoder geocoder;

    private ArrayList<User> users;
    private ArrayList<Marker> markers;

    private boolean curLocMarkerSet = false;
    private MyLocation myLocService;
    private boolean isBound = false;
    public ServiceConnection myLocServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyLocation.MyLocationBinder b = (MyLocation.MyLocationBinder) service;
            myLocService = b.getService();

            if (googleMap != null && !curLocMarkerSet) {
                setMyMarker();
                zoomToMarkers();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        initialize();
    }

    private void initialize() {
        if (getIntent().hasExtra(Constants.USER_TAG))
            users = getIntent().getParcelableArrayListExtra(Constants.USER_TAG);
        if (Geocoder.isPresent())
            this.geocoder = new Geocoder(this, Locale.getDefault());

        FragmentManager fm = getFragmentManager();
        this.mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
        this.mapFragment.getMapAsync(this);

        this.markers = new ArrayList<Marker>();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        UiSettings uiset = this.googleMap.getUiSettings();
        uiset.setZoomControlsEnabled(true);
        uiset.setZoomGesturesEnabled(true);

        ArrayList<LatLng> userPositions = null;
        if (this.geocoder != null) {
            userPositions = getUserPositions();
        } else {
            Utility.displayToast(this, Constants.NO_GEOCODER, false);
            return;
        }

        if (userPositions == null) {
            Utility.displayToast(this, Constants.NO_LOCATION_FOUND, false);
            return;
        }

        setUserMarkers(userPositions);

        if (this.myLocService != null && !this.curLocMarkerSet) {
            setMyMarker();
            zoomToMarkers();
        }
    }

    private ArrayList<LatLng> getUserPositions() {
        ArrayList<LatLng> positions = new ArrayList<LatLng>();

        for (int i = 0; i < this.users.size(); i++) {
            String locationName = users.get(i).getLocation();
            try {
                List<Address> addr = this.geocoder.getFromLocationName(locationName, 1);
                positions.add(new LatLng(addr.get(0).getLatitude(), addr.get(0).getLongitude()));
            } catch (IOException e) {
                Log.d("MILAN", e.getMessage());
                e.printStackTrace();
                positions.add(null);
            }
        }

        return positions;
    }

    private void setUserMarkers(ArrayList<LatLng> userPositions) {
        for (int i = 0; i < userPositions.size(); i++) {
            if (userPositions.get(i) != null) {
                MarkerOptions opts = new MarkerOptions();
                opts.position(userPositions.get(i))
                        .title(users.get(i).getUsername())
                        .snippet(users.get(i).getLocation().replace('\n', ','));
                Marker m = this.googleMap.addMarker(opts);

                googleMap.setOnInfoWindowClickListener(this);

                this.markers.add(m);
            }
        }
    }

    private void setMyMarker() {
        ArrayList<String> curLoc = this.myLocService.getCurrentLocation();
        if (curLoc.isEmpty())
            return;
        Log.d("MILAN", "curLoc");
        String s = curLoc.get(0) + ", " + curLoc.get(1) + " " + curLoc.get(2);
        LatLng curPos = null;
        try {
            List<Address> addr = this.geocoder.getFromLocationName(s, 1);
            curPos = new LatLng(addr.get(0).getLatitude(), addr.get(0).getLongitude());
        } catch (IOException e) {
            Log.d("MILAN", e.getMessage());
            e.printStackTrace();
        }

        if (curPos == null)
            return;
        Log.d("MILAN", "curPos");

        MarkerOptions opts = new MarkerOptions();
        opts.position(curPos)
                .title("Vaša lokacija");
        Marker myMarker = this.googleMap.addMarker(opts);
        this.curLocMarkerSet = true;

        this.markers.add(myMarker);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String username = marker.getTitle();
        User user = null;

        for (int i = 0; i < users.size(); i++) {
            if (username.equals(users.get(i).getUsername())) {
                user = users.get(i);
                break;
            }
        }

        if (user != null) {
            Intent i = new Intent(this, InstructorInfoActivity.class);
            i.putExtra(Constants.USER_TAG, user);
            startActivity(i);
        }
    }

    private void zoomToMarkers() {
        if (markers.isEmpty())
            return;

        LatLngBounds.Builder b = new LatLngBounds.Builder();
        for (int i = 0; i < this.markers.size(); i++) {
            b.include(this.markers.get(i).getPosition());
        }

        LatLngBounds bounds = b.build();

        int padding = 20;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        googleMap.animateCamera(cu);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent i = new Intent(this, MyLocation.class);
        this.isBound = bindService(i, this.myLocServiceConn, 0);
    }

    @Override
    protected void onStop() {
        if (this.isBound) {
            unbindService(this.myLocServiceConn);
            this.isBound = false;
        }
        super.onStop();
    }
}
