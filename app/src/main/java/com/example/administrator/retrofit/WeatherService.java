package com.example.administrator.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by LiuXiaocong on 8/12/2016.
 */
public interface  WeatherService {
    @GET("/x3/weather?key=a77bce0984944a66a621a113d1e23bda")
    Call<ResponseBody> getWeather(@Query("cityid") String cid);
}
