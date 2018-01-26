package com.zoudev.android.mangaapp.asynctask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.zoudev.android.mangaapp.generic.ConnectionManager;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//source
//http://stackoverflow.com/a/18953695
/**
 * Created by Youszef on 03/01/16.
 */
public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

    private String url;
    private ImageView imageView;
    private Context context;

    public ImageLoadTask(String url, ImageView imageView,Context context) {
        this.url = url;
        this.imageView = imageView;
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        if (ConnectionManager.hasConnection(context)) {

            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        if (result != null)
        {
            imageView.setImageBitmap(result);
        }
    }

}
