package me.hg.ilnaz.yandextestapp.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.hg.ilnaz.yandextestapp.api.models.Singer;

/**
 * Created by ilnaz on 15.04.16, 0:14.
 */
//Event for EventBus to open SingerFragment from List
@AllArgsConstructor
public class OpenSingerFragmentEvent {
    @Getter
    private Singer singer;
}
