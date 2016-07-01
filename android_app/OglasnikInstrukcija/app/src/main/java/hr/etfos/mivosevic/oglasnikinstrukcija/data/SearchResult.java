package hr.etfos.mivosevic.oglasnikinstrukcija.data;

import java.util.ArrayList;

/**
 * Created by admin on 28.6.2016..
 */
public class SearchResult {
    private User user;
    private ArrayList<Subject> subjects;

    public SearchResult(User user, ArrayList<Subject> subjects) {
        this.user = user;
        this.subjects = subjects;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<Subject> getSubjects() {
        return subjects;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setSubjects(ArrayList<Subject> subjects) {
        this.subjects = subjects;
    }
}
