package me.hg.ilnaz.yandextestapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import me.hg.ilnaz.yandextestapp.MainActivity;

/**
 * Created by ilnaz on 13.04.16, 13:47.
 */
public abstract class BaseFragment extends Fragment {
    public abstract FragmentInfo getFragmentInfo();

    private FragmentInfo fragmentInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentInfo = getFragmentInfo();
    }

    private boolean mFirstAttach = true;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentInfo = getFragmentInfo();
        if (mFirstAttach && fragmentInfo.isDependencyInject()) {
            inject();
            mFirstAttach = false;
        }
    }

    public void inject() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(fragmentInfo.getLayout(), container, false);
        if (fragmentInfo.isViewInject()) {
            ButterKnife.inject(this, v);
        }
        setHasOptionsMenu(true);
        return v;
    }
}
