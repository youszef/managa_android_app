package com.zoudev.android.mangaapp.generic;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoudev.android.mangaapp.Activities.MangaDetailActivity;
import com.zoudev.android.mangaapp.R;
import com.zoudev.android.mangaapp.asynctask.ImageLoadTask;
import com.zoudev.android.mangaapp.datasource.MangaDAO;
import com.zoudev.android.mangaapp.model.Manga;

import java.util.List;

/**
 * Created by Youszef on 02/01/16.
 */
public class MangaListAdapter extends ArrayAdapter<Manga>  {

    private final Context context;
    private final List<Manga> mangas;
    private BookMarkListAdapterInteractionListener listener;

    public MangaListAdapter(Context context, int resource, List<Manga> mangas,BookMarkListAdapterInteractionListener listener) {
        super(context, resource, mangas);
        this.context = context;
        this.mangas = mangas;
        this.listener = listener;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        if (row == null) {
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = li.inflate(R.layout.full_manga_list_item, parent, false);
        }

        ViewHolder holder = (ViewHolder) row.getTag();
        if (holder == null) {
            holder = new ViewHolder(row);
            row.setTag(holder);
        }


        holder.imageView.setTag(mangas.get(position).getMangaId());
        holder.textViewHits.setTag(mangas.get(position).getMangaId());
        holder.textViewTitle.setTag(mangas.get(position).getMangaId());

        holder.imageView.setOnClickListener(new MangaListComponentClick());
        holder.textViewHits.setOnClickListener(new MangaListComponentClick());
        holder.textViewTitle.setOnClickListener(new MangaListComponentClick());




        holder.imageButtonBookmark.setTag(position);
        if (mangas.get(position).isBookmark()) {
            holder.imageButtonBookmark.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_bookmark_white_48dp));
        } else {
            holder.imageButtonBookmark.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_bookmark_border_white_48dp));
        }


        if (mangas.get(position).isBookmark()) {
            holder.imageButtonBookmark.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_bookmark_white_48dp));
        }
        holder.imageButtonBookmark.setTag(position);
        holder.imageButtonBookmark.setOnClickListener(new View.OnClickListener() {

            private ViewHolder h;

            private View.OnClickListener setHolder(ViewHolder holder) {
                h = holder;
                return this;
            }

            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                mangas.get(pos).setBookmark(!mangas.get(pos).isBookmark());
                MangaDAO dao = new MangaDAO(getContext());
                dao.open();
                dao.setBookmark(mangas.get(pos).getMangaId(), mangas.get(pos).isBookmark());
                dao.close();
                if (mangas.get(pos).isBookmark()) {
                    h.imageButtonBookmark.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_bookmark_white_48dp));
                } else {
                    h.imageButtonBookmark.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_bookmark_border_white_48dp));
                }
                listener.onBookmarkClick();
            }
        }.setHolder(holder));
        if (mangas.get(position).getImageFile() != null && !mangas.get(position).getImageFile().isEmpty()) {
            new ImageLoadTask(Manga.MANGAEDEN_CDN_PATH + mangas.get(position).getImageFile(), holder.imageView,this.getContext()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }


        holder.textViewTitle.setText(mangas.get(position).getTitle());
        holder.textViewHits.setText(context.getResources().getString(R.string.manga_hits) + ": " + mangas.get(position).getHits());

        return row;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textViewTitle;
        TextView textViewHits;
        ImageButton imageButtonBookmark;
        int position;

        public ViewHolder(View row) {
            this.imageView = (ImageView) row.findViewById(R.id.mangalist_image);
            imageView = (ImageView) row.findViewById(R.id.mangalist_image);
            textViewTitle = (TextView) row.findViewById(R.id.manga_title_text);
            textViewHits = (TextView) row.findViewById(R.id.manga_amount_hits_text);
            imageButtonBookmark = (ImageButton) row.findViewById(R.id.bookmark_image_button);
        }
    }


    private class MangaListComponentClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            String mangaId = (String)v.getTag();

            Log.i(TAG.LOGTAG, "Clicked Manga ID is: " + mangaId);
            Intent intent = new Intent(v.getContext(), MangaDetailActivity.class);
            intent.putExtra("mangaId", mangaId);
            v.getContext().startActivity(intent);
        }
    }
    public interface BookMarkListAdapterInteractionListener {
        void onBookmarkClick();
    }
}
