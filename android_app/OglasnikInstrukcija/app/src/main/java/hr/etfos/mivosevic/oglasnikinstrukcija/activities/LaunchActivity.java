package hr.etfos.mivosevic.oglasnikinstrukcija.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import hr.etfos.mivosevic.oglasnikinstrukcija.services.MyLocation;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Constants;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        launchApp();
        startLocationService();
    }

    private void startLocationService() {
        Intent i = new Intent(this, MyLocation.class);
        startService(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.finish();
    }

    private void launchApp() {
        SharedPreferences userPrefs = getSharedPreferences(Constants.USER_PREFS_FILE, 0);

        Intent i = new Intent();
        if (userPrefs.contains(Constants.USERNAME_DB_TAG))
            i.setClass(this, MyProfileActivity.class);
        else
            i.setClass(this, LoginActivity.class);

        startActivity(i);
    }
}
