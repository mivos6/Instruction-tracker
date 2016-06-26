package hr.etfos.mivosevic.oglasnikinstrukcija.server;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import hr.etfos.mivosevic.oglasnikinstrukcija.R;

/**
 * Created by admin on 25.6.2016..
 */
public class SetImageTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView imgPortrait;

    public SetImageTask(ImageView iv) {
        this.imgPortrait = iv;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap image = null;

        try {
            if (params[0] != null) {
                URL link = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) link.openConnection();
                InputStream is = conn.getInputStream();
                BitmapFactory.Options bmpOpt = new BitmapFactory.Options();
                //Opcija za skaliranje slike na 1/4 izvornih dimenzija.
                bmpOpt.inSampleSize = 4;
                return BitmapFactory.decodeStream(is, null, bmpOpt);
            }
            else return null;
        } catch (Exception e) {
            Log.d("MILAN", "Exception: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap != null)
            imgPortrait.setImageBitmap(bitmap);
        else
            imgPortrait.setImageResource(R.drawable.icon_user_default);
    }
}
