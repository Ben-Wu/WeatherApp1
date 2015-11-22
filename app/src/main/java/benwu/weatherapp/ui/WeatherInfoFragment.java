package benwu.weatherapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import benwu.weatherapp.R;
import benwu.weatherapp.data.DataManager;
import benwu.weatherapp.data.WeatherDataObject;
import benwu.weatherapp.view.SlidingTabLayout;

/**
 * Created by Ben Wu on 11/14/2015.
 */
public class WeatherInfoFragment extends Fragment {

    public static final String TAG = "WeatherInfoFragment";

    private SlidingTabLayout mSlidingTabLayout;

    private ViewPager mViewPager;

    private String[] tabTitles = {"OpenWeatherMap", "Weather Underground", "Yahoo", "World Weather Online", "Aeris Weather"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new WeatherPagerAdapter());

        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    class WeatherPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            WeatherDataObject data = DataManager.getInstance().getWeatherData()[position];
            View view;
            TextView curTemp;
            TextView locationName;
            TextView time;
            TextView conditions;
            TextView humidity;
            ImageButton logoImage;
            ImageView weatherIcon;

            if(data == null) {
                view = getActivity().getLayoutInflater().inflate(R.layout.pager_nodata, container, false);
                logoImage = (ImageButton) view.findViewById(R.id.logoImage2);
            } else {
                view = getActivity().getLayoutInflater().inflate(R.layout.pager_weather, container, false);
                curTemp = (TextView) view.findViewById(R.id.curTemp);
                locationName = (TextView) view.findViewById(R.id.locationName);
                time = (TextView) view.findViewById(R.id.updatedTime);
                conditions = (TextView) view.findViewById(R.id.weatherDesc);
                humidity = (TextView) view.findViewById(R.id.curHumidity);
                logoImage = (ImageButton) view.findViewById(R.id.logoImage);
                weatherIcon = (ImageView) view.findViewById(R.id.weatherImage);

                weatherIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
                weatherIcon.setImageDrawable(container.getResources().getDrawable(matchIcon(data.getDescription())));

                curTemp.setText(String.format("%s °C", String.valueOf(data.getCurTemp())));
                locationName.setText(String.valueOf(data.getLocation()));
                time.setText(data.getTime());
                conditions.setText(data.getDescription());
                humidity.setText(data.getHumidity() + "%");
            }

            container.addView(view);

            switch(position) {
                case 0:
                    logoImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://openweathermap.org/"));
                            startActivity(intent);
                        }
                    });
                    break;
                case 1:
                    logoImage.setImageDrawable(getResources().getDrawable(R.drawable.logo_wunderground));
                    logoImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://www.wunderground.com/?apiref=08287701d9ba4ce8"));
                            startActivity(intent);
                        }
                    });
                    break;
                case 2:
                    logoImage.setImageDrawable(getResources().getDrawable(R.drawable.logo_yahoo));
                    logoImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://www.yahoo.com/?ilc=401"));
                            startActivity(intent);
                        }
                    });
                    break;
                case 3:
                    logoImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://www.worldweatheronline.com/"));
                            startActivity(intent);
                        }
                    });
                    break;
                case 4:
                    logoImage.setImageDrawable(getResources().getDrawable(R.drawable.logo_aeris));
                    logoImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://www.aerisweather.com/"));
                            startActivity(intent);
                        }
                    });
                    break;
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private int matchIcon(String description) {
        description = description.toLowerCase();
        if(description.contains("clear") || description.contains("sun")) {
            return R.drawable.weather_clear;
        } else if(description.contains("part")) {
            return R.drawable.weather_partial;
        } else if(description.contains("cloud")) {
            return R.drawable.weather_cloudy;
        } else if(description.contains("rain")) {
            return R.drawable.weather_rain;
        } else if(description.contains("snow")) {
            return R.drawable.weather_snow;
        }
        return R.drawable.weather_partial;
    }
}
