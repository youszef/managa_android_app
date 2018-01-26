package com.zoudev.android.mangaapp.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.zoudev.android.mangaapp.R;
import com.zoudev.android.mangaapp.fragments.MangaChaptersFragment;
import com.zoudev.android.mangaapp.fragments.MangaHomeFragment;
import com.zoudev.android.mangaapp.model.Manga;
import com.zoudev.android.mangaapp.service.ChapterService;

public class MangaDetailActivity extends AppCompatActivity implements MangaChaptersFragment.OnFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ChaptersReceiver receiver;
    private boolean retrieved = false;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the two
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        receiver = new ChaptersReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ChapterService.CHAPTER_SERVICE);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);


        if (savedInstanceState != null)
        {
            boolean previousRetrieved = savedInstanceState.getBoolean("Retrieved_Data");
            this.retrieved = previousRetrieved;
        }

        if (!this.retrieved)
        {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String mangaId = extras.getString("mangaId");

                Intent intent = new Intent(this, ChapterService.class);
                intent.putExtra("mangaId", mangaId);
                startService(intent);
            }

        }

    }


    public void loadManga(Manga manga) {
        this.retrieved = true;
        setTitle(manga.getTitle());
        ((SectionsPagerAdapter) mViewPager.getAdapter()).getChaptersFragment().fillViews(manga.getChapters());
        ((SectionsPagerAdapter) mViewPager.getAdapter()).getHomeFragment().fillViews(manga.getImageFile(),manga.getDescription());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manga_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ChapterService.CHAPTER_SERVICE);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        outState.putBoolean("Retrieved_Data", this.retrieved);


    }

    @Override
    public void onFragmentInteraction(String chapterId) {
        Intent intent = new Intent(MangaDetailActivity.this, FullChapterPageActivity.class);
        intent.putExtra("chapterId", chapterId);
        startActivity(intent);

        /*Fragment chapterFragment = new FullChapterPageActivity();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, chapterFragment).commit();
        */
    }


    /**
     * A {@link FragmentStatePagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        MangaHomeFragment homeFragment;
        MangaChaptersFragment chaptersFragment;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        //get item is only called when items are empty
        //no worry about checking if fragment exists
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    homeFragment = new MangaHomeFragment();
                    return homeFragment;
                case 1:
                    chaptersFragment = new MangaChaptersFragment();
                    return chaptersFragment;
            }
            return null;

            // return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.manga_intro);
                case 1:
                    return getResources().getString(R.string.manga_chapters);
            }
            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);


            switch (position) {
                case 0:
                    this.homeFragment = (MangaHomeFragment) fragment;
                    break;
                case 1:
                    this.chaptersFragment = (MangaChaptersFragment) fragment;
                    break;
                default:break;
            }
            return fragment;
        }



        public MangaHomeFragment getHomeFragment() {
            return homeFragment;
        }

        public MangaChaptersFragment getChaptersFragment() {
            return chaptersFragment;
        }
    }


    private class ChaptersReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (action.equals(ChapterService.CHAPTER_SERVICE)) {

                Manga manga = intent.getExtras().getParcelable("com.zoudev.android.mangaapp.model.Manga");
                loadManga(manga);
            }

        }
    }
}
