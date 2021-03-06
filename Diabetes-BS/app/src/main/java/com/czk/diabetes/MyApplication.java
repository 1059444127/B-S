package com.czk.diabetes;

import android.app.Application;

import com.czk.diabetes.util.FontIconDrawable;
import com.czk.diabetes.util.ThemeUtil;

/**
 * Created by 陈忠凯 on 2017/2/18.
 */
public class MyApplication extends Application {

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ThemeUtil.setTheme(this);
        FontIconDrawable.init(getAssets(), "font_icon.ttf");
    }

}
