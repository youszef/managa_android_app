package com.zoudev.android.mangaapp.asynctask;

import android.os.AsyncTask;

import com.zoudev.android.mangaapp.Activities.FullChapterPageActivity;
import com.zoudev.android.mangaapp.generic.ConnectionManager;
import com.zoudev.android.mangaapp.model.Page;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Youszef on 07/01/16.
 */
public class ChapterAllPagesTask extends AsyncTask<String, Void, Page[]> {

    private FullChapterPageActivity context;

    public ChapterAllPagesTask(FullChapterPageActivity context) {
        this.context = context;

    }

    @Override
    protected Page[] doInBackground(String... params) {
        if (ConnectionManager.hasConnection(context)) {
            try {
                URLConnection urlConnection = new URL(params[0]).openConnection();
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                String result = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }

                inputStream.close();

                JSONObject jsonResponse = new JSONObject(result);
                JSONArray jsonPages = jsonResponse.optJSONArray("images");
                ArrayList<Page> chapterPagesArrayList = new ArrayList<>();
                int pageNumber;
                String filePath;
                for (int j = 0; j < jsonPages.length(); j++) {
                    pageNumber = jsonPages.getJSONArray(j).getInt(0);
                    filePath = jsonPages.getJSONArray(j).getString(1);
                    chapterPagesArrayList.add(new Page(pageNumber, filePath));
                }

                Collections.sort(chapterPagesArrayList, new Comparator<Page>() {
                    @Override
                    public int compare(Page lhs, Page rhs) {

                        return lhs.getPageNumber() - rhs.getPageNumber();
                    }
                });

                return chapterPagesArrayList.toArray(new Page[chapterPagesArrayList.size()]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Page[] pages) {
        super.onPostExecute(pages);
        context.loadPages(pages);
    }

    //    @Override
//    protected void onPostExecute(Void aVoid) {
//        super.onPostExecute(aVoid);
//        context.loadPages(chapterPages);
//    }
}