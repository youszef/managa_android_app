package com.zoudev.android.mangaapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.zoudev.android.mangaapp.generic.ConnectionManager;
import com.zoudev.android.mangaapp.generic.TAG;
import com.zoudev.android.mangaapp.model.Chapter;
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
import java.util.ArrayList;

/**
 * Created by Youszef on 03/01/16.
 */
public class ChapterService extends IntentService {

    public static final String CHAPTER_SERVICE = "com.zoudev.android.mangaapp.service.ChapterService";

    public ChapterService()
    {
        super("ChapterService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (ConnectionManager.hasConnection(this.getApplicationContext())) {
            String mangaId = intent.getStringExtra("mangaId");

            Manga manga = null;
            try {
                URL mangaUrl = new URL("http://www.mangaeden.com/api/manga/" + mangaId);

                URLConnection urlConnection = mangaUrl.openConnection();
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

                manga = new Manga();

                ArrayList<Chapter> chapters = new ArrayList<>();

                //Get chapters
                JSONArray jsonChapters = jsonResponse.optJSONArray("chapters");


                for (int i = 0; i < jsonChapters.length(); i++) {
                    Chapter chapter = new Chapter();
                    JSONArray chapterJSONArray = jsonChapters.getJSONArray(i);

                    chapter.setChapterId(chapterJSONArray.getString(3));
                    chapter.setChapterNumber(chapterJSONArray.getInt(0));
                    chapter.setChapterPublishDate(chapterJSONArray.getInt(1));
                    chapters.add(chapter);
                }

                manga.setMangaId(mangaId);
                manga.setChapters(chapters);
                manga.setDescription(jsonResponse.optString("description"));
                manga.setImageFile(jsonResponse.optString("image"));
                manga.setArtist(jsonResponse.optString("author"));
                manga.setTitle(jsonResponse.optString("title"));
                manga.setHits(jsonResponse.optInt("hits"));


            } catch (java.net.SocketTimeoutException e) {

                Log.i(TAG.LOGTAG, "Connection is taking too long");
                e.printStackTrace();
            } catch (IOException e) {


                e.printStackTrace();
            } catch (JSONException e) {

                e.printStackTrace();
            }

            Intent broadCastReceiverIntent = new Intent();
            broadCastReceiverIntent.setAction(CHAPTER_SERVICE);
            // broadCastReceiverIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            broadCastReceiverIntent.putExtra("com.zoudev.android.mangaapp.model.Manga", manga);
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadCastReceiverIntent);
        }

    }
}
