package com.example.administrator.retrofit;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LiuXiaocong on 8/12/2016.
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherHolder> {

    private ArrayList<WeatherBean.RootBean.DailyForecastBean> mData = new ArrayList<>();

    private LayoutInflater mLayoutInflater;

    public void setData(List<WeatherBean.RootBean.DailyForecastBean> data) {
        if (data != null && data.size() > 0) {
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public WeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }
        View layout = mLayoutInflater.inflate(R.layout.weather_item, parent, false);
        return new WeatherHolder(layout);
    }

    @Override
    public void onBindViewHolder(WeatherHolder holder, int position) {
        WeatherBean.RootBean.DailyForecastBean dailyForecastBean = mData.get(position);
        holder.bind(dailyForecastBean);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class WeatherHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.day)
        TextView mDay;
        @BindView(R.id.weather_icon_start)
        SimpleDraweeView mWeatherIconStart;
        @BindView(R.id.weather_icon_end)
        SimpleDraweeView mWeatherIconEnd;
        @BindView(R.id.weather_text_start)
        TextView mWeatherStart;
        @BindView(R.id.weather_text_end)
        TextView mWeatherEnd;
        @BindView(R.id.temp_start)
        TextView mTempStart;
        @BindView(R.id.temp_end)
        TextView mTempEnd;
        @BindView(R.id.wind_style)
        TextView mWindStyle;
        @BindView(R.id.wind_power)
        TextView mWindsPower;

        public WeatherHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(WeatherBean.RootBean.DailyForecastBean dailyForecastBean) {
            mDay.setText(dailyForecastBean.getDate().substring(dailyForecastBean.getDate().indexOf("-") + 1));
            Uri uriStart = Uri.parse("http://files.heweather.com/cond_icon/" + dailyForecastBean.getCond().getCode_d() + ".png");
            mWeatherIconStart.setImageURI(uriStart);
            Uri uriEnd = Uri.parse("http://files.heweather.com/cond_icon/" + dailyForecastBean.getCond().getCode_n() + ".png");
            mWeatherIconEnd.setImageURI(uriEnd);
            mWeatherStart.setText(dailyForecastBean.getCond().getTxt_d());
            mWeatherEnd.setText(dailyForecastBean.getCond().getTxt_n());
            mTempStart.setText(dailyForecastBean.getTmp().getMin());
            mTempEnd.setText(dailyForecastBean.getTmp().getMax());
            mWindStyle.setText(dailyForecastBean.getWind().getDir());
            mWindsPower.setText(dailyForecastBean.getWind().getSc());
        }
    }
}
