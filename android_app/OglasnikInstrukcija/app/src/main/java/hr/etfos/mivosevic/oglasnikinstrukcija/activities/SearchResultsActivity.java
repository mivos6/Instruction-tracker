package hr.etfos.mivosevic.oglasnikinstrukcija.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import hr.etfos.mivosevic.oglasnikinstrukcija.R;
import hr.etfos.mivosevic.oglasnikinstrukcija.adapters.SearchResultAdapter;
import hr.etfos.mivosevic.oglasnikinstrukcija.data.SearchResult;
import hr.etfos.mivosevic.oglasnikinstrukcija.data.User;
import hr.etfos.mivosevic.oglasnikinstrukcija.server.SearchTask;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Constants;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Utility;

public class SearchResultsActivity extends AppCompatActivity
    implements View.OnClickListener,
        AdapterView.OnItemClickListener{
    ArrayList<String> filters;
    ArrayList<User> foundUsers = new ArrayList<User>();

    EditText etFilterSubject;
    EditText etFilterTown;
    Button bRefreshFilter;

    ListView lvSearchResults;
    Button bShowOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        initialize();

        if (getIntent().hasExtra(Constants.FILTERS_TAG)) {
            filters = getIntent().getStringArrayListExtra(Constants.FILTERS_TAG);
        }
        else {
            filters = new ArrayList<String>();
            filters.add(0, "");
            filters.add(1, "");
        }
        etFilterSubject.setText(filters.get(0));
        etFilterTown.setText(filters.get(1));

        new SearchTask(lvSearchResults, foundUsers, this).execute(filters.get(0), filters.get(1));
    }

    private void initialize() {
        etFilterSubject = (EditText) findViewById(R.id.etFilterSubject);
        etFilterTown = (EditText) findViewById(R.id.etFilterTown);
        bRefreshFilter = (Button) findViewById(R.id.bRefreshFilter);
        lvSearchResults = (ListView) findViewById(R.id.lvSearchResults);
        bShowOnMap = (Button) findViewById(R.id.bShowOnMap);

        bRefreshFilter.setOnClickListener(this);
        lvSearchResults.setOnItemClickListener(this);
        bShowOnMap.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bRefreshFilter:
                //Refresh the results based on new filter values
                refreshResults();
                break;
            case R.id.bShowOnMap:
                //Geocode user locations, open map and set markers
                Intent i = new Intent(this, MapActivity.class);
                i.putParcelableArrayListExtra(Constants.USER_TAG, foundUsers);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this, InstructorInfoActivity.class);
        SearchResult selected = (SearchResult) parent.getAdapter().getItem(position);
        i.putExtra(Constants.USER_TAG, selected.getUser());
        //i.putParcelableArrayListExtra(Constants.SUBJECT_TAG, selected.getSubjects());
        startActivity(i);
    }

    private void refreshResults() {
        filters.clear();

        if (etFilterSubject.getText().toString().matches("^[\\w0-9 ,.#$%&+-]+$")
                || etFilterSubject.getText().toString().equals("")) {
            filters.add(0, etFilterSubject.getText().toString());
        }
        else {
            Utility.displayToast(this, Constants.SUBJECT_NAME_INVALID, false);
            return;
        }
        if (etFilterTown.getText().toString().matches("^[\\w ,.'-]+$")
                || etFilterTown.getText().toString().equals("")) {
            filters.add(1, etFilterTown.getText().toString());
        }
        else {
            Utility.displayToast(this, Constants.TOWN_NOT_VALID, false);
            return;
        }


        new SearchTask(lvSearchResults, foundUsers, this).execute(filters.get(0), filters.get(1));
    }
}
