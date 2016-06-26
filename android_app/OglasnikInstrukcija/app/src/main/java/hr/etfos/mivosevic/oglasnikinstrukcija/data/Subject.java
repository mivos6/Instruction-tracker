package hr.etfos.mivosevic.oglasnikinstrukcija.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 26.6.2016..
 */
public class Subject implements Parcelable{
    private int id;
    private String username;
    private String name;
    private String[] tags;

    public Subject() {
        this.id = -1;
        this.username = null;
        this.name = null;
        this.tags = null;
    }

    public Subject(int id, String username, String name, String[] tags) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.tags = tags;
    }

    protected Subject(Parcel in) {
        id = in.readInt();
        username = in.readString();
        name = in.readString();
        tags = in.createStringArray();
    }

    public static final Creator<Subject> CREATOR = new Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel in) {
            return new Subject(in);
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String[] getTags() {
        return tags;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    @Override
    public int describeContents() {
        return this.id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(username);
        dest.writeString(name);
        dest.writeStringArray(tags);
    }

    public String toString() {
        return this.name;
    }
}
