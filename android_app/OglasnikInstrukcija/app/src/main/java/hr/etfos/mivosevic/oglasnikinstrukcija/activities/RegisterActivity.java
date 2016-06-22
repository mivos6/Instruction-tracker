package hr.etfos.mivosevic.oglasnikinstrukcija.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import hr.etfos.mivosevic.oglasnikinstrukcija.R;
import hr.etfos.mivosevic.oglasnikinstrukcija.data.User;
import hr.etfos.mivosevic.oglasnikinstrukcija.server.RegisterTask;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView imgSignupPortrait;
    String imagePath = Environment.getExternalStorageDirectory().getPath()
            + "/Download/icon-user-default.png";

    EditText etName;
    EditText etUsername;
    EditText etPassword;
    EditText etRepeatPassword;
    EditText etEmail;
    EditText etPhone;
    EditText etTown;
    EditText etStreet;
    EditText etNumber;
    EditText etAbout;

    Button bCurrentLocation;
    Button bMap;
    Button bCancel;
    Button bConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialize();
    }

    private void initialize() {
        imgSignupPortrait = (ImageView) findViewById(R.id.imgSignupPortrait);
        imgSignupPortrait.setImageResource(R.drawable.icon_user_default);

        etName = (EditText) findViewById(R.id.etName);
        etUsername = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRepeatPassword = (EditText) findViewById(R.id.etRepeatPassword);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etTown = (EditText) findViewById(R.id.etTown);
        etStreet = (EditText) findViewById(R.id.etStreet);
        etNumber = (EditText) findViewById(R.id.etNumber);
        etAbout = (EditText) findViewById(R.id.etAbout);
        bCurrentLocation = (Button) findViewById(R.id.bCurrentLocation);
        bMap = (Button) findViewById(R.id.bMap);
        bCancel = (Button) findViewById(R.id.bCancel);
        bConfirm = (Button) findViewById(R.id.bConfirm);

        bConfirm.setOnClickListener(this);
        bCancel.setOnClickListener(this);
        bCurrentLocation.setOnClickListener(this);
        bMap.setOnClickListener(this);
        imgSignupPortrait.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bConfirm:
                //Send data to server
                if (verifyInputs()) {
                    String location = etTown.getText().toString() + "\n"
                            + etStreet.getText().toString() + "\n"
                            + etNumber.getText().toString();

                    RegisterTask rt = new RegisterTask(this);
                    rt.execute(new User(etUsername.getText().toString(),
                            etPassword.getText().toString(),
                            etName.getText().toString(),
                            etEmail.getText().toString(),
                            etPhone.getText().toString(),
                            location,
                            etAbout.getText().toString(),
                            imagePath
                    ));
                }
                break;
            case R.id.bCancel:
                //Close activity
                break;
            case R.id.bCurrentLocation:
                //Get location from location service
                break;
            case R.id.bMap:
                //Open Google Map and get location from map marker
                break;
            case R.id.imgSignupPortrait:
                //Set ImageView from selected file
                break;
        }
    }

    private boolean verifyInputs() {
        return true;
    }
}
