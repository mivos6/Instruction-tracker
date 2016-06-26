package hr.etfos.mivosevic.oglasnikinstrukcija.server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import hr.etfos.mivosevic.oglasnikinstrukcija.data.Subject;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Constants;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Utility;

/**
 * Created by admin on 26.6.2016..
 */
public class AddSubjectTask extends AsyncTask<Subject, Void, Boolean>{
    private Context context;
    private ListView lvMySubjects;
    private String errorMsg;
    private Subject subject;

    public AddSubjectTask(Context c, ListView lv) {
        this.context = c;
        this.lvMySubjects = lv;
    }

    @Override
    protected Boolean doInBackground(Subject... params) {
        this.subject = params[0];

        String[] tags = this.subject.getTags();
        String tagsString = "";
        for (int i = 0; i < tags.length - 1; i++) {
            tagsString += tags[i] + ",";
        }
        tagsString += tags[tags.length-1];

        try {
            URL link = new URL(Constants.SERVER_ADDRESS + Constants.INSERT_SUBJECT_SCRIPT);
            HttpURLConnection conn = (HttpURLConnection) link.openConnection();
            conn.setDoOutput(true);

            String data = URLEncoder.encode(Constants.USERNAME_DB_TAG, "UTF-8")
                    + "=" + URLEncoder.encode(this.subject.getUsername(), "UTF-8")
                    + "&" + URLEncoder.encode(Constants.SUBJECT_NAME_DB_TAG, "UTF-8")
                    + "=" + URLEncoder.encode(this.subject.getName(), "UTF-8")
                    + "&" + URLEncoder.encode(Constants.SUBJECT_TAGS_DB_TAG, "UTF-8")
                    + "=" + URLEncoder.encode(tagsString, "UTF-8");

            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data);
            writer.flush();
            writer.close();

            BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String result = r.readLine();
            Log.d("MILAN", result);
            String line;
            while ((line = r.readLine()) != null) {
                Log.d("MILAN", line);
                errorMsg += line + "\n";
            }
            r.close();
            return result.equals("Success");
        }
        catch (Exception e) {
            Log.d("MILAN", "Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);

        if (!success)
            Utility.displayToast(this.context, errorMsg, false);
        else
            new UserSubjectsTask(context, lvMySubjects).execute(this.subject.getUsername());
    }
}
