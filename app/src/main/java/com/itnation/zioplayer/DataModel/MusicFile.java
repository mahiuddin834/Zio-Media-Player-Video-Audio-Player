package com.itnation.zioplayer.DataModel;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class MusicFile implements Parcelable {
    private String id;
    private String title;
    private String displayName;
    private String size;
    private String duration;
    private String path;
    private String dateAdded;
    private String artistName;


    public MusicFile(String id, String title, String displayName, String size, String duration, String path, String dateAdded, String artistName) {
        this.id = id;
        this.title = title;
        this.displayName = displayName;
        this.size = size;
        this.duration = duration;
        this.path = path;
        this.dateAdded = dateAdded;
        this.artistName = artistName;
    }

    protected MusicFile(Parcel in) {
        id = in.readString();
        title = in.readString();
        displayName = in.readString();
        size = in.readString();
        duration = in.readString();
        path = in.readString();
        dateAdded = in.readString();
        artistName = in.readString();
    }

    public static final Creator<MusicFile> CREATOR = new Creator<MusicFile>() {
        @Override
        public MusicFile createFromParcel(Parcel in) {
            return new MusicFile(in);
        }

        @Override
        public MusicFile[] newArray(int size) {
            return new MusicFile[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSize() {
        return size;
    }

    public String getDuration() {
        return duration;
    }

    public String getPath() {
        return path;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public String getArtistName() {
        return artistName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(displayName);
        parcel.writeString(size);
        parcel.writeString(duration);
        parcel.writeString(path);
        parcel.writeString(dateAdded);
        parcel.writeString(artistName);

    }
}
