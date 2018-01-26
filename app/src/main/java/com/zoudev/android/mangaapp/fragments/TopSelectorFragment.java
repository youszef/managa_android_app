package com.zoudev.android.mangaapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zoudev.android.mangaapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnTopSelectorFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class TopSelectorFragment extends Fragment {
    private static final String ARG_SELECTOR = "com.zoudev.mangaap.topselector";

    private int value = 10 ;
    private SeekBar topMangas;
    private TextView topNumber;



    private OnTopSelectorFragmentInteractionListener mListener;

    public TopSelectorFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onAttachFragment(getParentFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_top_selector, container, false);
        topMangas = (SeekBar) fragmentView.findViewById(R.id.top_mangas_seekbar);
        topNumber = (TextView) fragmentView.findViewById(R.id.top_number_textview);

        if (savedInstanceState != null)
        {
            value = savedInstanceState.getInt(ARG_SELECTOR);
            topMangas.setProgress(value);
        }else
        {
            value = 10;
        }
        topMangas.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value = progress + 10;
                topNumber.setText("Top " + (value));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                onDropDownItemSelect(value);
            }
        });

        return fragmentView;
    }

    public static TopSelectorFragment newInstance() {
        TopSelectorFragment fragment = new TopSelectorFragment();
        return fragment;
    }

    public int getValue() {
        return value;
    }

    public void onDropDownItemSelect(int item) {
        if (mListener != null) {
            mListener.onSeekBarSelectedUp(item);
        }
    }


    public void onAttachFragment(Fragment fragment)
    {
        try
        {
            mListener = (OnTopSelectorFragmentInteractionListener)fragment;

        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(
                    fragment.toString() + " must implement OnPlayerSelectionSetListener");
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_SELECTOR,value);
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
    public interface OnTopSelectorFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSeekBarSelectedUp(int item);
    }
}
