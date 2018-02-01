package com.flores.rodrigo.githubsearch.Class;

import android.os.Parcel;
import android.os.Parcelable;

public class Repository implements Parcelable {


    private String name;
    private String imageURL;
    private String description;
    private String contributorsURL;
    private String owner;

    public Repository() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContributorsURL() {
        return contributorsURL;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setContributorsURL(String contributorsURL) {
        this.contributorsURL = contributorsURL;
    }

    protected Repository(Parcel in) {
        name = in.readString();
        imageURL = in.readString();
        description = in.readString();
        contributorsURL = in.readString();
        owner=in.readString();
    }

    public static final Creator<Repository> CREATOR = new Creator<Repository>() {
        @Override
        public Repository createFromParcel(Parcel in) {
            return new Repository(in);
        }

        @Override
        public Repository[] newArray(int size) {
            return new Repository[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(imageURL);
        parcel.writeString(description);
        parcel.writeString(contributorsURL);
        parcel.writeString(owner);
    }
}
