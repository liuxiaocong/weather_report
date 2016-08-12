package com.example.administrator.retrofit;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    Random random;


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
        random = new Random();
        int target = random.nextInt(mRandomColorList.size());
        setFirstSuggestBg(mRandomColorList.get(target));
        mRandomColorList.remove(target);
        target = random.nextInt(mRandomColorList.size());
        setSecondSuggestBg(mRandomColorList.get(target));
        mRandomColorList.remove(target);
        target = random.nextInt(mRandomColorList.size());
        setThirdSuggestBg(mRandomColorList.get(target));
        mRandomColorList.remove(target);

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

    public static int getPxFromDp(Context context, int dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (dp * metrics.density);
    }

    private void setFirstSuggestBg(String color) {
        try {
            RadiusDrawable cd = new RadiusDrawable(getPxFromDp(this, 5),
                    getPxFromDp(this, 5),
                    0,
                    0,
                    Color.parseColor(color));
            suggestOne.setBackgroundDrawable(cd);
        } catch (Exception e) {
            Log.d("setFirstSuggestBg", e.toString());
        }
    }

    private void setSecondSuggestBg(String color) {
        try {
            RadiusDrawable cd = new RadiusDrawable(0,
                    0,
                    0,
                    0,
                    Color.parseColor(color));
            suggestTwo.setBackgroundDrawable(cd);
        } catch (Exception e) {
            Log.d("setSecondSuggestBg", e.toString());
        }
    }

    private void setThirdSuggestBg(String color) {
        try {
            RadiusDrawable cd = new RadiusDrawable(0,
                    0,
                    getPxFromDp(this, 5),
                    getPxFromDp(this, 5),
                    Color.parseColor(color));
            suggestThree.setBackgroundDrawable(cd);
        } catch (Exception e) {
            Log.d("setThirdSuggestBg", e.toString());
        }
    }
}
