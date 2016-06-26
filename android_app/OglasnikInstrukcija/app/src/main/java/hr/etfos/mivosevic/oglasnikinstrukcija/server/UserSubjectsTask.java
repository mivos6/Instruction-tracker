package hr.etfos.mivosevic.oglasnikinstrukcija.server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
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

import hr.etfos.mivosevic.oglasnikinstrukcija.data.Subject;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Constants;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Utility;

/**
 * Created by admin on 26.6.2016..
 */
public class UserSubjectsTask extends AsyncTask<String, Void, ArrayList<Subject>> {
    private Context context;
    private ListView lvMySubjects;
    private String errorMsg;

    public UserSubjectsTask(Context c, ListView lv) {
        this.context = c;
        this.lvMySubjects = lv;
    }

    @Override
    protected ArrayList<Subject> doInBackground(String... params) {
        try {
            URL link = new URL(Constants.SERVER_ADDRESS + Constants.GET_USER_SUBJECTS_SCRIPT);
            HttpURLConnection conn = (HttpURLConnection) link.openConnection();
            conn.setDoOutput(true);

            String data = URLEncoder.encode(Constants.USERNAME_DB_TAG, "UTF-8")
                    + "=" + URLEncoder.encode(params[0], "UTF-8");

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
                errorMsg = "No subjects found";
                return null;
            }
            if (!firstLine.equals("Success")) {
                errorMsg = builder.toString();
                return null;
            }

            return getSubjectsFromJSON(builder.toString());
        }
        catch (Exception e) {
            Log.d("MILAN", "Exception: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<Subject> getSubjectsFromJSON(String json) throws JSONException {
        ArrayList<Subject> subjects = new ArrayList<Subject>();

        String[] jsonObjStrings = json.split(";");
        for (int i = 0; i < jsonObjStrings.length; i++) {
            JSONObject o = new JSONObject(jsonObjStrings[i]);
            subjects.add(new Subject(o.getInt(Constants.SUBJECT_ID_DB_TAG),
                    o.getString(Constants.USERNAME_DB_TAG),
                    o.getString(Constants.SUBJECT_NAME_DB_TAG),
                    o.getString(Constants.SUBJECT_TAGS_DB_TAG).split(",")
            ));
        }

        return subjects;
    }

    @Override
    protected void onPostExecute(ArrayList<Subject> subjects) {
        super.onPostExecute(subjects);

        if (subjects != null && !subjects.isEmpty()) {
            lvMySubjects.setAdapter(new ArrayAdapter<Subject>(this.context,
                    android.R.layout.simple_list_item_1,
                    subjects
            ));
            Utility.setListViewHeightBasedOnChildren(lvMySubjects);
        }
        else {
            Log.d("MILAN", this.errorMsg);
            Utility.displayToast(this.context, this.errorMsg, true);
        }
    }
}
