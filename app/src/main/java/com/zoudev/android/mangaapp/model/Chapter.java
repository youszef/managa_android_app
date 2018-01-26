package com.zoudev.android.mangaapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Youszef on 08/12/15.
 */
public class Chapter implements Parcelable {


    private String chapterId;
    private int chapterNumber;
    // private Page[] pages;

    private int chapterPublishDate;


    @Override
    public int describeContents() {
        return this.hashCode();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(chapterId);
        dest.writeInt(chapterNumber);
        dest.writeInt(chapterPublishDate);
    }

    public static final Parcelable.Creator<Chapter> CREATOR = new Parcelable.Creator<Chapter>() {

        public Chapter createFromParcel(Parcel in)
        {
            return new Chapter(in);
        }

        public Chapter[] newArray(int size)
        {
            return new Chapter[size];
        }
    };


    public Chapter(Parcel in) {
        this.chapterId = in.readString();
        this.chapterNumber = in.readInt();
        this.chapterPublishDate = in.readInt();

    }


    public Chapter() {
    }

    /**
     * @param 'Array' item data from JSON API when getting Manga info, 4th item from "chapters" key array value represents chapter Id
     * @param 'Array' item data from JSON API when getting Manga info, 1st item from "chapters" key array value represents chapter number
     * @param 'Array' of Pages that needs to be constructed from Chapter see com.zoudev.android.mangaapp.model.Page
     * @param 'Array' item data from JSON API when getting Manga info, 2nd item from "chapters" key array value represents chapterPublishDate
     */
//    public Chapter(String chapterId, int chapterNumber, Page[] pages, int chapterPublishDate) {
//        this.chapterId = chapterId;
//        this.chapterNumber = chapterNumber;
//        this.pages = pages;
//        this.chapterPublishDate = chapterPublishDate;
//    }
    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public int getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(int chapterNumber) {
        this.chapterNumber = chapterNumber;
    }


    public int getChapterPublishDate() {
        return chapterPublishDate;
    }

    public void setChapterPublishDate(int chapterPublishDate) {
        this.chapterPublishDate = chapterPublishDate;
    }


}
