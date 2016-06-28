package hr.etfos.mivosevic.oglasnikinstrukcija.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import hr.etfos.mivosevic.oglasnikinstrukcija.R;
import hr.etfos.mivosevic.oglasnikinstrukcija.data.Subject;
import hr.etfos.mivosevic.oglasnikinstrukcija.data.User;
import hr.etfos.mivosevic.oglasnikinstrukcija.dialogs.NewSubjectDialog;
import hr.etfos.mivosevic.oglasnikinstrukcija.dialogs.RemoveDialog;
import hr.etfos.mivosevic.oglasnikinstrukcija.server.AddSubjectTask;
import hr.etfos.mivosevic.oglasnikinstrukcija.server.EditSubjectTask;
import hr.etfos.mivosevic.oglasnikinstrukcija.server.RemoveSubjectTask;
import hr.etfos.mivosevic.oglasnikinstrukcija.server.SetImageTask;
import hr.etfos.mivosevic.oglasnikinstrukcija.server.UserSubjectsTask;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Constants;

public class MyProfileActivity extends AppCompatActivity
    implements View.OnClickListener,
        NewSubjectDialog.NewSubjectDialogListener,
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,
        RemoveDialog.RemoveDialogListener{
    private User logged;
    private int currentSubjectPosition;

    private ImageView imgMyPortrait;
    private TextView tvMyName;
    private TextView tvMyUsername;
    private TextView tvMyEmail;
    private Button bEditData;
    private Button bLogout;

    private ListView lvMySubjects;
    private Button bAddSubject;

    private EditText etSearchSubject;
    private EditText etSearchTown;
    private EditText etSearchDistance;
    private Button bStartSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        setLoggedUser();
        initialize();
        setData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences userPrefs = getSharedPreferences(Constants.USER_PREFS_FILE, 0);
        if (!userPrefs.contains(Constants.USERNAME_DB_TAG)) this.finish();

        setLoggedUser();
        setData();
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

    private void initialize() {
        imgMyPortrait = (ImageView) findViewById(R.id.imgMyPortrait);
        tvMyName = (TextView) findViewById(R.id.tvMyName);
        tvMyUsername = (TextView) findViewById(R.id.tvMyUsername);
        tvMyEmail = (TextView) findViewById(R.id.tvMyEmail);
        bEditData = (Button) findViewById(R.id.bEditData);
        bLogout = (Button) findViewById(R.id.bLogout);
        lvMySubjects = (ListView) findViewById(R.id.lvMySubjects);
        bAddSubject = (Button) findViewById(R.id.bAddSubject);
        etSearchSubject = (EditText) findViewById(R.id.etSearchSubject);
        etSearchTown = (EditText) findViewById(R.id.etSearchTown);
        etSearchDistance = (EditText) findViewById(R.id.etSearchDistance);
        bStartSearch = (Button) findViewById(R.id.bStartSearch);

        lvMySubjects.setOnItemLongClickListener(this);
        lvMySubjects.setOnItemClickListener(this);
        bEditData.setOnClickListener(this);
        bLogout.setOnClickListener(this);
        bAddSubject.setOnClickListener(this);
        bStartSearch.setOnClickListener(this);
    }

    private void setData() {
        tvMyName.setText(this.logged.getName());
        tvMyUsername.setText(this.logged.getUsername());
        tvMyEmail.setText(this.logged.getEmail());

        new SetImageTask(imgMyPortrait).execute(this.logged.getImgUrl());
        new UserSubjectsTask(this, lvMySubjects).execute(this.logged.getUsername());
    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.bEditData:
                //Open RegisterActivity to edit user data
                i = new Intent(this, RegisterActivity.class);
                i.putExtra(Constants.USER_TAG, this.logged);
                startActivity(i);
                break;
            case R.id.bLogout:
                //Clear user data from shared prefs fila and switch to LoginActivity
                SharedPreferences.Editor e = getSharedPreferences(Constants.USER_PREFS_FILE, 0).edit();
                e.clear();
                e.commit();
                i = new Intent(this, LoginActivity.class);
                startActivity(i);
                this.finish();
                break;
            case R.id.bAddSubject:
                //Add item to database and update the lvMyClasses
                //Open dialog to enter subject name and tags
                NewSubjectDialog d = new NewSubjectDialog();
                d.setNewSubjectDialogListener(this);
                FragmentManager fm = getFragmentManager();
                d.show(fm, Constants.NEW_SUBJECT_DIALOG_TAG);
                break;
            case R.id.bStartSearch:
                //Begin store filters and open SearchResultsActivity
                break;
        }
    }

    //Receive data from NewSubjectDialog and save it in database
    @Override
    public void onPositiveClick(long id, String name, String[] tags) {
        if (id == -1)
            new AddSubjectTask(this, lvMySubjects).execute(new Subject(0, this.logged.getUsername(), name, tags));
        else
            new EditSubjectTask(this, lvMySubjects).execute(new Subject(id, this.logged.getUsername(), name, tags));
    }

    //Remove subject from database
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        this.currentSubjectPosition = position;

        RemoveDialog d = new RemoveDialog();
        d.setRemoveDialogListener(this);
        FragmentManager fm = getFragmentManager();
        d.show(fm, Constants.REMOVE_DIALOG_TAG);

        return true;
    }

    @Override
    public void onRemoveClick() {
        new RemoveSubjectTask(this, lvMySubjects).execute((Subject) lvMySubjects.getAdapter().getItem(this.currentSubjectPosition));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.SUBJECT_TAG, (Subject) parent.getAdapter().getItem(position));

        NewSubjectDialog d = new NewSubjectDialog();
        d.setArguments(args);
        d.setNewSubjectDialogListener(this);
        FragmentManager fm = getFragmentManager();
        d.show(fm, Constants.NEW_SUBJECT_DIALOG_TAG);
    }
}
