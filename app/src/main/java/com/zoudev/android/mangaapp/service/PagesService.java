package com.zoudev.android.mangaapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.LocalBroadcastManager;

import com.zoudev.android.mangaapp.generic.ConnectionManager;
import com.zoudev.android.mangaapp.model.Manga;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Youszef on 07/01/16.
 */
public class PagesService extends IntentService {


    public static final String PAGES_SERVICE = "com.zoudev.android.mangaapp.service.PagesService";

    public PagesService() {
        super("PagesService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        if (ConnectionManager.hasConnection(this.getApplicationContext())) {
            String[] chapterPages = intent.getStringArrayExtra("chapterPages");
            String cdn_path = Manga.MANGAEDEN_CDN_PATH;

            Bitmap[] chapterImages = new Bitmap[chapterPages.length];

            for (int i = 0; i < chapterPages.length; i++) {
                try {
                    URL urlConnection = new URL(cdn_path + chapterPages[i]);
                    HttpURLConnection connection = (HttpURLConnection) urlConnection
                            .openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    chapterImages[i] = myBitmap;
                } catch (Exception e) {
                    //add default notfount image instead of null
                    chapterImages[i] = null;
                    e.printStackTrace();
                }
            }


            Intent broadCastReceiverIntent = new Intent();
            broadCastReceiverIntent.setAction(PAGES_SERVICE);
            // broadCastReceiverIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            broadCastReceiverIntent.putExtra("com.zoudev.android.mangaapp.model.Page.BitMap", chapterImages);
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadCastReceiverIntent);
        }

    }
}
