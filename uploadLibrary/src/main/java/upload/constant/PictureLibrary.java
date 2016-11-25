package upload.constant;

import android.content.Context;

/**
 * author：created by Snail.江
 * time: 7/23/2016 13:13
 * email：409962004@qq.com
 * TODO:
 */
public class PictureLibrary {

    public static Context context;

    public static void init(Context context,String cachePath){
        PictureLibrary.context = context;
        Constant.CACHE_PATH = cachePath;
    }
}
