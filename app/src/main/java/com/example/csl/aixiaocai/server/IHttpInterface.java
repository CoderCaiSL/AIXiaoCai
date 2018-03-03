package com.example.csl.aixiaocai.server;

import com.example.csl.aixiaocai.enity.ResultTuLing;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by csl on 2018/3/2.
 */

public interface IHttpInterface {

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("openapi/api/v2")
    Call<ResultTuLing> InputTuLing(@Body RequestBody route
    );
}
