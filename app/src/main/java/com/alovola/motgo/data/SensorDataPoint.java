package com.alovola.motgo.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Class storing all relevant sensor + location information
 */
public class SensorDataPoint
{
  //TODO: Perhaps finding a more efficient data structure to store this?
  private long millis;
  private float[] dataPoint;
  private double latitude;
  private double longitude;
  private float speed;

  public SensorDataPoint(long millis, float[] dataPoint, double latitude, double longitude,
                         float speed) {
    this.millis = millis;
    this.dataPoint = dataPoint;
    this.latitude = latitude;
    this.longitude = longitude;
    this.speed = speed;
  }

  public float[] getDataPoint() {
    return dataPoint;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public long getMillis() {
    return millis;
  }

  public String toString() {

    Calendar calendar = Calendar.getInstance();
    calendar.setTimeZone(TimeZone.getDefault());
    calendar.setTimeInMillis(millis);
    DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
    String dateFormatted = formatter.format(calendar.getTime());
    return String.format("%s,%f,%f,%.2f,%.1f,%.1f,%.1f\n",
        dateFormatted, latitude, longitude, speed, dataPoint[0], dataPoint[1], dataPoint[2]);
  }
}