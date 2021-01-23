package com.studioadriatic.sehacarmanager.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Andrej on 15.2.2016..
 */
public class LocationModel implements Parcelable {
    private int id;
    private String name;
    private String address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.address);
    }

    public LocationModel() {
    }

    protected LocationModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.address = in.readString();
    }

    public static final Parcelable.Creator<LocationModel> CREATOR = new Parcelable.Creator<LocationModel>() {
        public LocationModel createFromParcel(Parcel source) {
            return new LocationModel(source);
        }

        public LocationModel[] newArray(int size) {
            return new LocationModel[size];
        }
    };
}