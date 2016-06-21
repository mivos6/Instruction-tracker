package hr.etfos.mivosevic.oglasnikinstrukcija.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 21.6.2016..
 */
public class User implements Parcelable{
    private String username;
    private String password;
    private String name;
    private String email;
    private String phoneNumber;
    private String location;
    private String about;
    private String imgUrl;

    public User() {
        this.username = null;
        this.password = null;
        this.name = null;
        this.email = null;
        this.phoneNumber = null;
        this.location = null;
        this.about = null;
        this.imgUrl = null;
    }

    public User(String username, String password, String name, String email, String phoneNumber, String location, String about, String imgUrl) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.about = about;
        this.imgUrl = imgUrl;
    }

    protected User(Parcel in) {
        username = in.readString();
        password = in.readString();
        name = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        location = in.readString();
        about = in.readString();
        imgUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public String getAbout() {
        return about;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phoneNumber);
        dest.writeString(location);
        dest.writeString(about);
        dest.writeString(imgUrl);
    }
}
