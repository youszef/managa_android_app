package com.zoudev.android.mangaapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Youszef on 08/12/15.
 */
public class Manga implements Parcelable {

    public static final String MANGAEDEN_CDN_PATH = "http://cdn.mangaeden.com/mangasimg/";

   // public enum Status {Ongoing, Completed}
    //for image =>>> http://stackoverflow.com/a/7331698
    private String mangaId, title,  imageFile ;
    //private String[] categories;
    private int hits;

    private boolean bookmark;


    //optional members
    private String artist,description;
    private ArrayList<Chapter> chapters;


    //why I used int for unix time stamp =>>> http://stackoverflow.com/a/4289801
    //UNIX TIMESTAMP
   // private int dateLastChapter;


    public Manga() {
    }

    private Manga(String mangaId, String title, String imageFile, int hits, String artist, String description, ArrayList<Chapter> chapters) {
        this.mangaId = mangaId;
        this.title = title;
        this.imageFile = imageFile;
        this.hits = hits;
        this.artist = artist;
        this.description = description;
        this.chapters = chapters;

    }

    public Manga(String mangaId, String title, int hits, String imageFile, int bookmark) {
        this.mangaId = mangaId;
        this.title = title;
        this.hits = hits;
        this.imageFile = imageFile;
        this.bookmark = (bookmark == 1);
    }


    public String getMangaId() {
        return mangaId;
    }

    public void setMangaId(String mangaId) {
        this.mangaId = mangaId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(ArrayList<Chapter> chapters) {
        this.chapters = chapters;
    }
    public boolean isBookmark() {
        return bookmark;
    }

    public void setBookmark(boolean bookmark) {
        this.bookmark = bookmark;
    }

    public int isBookmarkAsBit() {
        return (bookmark ? 1: 0);
    }

    public void setBookmarkAsBit(int bookmarkAsBit) {
        this.bookmark = (bookmarkAsBit == 1);
    }


    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(mangaId);
        dest.writeString(title);
        dest.writeString(imageFile);
        dest.writeInt(hits);
        dest.writeString(artist);
        dest.writeString(description);
        //http://stackoverflow.com/a/7089687
        dest.writeByte((byte) (bookmark ? 1 : 0));
        dest.writeTypedList(chapters);
    }
    public static final Parcelable.Creator<Manga> CREATOR
            = new Parcelable.Creator<Manga>() {
        public Manga createFromParcel(Parcel in) {
            return new Manga(in);
        }

        public Manga[] newArray(int size) {
            return new Manga[size];
        }
    };

    public Manga(Parcel in) {
        this.mangaId = in.readString();
        this.title = in.readString();
        this.imageFile = in.readString();
        this.hits = in.readInt();
        this.artist = in.readString();
        this.description = in.readString();
        this.bookmark = in.readByte() != 0;
        this.chapters =in.createTypedArrayList(Chapter.CREATOR);
    }

}
