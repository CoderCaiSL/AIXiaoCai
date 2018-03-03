package com.example.csl.aixiaocai.httpRetrofitClient;

import com.example.csl.aixiaocai.enity.InputTuLing;
import com.example.csl.aixiaocai.enity.ResultTuLing;
import com.example.csl.aixiaocai.server.IHttpInterface;
import com.google.gson.Gson;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 图灵机器人连接
 * Created by csl on 2018/3/2.
 */

public class InputTuLingHttp {

    public void InputTuLingText(InputTuLing inputTuLing){
        Gson gson = new Gson();
        IHttpInterface iHttpInterface = RetrofitClient.getRetrofit();
        String strEntity = gson.toJson(inputTuLing);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"),strEntity);
        Call<ResultTuLing> call = iHttpInterface.InputTuLing(body);
        call.enqueue(new Callback<ResultTuLing>() {
            @Override
            public void onResponse(Call<ResultTuLing> call, Response<ResultTuLing> response) {
                getRetrofitListener.getResultSuccess(response);
            }
            @Override
            public void onFailure(Call<ResultTuLing> call, Throwable t) {
                getRetrofitListener.getResultFailure(t.getMessage());
            }
        });
    }

    public interface GetRetrofitListener{
        void getResultSuccess(Response<ResultTuLing> response);
        void getResultFailure(String message);
    }

    private GetRetrofitListener getRetrofitListener;
    public void setGetRetrofitListener(GetRetrofitListener getRetrofitListener){
        this.getRetrofitListener = getRetrofitListener;
    }
}
