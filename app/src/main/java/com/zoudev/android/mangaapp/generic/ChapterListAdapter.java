package com.zoudev.android.mangaapp.generic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zoudev.android.mangaapp.R;
import com.zoudev.android.mangaapp.model.Chapter;

import java.util.List;

/**
 * Created by Youszef on 03/01/16.
 */
public class ChapterListAdapter extends ArrayAdapter<Chapter> {

    private final Context context;
    private final List<Chapter> chapters;


    public ChapterListAdapter(Context context, int resource, List<Chapter> chapters) {
        super(context, resource, chapters);
        this.context = context;
        this.chapters = chapters;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View row = convertView;
        if (row == null) {
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = li.inflate(R.layout.chapter_list_item, parent, false);
        }
        TextView textViewTitle = (TextView) row.findViewById(R.id.chapter_number);
        textViewTitle.setText("Chapter " + chapters.get(position).getChapterNumber());
        return row;
    }
}
