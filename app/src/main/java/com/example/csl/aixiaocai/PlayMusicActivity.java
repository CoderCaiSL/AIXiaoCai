package com.example.csl.aixiaocai;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;

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
        //加上下面这句就解决在沉浸状态栏的不显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //此FLAG可使状态栏透明，且当前视图在绘制时，从屏幕顶端开始即top = 0开始绘制，这也是实现沉浸效果的基础
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//可不加
        }
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
        /*QMUIStatusBarHelper.translucent(PlayMusicActivity.this);*/
        topbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        collapsingTopbarLayout.setTitle("音乐播放器");
        collapsingTopbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.qmui_config_color_background));
        collapsingTopbarLayout.setExpandedTitleColor(getResources().getColor(R.color.qmui_config_color_background));
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
