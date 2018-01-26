package com.zoudev.android.mangaapp.generic;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

//http://stackoverflow.com/a/9650884
/**
 * Created by Youszef on 08/01/16.
 */
public class ChapterViewPager  extends ViewPager {

    private boolean enableSwipe = true;

    public ChapterViewPager(Context context) {
        super(context);
    }

    public ChapterViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enableSwipe) {
            return super.onInterceptTouchEvent(event);
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enableSwipe) {
            return super.onTouchEvent(event);
        }
        else
        {
            return false;
        }
    }

    public void setEnableSwipe(boolean enableSwipe) {
        this.enableSwipe = enableSwipe;
    }
}