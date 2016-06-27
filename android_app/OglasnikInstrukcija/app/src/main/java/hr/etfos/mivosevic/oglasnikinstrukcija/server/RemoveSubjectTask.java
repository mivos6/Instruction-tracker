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
 * Created by admin on 27.6.2016..
 */
public class RemoveSubjectTask extends AsyncTask<Subject, Void, Boolean> {
    private Context context;
    private ListView lvMySubjects;
    private String errorMsg;
    private Subject subject;

    public RemoveSubjectTask(Context c, ListView lv) {
        this.context = c;
        this.lvMySubjects = lv;
    }

    @Override
    protected Boolean doInBackground(Subject... params) {
        this.subject = params[0];
        String idStr = Long.toString(subject.getId());

        try {
            URL link = new URL(Constants.SERVER_ADDRESS + Constants.REMOVE_SUBJECT_SCRIPT);
            HttpURLConnection conn = (HttpURLConnection) link.openConnection();
            conn.setDoOutput(true);

            String data = URLEncoder.encode(Constants.SUBJECT_ID_DB_TAG, "UTF-8")
                    + "=" + URLEncoder.encode(idStr, "UTF-8");

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
