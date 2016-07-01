package hr.etfos.mivosevic.oglasnikinstrukcija.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import hr.etfos.mivosevic.oglasnikinstrukcija.R;
import hr.etfos.mivosevic.oglasnikinstrukcija.data.SearchResult;
import hr.etfos.mivosevic.oglasnikinstrukcija.data.Subject;

/**
 * Created by admin on 30.6.2016..
 */
public class SearchResultAdapter extends BaseAdapter {
    ArrayList<SearchResult> searchResults;

    public SearchResultAdapter(ArrayList<SearchResult> searchResults) {
        this.searchResults = searchResults;
    }

    public ArrayList<SearchResult> getSearchResults() {
        return searchResults;
    }

    @Override
    public int getCount() {
        return searchResults.size();
    }

    @Override
    public Object getItem(int position) {
        return searchResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchResultsViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.search_results_list_element, null);

            holder = new SearchResultsViewHolder();
            holder.tvResultUsername = (TextView) convertView.findViewById(R.id.tvResultUsername);
            holder.tvResultLocation = (TextView) convertView.findViewById(R.id.tvResultLocation);
            holder.tvResultSubjects = (TextView) convertView.findViewById(R.id.tvResultSubjects);
            convertView.setTag(holder);
        }
        else {
            holder = (SearchResultsViewHolder) convertView.getTag();
        }

        SearchResult current = searchResults.get(position);

        String subjects = "";
        ArrayList<Subject> userSubjects = current.getSubjects();
        for (int i = 0; i < userSubjects.size() - 1; i++) {
            subjects += userSubjects.get(i).getName() + ", ";
        }
        subjects += userSubjects.get(userSubjects.size() - 1).getName();

        holder.tvResultUsername.setText(current.getUser().getUsername());
        holder.tvResultLocation.setText(current.getUser().getLocation().split("\n")[0]);
        holder.tvResultSubjects.setText(subjects);

        return convertView;
    }

    static class SearchResultsViewHolder {
        TextView tvResultUsername;
        TextView tvResultLocation;
        TextView tvResultSubjects;
    }
}
