package benwu.weatherapp.data;

/**
 * Created by Ben Wu on 11/14/2015.
 */
public class WeatherDataObject {
    private String location;
    private double curTemp;
    private double minTemp;
    private double maxTemp;
    private double humidity;
    private String description;

    @Override
    public String toString() {
        return "Location: " + location + ", temps: " + curTemp + ", " +
                minTemp + ", " + maxTemp + ", humidity: " + humidity + ", desc: " + description;
    }

    // setters
    public void setLocation(String location) {
        this.location = location;
    }
    public void setCurTemp(double curTemp) {
        this.curTemp = curTemp;
    }
    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }
    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }
    public void setHumidity(double humidity) {
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
    public double getMinTemp() {
        return minTemp;
    }
    public double getMaxTemp() {
        return maxTemp;
    }
    public double getHumidity() {
        return humidity;
    }
    public String getDescription() {
        return description;
    }
}
