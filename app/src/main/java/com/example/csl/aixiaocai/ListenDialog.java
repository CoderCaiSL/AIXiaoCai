package com.example.csl.aixiaocai;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by csl on 2018/3/3.
 */

public class ListenDialog extends Dialog{

    private Context context;
    private String type;
    private ImageView imgGif;
    public ListenDialog(Context context,String type) {
        super(context);
        this.context = context;
        this.type = type;
    }

    public ListenDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        View mView = mLayoutInflater.inflate(R.layout.listen_dialog, null);
        setContentView(mView);
        imgGif = this.findViewById(R.id.imgGif);
        /*if (type.equals("thinking")){
            Glide.with(context).load(R.drawable.chat4).into(new GlideDrawableImageViewTarget(imgGif));
        }*/
    }
    public void setImgGif(String type){

    }
}
