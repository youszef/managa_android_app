package com.zoudev.android.mangaapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.zoudev.android.mangaapp.R;
import com.zoudev.android.mangaapp.generic.ChapterListAdapter;
import com.zoudev.android.mangaapp.model.Chapter;

import java.util.ArrayList;

/**
 * Created by Youszef on 07/01/16.
 */
public class MangaChaptersFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private ArrayList<Chapter> chapters;
    private ListView chaptersList;

    private ProgressBar progress;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            ArrayList<Chapter> c = savedInstanceState.getParcelableArrayList("Chapters");
            if (c != null)
            {
                fillViews(c);
            }
        } else {
            this.progress.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chapters_overview, container, false);
        chaptersList = (ListView) rootView.findViewById(R.id.chapters_list);
        progress = (ProgressBar) rootView.findViewById(R.id.chapters_loading_inf_progress);

        return rootView;
    }


    public void fillViews(ArrayList<Chapter> chapters) {
        this.progress.setVisibility(View.GONE);
        this.chapters = chapters;
        ChapterListAdapter adapter = new ChapterListAdapter(this.getContext(), android.R.layout.simple_list_item_1, chapters);
        chaptersList.setAdapter(adapter);

        chaptersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Chapter chapter = (Chapter) parent.getAdapter().getItem(position);

                Log.i(com.zoudev.android.mangaapp.generic.TAG.LOGTAG, "Clicked Chapter ID is: " + chapter.getChapterId());

                onChapterPressed(chapter.getChapterId());


                //   Intent intent = new Intent(getActivity(), MangaPageActivity.class);
                //  intent.putExtra("chapterId",chapter.getChapterId()) ;
                //   startActivity(intent);

            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onChapterPressed(String mangaId) {
        if (mListener != null) {
            mListener.onFragmentInteraction(mangaId);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("Chapters", this.chapters);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFullMangasFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(String chapterId);
    }
}
