package com.alovola.motgo.data;

import com.google.common.base.Optional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Class storing all relevant sensor + location info
 */
public class SensorDataPoint
{

  SensorDataType dataType;
  private long millis;
  private Optional<Float> leanAngle;
  private Optional<Float> xAcceleration;
  private Optional<Float> yAcceleration;
  private double latitude;
  private double longitude;
  private float speed;

  public SensorDataPoint(SensorDataType dataType, long millis, Optional<Float> leanAngle,
                         Optional<Float> xAcceleration, Optional<Float> yAcceleration,
                         double latitude,
                         double longitude, float speed) {

    this.dataType = dataType;
    this.millis = millis;
    this.leanAngle = leanAngle;
    this.xAcceleration = xAcceleration;
    this.yAcceleration = yAcceleration;
    this.latitude = latitude;
    this.longitude = longitude;
    this.speed = speed;
  }

  public SensorDataType getDataType() {
    return dataType;
  }

  public String getLatitude() {
    return String.format(Locale.getDefault(), "%f", longitude);
  }

  public String getLongitude() {
    return String.format(Locale.getDefault(), "%f", longitude);
  }

  public String getSpeed() {
    return String.format(Locale.getDefault(), "%.2f", speed);
  }

  public String getLeanAngle() {
    return leanAngle.isPresent() ?
        String.format(Locale.getDefault(), "%.2f", leanAngle.get()) :
        "";
  }

  public String getXAcceleration() {
    return xAcceleration.isPresent() ?
        String.format(Locale.getDefault(), "%.2f", xAcceleration.get()) :
        "";
  }

  public String getYAcceleration() {
    return yAcceleration.isPresent() ?
        String.format(Locale.getDefault(), "%.2f", yAcceleration.get()) :
        "";
  }

  public String getDateTime() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeZone(TimeZone.getDefault());
    calendar.setTimeInMillis(millis);
    DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS", Locale.getDefault());
    return formatter.format(calendar.getTime());
  }

  @Override
  public String toString() {
    return String.format("%s,%s,%s,%s,%s,%s,%s,%s\n",
        getDateTime(), dataType, getLatitude(), getLongitude(), getSpeed(),
        getLeanAngle(), getXAcceleration(), getYAcceleration());
  }

  public enum SensorDataType {
    ORIENTATION("Orientation"),
    ACCELERATION("Acceleration");

    private final String textRepresentation;

    SensorDataType(String textRepresentation) {
      this.textRepresentation = textRepresentation;
    }

    @Override
    public String toString() {
      return textRepresentation;
    }
  }
}