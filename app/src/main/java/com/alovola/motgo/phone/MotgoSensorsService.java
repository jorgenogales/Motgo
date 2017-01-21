package com.alovola.motgo.phone;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MotgoSensorsService extends IntentService {

  public static final String MOTGO_BROADCAST = "com.alovola.motgo.broadcast.EVENT.";

  public static final String SENSOR_TYPE = "SENSOR_TYPE";
  public static final String SENSOR_TYPE_ACC = "SENSOR_TYPE_ACC";
  public static final String SENSOR_TYPE_ORIENTATION = "SENSOR_TYPE_ORIENTATION";
  public static final String SENSOR_VALUE = "SENSOR_VALUE";
  public static final String TIMESTAMP = "TIMESTAMP";

  private SensorManager sensorManager;

  public MotgoSensorsService() {
    super("");
  }

  @Override
  protected void onHandleIntent(Intent workIntent) {
    sensorManager = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
    Sensor accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    //Sensor orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    Sensor orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    //TODO: Add a handler so we treat the event in a thread instead of the app's main thread
    sensorManager.registerListener(new MotgoSensorListener(),
        orientationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    sensorManager.registerListener(new MotgoSensorListener(),
        accSensor, SensorManager.SENSOR_DELAY_NORMAL);
  }

  class MotgoSensorListener implements SensorEventListener {

    private float x;
    private float y;
    private float z;

    public MotgoSensorListener() {
      x = 0;
      y = 0;
      z = 0;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
      String sensorType = event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION
          ? SENSOR_TYPE_ACC : SENSOR_TYPE_ORIENTATION;

      float currentX = 0;
      float currentY = 0;
      float currentZ = 0;
      switch (sensorType) {
        case SENSOR_TYPE_ORIENTATION:
          float rotationMatrix[]=new float[16];
          SensorManager.getRotationMatrixFromVector(rotationMatrix,event.values);
          float[] orientationValues = new float[3];
          SensorManager.getOrientation(rotationMatrix, orientationValues);
          currentX = Math.round((orientationValues[0] * 180 / Math.PI) * 10f) / 10f;
          currentY = Math.round((orientationValues[1] * 180 / Math.PI) * 10f) / 10f;
          currentZ = Math.round((orientationValues[2] * 180 / Math.PI) * 10f) / 10f;
          break;
        case SENSOR_TYPE_ACC:
          currentX = Math.round(event.values[0] * 10f) / 10f;
          currentY = Math.round(event.values[1] * 10f) / 10f;
          currentZ = Math.round(event.values[2] * 10f) / 10f;
          break;
      }


      if (currentX != x || currentY != y || currentZ != z) {
        x = currentX;
        y = currentY;
        z = currentZ;
        Intent intent = new Intent(MOTGO_BROADCAST + sensorType);
        intent.putExtra(SENSOR_TYPE, sensorType);
        intent.putExtra(SENSOR_VALUE, new float[] {x, y, z});
        intent.putExtra(TIMESTAMP, event.timestamp/1000000);
        sendBroadcast(intent);

      }
    }
  }

}
