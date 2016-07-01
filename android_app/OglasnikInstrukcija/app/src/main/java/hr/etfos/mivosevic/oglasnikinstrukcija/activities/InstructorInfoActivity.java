package hr.etfos.mivosevic.oglasnikinstrukcija.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import hr.etfos.mivosevic.oglasnikinstrukcija.R;
import hr.etfos.mivosevic.oglasnikinstrukcija.data.Subject;
import hr.etfos.mivosevic.oglasnikinstrukcija.data.User;
import hr.etfos.mivosevic.oglasnikinstrukcija.server.SetImageTask;
import hr.etfos.mivosevic.oglasnikinstrukcija.server.UserSubjectsTask;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Constants;

public class InstructorInfoActivity extends AppCompatActivity
    implements View.OnClickListener{
    private User instructor;

    ImageView imgPortrait;
    TextView tvName;
    TextView tvUsername;
    TextView tvEmail;
    TextView tvPhoneNumber;
    TextView tvTown;
    TextView tvStreet;

    ListView lvSujbects;

    Button bSendEmail;
    Button bSendSMS;
    Button bCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_info);

        Intent i = getIntent();
        if(i.hasExtra(Constants.USER_TAG))
            this.instructor = i.getParcelableExtra(Constants.USER_TAG);

        initialize();
        setData();
    }

    private void initialize() {
        imgPortrait = (ImageView) findViewById(R.id.imgPortrait);
        tvName = (TextView) findViewById(R.id.tvName);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvPhoneNumber = (TextView) findViewById(R.id.tvPhoneNumber);
        tvTown = (TextView) findViewById(R.id.tvTown);
        tvStreet = (TextView) findViewById(R.id.tvStreet);
        lvSujbects = (ListView) findViewById(R.id.lvSubjects);
        bSendEmail = (Button) findViewById(R.id.bSendEmail);
        bSendSMS = (Button) findViewById(R.id.bSendSMS);
        bCall = (Button) findViewById(R.id.bCall);

        bSendEmail.setOnClickListener(this);
        if (!instructor.getPhoneNumber().equals("")) {
            bSendSMS.setOnClickListener(this);
            bCall.setOnClickListener(this);
        }
        else {
            bSendSMS.setAlpha((float)0.5);
            bCall.setAlpha((float)0.5);
        }
    }

    private void setData() {
        tvName.setText(this.instructor.getName());
        tvUsername.setText(this.instructor.getUsername());
        tvEmail.setText(this.instructor.getEmail());
        tvPhoneNumber.setText(this.instructor.getPhoneNumber());

        String[] loc = this.instructor.getLocation().split("\n");
        tvTown.setText(loc[0]);
        tvStreet.setText(loc[1] + " " + loc[2]);

        new SetImageTask(imgPortrait).execute(this.instructor.getImgUrl());
        new UserSubjectsTask(this, lvSujbects).execute(this.instructor.getUsername());
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent();
        switch (v.getId()) {
            case R.id.bSendEmail:
                i.setAction(Intent.ACTION_SENDTO);
                i.setType("text/plain");
                i.setData(Uri.parse("mailto:" + instructor.getEmail()));
                break;
            case R.id.bSendSMS:
                i.setAction(Intent.ACTION_VIEW);
                i.setData(Uri.parse("sms:" + instructor.getPhoneNumber()));
                break;
            case R.id.bCall:
                i.setAction(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:" + instructor.getPhoneNumber()));
                break;
        }

        startActivity(i);
    }
}
