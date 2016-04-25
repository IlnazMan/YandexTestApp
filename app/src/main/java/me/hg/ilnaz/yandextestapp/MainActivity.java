package me.hg.ilnaz.yandextestapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.hg.ilnaz.yandextestapp.fragments.BaseFragment;
import me.hg.ilnaz.yandextestapp.fragments.SingerFragment;
import me.hg.ilnaz.yandextestapp.fragments.SingersListFragment;
import me.hg.ilnaz.yandextestapp.utils.OpenSingerFragmentEvent;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        manager = getSupportFragmentManager();
        toolbar.setNavigationOnClickListener(v -> {
            if (manager.getBackStackEntryCount() == 0) {
                finish();
            } else {
                manager.popBackStack();
            }
        });
        int fragmentCountsInStack = 0;
        if (savedInstanceState != null) {
            fragmentCountsInStack = savedInstanceState.getInt("stack_size");
        }
        if (fragmentCountsInStack == 0) {
            replaceFragment(new SingersListFragment());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("stack_size", manager.getBackStackEntryCount());
    }

    public void replaceFragment(BaseFragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.container, fragment);
        transaction
                .commit();
    }

    public void showFragment(BaseFragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .addToBackStack(fragment.getClass().getName())
                .replace(R.id.container, fragment);
        transaction
                .commit();
    }

    @Subscribe
    public void onEvent(OpenSingerFragmentEvent event) {
        showFragment(SingerFragment.newInstance(event.getSinger()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
