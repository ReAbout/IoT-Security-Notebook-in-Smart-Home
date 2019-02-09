package com.jd.smartcloudmobilesdk.demo;

import android.app.Application;
import android.content.Context;

import com.jd.smartcloudmobilesdk.init.JDSmartSDK;
import com.jd.smartcloudmobilesdk.utils.Constant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class JDApplication extends Application {
    private static JDApplication instance;

    public static JDApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // 初始化ImageLoader
        ImageLoader.getInstance().init(getImageLoaderConfiguration(this));

        // JDSmartSDK初始化
        JDSmartSDK.getInstance().setServer(Constant.SERVER_ONLINE);
        JDSmartSDK.getInstance().init(this, Config.appKey);
    }

    private ImageLoaderConfiguration getImageLoaderConfiguration(Context context) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.device_occupied)
                .showImageForEmptyUri(R.mipmap.device_occupied)
                .showImageOnFail(R.mipmap.device_occupied)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(5)
                .defaultDisplayImageOptions(options)
                .build();

        return config;
    }
}
