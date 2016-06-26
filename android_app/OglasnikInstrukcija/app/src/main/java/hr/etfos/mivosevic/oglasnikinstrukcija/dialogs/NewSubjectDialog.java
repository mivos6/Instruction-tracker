package hr.etfos.mivosevic.oglasnikinstrukcija.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import hr.etfos.mivosevic.oglasnikinstrukcija.R;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Constants;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Utility;

/**
 * Created by admin on 26.6.2016..
 */
public class NewSubjectDialog extends DialogFragment {
    private NewSubjectDialogListener listener;

    private EditText etSubjectName;
    private EditText etSubjectTags;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.new_subject_dialog_layout, null);
        builder.setView(v);

        etSubjectName = (EditText) v.findViewById(R.id.etSubjectName);
        etSubjectTags = (EditText) v.findViewById(R.id.etSubjectTags);

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NewSubjectDialog.this.getDialog().cancel();
            }
        });
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (validataInput())
                    listener.onPositiveClick(etSubjectName.getText().toString(), etSubjectTags.getText().toString().split(","));
            }
        });

        return builder.create();
    }

    public interface NewSubjectDialogListener {
        void onPositiveClick(String name, String[] tags);
    }

    public void setNewSubjectDialogListener(NewSubjectDialogListener listener) {
        this.listener = listener;
    }

    private boolean validataInput() {
        if (!etSubjectName.getText().toString().matches("^[a-zA-Z0-9 ,.'-]+$")) {
            Utility.displayToast(NewSubjectDialog.this.getActivity(), Constants.SUBJECT_NAME_INVALID, true);
            return false;
        }
        if (!etSubjectTags.getText().toString().matches("^[a-z,]+$")) {
            Utility.displayToast(NewSubjectDialog.this.getActivity(), Constants.SUBJECT_TAGS_INVALID, true);
            return false;
        }

        return true;
    }
}