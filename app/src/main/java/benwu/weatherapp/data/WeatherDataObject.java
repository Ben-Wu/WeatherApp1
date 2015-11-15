package benwu.weatherapp.data;

import java.util.Date;

/**
 * Created by Ben Wu on 11/14/2015.
 */
public class WeatherDataObject {
    private String location;
    private double curTemp;
    private String time;
    private String humidity;
    private String description;

    @Override
    public String toString() {
        return "Location: " + location + ", temps: " + curTemp + ", humidity: " + humidity + ", desc: " + description;
    }

    // setters
    public void setLocation(String location) {
        this.location = location;
    }
    public void setCurTemp(double curTemp) {
        this.curTemp = curTemp;
    }
    public void setTime(long time) {
        this.time = convertTime(time);
    }
    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    // getters
    public String getLocation() {
        return location;
    }
    public double getCurTemp() {
        return curTemp;
    }
    public String getTime() {
        return time;
    }
    public String getHumidity() {
        return humidity;
    }
    public String getDescription() {
        return description;
    }

    private String convertTime(long epochTime) {
        Date date = new Date(epochTime*1000);
        return date.toString();
    }
}
