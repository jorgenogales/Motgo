package com.alovola.motgo.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Class storing all relevant sensor + location info
 */
public class SensorDataPoint
{

  public enum SensorDataType {
    ORIENTATION("Orientation"),
    ACCELERATION("Acceleration");

    private final String textRepresentation;

    private SensorDataType(String textRepresentation) {
      this.textRepresentation = textRepresentation;
    }

    @Override
    public String toString() {
      return textRepresentation;
    }
  }

  SensorDataType dataType;
  private long millis;
  private float[] dataPoint;
  private double latitude;
  private double longitude;
  private float speed;

  public SensorDataPoint(SensorDataType dataType, long millis, float[] dataPoint, double latitude,
                         double longitude, float speed) {

    this.dataType = dataType;
    this.millis = millis;
    this.dataPoint = dataPoint;
    this.latitude = latitude;
    this.longitude = longitude;
    this.speed = speed;
  }

  public float[] getDataPoint() {
    return dataPoint;
  }

  public SensorDataType getDataType() {
    return dataType;
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

  @Override
  public String toString() {

    Calendar calendar = Calendar.getInstance();
    calendar.setTimeZone(TimeZone.getDefault());
    calendar.setTimeInMillis(millis);
    DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
    String dateFormatted = formatter.format(calendar.getTime());
    return String.format("%s,%s,%f,%f,%.2f,%.1f,%.1f,%.1f\n",
        dateFormatted, dataType, latitude, longitude, speed,
        dataPoint[0], dataPoint[1], dataPoint[2]);
  }
}