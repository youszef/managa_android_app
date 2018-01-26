package com.zoudev.android.mangaapp.asynctask;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.zoudev.android.mangaapp.generic.ConnectionManager;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Youszef on 08/01/16.
 */
//http://developer.android.com/training/displaying-bitmaps/process-bitmap.html
public class ChapterPageImageTask extends AsyncTask<Void, Integer, Bitmap> {

    private String url;
    private ImageView imageView;
    private ProgressBar progressBar;
    private int animationTime;
    private Context context;

    public ChapterPageImageTask(String url, ImageView imageView, ProgressBar progressBar, int animationTime,Context context) {
        this.url = url;
        this.imageView = imageView;
        this.progressBar = progressBar;
        this.animationTime = animationTime;
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
    protected void onProgressUpdate(Integer... values) {
        progressBar.setProgress(values[0]);
    }


    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        if (result != null)
        {
            imageView.setImageBitmap(result);
            crossfade();
        }
    }

    //http://developer.android.com/training/animation/crossfade.html
    private void crossfade() {

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        imageView.setAlpha(0f);
        imageView.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        imageView.animate()
                .alpha(1f)
                .setDuration(animationTime)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        progressBar.animate()
                .alpha(0f)
                .setDuration(animationTime)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }


}
