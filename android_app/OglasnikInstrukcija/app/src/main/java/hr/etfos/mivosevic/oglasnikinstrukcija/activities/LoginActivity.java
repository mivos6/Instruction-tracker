package hr.etfos.mivosevic.oglasnikinstrukcija.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import hr.etfos.mivosevic.oglasnikinstrukcija.R;
import hr.etfos.mivosevic.oglasnikinstrukcija.server.LoginTask;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    EditText etUserName;
    EditText etPassword;
    Button bLogin;
    Button bRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
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
                String username = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                new LoginTask(this).execute(username, password);
                break;
            case R.id.bRegister:
                Intent i = new Intent(this, RegisterActivity.class);
                startActivity(i);
                break;
        }
    }
}
