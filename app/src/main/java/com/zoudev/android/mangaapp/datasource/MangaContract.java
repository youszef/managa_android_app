package com.zoudev.android.mangaapp.datasource;

import android.provider.BaseColumns;

/**
 * Created by Youszef on 03/01/16.
 */
public class MangaContract {

    public MangaContract() {
    }

    // CREATE TABLE tabelnaam (kolom1 datatype, kolom2 datatype)
    public static final String TEXT_TYPE = " TEXT";
    public static final String INT_TYPE = " INT";
    public static final String BOOL_TYPE = " BOOLEAN";
    public static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES=
            "CREATE TABLE " + MangaEntry.TABLE_NAME + " (" +
                    MangaEntry.COLUMN_NAME_MANGA_ID + TEXT_TYPE + " PRIMARY KEY NOT NULL" + COMMA_SEP +
                    MangaEntry.COLUMN_NAME_MANGA_TITLE + TEXT_TYPE +COMMA_SEP +
                    MangaEntry.COLUMN_NAME_MANGA_HITS + INT_TYPE + COMMA_SEP +
                    MangaEntry.COLUMN_NAME_MANGA_IMAGE + TEXT_TYPE + COMMA_SEP +
                    MangaEntry.COLUMN_NAME_MANGA_BOOKMARK + BOOL_TYPE +" )";

    /* CREATE TABLE MANGAS (
                MANGA_ID TEXT PRIMARY KEY NOT NULL,
                TITLE TEXT,
                HITS INT,
                IMAGE TEXT,
                BOOKMARK BOOLEAN)
     */

    public static abstract class MangaEntry implements BaseColumns {
        public static final String TABLE_NAME = "MANGAS";
        public static final String COLUMN_NAME_MANGA_ID = "MANGA_ID";
        public static final String COLUMN_NAME_MANGA_TITLE = "TITLE";
        public static final String COLUMN_NAME_MANGA_HITS = "HITS";
        public static final String COLUMN_NAME_MANGA_IMAGE = "IMAGE";
        public static final String COLUMN_NAME_MANGA_BOOKMARK = "BOOKMARK";

    }
}
