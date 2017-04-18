package com.feicui.speechdemo.speechdemo;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;


/**
 * Created by gqq on 2017/4/17.
 */

public class SpeechApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化的工作
        // 将“12345678”替换成您申请的APPID，申请地址：http://open.voicecloud.cn
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=58f44088");
    }
}
