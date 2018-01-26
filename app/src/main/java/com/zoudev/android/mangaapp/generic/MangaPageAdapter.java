package com.zoudev.android.mangaapp.generic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.zoudev.android.mangaapp.fragments.ChapterPageFragment;

//http://developer.android.com/training/displaying-bitmaps/display-bitmap.html
/**
 * Created by Youszef on 07/01/16.
 */
// automatically destroys and saves state of the Fragments in the ViewPager as they disappear off-screen, keeping memory usage down.
public class MangaPageAdapter extends FragmentStatePagerAdapter {
    private final int mSize;

    public MangaPageAdapter(FragmentManager fm, int size) {
        super(fm);
        mSize = size;

    }

    @Override
    public int getCount() {
        return mSize;
    }

    @Override
    public Fragment getItem(int position) {
        return ChapterPageFragment.newInstance(position);
    }
}