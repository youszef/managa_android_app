package com.zoudev.android.mangaapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zoudev.android.mangaapp.R;
import com.zoudev.android.mangaapp.generic.MangaListAdapter;
import com.zoudev.android.mangaapp.model.Manga;

import java.util.ArrayList;
import java.util.List;


public class FullMangasFragment extends Fragment implements MangaListAdapter.BookMarkListAdapterInteractionListener {


    private Activity parentActivity;
    private ArrayList<Manga> mangas;
    private OnBookMarkChangeFullMangaFragmentInteractionListener bookmarkListener;
    private OnFirstTimeLoadFullMangaFragmentInteractionListener onStartListener;


    private ListView fullMangasList;


    private static final String ARG_MANGAS = "com.zoudev.mangaap.fullmangasfragment.mangas";

    public FullMangasFragment() {

    }

    public static FullMangasFragment newInstance() {
        FullMangasFragment fragment = new FullMangasFragment();
        return fragment;
    }


    public void loadMangas(List<Manga> mangas)
    {
        this.mangas = (ArrayList)mangas;
       //     Bundle args = new Bundle();

      //  getArguments().putParcelableArray(ARG_MANGAS, this.mangas);
      //  this.setArguments(args);

        loadListView(this.mangas);
    }

    private void loadListView(List<Manga> mangas) {
       //bcs getACtivity return NPE sometimes Lord knows why

        if (parentActivity != null)
        {
            MangaListAdapter adapter = new MangaListAdapter(parentActivity, android.R.layout.simple_list_item_1, mangas,this);
            fullMangasList.setAdapter(adapter);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ARG_MANGAS, mangas);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.parentActivity = (Activity)context;
        if (context instanceof OnBookMarkChangeFullMangaFragmentInteractionListener) {
            bookmarkListener = (OnBookMarkChangeFullMangaFragmentInteractionListener) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBookMarkChangeFullMangaFragmentInteractionListener");
        }
        if (context instanceof OnFirstTimeLoadFullMangaFragmentInteractionListener) {
            onStartListener = (OnFirstTimeLoadFullMangaFragmentInteractionListener) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFirstTimeLoadFullMangaFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        bookmarkListener = null;
        onStartListener = null;
    }

    @Override
    public void onBookmarkClick() {
        if (bookmarkListener != null) {
            bookmarkListener.onBookmarkChangeFullManga();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(this.mangas != null)
        {
            loadListView(this.mangas);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mangas = savedInstanceState.getParcelableArrayList(ARG_MANGAS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_full_mangas, container, false);
        fullMangasList = (ListView) fragmentView.findViewById(R.id.mangas_list);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mangas == null)
        {
            onStartListener.onLoadFragmentFullManga();
        }
    }

    public interface OnBookMarkChangeFullMangaFragmentInteractionListener {
        void onBookmarkChangeFullManga();
    }

    public interface OnFirstTimeLoadFullMangaFragmentInteractionListener {
        void onLoadFragmentFullManga();
    }


}
