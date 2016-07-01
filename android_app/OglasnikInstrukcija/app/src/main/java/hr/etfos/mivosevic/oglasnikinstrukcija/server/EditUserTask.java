package hr.etfos.mivosevic.oglasnikinstrukcija.server;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
 * Created by admin on 27.6.2016..
 */
public class EditUserTask extends AsyncTask<User, Void, Boolean> {
    private Activity activity;
    private User logged;
    private String errorMsg = "Failed: ";
    private String oldUsername;

    public EditUserTask(Activity a, String oldUsername) {
        this.activity = a;
        this.oldUsername = oldUsername;
    }

    @Override
    protected Boolean doInBackground(User... params) {
        this.logged = params[0];

        String serverFilePath = "";
        if (this.logged.getImgUrl() != null) {
            serverFilePath = Constants.SERVER_ADDRESS
                    + Constants.SERVER_UPLOAD_DIR
                    + this.logged.getUsername() + ".jpg";
        }

        try {
            //POST user logged and write it to database
            URL dataLink = new URL(Constants.SERVER_ADDRESS + Constants.UPDATE_USER_SCRIPT);
            HttpURLConnection dataConn = (HttpURLConnection) dataLink.openConnection();
            dataConn.setDoOutput(true);

            Log.d("MILAN", "update");

            String postData = URLEncoder.encode(Constants.USERNAME_DB_TAG, "UTF-8")
                    + "=" + URLEncoder.encode(this.logged.getUsername(), "UTF-8")
                    + "&" + URLEncoder.encode(Constants.PASSWORD_DB_TAG, "UTF-8")
                    + "=" + URLEncoder.encode(this.logged.getPassword(), "UTF-8")
                    + "&" + URLEncoder.encode(Constants.NAME_DB_TAG, "UTF-8")
                    + "=" + URLEncoder.encode(this.logged.getName(), "UTF-8")
                    + "&" + URLEncoder.encode(Constants.EMAIL_DB_TAG, "UTF-8")
                    + "=" + URLEncoder.encode(this.logged.getEmail(), "UTF-8")
                    + "&" + URLEncoder.encode(Constants.PHONE_DB_TAG, "UTF-8")
                    + "=" + URLEncoder.encode(this.logged.getPhoneNumber(), "UTF-8")
                    + "&" + URLEncoder.encode(Constants.LOCATION_DB_TAG, "UTF-8")
                    + "=" + URLEncoder.encode(this.logged.getLocation(), "UTF-8")
                    + "&" + URLEncoder.encode(Constants.ABOUT_DB_TAG, "UTF-8")
                    + "=" + URLEncoder.encode(this.logged.getAbout(), "UTF-8")
                    + "&" + URLEncoder.encode(Constants.IMAGEURL_DB_TAG, "UTF-8")
                    + "=" + URLEncoder.encode(serverFilePath, "UTF-8")
                    + "&" + URLEncoder.encode(Constants.OLD_USERNAME_TAG, "UTF-8")
                    + "=" + URLEncoder.encode(this.oldUsername, "UTF-8");

            OutputStreamWriter wr = new OutputStreamWriter(dataConn.getOutputStream());
            wr.write(postData);
            wr.flush();
            wr.close();

            BufferedReader r = new BufferedReader(new InputStreamReader(dataConn.getInputStream()));
            String result = r.readLine();
            Log.d("MILAN", result);
            String line;
            while ((line = r.readLine()) != null) {
                Log.d("MILAN", line);
                errorMsg += line + "\n";
            }
            r.close();
            if (!result.equals("Success")) return false;
            Log.d("MILAN", "post success");

            if (this.logged.getImgUrl() != null && !this.logged.getImgUrl().contains(Constants.SERVER_ADDRESS)) {
                //Upload user portrait to server
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                File sourceFile = new File(this.logged.getImgUrl());

                FileInputStream fileStream = new FileInputStream(sourceFile);

                URL fileLink = new URL(Constants.SERVER_ADDRESS + Constants.FILE_UPLOAD_SCRIPT);
                HttpURLConnection fileConn = (HttpURLConnection) fileLink.openConnection();
                fileConn.setDoInput(true);
                fileConn.setDoOutput(true);
                fileConn.setUseCaches(false);
                fileConn.setRequestMethod("POST");
                fileConn.setRequestProperty("Connection", "Keep-Alive");
                fileConn.setRequestProperty("ENCTYPE", "multipart/form-data");
                fileConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                fileConn.setRequestProperty("uploaded_file", this.logged.getImgUrl());

                DataOutputStream dos = new DataOutputStream(fileConn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";"
                        + "filename=\"" + this.logged.getUsername() + ".jpg\"" + lineEnd);
                dos.writeBytes(lineEnd);

                bytesAvailable = fileStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                bytesRead = fileStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    //Write buffer to output
                    dos.write(buffer, 0, bufferSize);

                    //Read next buffer from input
                    bytesAvailable = fileStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileStream.read(buffer, 0, bufferSize);
                }

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                Log.d("MILAN", "Response code: " + fileConn.getResponseCode()
                        + "\n" + fileConn.getResponseMessage());

                fileStream.close();
                dos.flush();
                dos.close();

                r = new BufferedReader(new InputStreamReader(fileConn.getInputStream()));
                result = r.readLine();
                Log.d("MILAN", result);
                while ((line = r.readLine()) != null) {
                    Log.d("MILAN", line);
                    errorMsg += line + "\n";
                }
                r.close();

                if (!result.equals("Success")) return false;
            }
        }
        catch (Exception e) {
            Log.d("MILAN", "Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        this.logged.setImgUrl(serverFilePath);
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (result) {
            Utility.displayToast(this.activity, "Success", false);

            Log.d("MILAN", this.logged.getImgUrl());

            Intent i = new Intent(this.activity, MyProfileActivity.class);
            i.putExtra(Constants.USER_TAG, this.logged);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.activity.startActivity(i);
            this.activity.finish();
        }
        else {
            if (errorMsg.contains("Duplicate entry")) {
                errorMsg = "Failed: Username already exists";
            }
            Utility.displayToast(this.activity, errorMsg, false);
        }
    }
}
