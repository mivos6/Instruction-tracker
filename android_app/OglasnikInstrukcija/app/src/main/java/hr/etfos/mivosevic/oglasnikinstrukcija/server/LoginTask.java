package hr.etfos.mivosevic.oglasnikinstrukcija.server;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import hr.etfos.mivosevic.oglasnikinstrukcija.activities.MyProfileActivity;
import hr.etfos.mivosevic.oglasnikinstrukcija.data.User;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Constants;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Utility;

/**
 * Created by admin on 18.6.2016..
 */
public class LoginTask extends AsyncTask<String, Void, User> {
    private Context context;
    private String errorMsg = "Username and password combination does not exist.";

    public LoginTask(Context c) {
        this.context = c;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected User doInBackground(String... params) {
        String username = params[0];
        String password = params[1];

        try {
            URL link = new URL(Constants.SERVER_ADDRESS + Constants.CHECK_USER_SCRIPT);
            HttpURLConnection conn = (HttpURLConnection) link.openConnection();
            conn.setDoOutput(true);

            String data = URLEncoder.encode(Constants.USERNAME_DB_TAG, "UTF-8")
                    + "=" + URLEncoder.encode(username, "UTF-8")
                    + "&" + URLEncoder.encode(Constants.PASSWORD_DB_TAG, "UTF-8")
                    + "=" + URLEncoder.encode(password, "UTF-8");

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
                return null;
            }
            if (!firstLine.equals("Success")) {
                errorMsg = builder.toString();
                return null;
            }

            return getUserFromJSON(builder.toString());
        }
        catch (Exception e) {
            Log.d("MILAN", "Exception: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private User getUserFromJSON(String JSONString) throws JSONException {
        User fromJSON = new User();

        JSONObject data = new JSONObject(JSONString);
        fromJSON.setUsername(data.getString(Constants.USERNAME_DB_TAG));
        fromJSON.setPassword(data.getString(Constants.PASSWORD_DB_TAG));
        fromJSON.setName(data.getString(Constants.NAME_DB_TAG));
        fromJSON.setEmail(data.getString(Constants.EMAIL_DB_TAG));
        fromJSON.setPhoneNumber(data.getString(Constants.PHONE_DB_TAG));
        fromJSON.setLocation(data.getString(Constants.LOCATION_DB_TAG));
        fromJSON.setAbout(data.getString(Constants.ABOUT_DB_TAG));
        fromJSON.setImgUrl(data.getString(Constants.IMAGEURL_DB_TAG));

        return fromJSON;
    }

    @Override
    protected void onPostExecute(User user) {
        super.onPostExecute(user);
        if (user == null) {
            Utility.displayToast(this.context, errorMsg, false);
            return;
        }

        Utility.displayToast(this.context, "Successful login: " + user.getUsername(), false);
        Intent i = new Intent(this.context, MyProfileActivity.class);
        i.putExtra(Constants.USER_TAG, user);
        this.context.startActivity(i);
    }
}
