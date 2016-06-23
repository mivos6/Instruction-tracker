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
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Constants;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Utility;

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
                if (verifyInput()) {
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

    private boolean verifyInput() {
        //Check if username is valid
        if (!etUsername.getText().toString().matches(Constants.USERNAME_REGEX)){
            Utility.displayToast(this, Constants.USERNAME_NOT_VALID, true);
            return false;
        }
        //Check if password is valid
        if (!etPassword.getText().toString().matches(Constants.PASSWORD_REGEX)) {
            Utility.displayToast(this, Constants.PASSWORD_NOT_VALID, true);
            return false;
        }
        //Check if repeated password matches
        if (!etPassword.getText().toString().equals(etRepeatPassword.getText().toString())) {
            Utility.displayToast(this, Constants.PASSWORD_NOT_MATCH, false);
            return false;
        }
        //Check if email is valid (regular expression RFC 5322 Official Standard)
        if (!etEmail.getText().toString().matches(Constants.EMAIL_REGEX)) {
            Utility.displayToast(this, Constants.EMAIL_NOT_VALID, false);
            return false;
        }
        //Check phone number
        if (!etPhone.getText().toString().equals("")) {
            if (!etPhone.getText().toString().matches("^[\\d]+$")) {
                Utility.displayToast(this, Constants.PHONE_NOT_VALID, false);
                return false;
            }
        }
        //Check town name, street name and number
        if (!(etTown.getText().toString().matches("^[a-zA-Z ,.'-]+$")
            && etStreet.getText().toString().matches("^[a-zA-Z ,.'-]+$")
            && etNumber.getText().toString().matches("^[\\d]+$"))) {
            Utility.displayToast(this, Constants.ADDRESS_NOT_VALID, false);
            return false;
        }

        return true;
    }
}
