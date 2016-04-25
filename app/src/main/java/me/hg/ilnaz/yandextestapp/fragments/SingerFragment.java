package me.hg.ilnaz.yandextestapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.InjectView;
import me.hg.ilnaz.yandextestapp.MainActivity;
import me.hg.ilnaz.yandextestapp.R;
import me.hg.ilnaz.yandextestapp.api.models.Singer;

/**
 * Created by ilnaz on 14.04.16, 23:25.
 */
public class SingerFragment extends BaseFragment {

    @InjectView(R.id.big)
    ImageView big;
    @InjectView(R.id.desc)
    TextView desc;
    @InjectView(R.id.link)
    TextView link;
    @InjectView(R.id.genres)
    TextView genres;

    @Override
    public FragmentInfo getFragmentInfo() {
        return new FragmentInfo()
                .setViewInject(true)
                .setLayout(R.layout.singer_screen);
    }

    @Override
    public void inject() {

    }

    public static SingerFragment newInstance(Singer singer) {
        Bundle args = new Bundle();
        args.putParcelable("singer", singer);
        SingerFragment fragment = new SingerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Singer singer = getArguments().getParcelable("singer");
        if (singer != null) {
            Picasso.with(getContext())
                    .load(singer.getCover()
                    .get("big").toString())
                    .placeholder(R.drawable.place_holder)
                    .error(R.drawable.place_holder)
                    .into(big)

            ;
            desc.setText(singer.getDescription());
            StringBuilder genresTxt = new StringBuilder();
            for (String genre : singer.getGenres()) {
                genresTxt.append(genre + ", ");
            }
            genresTxt.deleteCharAt(genresTxt.length() - 2);
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(singer.getName());
            genres.setText(genresTxt);
            if (singer.getLink() != null) {
                link.setText(Html.fromHtml("<a href=\"" + singer.getLink() + "\">" + singer.getLink() + "</a>"));

                link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(singer.getLink()));
                        startActivity(i);
                    }
                });
            }
        }
    }
}
