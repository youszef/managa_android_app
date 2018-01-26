package com.zoudev.android.mangaapp.model;

/**
 * Created by Youszef on 08/12/15.
 */
public class Page {

    private int pageNumber;

    //for image =>> http://stackoverflow.com/a/7331698
    private String imageFile;


    /**
     * @param 'Data' can be retrieved when getting info from Chapter's Id 1st item from array is page number
     * @param 'Data' can be retrieved when getting info from Chapter's Id 2nd item from array is imagefilepath remember to use cdn
     */
    public Page(int pageNumber, String image) {
        this.pageNumber = pageNumber;
        this.imageFile = image;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public String getImageFile() {
        return imageFile;
    }
}
