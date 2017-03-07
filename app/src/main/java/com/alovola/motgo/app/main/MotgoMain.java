package com.alovola.motgo.app.main;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alovola.motgo.R;
import com.alovola.motgo.data.MotgoAccelerationDataManager;
import com.alovola.motgo.data.MotgoLocationManager;
import com.alovola.motgo.data.MotgoOrientationDataManager;
import com.alovola.motgo.data.SensorDataPointList;
import com.alovola.motgo.phone.MotgoSensorsService;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MotgoMain extends AppCompatActivity {

  private final int MY_PERMISSIONS_REQUEST = 1;
  private MotgoMainViewHelper viewHelper;
  private MotgoLocationManager locationManager;
  private MotgoOrientationDataManager orientationDataManager;
  private MotgoAccelerationDataManager accelerationDataManager;
  private SensorDataPointList sensorDataPoints;
  private Intent motgoService;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Toast.makeText(this, "CREATING", Toast.LENGTH_SHORT).show();
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_motgo_main);
    checkPermissions();
    this.viewHelper = new MotgoMainViewHelper(this);
    this.sensorDataPoints = new SensorDataPointList();

    // Starting location manager
    this.locationManager = new MotgoLocationManager(this.viewHelper,
        this.getApplicationContext(), true);

    motgoService = new Intent(getApplicationContext(), MotgoSensorsService.class);
    startService(motgoService);

    // Starting orientation events manager
    this.orientationDataManager = new MotgoOrientationDataManager(this.viewHelper,
        this.locationManager, this.sensorDataPoints);

    // Starting acceleration events manager
    this.accelerationDataManager = new MotgoAccelerationDataManager(this.viewHelper,
        this.locationManager, this.sensorDataPoints);

    registerReceiver(orientationDataManager,
        new IntentFilter(MotgoSensorsService.MOTGO_BROADCAST + MotgoSensorsService.SENSOR_TYPE_ORIENTATION));
    registerReceiver(accelerationDataManager,
        new IntentFilter(MotgoSensorsService.MOTGO_BROADCAST + MotgoSensorsService.SENSOR_TYPE_ACC));

    findViewById(R.id.startButton).setOnClickListener(new StartButtonListener());
    findViewById(R.id.stopButton).setOnClickListener(new StopButtonListener());
  }

  private void checkPermissions() {
    //TODO: Check whether the API is 19 or 23 to rely on this or on the manifest file
    if ((checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED)
        || (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED)
        || (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED)) {

      requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
              Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE},
          MY_PERMISSIONS_REQUEST);
    }
  }

  @Override
  public void onDestroy() {
    Toast.makeText(this, "DESTROYING", Toast.LENGTH_SHORT).show();
    super.onDestroy();
    this.locationManager.pause();
    unregisterReceiver(this.orientationDataManager);
    unregisterReceiver(this.accelerationDataManager);
    stopService(this.motgoService);
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    setContentView(R.layout.activity_motgo_main);
    // Checks the orientation of the screen
    //if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
    //  setContentView(R.layout.activity_motgo_main);
    //} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
    //  setContentView(R.layout.activity_motgo_main);
    //}
  }


  class StartButtonListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
      locationManager.startResume();
      viewHelper.toast("START - Gathering ride info");
    }
  }

  class StopButtonListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
      // Pausing receivers and stopping service
      locationManager.pause();

      viewHelper.toast("STOP - Gathering ride info");
      SensorDataPointList accelerationDataPoints =
          accelerationDataManager.getSensorDataPoints();
      Calendar calendar = Calendar.getInstance();
      calendar.setTimeInMillis(System.currentTimeMillis());
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
      String date = dateFormat.format(calendar.getTime());
      try {
        accelerationDataPoints.writeToFile(date + "-motgo-ride.csv", getApplicationContext());
      } catch (Exception e) {
        Log.e(MotgoMain.class.toString(), "Exception when writing files", e);
      }
      ArrayList<LatLng> locations = locationManager.getLocations();
      locationManager.clearLocations();

      viewHelper.drawRoute(locations);
    }
  }

}
