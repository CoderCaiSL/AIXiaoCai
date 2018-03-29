package com.example.csl.aixiaocai;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.qmuiteam.qmui.widget.QMUICollapsingTopBarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 音乐播放器
 * Created by csl on 2018/3/29.
 */

public class PlayMusicActivity extends AppCompatActivity {

    protected String musicText = "";
    /*@BindView(R.id.topBar)
    QMUITopBar topBar;*/
    @BindView(R.id.topBarLayout)
    QMUICollapsingTopBarLayout topBarLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        ButterKnife.bind(this);
        musicText = getIntent().getStringExtra("musicText");
        initView();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        /*topBar.setTitle("音乐播放");*/
    }

    public static void startAction(Context context, String musicText) {
        Intent intent = new Intent(context, PlayMusicActivity.class);
        intent.putExtra("musicText", musicText);
        context.startActivity(intent);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


        }
    };
}
