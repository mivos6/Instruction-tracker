package hr.etfos.mivosevic.oglasnikinstrukcija.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import hr.etfos.mivosevic.oglasnikinstrukcija.R;
import hr.etfos.mivosevic.oglasnikinstrukcija.data.Subject;
import hr.etfos.mivosevic.oglasnikinstrukcija.utilities.Utility;

/**
 * Created by admin on 27.6.2016..
 */
public class SubjectAdapter extends BaseAdapter {
    ArrayList<Subject> subjects;

    public SubjectAdapter(ArrayList<Subject> subjects) {
        this.subjects = subjects;
    }

    @Override
    public int getCount() {
        return subjects.size();
    }

    @Override
    public Object getItem(int position) {
        return subjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return subjects.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SubjectViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.subject_list_element, null);

            holder = new SubjectViewHolder();
            holder.tvSubjectName = (TextView) convertView.findViewById(R.id.tvSubjectName);
            holder.tvSubjectTags = (TextView) convertView.findViewById(R.id.tvSubjectTags);
            convertView.setTag(holder);
        }
        else {
            holder = (SubjectViewHolder) convertView.getTag();
        }

        Subject current = subjects.get(position);
        holder.tvSubjectName.setText(current.getName());
        holder.tvSubjectTags.setText(Utility.convertTagsToString(current.getTags()));

        return convertView;
    }

    static class SubjectViewHolder {
        private TextView tvSubjectName;
        private TextView tvSubjectTags;
    }
}
