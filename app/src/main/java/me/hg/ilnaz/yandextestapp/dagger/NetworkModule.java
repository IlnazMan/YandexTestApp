package me.hg.ilnaz.yandextestapp.dagger;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.hg.ilnaz.yandextestapp.api.Api;
import me.hg.ilnaz.yandextestapp.App;
import me.hg.ilnaz.yandextestapp.fragments.SingersListFragment;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by ilnaz on 13.04.16, 14:05.
 */
@Module(injects = SingersListFragment.class, library = true)
public class NetworkModule {

    final Application application;

    public NetworkModule(Application application) {
        this.application = application;
    }

    @Provides
    Context provideApplicationContext() {
        return application;
    }

    @Provides
    App provideApplication() {
        return (App) application;
    }

    @Provides
    @Singleton
    OkHttpClient provideHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newBuilder().connectTimeout(20, TimeUnit.SECONDS);
        okHttpClient.newBuilder().readTimeout(20, TimeUnit.SECONDS);
        return okHttpClient;
    }

    @Provides
    @Singleton
    ObjectMapper provideObjectMapper() {
        return new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES)
                .enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
                .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
                .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }


    @Provides
    @Singleton
    Api provideApi(OkHttpClient client, ObjectMapper mapper) {
        return new Retrofit.Builder()
                .baseUrl("http://cache-default01d.cdn.yandex.net/")
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build()
                .create(Api.class);
    }
}
