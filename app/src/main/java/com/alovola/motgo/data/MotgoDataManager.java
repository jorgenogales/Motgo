package com.alovola.motgo.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alovola.motgo.app.main.MotgoMainViewHelper;
import com.alovola.motgo.phone.MotgoSensorsService;
import com.google.common.collect.Lists;

import java.util.ArrayList;

/**
 * Class intended to handle sensor events and add location information to each of those
 */
public abstract class MotgoDataManager extends BroadcastReceiver {

  protected MotgoMainViewHelper viewHelper;
  protected MotgoLocationManager locationManager;
  protected SensorDataPointList sensorDataPoints;
  private Context context;

  protected float xMinValue = 0;
  protected float xMaxValue = 0;
  protected float yMinValue = 0;
  protected float yMaxValue = 0;
  protected float zMinValue = 0;
  protected float zMaxValue = 0;

  // Current sensor data values
  protected float xCurrent = 0;
  protected float yCurrent = 0;
  protected float zCurrent = 0;

  public MotgoDataManager() {
    super();
  }

  public MotgoDataManager(MotgoMainViewHelper viewHelper, MotgoLocationManager locationManager,
                          SensorDataPointList sensorDataPoints) {
    super();
    this.viewHelper = viewHelper;
    this.locationManager = locationManager;
    this.sensorDataPoints = sensorDataPoints;
  }

  /**
   * Reset all sensor values
   */
  public void reset() {
    xMinValue = 0;
    xMaxValue = 0;
    yMinValue = 0;
    yMaxValue = 0;
    zMinValue = 0;
    zMaxValue = 0;

    xCurrent = 0;
    yCurrent = 0;
    zCurrent = 0;
  }

  /**
   * Generates offsets with the sensors current values on each axis so we establish the initial
   * position. Depending on whether this is Orientation or Acceleration it'll have different impact
   */
  public abstract void calibrate();

  /**
   * Checks whether the values in sensorValues hit the previous min&max Acc values
   * @param x sensor value for X axis
   * @param y sensor value for Y axis
   * @param z sensor value for Z axis
   */
  protected void findMaxMin(float x, float y, float z) {
    if (x > xMaxValue) {
      xMaxValue = x;
    }
    if (x < xMinValue) {
      xMinValue = x;
    }
    if (y > yMaxValue) {
      yMaxValue = y;
    }
    if (y < yMinValue) {
      yMinValue = y;
    }
    if (z > zMaxValue) {
      zMaxValue = z;
    }
    if (z < zMinValue) {
      zMinValue = z;
    }
  }

  protected abstract void processSensorValues(float sensorValues[], long millis);

  @Override
  public void onReceive(Context context, Intent intent) {
    if (!this.locationManager.isOnPause()) {
      float sensorValues[] = intent.getFloatArrayExtra(MotgoSensorsService.SENSOR_VALUE);
      String sensorType = intent.getStringExtra(MotgoSensorsService.SENSOR_TYPE);
      Log.d(MotgoDataManager.class.getName(),
          String.format("(Lat: %.1f, Long: %.1f, Speed: %.2f) Event received. Type %s. Values: %.1f, %.1f, %.1f",
              this.locationManager.getLatitude(),
              this.locationManager.getLongitude(),
              this.locationManager.getSpeed(),
              sensorType,
              sensorValues[0],
              sensorValues[1],
              sensorValues[2]
          )
      );

      xCurrent = sensorValues[0];
      yCurrent = sensorValues[1];
      zCurrent = sensorValues[2];

      processSensorValues(sensorValues, intent.getLongExtra(MotgoSensorsService.TIMESTAMP,0));
    }
  }

  public SensorDataPointList getSensorDataPoints() {
    return this.sensorDataPoints;
  }

  public void clearDataPoints() {
    this.sensorDataPoints.clear();
  }
}
