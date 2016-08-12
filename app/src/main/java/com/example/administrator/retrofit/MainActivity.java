package com.example.administrator.retrofit;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

//https://api.heweather.com/x3/weather?cityid=CN101281501&key=a77bce0984944a66a621a113d1e23bda
public class MainActivity extends AppCompatActivity {
    Call<ResponseBody> mCall;
    @BindView(R.id.air)
    TextView airTextView;
    @BindView(R.id.city)
    TextView cityTextView;
    @BindView(R.id.temp_now)
    TextView tempNowTextView;
    @BindView(R.id.wind_style)
    TextView windStyleTextView;
    @BindView(R.id.wind_power)
    TextView windPowerTextView;
    @BindView(R.id.now_weather_icon)
    SimpleDraweeView mWeatherIconDraweeView;
    @BindView(R.id.now_weather_text)
    TextView mWeatherNowTextView;
    @BindView(R.id.future_weather_list)
    RecyclerView mFutureWeather;
    @BindView(R.id.suggest_one)
    TextView suggestOne;
    @BindView(R.id.suggest_two)
    TextView suggestTwo;
    @BindView(R.id.suggest_three)
    TextView suggestThree;
    private WeatherAdapter mWeatherAdapter;

    List<String> mRandomColorList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mFutureWeather.setLayoutManager(linearLayoutManager);
        mWeatherAdapter = new WeatherAdapter();
        mFutureWeather.setAdapter(mWeatherAdapter);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.heweather.com/")
                .build();
        mRandomColorList.add("#00b271");
        mRandomColorList.add("#479ac7");
        mRandomColorList.add("#b45b3e");
        mRandomColorList.add("#66cccc");
        mRandomColorList.add("#336699");
        mRandomColorList.add("#8080c0");


        WeatherService service = retrofit.create(WeatherService.class);
        mCall = service.getWeather("CN101281501");
        load();
    }

    private void load() {
        mCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String res = response.body().string();
                    res = res.replace("HeWeather data service 3.0", "root");
                    WeatherBean bean = GsonImpl.get().toObject(res, WeatherBean.class);
                    Log.d("retrofit", res);
                    refreshUI(bean);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void refreshUI(WeatherBean bean) {
        if (bean == null) return;
        airTextView.setText(bean.getRoot().get(0).getAqi().getCity().getQlty());
        cityTextView.setText(bean.getRoot().get(0).getBasic().getCity());
        tempNowTextView.setText(bean.getRoot().get(0).getNow().getTmp() + "");
        windStyleTextView.setText(bean.getRoot().get(0).getNow().getWind().getDir());
        windPowerTextView.setText(bean.getRoot().get(0).getNow().getWind().getSc());
        Uri uri = Uri.parse("http://files.heweather.com/cond_icon/" + bean.getRoot().get(0).getNow().getCond().getCode() + ".png");
        mWeatherIconDraweeView.setImageURI(uri);
        mWeatherNowTextView.setText(bean.getRoot().get(0).getNow().getCond().getTxt());
        mWeatherAdapter.setData(bean.getRoot().get(0).getDaily_forecast());
        suggestOne.setText(bean.getRoot().get(0).getSuggestion().getComf().getTxt());
        suggestTwo.setText(bean.getRoot().get(0).getSuggestion().getDrsg().getTxt());
        suggestThree.setText(bean.getRoot().get(0).getSuggestion().getFlu().getTxt());
    }
}
