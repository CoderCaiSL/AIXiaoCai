package com.example.csl.aixiaocai;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qmuiteam.qmui.widget.QMUICollapsingTopBarLayout;
import com.qmuiteam.qmui.widget.QMUITopBar;

import adapter.QDRecyclerViewAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 音乐播放器
 * Created by csl on 2018/3/29.
 */

public class PlayMusicActivity extends AppCompatActivity {

    protected String musicText = "";
    @BindView(R.id.topbar)
    QMUITopBar topbar;
    @BindView(R.id.collapsing_topbar_layout)
    QMUICollapsingTopBarLayout collapsingTopbarLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    QDRecyclerViewAdapter mRecyclerViewAdapter;
    LinearLayoutManager mPagerLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        ButterKnife.bind(this);
        musicText = getIntent().getStringExtra("musicText");
        initView();
        mPagerLayoutManager = new LinearLayoutManager(PlayMusicActivity.this);
        recyclerView.setLayoutManager(mPagerLayoutManager);
        mRecyclerViewAdapter = new QDRecyclerViewAdapter();
        mRecyclerViewAdapter.setItemCount(10);
        recyclerView.setAdapter(mRecyclerViewAdapter);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        /*topBar.setTitle("音乐播放");*/
        topbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        collapsingTopbarLayout.setTitle("音乐播放器");
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
