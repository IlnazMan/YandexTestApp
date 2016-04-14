package me.hg.ilnaz.yandextestapp.api;

import java.util.List;

import me.hg.ilnaz.yandextestapp.api.models.Singer;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by ilnaz on 29.03.16, 22:04.
 */
public interface Api {
    @GET("/download.cdn.yandex.net/mobilization-2016/artists.json")
    Observable<List<Singer>> getArtists();
}
