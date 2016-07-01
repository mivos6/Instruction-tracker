package hr.etfos.mivosevic.oglasnikinstrukcija.server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import hr.etfos.mivosevic.oglasnikinstrukcija.adapters.SearchResultAdapter;
import hr.etfos.mivosevic.oglasnikinstrukcija.data.SearchResult;
import hr.etfos.mivosevic.oglasnikinstrukcija.data.Subject;
import hr.etfos.mivosevic.oglasnikinstrukcija.data.User;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Constants;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Utility;

/**
 * Created by admin on 30.6.2016..
 */
public class SearchTask extends AsyncTask<String, Void, ArrayList<SearchResult>>{
    private ListView lvSearchResults;
    private Context context;
    private ArrayList<User> foundUsers;
    private String errorMsg;

    public SearchTask(ListView lvSearchResults, ArrayList<User> found, Context context) {
        this.lvSearchResults = lvSearchResults;
        this.foundUsers = found;
        this.context = context;
    }

    @Override
    protected ArrayList<SearchResult> doInBackground(String... params) {
        try {
            URL link = new URL(Constants.SERVER_ADDRESS + Constants.SEARCH_SCRIPT);
            HttpURLConnection conn = (HttpURLConnection) link.openConnection();
            conn.setDoOutput(true);

            String data = null;
            if (!params[0].equals("")) {
                data = URLEncoder.encode(Constants.SUBJECT_NAME_DB_TAG, "UTF-8")
                        + "=" + URLEncoder.encode(params[0], "UTF-8");
            }
            if (!params[1].equals("")) {
                if (!params[0].equals("")) {
                    data += "&" + URLEncoder.encode(Constants.LOCATION_DB_TAG, "UTF-8")
                            + "=" + URLEncoder.encode(params[1], "UTF-8");
                }
                else {
                    data = URLEncoder.encode(Constants.LOCATION_DB_TAG, "UTF-8")
                            + "=" + URLEncoder.encode(params[1], "UTF-8");
                }
            }

            if (data == null) {
                errorMsg = "No data";
                return null;
            }

            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data);
            writer.flush();
            writer.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String firstLine = reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();

            if (builder.length() == 0) {
                errorMsg = "No instructors found";
                return null;
            }
            if (!firstLine.equals("Success")) {
                errorMsg = builder.toString();
                return null;
            }

            return getResultsFromJSON(builder.toString());
        }
        catch (Exception e) {
            Log.d("MILAN", "Exception: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<SearchResult> getResultsFromJSON(String JSONstring) throws JSONException {
        ArrayList<SearchResult> results = new ArrayList<SearchResult>();

        String[] jsonObjStrings = JSONstring.split(";");
        String previousUsername = "";
        for (int i = 0; i < jsonObjStrings.length; i++) {
            JSONObject o = new JSONObject(jsonObjStrings[i]);
            if (!o.getString(Constants.USERNAME_DB_TAG).equals(previousUsername)) {
                User newUser = new User(o.getString(Constants.USERNAME_DB_TAG),
                        "",
                        o.getString(Constants.NAME_DB_TAG),
                        o.getString(Constants.EMAIL_DB_TAG),
                        o.getString(Constants.PHONE_DB_TAG),
                        o.getString(Constants.LOCATION_DB_TAG),
                        o.getString(Constants.ABOUT_DB_TAG),
                        o.getString(Constants.IMAGEURL_DB_TAG)
                );
                results.add(new SearchResult(newUser, new ArrayList<Subject>()));
                this.foundUsers.add(newUser);
                previousUsername = o.getString(Constants.USERNAME_DB_TAG);
            }

            Subject newSubject = new Subject(-1,
                    o.getString(Constants.USERNAME_DB_TAG),
                    o.getString(Constants.SUBJECT_NAME_DB_TAG),
                    o.getString(Constants.SUBJECT_TAGS_DB_TAG).split(",")
            );
            results.get(results.size() - 1).getSubjects().add(newSubject);
        }

        return results;
    }

    @Override
    protected void onPostExecute(ArrayList<SearchResult> searchResults) {
        super.onPostExecute(searchResults);

        if (searchResults != null && !searchResults.isEmpty()) {
            lvSearchResults.setAdapter(new SearchResultAdapter(searchResults));
            Utility.setListViewHeightBasedOnChildren(lvSearchResults);
        }
        else {
            Log.d("MILAN", "Error: " + this.errorMsg);
            Utility.displayToast(this.context, this.errorMsg, true);
            lvSearchResults.setAdapter(new SearchResultAdapter(new ArrayList<SearchResult>()));
            Utility.setListViewHeightBasedOnChildren(lvSearchResults);
        }
    }
}
