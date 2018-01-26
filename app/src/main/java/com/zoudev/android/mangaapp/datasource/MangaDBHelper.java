package com.zoudev.android.mangaapp.datasource;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Youszef on 03/01/16.
 */
public class MangaDBHelper extends SQLiteOpenHelper{
    private static int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "Mangas.db";

    public MangaDBHelper(Context context) {

        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MangaContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + MangaContract.MangaEntry.TABLE_NAME);
        onCreate(db);
    }
}
