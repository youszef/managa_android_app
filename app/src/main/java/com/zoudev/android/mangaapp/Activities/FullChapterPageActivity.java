package com.zoudev.android.mangaapp.Activities;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.zoudev.android.mangaapp.R;
import com.zoudev.android.mangaapp.asynctask.ChapterPageImageTask;
import com.zoudev.android.mangaapp.asynctask.ChapterAllPagesTask;
import com.zoudev.android.mangaapp.generic.ChapterViewPager;
import com.zoudev.android.mangaapp.generic.MangaPageAdapter;
import com.zoudev.android.mangaapp.generic.ZoomOutPageTransformer;
import com.zoudev.android.mangaapp.model.Page;

//http://developer.android.com/training/displaying-bitmaps/display-bitmap.html
public class FullChapterPageActivity extends AppCompatActivity {


   // private final String CURRENT_CHAPTERPAGES_TAG = "current_chapterpages";

    private Page[] chapterPages;
    private MangaPageAdapter mangaPageAdapter;
    private ChapterViewPager pager;

    public FullChapterPageActivity() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_chapter);


        pager = (ChapterViewPager) findViewById(R.id.chapter_view_pager);
        pager.setPageTransformer(true, new ZoomOutPageTransformer());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String chapterId = extras.getString("chapterId");
            // String[] pages = null;
            String fullpath = "http://www.mangaeden.com/api/chapter/" + chapterId;
            new ChapterAllPagesTask(this).execute(fullpath);


            processFullView();
            //call service
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        processFullView();
    }



    /**
     * Detects and toggles immersive mode (also known as "hidey bar" mode).
     */

    public void processFullView() {

        // BEGIN_INCLUDE (get_current_ui_flags)
        // The UI options currently enabled are represented by a bitfield.
        // getSystemUiVisibility() gives us that bitfield.
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        // END_INCLUDE (get_current_ui_flags)
        // BEGIN_INCLUDE (toggle_ui_flags)
        if (Build.VERSION.SDK_INT < 19) {

            uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.

            uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        }


        //END_INCLUDE (set_ui_flags)


        View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    //http://developer.android.com/training/system-ui/visibility.html
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        // Note that system bars will only be "visible" if none of the
                        // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            // TODO: The system bars are visible. Make any desired
                            // adjustments to your UI, such as showing the action bar or
                            // other navigational controls.
                            if (Build.VERSION.SDK_INT < 19) { // lower api
                                getWindow().getDecorView().setSystemUiVisibility(
                                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                                //unfortunately this cannot be hidden all the time
                                                //There is a limitation: because navigation controls are so important, the least user interaction will cause them to reappear immediately.
                                                // When this happens, both this flag and SYSTEM_UI_FLAG_FULLSCREEN will be cleared automatically, so that both elements reappear at the same time.
                                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                            }
                        }
                    }
                });


    }


    public void loadPages(Page[] chapterPages) {
        this.chapterPages = chapterPages;
        mangaPageAdapter = new MangaPageAdapter(getSupportFragmentManager(), chapterPages.length);


        pager.setAdapter(mangaPageAdapter);


        //load bitmap of active fragment's imageView
    }

    public void loadBitmap(String imagePath, ImageView imageView, ProgressBar progressBar) {


        new ChapterPageImageTask(imagePath, imageView, progressBar, getResources().getInteger(android.R.integer.config_shortAnimTime),this.getBaseContext()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);;
    }


    public Page[] getChapterPages() {
        return chapterPages;
    }
}
