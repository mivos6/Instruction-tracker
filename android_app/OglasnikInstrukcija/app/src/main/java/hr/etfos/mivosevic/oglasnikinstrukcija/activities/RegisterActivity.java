package hr.etfos.mivosevic.oglasnikinstrukcija.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import hr.etfos.mivosevic.oglasnikinstrukcija.R;
import hr.etfos.mivosevic.oglasnikinstrukcija.services.MyLocation;
import hr.etfos.mivosevic.oglasnikinstrukcija.data.User;
import hr.etfos.mivosevic.oglasnikinstrukcija.server.EditUserTask;
import hr.etfos.mivosevic.oglasnikinstrukcija.server.RegisterTask;
import hr.etfos.mivosevic.oglasnikinstrukcija.server.SetImageTask;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Constants;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Utility;

public class RegisterActivity extends AppCompatActivity
    implements View.OnClickListener {
    private ImageView imgSignupPortrait;
    private String imagePath =  null;
    private boolean update = false;
    private User oldData;

    private MyLocation myLocService;
    private boolean isBound = false;
    public ServiceConnection myLocServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyLocation.MyLocationBinder b = (MyLocation.MyLocationBinder) service;
            myLocService = b.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {}
    };

    private EditText etName;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etRepeatPassword;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etTown;
    private EditText etStreet;
    private EditText etNumber;
    private EditText etAbout;

    private Button bCurrentLocation;
    private Button bCancel;
    private Button bConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialize();
        setData();
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
        bCancel = (Button) findViewById(R.id.bCancel);
        bConfirm = (Button) findViewById(R.id.bConfirm);

        bConfirm.setOnClickListener(this);
        bCancel.setOnClickListener(this);
        bCurrentLocation.setOnClickListener(this);
        imgSignupPortrait.setOnClickListener(this);

        Intent i = new Intent(this, MyLocation.class);
        this.isBound = bindService(i, this.myLocServiceConn, 0);
    }

    private void setData() {
        if (getIntent().hasExtra(Constants.USER_TAG)) {
            this.oldData = getIntent().getParcelableExtra(Constants.USER_TAG);
            imagePath = this.oldData.getImgUrl();
            update = true;
        }
        else {
            this.oldData = new User();
            update = false;
        }

        String[] location;
        if (oldData.getLocation() != null)
            location = oldData.getLocation().split("\n");
        else
            location = new String[]{"", "", ""};

        etName.setText(this.oldData.getName());
        etUsername.setText(this.oldData.getUsername());
        etPassword.setText(this.oldData.getPassword());
        etRepeatPassword.setText(this.oldData.getPassword());
        etEmail.setText(this.oldData.getEmail());
        etPhone.setText(this.oldData.getPhoneNumber());
        etTown.setText(location[0]);
        etStreet.setText(location[1]);
        etNumber.setText(location[2]);
        etAbout.setText(this.oldData.getAbout());

        if (oldData.getImgUrl() != null && !oldData.getImgUrl().equals("")) {
            new SetImageTask(imgSignupPortrait).execute(oldData.getImgUrl());
        }
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

                    if (!update) {
                        new RegisterTask(this).execute(new User(etUsername.getText().toString(),
                                etPassword.getText().toString(),
                                etName.getText().toString(),
                                etEmail.getText().toString(),
                                etPhone.getText().toString(),
                                location,
                                etAbout.getText().toString(),
                                imagePath
                        ));
                    }
                    else {
                        new EditUserTask(this, this.oldData.getUsername()).execute(new User(etUsername.getText().toString(),
                                etPassword.getText().toString(),
                                etName.getText().toString(),
                                etEmail.getText().toString(),
                                etPhone.getText().toString(),
                                location,
                                etAbout.getText().toString(),
                                imagePath
                        ));
                    }
                }
                break;
            case R.id.bCancel:
                //Close activity
                this.finish();
                break;
            case R.id.bCurrentLocation:
                //Get location from location service
                if (this.myLocService != null) {
                    ArrayList<String> curLoc = this.myLocService.getCurrentLocation();
                    if (!curLoc.isEmpty()) {
                        etTown.setText(curLoc.get(0));
                        etStreet.setText(curLoc.get(1));
                        etNumber.setText(curLoc.get(2));
                    }
                }
                break;
            case R.id.imgSignupPortrait:
                //Launch galery to select image
                Intent i = new Intent();
                i.setAction(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, Constants.SELECT_IMAGE_CODE);
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
        if (!(etTown.getText().toString().matches("^[\\w ,.'-]+$")
            && etStreet.getText().toString().matches("^[\\w ,.'-]+$")
            && etNumber.getText().toString().matches("^[\\d]+$"))) {
            Utility.displayToast(this, Constants.ADDRESS_NOT_VALID, false);
            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Display the selected image on screen
        if (requestCode == Constants.SELECT_IMAGE_CODE) {
            if (resultCode == RESULT_OK) {
                Uri imageUri = data.getData();
                try {
                    InputStream is = getContentResolver().openInputStream(imageUri);

                    BitmapFactory.Options bmpOpt = new BitmapFactory.Options();
                    //Opcija za skaliranje slike na 1/4 izvornih dimenzija.
                    bmpOpt.inSampleSize = 4;
                    imgSignupPortrait.setImageBitmap(BitmapFactory.decodeStream(is, null, bmpOpt));

                    //Get absolute path from Uri
                    this.imagePath = Utility.getPathFromUri(this, imageUri);
                } catch (FileNotFoundException e) {
                    Log.d("MILAN", e.getMessage());
                    e.printStackTrace();
                }
            }
        }
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
