package com.alovola.motgo.data;

import com.alovola.motgo.app.main.MotgoMainViewHelper;

/**
 * Class intended to handle sensor events and add location information to each of those
 */
public class MotgoOrientationDataManager extends MotgoDataManager {

  // Variables for calibration purposes
  private boolean hasItBeenCalibrated = false;
  private float xOffset = 0;
  private float yOffset = 0;
  private float zOffset = 0;

  public MotgoOrientationDataManager() {
    super();
  }

  public MotgoOrientationDataManager(MotgoMainViewHelper viewHelper, MotgoLocationManager locationManager) {
    super(viewHelper, locationManager);
  }

  public void calibrate() {
    xOffset = xCurrent;
    yOffset = yCurrent;
    zOffset = zCurrent;
    this.hasItBeenCalibrated = true;
    this.viewHelper.printOffsetsValue(xOffset, yOffset, zOffset);
  }

  @Override
  protected void processSensorValues(final float sensorValues[], final long millis) {
    if (this.hasItBeenCalibrated) {
      viewHelper.printCurrentMotionValues(
          sensorValues[0] - xOffset,
          sensorValues[1] - yOffset,
          sensorValues[2] - zOffset,
          false);

      findMaxMin(
          sensorValues[0] - xOffset,
          sensorValues[1] - yOffset,
          sensorValues[2] - zOffset
      );
      sensorDataPoints.add(new SensorDataPoint(
          millis,
          new float[]{
              sensorValues[0] - xOffset,
              sensorValues[1] - yOffset,
              sensorValues[2] - zOffset},
          locationManager.getLatitude(),
          locationManager.getLongitude(),
          locationManager.getSpeed()
      ));

      viewHelper.printMotionValues(xMaxValue, xMinValue, yMaxValue, yMinValue, zMaxValue, zMinValue,
          false);
    } else {
      this.calibrate();
    }

  }
}
