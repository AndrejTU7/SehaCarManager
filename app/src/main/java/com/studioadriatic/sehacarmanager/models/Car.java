package com.studioadriatic.sehacarmanager.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kristijandraca@gmail.com
 */
public class Car implements Parcelable {

    /**
     * id : 1
     * driver_id : 0
     * name : Mercedes C
     * registration : ZG 123 AD
     */

    private String id;
    private String driver_id;
    private String driver_name;
    private String name;
    private String registration;

    public void setId(String id) {
        this.id = id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getId() {
        return id;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public String getName() {
        return name;
    }

    public String getRegistration() {
        return registration;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    protected Car(Parcel in) {
        id = in.readString();
        driver_id = in.readString();
        driver_name = in.readString();
        name = in.readString();
        registration = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(driver_id);
        dest.writeString(driver_name);
        dest.writeString(name);
        dest.writeString(registration);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Car> CREATOR = new Parcelable.Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };
}