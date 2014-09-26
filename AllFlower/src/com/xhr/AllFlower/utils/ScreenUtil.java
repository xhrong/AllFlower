package com.xhr.AllFlower.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by xhrong on 2014/9/26.
 */
public class ScreenUtil {
    public static int getScreenWidth(Activity context){
        DisplayMetrics dm = new DisplayMetrics();
        context. getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
}
