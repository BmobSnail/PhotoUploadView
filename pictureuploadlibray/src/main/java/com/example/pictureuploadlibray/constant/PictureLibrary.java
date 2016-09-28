package com.example.pictureuploadlibray.constant;

import android.content.Context;

/**
 * Created by Administrator on 2016/7/22.
 */
public class PictureLibrary {

    public static Context context;

    public static void init(Context context,String cachePath){
        PictureLibrary.context = context;
        Constant.CACHE_PATH = cachePath;
    }
}
