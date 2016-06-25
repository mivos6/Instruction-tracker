package hr.etfos.mivosevic.oglasnikinstrukcija.activities;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import hr.etfos.mivosevic.oglasnikinstrukcija.R;
import hr.etfos.mivosevic.oglasnikinstrukcija.data.User;
import hr.etfos.mivosevic.oglasnikinstrukcija.server.RegisterTask;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Constants;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Utility;

public class RegisterActivity extends AppCompatActivity
        implements View.OnClickListener {
    private ImageView imgSignupPortrait;
    private String imagePath =  null; //Environment.getExternalStorageDirectory().getPath() + "/Download/icon-user-default.png";

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
    private Button bMap;
    private Button bCancel;
    private Button bConfirm;

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
                this.finish();
                break;
            case R.id.bCurrentLocation:
                //Get location from location service
                break;
            case R.id.bMap:
                //Open Google Map and get location from map marker
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
        if (!(etTown.getText().toString().matches("^[a-zA-Z ,.'-]+$")
            && etStreet.getText().toString().matches("^[a-zA-Z ,.'-]+$")
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

                    //Get absolutr path from Uri
                    this.imagePath = getPathFromUri(imageUri);
                } catch (FileNotFoundException e) {
                    Log.d("MILAN", e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    private String getPathFromUri(Uri data) {
        String result;

        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, data, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        result = cursor.getString(columnIndex);
        cursor.close();

        return result;
    }
}
