package me.hg.ilnaz.yandextestapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ilnaz on 14.04.16, 23:29.
 */
public class SP {
    public static SharedPreferences get(Context context) {
        return context.getSharedPreferences("PREF", Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor edit(Context context) {
        return context.getSharedPreferences("PREF", Context.MODE_PRIVATE).edit();
    }

}
