package com.zoudev.android.mangaapp.fragments;

import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.zoudev.android.mangaapp.Activities.FullChapterPageActivity;
import com.zoudev.android.mangaapp.R;
import com.zoudev.android.mangaapp.generic.ChapterViewPager;
import com.zoudev.android.mangaapp.model.Manga;

/**
 * Created by Youszef on 08/01/16.
 */
public class ChapterPageFragment extends Fragment {

    // private final String CURRENT_PAGEIMAGE_TAG = "current_pageimage";
    private static final String IMAGE_DATA_EXTRA = "resId";
    private int pageNumber;
    private ImageView pageImageView;
    private ProgressBar progressBar;


    private GestureDetectorCompat mDetector;

    public ChapterPageFragment() {
    }

    public static ChapterPageFragment newInstance(int pageNumber) {
        final ChapterPageFragment f = new ChapterPageFragment();
        final Bundle args = new Bundle();
        args.putInt(IMAGE_DATA_EXTRA, pageNumber);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetector = new GestureDetectorCompat(getContext(), new MyGestureListener());
        pageNumber = getArguments() != null ? getArguments().getInt(IMAGE_DATA_EXTRA) : -1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_page_image, container, false);
        progressBar = (ProgressBar) v.findViewById(R.id.loading_spinner);
        pageImageView = (ImageView) v.findViewById(R.id.page_image);
        pageImageView.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mDetector.onTouchEvent(event);
            }
        });


        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (FullChapterPageActivity.class.isInstance(getActivity())) {
            final String imagePath = ((FullChapterPageActivity) getActivity()).getChapterPages()[pageNumber].getImageFile();
            // Call out to FullChapterPageActivity to load the bitmap in a background thread
            ((FullChapterPageActivity) getActivity()).loadBitmap(Manga.MANGAEDEN_CDN_PATH + imagePath, pageImageView, progressBar);
        }
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        outState.putParcelable(CURRENT_PAGEIMAGE_TAG, ((BitmapDrawable) pageImageView.getDrawable()).getBitmap());
//
//    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";
        private ImageView imageView;
        private boolean enableSwipe = true;
        private boolean firstTimeToPan = false;

        int totalX, totalY;
        // set scroll limits
        int maxLeft;
        int maxRight;
        int maxTop;
        int maxBottom;


//            float downX, downY;
//            int totalX, totalY;
//            int scrollByX, scrollByY;


        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            if (enableSwipe) {
                return super.onScroll(e1, e2, distanceX, distanceY);
            } else {

                ImageView imageView = ChapterPageFragment.this.pageImageView;

                if (firstTimeToPan) {
                    setSizes(imageView);
                }


                //inspired by http://stackoverflow.com/a/3732377
                //but did some major modification to fit my needs

                //after scale_crop_center sometimes it happens that vertical or horizontal axis are scaled out of bound to fit the ratio. Apparently it seems to be always 72 px from one side thus 144px
                //hence see what the biggest is to see whether x or y is out of bound to do panning
                int scrollByX = (int) distanceX;
                int scrollByY = (int) distanceY;

                if (maxRight > maxTop - 72) {
                    //Scrolling only horizontal

                    // moving image to left
                    if (distanceX > 0) {
                        //if max is reached
                        if (totalX == maxLeft) {
                            scrollByX = 0;
                        } else
                            //if space remains
                            if (totalX > maxLeft) {
                                totalX -= scrollByX;
                            } else
                                //if max is breached
                                if (totalX < maxLeft) {
                                    scrollByX = totalX + Math.abs(maxLeft);
                                    totalX = maxLeft;
                                }
                    }

                    // moving image to right
                    if (distanceX < 0) {
                        if (totalX == maxRight) {
                            scrollByX = 0;
                        } else if (totalX < maxRight) {
                            totalX += Math.abs(scrollByX);
                        } else if (totalX > maxRight) {
                            scrollByX = totalX - maxRight;
                            totalX = maxRight;
                        }
                    }
                    scrollByY = 0;
                } else {
                    // moving pic to top
                    if (distanceY > 0) {
                        if (totalY == maxTop) {
                            scrollByY = 0;
                        } else if (totalY < maxTop) {
                            totalY += scrollByY;
                        } else if (totalY > maxTop) {
                            scrollByY = maxTop - totalY;
                            totalY = maxTop;
                        }
                    }

                    // moving pic to bottom
                    if (distanceY < 0) {
                        if (totalY == maxBottom) {
                            scrollByY = 0;
                        } else if (totalY > maxBottom) {
                            totalY += scrollByY;
                        } else if (totalY < maxBottom) {
                            scrollByY = maxBottom + Math.abs(totalY);
                            totalY = maxBottom;
                        }
                    }
                    scrollByX = 0;
                }


                imageView.scrollBy(scrollByX, scrollByY);


                return true;
            }
        }

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }


        @Override
        public boolean onDoubleTap(MotionEvent e) {
            ImageView imageView = ChapterPageFragment.this.pageImageView;
            if (imageView.getScaleType() == ImageView.ScaleType.FIT_CENTER) {

                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                totalX = totalY = 0;
                enableSwipe = false;
                firstTimeToPan = true;
                ((ChapterViewPager) getActivity().findViewById(R.id.chapter_view_pager)).setEnableSwipe(enableSwipe);
            } else {
                imageView.scrollBy(totalX, -totalY);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                enableSwipe = true;
                firstTimeToPan = false;
                ((ChapterViewPager) getActivity().findViewById(R.id.chapter_view_pager)).setEnableSwipe(enableSwipe);
            }
            return super.onDoubleTap(e);
        }


        private void setSizes(ImageView image) {

            this.imageView = image;
            //get the size of the image and  the screen

            Point imageSize = getMeasure(image);

            int bitmapWidth = imageSize.x;
            int bitmapHeight = imageSize.y;
            Point maxPoint = new Point();
            getActivity().getWindowManager().getDefaultDisplay().getSize(maxPoint);
            int screenWidth = maxPoint.x;
            int screenHeight = maxPoint.y;

            // set maximum scroll amount (based on center of image)
            int maxX = ((bitmapWidth / 2) - (screenWidth / 2));
            int maxY = ((bitmapHeight / 2) - (screenHeight / 2));

            maxLeft = (maxX * -1);
            maxRight = maxX;
            maxTop = maxY;
            maxBottom = (maxY * -1);
            firstTimeToPan = false;

        }

        //http://stackoverflow.com/a/15538856
        private Point getMeasure(ImageView image) {
            // Get image matrix values and place them in an array
            float[] f = new float[9];
            image.getImageMatrix().getValues(f);

            // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
            final float scaleX = f[Matrix.MSCALE_X];
            final float scaleY = f[Matrix.MSCALE_Y];

            // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
            final Drawable d = image.getDrawable();
            final int origW = d.getIntrinsicWidth();
            final int origH = d.getIntrinsicHeight();

            // Calculate the actual dimensions
            final int actW = Math.round(origW * scaleX);
            final int actH = Math.round(origH * scaleY);

            return new Point(actW, actH);
        }
    }
}