package hr.etfos.mivosevic.oglasnikinstrukcija.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import hr.etfos.mivosevic.oglasnikinstrukcija.R;
import hr.etfos.mivosevic.oglasnikinstrukcija.server.LoginTask;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Constants;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Utility;

public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener {
    private EditText etUserName;
    private EditText etPassword;
    private Button bLogin;
    private Button bRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences userPrefs = getSharedPreferences(Constants.USER_PREFS_FILE, 0);
        if (userPrefs.contains(Constants.USERNAME_DB_TAG)) this.finish();
    }

    private void initialize() {
        etUserName = (EditText) findViewById(R.id.etLoginUserName);
        etPassword = (EditText) findViewById(R.id.etLoginPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        bRegister = (Button) findViewById(R.id.bRegister);

        bLogin.setOnClickListener(this);
        bRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bLogin:
                if (validateInput()) {
                    String username = etUserName.getText().toString();
                    String password = etPassword.getText().toString();
                    new LoginTask(this).execute(username, password);
                }
                break;
            case R.id.bRegister:
                Intent i = new Intent(this, RegisterActivity.class);
                startActivity(i);
                break;
        }
    }

    private boolean validateInput() {
        //Check if username is valid
        if (!etUserName.getText().toString().matches(Constants.USERNAME_REGEX)){
            Utility.displayToast(this, Constants.USERNAME_NOT_VALID, true);
            return false;
        }
        //Check if password is valid
        if (!etPassword.getText().toString().matches(Constants.PASSWORD_REGEX)) {
            Utility.displayToast(this, Constants.PASSWORD_NOT_VALID, true);
            return false;
        }

        return true;
    }
}
