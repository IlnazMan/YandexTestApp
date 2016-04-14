package me.hg.ilnaz.yandextestapp.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import me.hg.ilnaz.yandextestapp.MainActivity;
import me.hg.ilnaz.yandextestapp.R;
import me.hg.ilnaz.yandextestapp.api.Api;
import me.hg.ilnaz.yandextestapp.api.models.Singer;
import me.hg.ilnaz.yandextestapp.dagger.Injector;
import me.hg.ilnaz.yandextestapp.fragments.adapters.SingersAdapter;
import me.hg.ilnaz.yandextestapp.utils.Consts;
import me.hg.ilnaz.yandextestapp.utils.SP;
import rx.schedulers.Schedulers;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

/**
 * Created by ilnaz on 13.04.16, 13:53.
 */
public class SingersListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Inject
    Api api;
    private SingersAdapter adapter;
    @InjectView(R.id.rv)
    RecyclerView recyclerView;
    @InjectView(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.fast_scroller)
    VerticalRecyclerViewFastScroller fastScroller;

    @Override
    public FragmentInfo getFragmentInfo() {
        return new FragmentInfo().setLayout(R.layout.fragment_list)
                .setMenu(R.menu.list)
                .setDependencyInject(true)
                .setViewInject(true);
    }

    @Override
    public void inject() {
        Injector.inject(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (adapter != null && adapter.getItemCount() > 0) {
            outState.putParcelableArrayList("list", new ArrayList<>(adapter.getSingers()));
            outState.putParcelableArrayList("listFilter", new ArrayList<>(adapter.getFilterSingers()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_red_light, android.R.color.holo_blue_light);
        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        swipeRefreshLayout.setEnabled(true);
                        break;
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                swipeRefreshLayout.setEnabled(false);

            }
        });
        adapter = new SingersAdapter(getContext());
        fastScroller.setRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(fastScroller.getOnScrollListener());
        recyclerView.setAdapter(adapter);
        if (savedInstanceState != null) {
            List<Singer> list = savedInstanceState.getParcelableArrayList("list");
            List<Singer> listFilter = savedInstanceState.getParcelableArrayList("listFilter");
            if (list != null && !list.isEmpty()) {
                setSingersToAdapter(list, listFilter);
            } else {
                loadSingers();
            }
        } else {
            loadSingers();
        }
    }

    private void loadSingers() {
        String jsonStr = SP.get(getContext()).getString(Consts.SINGERS_CACHE, "");
        Type listType = new TypeToken<List<Singer>>() {
        }.getType();
        List<Singer> cachedSingers = new Gson().fromJson(jsonStr, listType);
        if (cachedSingers == null || cachedSingers.isEmpty()) {
            swipeRefreshLayout.setRefreshing(true);
            api.getArtists().subscribeOn(Schedulers.newThread())
                    .doOnEach(notification -> getActivity().runOnUiThread(() -> swipeRefreshLayout.setRefreshing(false)))
                    .doOnError(err -> {
                        Log.wtf(Consts.TAG, err.getMessage());
                        Snackbar.make(getView(), err.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
                    })
                    .onErrorReturn(err -> null)
                    .doOnNext(singers -> {
                                if (singers == null) return;

                                SP.edit(getContext()).putString(Consts.SINGERS_CACHE, new Gson().toJson(singers)).apply();
                                getActivity().runOnUiThread(() -> {
                                    setSingersToAdapter(singers, singers);
                                });
                            }
                    )
                    .subscribe();
        } else {
            swipeRefreshLayout.setRefreshing(false);
            setSingersToAdapter(cachedSingers, cachedSingers);
        }
    }

    private void setSingersToAdapter(List<Singer> singers, List<Singer> filtered) {
        adapter.setSingers(singers);
        adapter.setFilterSingers(filtered);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        loadSingers();
    }


    private boolean onSearch(String query) {
        ArrayList<Singer> list = new ArrayList<>();
        for (Singer singer : adapter.getSingers()) {
            if (singer.getName().toLowerCase().contains(query.toLowerCase())) {
                list.add(singer);
            }
        }
        adapter.setFilterSingers(list);
        adapter.notifyDataSetChanged();
        return true;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.list, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        if (item == null) return;
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) item.getActionView();
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                public boolean onQueryTextChange(String newText) {
                    return onSearch(newText);
                }

                public boolean onQueryTextSubmit(String query) {
                    return onSearch(query);
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
    }
}
