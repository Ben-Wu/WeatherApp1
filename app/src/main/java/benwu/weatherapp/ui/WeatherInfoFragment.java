package benwu.weatherapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private String[] tabTitles = {"OpenWeatherMap", "Source 2", "Source 3"};

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
            return 3;
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
            // Inflate a new layout from our resources
            View view = getActivity().getLayoutInflater().inflate(R.layout.pager_weather, container, false);
            // Add the newly created View to the ViewPager
            container.addView(view);

            // Retrieve a TextView from the inflated View, and update it's text
            TextView curTemp = (TextView) view.findViewById(R.id.curTemp);
            TextView maxTemp = (TextView) view.findViewById(R.id.maxTemp);
            TextView minTemp = (TextView) view.findViewById(R.id.minTemp);
            TextView conditions = (TextView) view.findViewById(R.id.conditions);

            curTemp.setText(String.valueOf(data.getCurTemp()));
            maxTemp.setText(String.valueOf(data.getMaxTemp()));
            minTemp.setText(String.valueOf(data.getMinTemp()));
            conditions.setText(data.getDescription());

            // Return the View
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
}
