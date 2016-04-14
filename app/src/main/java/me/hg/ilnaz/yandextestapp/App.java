package me.hg.ilnaz.yandextestapp;

import android.app.Application;

import me.hg.ilnaz.yandextestapp.dagger.Injector;
import me.hg.ilnaz.yandextestapp.dagger.NetworkModule;

/**
 * Created by ilnaz on 13.04.16, 14:52.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Injector.init(new NetworkModule(this));
    }
}
