package com.alovola.motgo.data;

import android.content.Context;

import com.alovola.motgo.app.main.MotgoMainViewHelper;

/**
 * Class intended to handle sensor events and add location information to each of those
 */
public class MotgoAccelerationDataManager extends MotgoDataManager {

  public MotgoAccelerationDataManager() {
    super();
  }

  public MotgoAccelerationDataManager(MotgoMainViewHelper viewHelper, MotgoLocationManager locationManager) {
    super(viewHelper, locationManager);
  }

  /**
   * Does nothing for the moment because we don't want to calibrate "acceleration"
   */
  public void calibrate() {}

  @Override
  protected void processSensorValues(float sensorValues[], long millis) {
    viewHelper.printCurrentMotionValues(
        sensorValues[0],
        sensorValues[1],
        sensorValues[2],
        true);

    this.findMaxMin(
        sensorValues[0],
        sensorValues[1],
        sensorValues[2]
    );

    sensorDataPoints.add(new SensorDataPoint(
        millis,
        new float[]{
            sensorValues[0],
            sensorValues[1],
            sensorValues[2]},
        locationManager.getLatitude(),
        locationManager.getLongitude(),
        locationManager.getSpeed()
    ));

    viewHelper.printMotionValues(xMaxValue, xMinValue, yMaxValue, yMinValue, zMaxValue, zMinValue,
        true);
  }

}