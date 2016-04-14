package me.hg.ilnaz.yandextestapp.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by ilnaz on 13.04.16, 13:35.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Singer implements Parcelable {
    private long id;
    private String name;
    private String link;
    private String description;
    private List<String> genres;
    private int tracks;
    private int albums;
    private HashMap cover;

    protected Singer(Parcel in) {
        id = in.readLong();
        name = in.readString();
        link = in.readString();
        description = in.readString();
        genres = in.createStringArrayList();
        tracks = in.readInt();
        albums = in.readInt();
        cover = in.readHashMap(String.class.getClassLoader());
    }

    public static final Creator<Singer> CREATOR = new Creator<Singer>() {
        @Override
        public Singer createFromParcel(Parcel in) {
            return new Singer(in);
        }

        @Override
        public Singer[] newArray(int size) {
            return new Singer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(link);
        dest.writeString(description);
        dest.writeStringList(genres);
        dest.writeInt(tracks);
        dest.writeInt(albums);
        dest.writeMap(cover);
    }
}
