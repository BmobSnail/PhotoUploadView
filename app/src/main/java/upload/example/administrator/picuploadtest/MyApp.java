package upload.example.administrator.picuploadtest;

import android.app.Application;

import upload.constant.PictureLibrary;
import upload.utils.StorageUtils;

/**
 * author：created by Snail.江
 * time: 7/29/2016 15:24
 * email：409962004@qq.com
 * TODO:
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        String cache = StorageUtils.getOwnCacheDirectory(this, "Upload/photo").getAbsolutePath();
        PictureLibrary.init(this, cache + "/");
    }
}
