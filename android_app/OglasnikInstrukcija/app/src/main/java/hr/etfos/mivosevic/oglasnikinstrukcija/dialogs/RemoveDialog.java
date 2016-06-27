package hr.etfos.mivosevic.oglasnikinstrukcija.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import hr.etfos.mivosevic.oglasnikinstrukcija.R;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Constants;

/**
 * Created by admin on 27.6.2016..
 */
public class RemoveDialog extends DialogFragment{
    RemoveDialogListener listener;
    int itemPosition;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.remove_item);
        builder.setMessage(R.string.really_remove);
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RemoveDialog.this.getDialog().cancel();
            }
        });
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onRemoveClick();
            }
        });

        return builder.create();
    }

    public interface RemoveDialogListener {
        void onRemoveClick();
    }

    public void setRemoveDialogListener(RemoveDialogListener listener) {
        this.listener = listener;
    }
}
