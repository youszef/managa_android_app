package com.zoudev.android.mangaapp.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.zoudev.android.mangaapp.model.Manga;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Youszef on 03/01/16.
 */
public class MangaDAO {
    private SQLiteDatabase db;
    private MangaDBHelper helper;

    public MangaDAO(Context c) {
        helper = new MangaDBHelper(c);
    }

    public void open() {
        db = helper.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

//    public void insertManga(Manga manga) {
//        ContentValues values = new ContentValues();
//        values.put(MangaContract.MangaEntry.COLUMN_NAME_MANGA_ID, manga.getMangaId());
//        values.put(MangaContract.MangaEntry.COLUMN_NAME_MANGA_TITLE, manga.getTitle());
//        values.put(MangaContract.MangaEntry.COLUMN_NAME_MANGA_HITS, manga.getHits());
//        values.put(MangaContract.MangaEntry.COLUMN_NAME_MANGA_IMAGE, manga.getImageFile());
//        values.put(MangaContract.MangaEntry.COLUMN_NAME_MANGA_BOOKMARK, manga.isBookmark());
//
//        db.insert(MangaContract.MangaEntry.TABLE_NAME, null, values);
//    }



    public void insertMultipleManga(Manga[] mangas)
    {
        try
        {
            db.beginTransaction();

            for (int i = 0; i < mangas.length; i++)
            {
                ContentValues values = new ContentValues();
                values.put(MangaContract.MangaEntry.COLUMN_NAME_MANGA_ID, mangas[i].getMangaId());
                values.put(MangaContract.MangaEntry.COLUMN_NAME_MANGA_TITLE, mangas[i].getTitle());
                values.put(MangaContract.MangaEntry.COLUMN_NAME_MANGA_HITS, mangas[i].getHits());
                values.put(MangaContract.MangaEntry.COLUMN_NAME_MANGA_IMAGE, mangas[i].getImageFile());
                values.put(MangaContract.MangaEntry.COLUMN_NAME_MANGA_BOOKMARK, mangas[i].isBookmark());
                db.insert(MangaContract.MangaEntry.TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        }
        catch (SQLException e) {}
        finally
        {
            db.endTransaction();
        }
    }


    public void setBookmark(String mangaId, boolean isBookmarked)
    {
        ContentValues values = new ContentValues();
        String where = MangaContract.MangaEntry.COLUMN_NAME_MANGA_ID + "='" + mangaId +"'";
        values.put(MangaContract.MangaEntry.COLUMN_NAME_MANGA_BOOKMARK,(isBookmarked ? 1 :0));


        db.update(MangaContract.MangaEntry.TABLE_NAME,values,where,null);

    }



    public int getTotal()
    {

        String countQuery = "SELECT  * FROM " + MangaContract.MangaEntry.TABLE_NAME;
        //http://stackoverflow.com/a/18098603
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public List<Manga> getMangaBySearchTerm(String term)
    {
        List<Manga> mangas = new ArrayList<>();
        Cursor results = db.rawQuery("Select * from " + MangaContract.MangaEntry.TABLE_NAME + " where " + MangaContract.MangaEntry.COLUMN_NAME_MANGA_TITLE + " like %" + term + "%", null);


        if (results.getCount() > 0) {
            results.moveToFirst();
            while (!results.isLast()) {
                mangas.add(new Manga(results.getString(0), results.getString(1), results.getInt(2), results.getString(3),results.getInt(4)));
                results.moveToNext();
            }
            //get the last entry
            mangas.add(new Manga(results.getString(0), results.getString(1), results.getInt(2), results.getString(3),results.getInt(4)));
        }

        return mangas;
    }


    public List<Manga> getBookmarkedMangas()
    {
        List<Manga> mangas = new ArrayList<>();
        Cursor results = db.rawQuery("Select * from " + MangaContract.MangaEntry.TABLE_NAME + " where " + MangaContract.MangaEntry.COLUMN_NAME_MANGA_BOOKMARK + " = " + 1 , null);


        if (results.getCount() > 0) {
            results.moveToFirst();
            while (!results.isLast()) {
                mangas.add(new Manga(results.getString(0), results.getString(1), results.getInt(2), results.getString(3),results.getInt(4)));
                results.moveToNext();
            }
            //get the last entry
            mangas.add(new Manga(results.getString(0), results.getString(1), results.getInt(2), results.getString(3),results.getInt(4)));
        }

        return mangas;
    }


    public List<Manga> getPopularManga(int top)
    {
        List<Manga> mangas = new ArrayList<>();
        Cursor results = db.rawQuery("Select * from " + MangaContract.MangaEntry.TABLE_NAME + " order by " + MangaContract.MangaEntry.COLUMN_NAME_MANGA_HITS + " desc limit " + top , null);


        if (results.getCount() > 0) {
            results.moveToFirst();
            while (!results.isLast()) {
                mangas.add(new Manga(results.getString(0), results.getString(1), results.getInt(2), results.getString(3),results.getInt(4)));
                results.moveToNext();
            }
            //get the last entry
            mangas.add(new Manga(results.getString(0), results.getString(1), results.getInt(2), results.getString(3),results.getInt(4)));
        }

        return mangas;
    }




    public List<Manga> getMangas() {
        List<Manga> mangas = new ArrayList<>();

        Cursor results = db.rawQuery("Select * from " + MangaContract.MangaEntry.TABLE_NAME, null);

        if (results.getCount() > 0) {
            results.moveToFirst();
            while (!results.isLast()) {
                mangas.add(new Manga(results.getString(0), results.getString(1), results.getInt(2), results.getString(3),results.getInt(4)));
                results.moveToNext();
            }
            //get the last entry
            mangas.add(new Manga(results.getString(0), results.getString(1), results.getInt(2), results.getString(3),results.getInt(4)));
        }

        return mangas;
    }


}
