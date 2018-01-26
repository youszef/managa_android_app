package com.zoudev.android.mangaapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoudev.android.mangaapp.R;
import com.zoudev.android.mangaapp.asynctask.ImageLoadTask;
import com.zoudev.android.mangaapp.model.Manga;

/**
 * Created by Youszef on 07/01/16.
 */
public class MangaHomeFragment extends Fragment {


    private ImageView mangaImageView;
    private TextView descriptionTextView;
    private String description;
    private String imagePath;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            this.description = savedInstanceState.getString("Manga_Description");
            this.imagePath = savedInstanceState.getString("Manga_ImagePath");
            fillViews(imagePath, description);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manga_detail, container, false);
        mangaImageView = (ImageView) rootView.findViewById(R.id.manga_image);
        descriptionTextView = (TextView) rootView.findViewById(R.id.description_text);
        return rootView;
    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("Manga_Description", this.description);
        outState.putString("Manga_ImagePath", this.imagePath);
    }

    public void fillViews(String imagePath, String description) {
        this.imagePath = imagePath;
        this.description = description;
        //  this.manga.setChapters(null);
        if (imagePath != null && !imagePath.isEmpty()) {
            new ImageLoadTask(Manga.MANGAEDEN_CDN_PATH + imagePath, mangaImageView,this.getContext()).execute();
        }
        descriptionTextView.setText(description);
    }
}