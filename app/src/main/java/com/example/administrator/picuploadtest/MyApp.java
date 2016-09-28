package com.example.administrator.picuploadtest;

import android.app.Application;
import android.content.Context;

import com.example.pictureuploadlibray.constant.PictureLibrary;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * Created by Administrator on 9/28/2016.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(this);

        String cache = StorageUtils.getOwnCacheDirectory(this, "PictureUpload/photo").getAbsolutePath();
        PictureLibrary.init(this, cache+"/");
    }

    public void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)

                .memoryCacheExtraOptions(480, 800)
                .threadPoolSize(Thread.NORM_PRIORITY)
                // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY)
                .denyCacheImageMultipleSizesInMemory()
                // 如果内存内有相同url的图片，但大小不同，会删除前一张图片
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                // 任务顺序为先进先出
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCache(new WeakMemoryCache())
                .diskCacheFileNameGenerator(new
                        Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100)
                // 缓存的文件数量

                .diskCache(new UnlimitedDiskCache(StorageUtils.getOwnCacheDirectory(this, "pictureUpload/Cache")))
                // 自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(
                        new BaseImageDownloader(context, 10 * 1000, 30 * 1000)) // connectTime,connectTimeout
                .build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

//    public static DisplayImageOptions.Builder getBaseDisplayImageOption(int res) {
//        return new DisplayImageOptions.Builder()
//                .showImageOnFail(res)
//                .showImageForEmptyUri(res)
//                .cacheInMemory(true)//设置下载图片是否在内存之中
//                .cacheOnDisk(true)//设置下载图片是否在sd卡之中
//                .considerExifParams(true) //是否考虑JPEG图像EXIF参数（旋转，翻转）
//                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
//                .decodingOptions(new android.graphics.BitmapFactory.Options());// 设置图片的解码配置
//    }
//
//    public static DisplayImageOptions getDisplayImageShadeOption(int t) {
//        return getBaseDisplayImageOption(R.color.half_transparent)
//                .displayer(new FadeInBitmapDisplayer(t))
//                .build();
//    }
}
