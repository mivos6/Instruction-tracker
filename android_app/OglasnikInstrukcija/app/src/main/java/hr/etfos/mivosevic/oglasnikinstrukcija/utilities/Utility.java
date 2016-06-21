package hr.etfos.mivosevic.oglasnikinstrukcija.utilities;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by admin on 18.6.2016..
 */
public class Utility {
    public static void displayToast(Context c, String msg, boolean isLong) {
        int len = isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;

        Toast.makeText(c, msg, len).show();
    }
}
