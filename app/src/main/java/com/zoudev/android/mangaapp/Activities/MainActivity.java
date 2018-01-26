package com.zoudev.android.mangaapp.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zoudev.android.mangaapp.R;
import com.zoudev.android.mangaapp.datasource.MangaDAO;
import com.zoudev.android.mangaapp.fragments.FullMangasFragment;
import com.zoudev.android.mangaapp.fragments.TopMangasFragment;
import com.zoudev.android.mangaapp.model.Manga;
import com.zoudev.android.mangaapp.service.MangaService;

import java.util.List;

public class MainActivity extends AppCompatActivity implements FullMangasFragment.OnBookMarkChangeFullMangaFragmentInteractionListener, FullMangasFragment.OnFirstTimeLoadFullMangaFragmentInteractionListener {

    public static final String RECEIVE_NEW_MANGAS = "com.zoudev.android.mangaapp.Activities.MainActivity.RECEIVE_NEW_MANGAS";
    public static final String FIRST_TIME_LOADING = "com.zoudev.android.mangaapp.Activities.MainActivity.first_time_laoding";


    private MangaDAO dao;
    private ProgressBar mangaLoadingFromServerProgressBar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private boolean isFirstTime = true;

    private MangasFragmentPagerAdapter pagerAdapter;

    private MangasReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //setup of tab navigation
        pagerAdapter = new MangasFragmentPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.mangas_container);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(2);


        tabLayout = (TabLayout) findViewById(R.id.mangas_tabs);
        tabLayout.setupWithViewPager(viewPager);


        mangaLoadingFromServerProgressBar = (ProgressBar) findViewById(R.id.amountmangas_progressbar);
        dao = new MangaDAO(this);

        receiver = new MangasReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MangaService.MANGA_SERVICE);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);

        if (savedInstanceState != null)
        {
            isFirstTime = savedInstanceState.getBoolean(FIRST_TIME_LOADING);
        }

        loadMangas();
    }

    public void loadMangas() {
        dao.open();
        int count = dao.getTotal();
        dao.close();
        //check if there are pages pending thus > 0
        //call intent to service
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.zoudev.mangaapp", Context.MODE_PRIVATE);
        int pageNumber = sharedPreferences.getInt(MangaService.MANGA_SERVICE, 0);

        if (count == 0) {
            loadMangaListFromServer();
            updateProgress(count);
            return;
        } else if (pageNumber > 0) {
            //pagerAdapter.reloadBookmarks();
            loadMangaListFromServer();
            updateProgress(count);
            return;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean(FIRST_TIME_LOADING,isFirstTime);
    }

    private void loadMangaListFromServer() {
        Intent intent = new Intent(this, MangaService.class);
        startService(intent);
    }


    public void newMangasAdded() {
        dao.open();
        int count = dao.getTotal();
        dao.close();
        updateProgress(count);
        //if user is not on the toplist, refresh list so that user does not got annoyed
        if (viewPager.getCurrentItem() == 0) {
            pagerAdapter.updateView(1);
        }
    }

    private void updateProgress(float count) {
        mangaLoadingFromServerProgressBar.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.zoudev.mangaapp", Context.MODE_PRIVATE);
        int totalMangasInServer = sharedPreferences.getInt(MangaService.MANGA_SERVICE_TOTALPAGES, 0);
        float ratio = count / totalMangasInServer;
        int percentage = (int) (ratio * 100);
        mangaLoadingFromServerProgressBar.setProgress(percentage);

        if (percentage == 100) {
            mangaLoadingFromServerProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, R.string.toast_all_mangas_loaded, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MangaService.MANGA_SERVICE);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    public void onBookmarkChangeFullManga() {
        pagerAdapter.updateView(0);
    }

    @Override
    public void onLoadFragmentFullManga() {

        if (isFirstTime)
        {
            pagerAdapter.updateView(0);
            pagerAdapter.updateView(1);
            isFirstTime = false;
        }
    }


    private class MangasReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(MangaService.MANGA_SERVICE)) {
                boolean isLoaded = intent.getExtras().getBoolean("isLoaded");
                if (isLoaded) {
                    newMangasAdded();
                }
            }

        }
    }


    private class MangasFragmentPagerAdapter extends FragmentPagerAdapter {

        private FullMangasFragment bookmarkedFullMangasFragment;
        private TopMangasFragment topMangasFragment;

        public MangasFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    bookmarkedFullMangasFragment = FullMangasFragment.newInstance();
                    return bookmarkedFullMangasFragment;
                case 1:
                    topMangasFragment = TopMangasFragment.newInstance();
                    return topMangasFragment;
            }
            return null;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            switch (position) {
                case 0:
                    this.bookmarkedFullMangasFragment = (FullMangasFragment) fragment;
                    break;
                case 1:
                    this.topMangasFragment = (TopMangasFragment) fragment;
                    break;
                default:
                    break;
            }
            return fragment;
        }


        private void reloadMangas() {
            if (!isDBEmpty()) {
                topMangasFragment.reload();
            }
        }

        public void reloadBookmarks() {
            if (!isDBEmpty()) {
                bookmarkedFullMangasFragment.loadMangas(getBookmarkedMangas());
            }
        }

        private boolean isDBEmpty() {
            dao.open();
            int total = dao.getTotal();
            dao.close();
            return total == 0;
        }


        private List<Manga> getBookmarkedMangas() {
            dao.open();
            List<Manga> mangas = dao.getBookmarkedMangas();
            dao.close();
            return mangas;
        }


        @Override
        public int getCount() {
            return 2;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.mangas_bookmarks);
                case 1:
                    return getResources().getString(R.string.mangas_trending);
            }
            return null;
        }


        public void updateView(int currentItem) {

            switch (currentItem) {
                case 0:
                    reloadBookmarks();
                    break;
                case 1:
                    reloadMangas();
                    break;
            }
        }
    }
}