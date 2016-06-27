package hr.etfos.mivosevic.oglasnikinstrukcija.utilities;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by admin on 18.6.2016..
 */
public class Utility {
    public static void displayToast(Context c, String msg, boolean isLong) {
        int len = isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;

        Toast.makeText(c, msg, len).show();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static String convertTagsToString(String[] tags) {
        if (tags == null)
            return "";

        String tagsString = "";
        for (int i = 0; i < tags.length - 1; i++) {
            tagsString += tags[i] + ",";
        }
        tagsString += tags[tags.length-1];

        return tagsString;
    }
}
