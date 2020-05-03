package com.dev.staffapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Barber implements Parcelable {
    private String name, username, password, barberId,salon,city;
    private long rating;

    public Barber() {
    }

    protected Barber(Parcel in) {
        name = in.readString();
        username = in.readString();
        password = in.readString();
        barberId = in.readString();
        salon = in.readString();
        city = in.readString();
        rating = in.readLong();
    }

    public static final Creator<Barber> CREATOR = new Creator<Barber>() {
        @Override
        public Barber createFromParcel(Parcel in) {
            return new Barber(in);
        }

        @Override
        public Barber[] newArray(int size) {
            return new Barber[size];
        }
    };

    public Barber(String name, String username, String password, String barberId, String salon, String city, long rating) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.barberId = barberId;
        this.salon = salon;
        this.city = city;
        this.rating = rating;
    }

    public String getSalon() {
        return salon;
    }

    public void setSalon(String salon) {
        this.salon = salon;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public String getBarberId() {
        return barberId;
    }

    public void setBarberId(String barberId) {
        this.barberId = barberId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(username);
        parcel.writeString(password);
        parcel.writeString(barberId);
        parcel.writeLong(rating);
    }
}
