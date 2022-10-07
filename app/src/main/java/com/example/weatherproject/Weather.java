package com.example.weatherproject;

public class Weather {

    private String time;
    private String icon;
    private String temperature;
    private String wind;

    public Weather(String time, String icon, String temperature, String wind) {
        this.time = time;
        this.icon = icon;
        this.temperature = temperature;
        this.wind = wind;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }
}
