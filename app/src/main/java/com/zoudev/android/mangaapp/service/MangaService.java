package com.zoudev.android.mangaapp.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.zoudev.android.mangaapp.datasource.MangaDAO;
import com.zoudev.android.mangaapp.generic.ConnectionManager;
import com.zoudev.android.mangaapp.generic.TAG;
import com.zoudev.android.mangaapp.model.Manga;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Youszef on 2/01/2016.
 */
public class MangaService extends IntentService {

    public static final String MANGA_SERVICE = "com.zoudev.android.mangaapp.service.MangaService";
    public static final String MANGA_SERVICE_TOTALPAGES = "com.zoudev.android.mangaapp.service.MangaService.totalpages";

    public MangaService() {
        super("MangaService");
    }

    private int pageNumber;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG.LOGTAG, "Manga service is started.");
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.zoudev.mangaapp", Context.MODE_PRIVATE);
        pageNumber = sharedPreferences.getInt(MANGA_SERVICE, 0);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (ConnectionManager.hasConnection(this.getApplicationContext())) {
            boolean isSuccess = false;
            MangaDAO dao = new MangaDAO(this);
            try {

                int mangasSize = 100;

                final int pageSize = 100;

                while (mangasSize >= pageSize) {
                    URL mangaUrl = new URL(String.format("http://www.mangaeden.com/api/list/0//?p=%d&l=%d", pageNumber++, pageSize));

                    URLConnection urlConnection = mangaUrl.openConnection();
                    //  urlConnection.setConnectTimeout(10000);
                    //  urlConnection.setReadTimeout(10000);

                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

                    //when getting 16k items ==>> takes usually around 6-7 sec. Issues arises
                    //Buffered reader is not able to succeed GC kicks in and does AllocSpace objects.
                    //Did some research but could not find why it happens.
                    //hence lowering the amount of items to 5k
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    String result = "";

                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }

                    inputStream.close();

                    JSONObject jsonResponse = new JSONObject(result);
                    int totalMangas = jsonResponse.optInt("total");
                    SharedPreferences sharedPreferences = this.getSharedPreferences("com.zoudev.mangaapp", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putInt(MANGA_SERVICE_TOTALPAGES, totalMangas).commit();
                    JSONArray mangas = jsonResponse.optJSONArray("manga");
                    mangasSize = mangas.length();
                    Manga[] newMangas = new Manga[mangasSize];
                    for (int i = 0; i < mangas.length(); i++) {

                        Manga manga = new Manga();
                        JSONObject mangasJSONObject = mangas.getJSONObject(i);
                        manga.setMangaId(mangasJSONObject.getString(TAG.MANGA_ID));
                        manga.setTitle(mangasJSONObject.getString(TAG.MANGA_TITLE));
                        manga.setHits(mangasJSONObject.getInt(TAG.MANGA_HITS));
                        manga.setImageFile(mangasJSONObject.getString(TAG.MANGA_IMAGE));
                        newMangas[i] = manga;

                    }
                    dao.open();
                    dao.insertMultipleManga(newMangas);
                    dao.close();
                    isSuccess = true;

                    putPageNumberSharedPreference();
                    notifyReceivers(isSuccess);


                }
                pageNumber = 0;
                putPageNumberSharedPreference();
                notifyReceivers(isSuccess);

            } catch (java.net.SocketTimeoutException e) {
                isSuccess = false;
                Log.i(TAG.LOGTAG, "Connection is taking too long");
                e.printStackTrace();
            } catch (IOException e) {
                isSuccess = false;

                e.printStackTrace();
            } catch (JSONException e) {
                isSuccess = false;
                e.printStackTrace();
            }
        }



    }

    private void notifyReceivers(boolean isSuccess) {
        Intent broadCastReceiverIntent = new Intent();
        broadCastReceiverIntent.setAction(MANGA_SERVICE);
        // broadCastReceiverIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        broadCastReceiverIntent.putExtra("isLoaded", isSuccess);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadCastReceiverIntent);
    }


    private void putPageNumberSharedPreference() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.zoudev.mangaapp", Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(MANGA_SERVICE, pageNumber).commit();
    }
}
