package me.hg.ilnaz.yandextestapp.fragments.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import me.hg.ilnaz.yandextestapp.R;
import me.hg.ilnaz.yandextestapp.api.models.Singer;
import me.hg.ilnaz.yandextestapp.utils.OpenSingerFragmentEvent;

/**
 * Created by ilnaz on 13.04.16, 23:04.
 */
public class SingersAdapter extends RecyclerView.Adapter<SingersAdapter.SingerHolder> {

    private List<Singer> singers;
    private List<Singer> filterSingers;
    private Context context;

    public List<Singer> getSingers() {
        return singers;
    }

    public SingersAdapter(List<Singer> singers, Context context) {
        this.singers = singers;
        this.filterSingers = singers;
        this.context = context;
    }

    public SingersAdapter(Context context) {
        singers = new ArrayList<>();
        filterSingers = new ArrayList<>();
        this.context = context;
    }

    public void setSingers(List<Singer> singers) {
        this.singers = singers;
    }

    public List<Singer> getFilterSingers() {
        return filterSingers;
    }

    public void setFilterSingers(List<Singer> filterSingers) {
        this.filterSingers = filterSingers;
    }

    @Override
    public SingerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new SingerHolder(inflater.inflate(R.layout.item_singer, parent, false), context);
    }

    @Override
    public void onBindViewHolder(SingerHolder holder, int position) {
        holder.draw(filterSingers.get(position));
        holder
                .itemView
                .setOnClickListener(v -> EventBus.getDefault().post(new OpenSingerFragmentEvent(filterSingers.get(position))));
    }

    @Override
    public int getItemCount() {
        return filterSingers.size();
    }

    static class SingerHolder extends RecyclerView.ViewHolder {

        private Context context;

        public SingerHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
        }

        public void draw(Singer singer) {
            ((TextView) itemView.findViewById(R.id.name)).setText(singer.getName());
            ((TextView) itemView.findViewById(R.id.genre)).setText(singer.getGenres().toString());
            ((TextView) itemView.findViewById(R.id.songs_albums_count)).setText(String.format(context.getString(R.string.albums_songs_count), singer.getAlbums(), singer.getTracks()));
            Picasso.with(context).load(singer.getCover().get("small")
                    .toString())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(((ImageView) itemView.findViewById(R.id.small)));
        }
    }
}
