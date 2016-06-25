package hr.etfos.mivosevic.oglasnikinstrukcija.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import hr.etfos.mivosevic.oglasnikinstrukcija.R;
import hr.etfos.mivosevic.oglasnikinstrukcija.data.User;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Constants;

public class MyProfileActivity extends AppCompatActivity {
    private User logged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        setLoggedUser();
    }

    private void setLoggedUser() {
        Intent i = getIntent();
        if (i.hasExtra(Constants.USER_TAG)) {
            this.logged = i.getParcelableExtra(Constants.USER_TAG);

            SharedPreferences prefs = getSharedPreferences(Constants.USER_PREFS_FILE, 0);

            SharedPreferences.Editor e = prefs.edit();
            e.putString(Constants.USERNAME_DB_TAG, this.logged.getUsername());
            e.putString(Constants.PASSWORD_DB_TAG, this.logged.getPassword());
            e.putString(Constants.NAME_DB_TAG, this.logged.getName());
            e.putString(Constants.EMAIL_DB_TAG, this.logged.getEmail());
            e.putString(Constants.PHONE_DB_TAG, this.logged.getPhoneNumber());
            e.putString(Constants.LOCATION_DB_TAG, this.logged.getLocation());
            e.putString(Constants.ABOUT_DB_TAG, this.logged.getAbout());
            e.putString(Constants.IMAGEURL_DB_TAG, this.logged.getImgUrl());
            e.commit();
        }
        else {
            SharedPreferences prefs = getSharedPreferences(Constants.USER_PREFS_FILE, 0);

            this.logged = new User(prefs.getString(Constants.USERNAME_DB_TAG, ""),
                    prefs.getString(Constants.PASSWORD_DB_TAG, ""),
                    prefs.getString(Constants.NAME_DB_TAG, ""),
                    prefs.getString(Constants.EMAIL_DB_TAG, ""),
                    prefs.getString(Constants.PHONE_DB_TAG, ""),
                    prefs.getString(Constants.LOCATION_DB_TAG, ""),
                    prefs.getString(Constants.ABOUT_DB_TAG, ""),
                    prefs.getString(Constants.IMAGEURL_DB_TAG, "")
            );
        }
    }
}
