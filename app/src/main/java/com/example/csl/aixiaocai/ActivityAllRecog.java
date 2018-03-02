package com.example.csl.aixiaocai;

import android.content.Context;
import android.content.Intent;

import com.example.csl.aixiaocai.recognization.CommonRecogParams;
import com.example.csl.aixiaocai.recognization.all.AllRecogParams;
import com.example.csl.aixiaocai.setting.AllSetting;

/**
 * Created by fujiayi on 2017/6/24.
 */

public class ActivityAllRecog extends ActivityRecog {
    {
        descText = "所有识别参数一起的示例。请先参照之前的3个识别示例。\n";

        enableOffline = true; // 请确认不使用离线命令词功能后，改为false
    }

    public ActivityAllRecog() {
        super();
        settingActivityClass = AllSetting.class;
    }

    @Override
    protected CommonRecogParams getApiParams() {
        return new AllRecogParams(this);
    }

    /**
     * 启动界面
     * @param context
     */
    public static void  ActionStart(Context context){
        Intent intent = new Intent(context,ActivityAllRecog.class);
        context.startActivity(intent);
    }

}
