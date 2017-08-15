package com.micro.hellowold;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Administrator on 2017/8/15.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //进行xutils注册
        x.Ext.init(this);
        x.Ext.setDebug(true); //是否输出debug日志，开启debug会影响性能。
    }
}
