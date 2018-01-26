package com.zoudev.android.mangaapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zoudev.android.mangaapp.R;
import com.zoudev.android.mangaapp.datasource.MangaDAO;
import com.zoudev.android.mangaapp.model.Manga;

import java.util.List;


public class TopMangasFragment extends Fragment implements TopSelectorFragment.OnTopSelectorFragmentInteractionListener {



    private static final String ARG_TOPMANGAS = "com.zoudev.mangaap.topmangasfragment.topmangas";
    private static final String ARG_FULLMANGAFRAGMENT = "com.zoudev.mangaap.topmangasfragment.fullmangas";
    private static final String ARG_TOPSEEKFRAGMENT = "com.zoudev.mangaap.topmangasfragment.topseek";




    private TopSelectorFragment topSelectorFragment;
    private FullMangasFragment fullMangasFragment;
    private boolean isFirstTime;
//    private SwipeRefreshLayout swipeRefreshLayout;

    public TopMangasFragment() {
        // Required empty public constructor
    }


    public static TopMangasFragment newInstance() {
        TopMangasFragment fragment = new TopMangasFragment();
        return fragment;
    }


    public void reload()
    {
        //TopSelectorFragment topSelectorFragment = (TopSelectorFragment) getChildFragmentManager().findFragmentByTag(ARG_TOPSEEKFRAGMENT);

        if (fullMangasFragment != null && topSelectorFragment != null)
        {
            MangaDAO dao = new MangaDAO(this.getContext());
            dao.open();
            List<Manga> mangas = dao.getPopularManga(topSelectorFragment.getValue());
            dao.close();
            fullMangasFragment.loadMangas(mangas);
        }
        else
        {
             isFirstTime = true;
        }
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_top_mangas, container, false);
        topSelectorFragment = TopSelectorFragment.newInstance();
        fullMangasFragment = FullMangasFragment.newInstance();
        android.support.v4.app.FragmentManager fm = getChildFragmentManager();
        if (fm.getFragments() == null || fm.getFragments().isEmpty())
        {
            android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(R.id.selector_frame, topSelectorFragment,ARG_TOPSEEKFRAGMENT);
            transaction.add(R.id.fullmangas_list_frame, fullMangasFragment, ARG_FULLMANGAFRAGMENT);
            transaction.commit();
        }
//        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.mangas_refresh_layout);

//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                reload();
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
        if (isFirstTime)
        {
            reload();
            isFirstTime = false;
        }

        return rootView;
    }
    @Override
    public void onSeekBarSelectedUp(int item) {

        FullMangasFragment fullMangasFragment = (FullMangasFragment) getChildFragmentManager().findFragmentByTag(ARG_FULLMANGAFRAGMENT);
        if(fullMangasFragment != null)
        {
            MangaDAO dao = new MangaDAO(this.getContext());

            dao.open();
            List<Manga> mangas = dao.getPopularManga(item);
            dao.close();
            fullMangasFragment.loadMangas(mangas);
        }
    }


}
