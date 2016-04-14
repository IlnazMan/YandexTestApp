package me.hg.ilnaz.yandextestapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

    @Override
    public FragmentInfo getFragmentInfo() {
        return new FragmentInfo()
                .setViewInject(true)
                .setLayout(R.layout.singer_screen);
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
            Picasso.with(getContext()).load(singer.getCover().get("big").toString()).into(big);
            desc.setText(singer.getDescription());
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(singer.getName());
        }
    }
}
