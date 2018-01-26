package com.zoudev.android.mangaapp;

import android.test.AndroidTestCase;

import com.zoudev.android.mangaapp.datasource.MangaDAO;
import com.zoudev.android.mangaapp.model.Manga;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by Youszef on 11/01/16.
 */
public class MangaDAO_Test extends AndroidTestCase{

    private MangaDAO dao;
    @BeforeClass
    public void prepare_DataBase_Connection()
    {
        dao = new MangaDAO(getContext());

    }


    @Before
    public void open_db_connection()
    {
        dao.open();
    }


    @Before
    public void prepare_mangas()
    {

        Manga [] mangas = new Manga[5];
        mangas[0] = new Manga("manga_id","manga_title",4,"url_path",1);
        mangas[1] = new Manga("manga_id1","manga_title2",7,"url_path1",0);
        mangas[2] = new Manga("manga_id2","manga_title3",1,"url_path2",0);
        //same replace
        mangas[3] = new Manga("manga_id2","manga_atitle4",0,"url_path2",0);

        mangas[4] = new Manga("manga_id4","manga_atitle5",10,"url_path5",1);
        dao.insertMultipleManga(mangas);

    }

    @Test
    public void look_for_bookmarked_mangas() {
        assertNotNull("Bookmarked mangas cannot be retrieved", dao.getBookmarkedMangas());
        assertEquals("Not correct amount of mangas", 2, dao.getBookmarkedMangas().size());
    }



    @Test
    public void search_on_manga_title() {
        assertNotNull("Searched terms cannot be retrieved", dao.getMangaBySearchTerm("at"));
        assertFalse("amount of searched words not right", 3 == dao.getMangaBySearchTerm("at").size());

    }

    @Test
    public void set_bookmark() {
        int firsAmount = dao.getBookmarkedMangas().size();
        dao.setBookmark("manga_id1", true);
        assertTrue("Manga cannot be Bookmarked", ++firsAmount == dao.getBookmarkedMangas().size());

    }

    @Test
    public void get_popular_manga() {

        assertTrue("Popular manga is not ordered correctly",10 == dao.getPopularManga(10).get(0).getHits());
    }

    @After
    public void close_db_connection()
    {
        dao.close();
    }





}
