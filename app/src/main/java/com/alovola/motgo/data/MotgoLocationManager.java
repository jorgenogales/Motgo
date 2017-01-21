package com.alovola.motgo.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.alovola.motgo.app.main.MotgoMainViewHelper;
import com.alovola.motgo.phone.MotgoSensorsService;
import com.google.android.gms.maps.model.LatLng;
import com.google.common.collect.Lists;

import java.util.ArrayList;

/**
 * Class intended to handle location changes.
 */
public class MotgoLocationManager {

  protected MotgoMainViewHelper viewHelper;
  protected LocationManager locationManager;
  private ArrayList<LatLng> locationList;
  private Context context;

  private double latitude = 0;
  private double longitude = 0;

  // This is used internally when a 0 speed is detected or GPS signal is lost
  private boolean waitingForMovement;

  // This is to externally set the location manager on pause
  private boolean onPause;
  protected float speed;
  private float maxSpeed;

  public MotgoLocationManager() {
    super();
  }

  public MotgoLocationManager(MotgoMainViewHelper viewHelper, Context context) {
    super();
    this.viewHelper = viewHelper;
    this.context = context;
    this.locationList = Lists.newArrayList();
    this.initLocation();
    this.waitingForMovement = true;
    this.onPause = true;
    this.speed = 0;
    this.maxSpeed = 0;
    this.latitude = 0;
    this.longitude = 0;
  }

  public void startResume() {
    this.onPause = false;
  }

  public void pause() {
    this.onPause = true;
  }

  /**
   * Initiates the GPS location manager and gets periodic updates on the location
   */
  private void initLocation() {
    viewHelper.setGpsValue(latitude, longitude);
    this.locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
    try {
      Location latestLocation = this.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
      if (latestLocation != null) {
        latitude = latestLocation.getLatitude();
        longitude = latestLocation.getLongitude();
      }
    } catch (SecurityException e) {
      Log.e(MotgoLocationManager.class.getName(), "Problem when starting location", e);
    }

    LocationListener locationListener = new LocationListener() {
      public void onLocationChanged(Location location) {
        speed = location.getSpeed() * 3600 / 1000;
        maxSpeed = (speed > maxSpeed) ? speed : maxSpeed;
        if (!onPause && speed > 0 && LocationManager.GPS_PROVIDER.equals(location.getProvider())) {
          waitingForMovement = false;

          viewHelper.printSpeedValues(speed, maxSpeed);
          latitude = location.getLatitude();
          longitude = location.getLongitude();
          locationList.add(new LatLng(latitude, longitude));
          viewHelper.drawRoute(locationList);
          viewHelper.setGpsValue(latitude, longitude);
          Log.d(MotgoLocationManager.class.getName(),
              String.format("New GPS position received %.6f,%.6f", latitude, longitude));
        } else {
          waitingForMovement = true;
          if (speed <= 0) {
            viewHelper.printSpeedValues(0, 0);
          }
        }
      }

      public void onStatusChanged(String provider, int status, Bundle extras) {
      }

      public void onProviderEnabled(String provider) {
      }

      public void onProviderDisabled(String provider) {
      }
    };

    try {
      // Register the listener with the Location Manager to receive frequent (0,0) location updates
      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    } catch (SecurityException e) {
      Log.e(MotgoLocationManager.class.getName(), "Problem when receiving GPS location", e);
    }
  }

  public ArrayList<LatLng> getLocations() {
    return this.locationList;
  }

  /**
   * Returns whether the object is on pause because of a 0 speed or a not GPS position or because
   * the location manager has been put on pause
   * @return
   */
  public boolean isOnPause() {
    return (this.onPause || this.waitingForMovement);
  }

  public float getSpeed() {
    return this.speed;
  }

  public double getLatitude() {
    return this.latitude;
  }

  public double getLongitude() {
    return this.longitude;
  }

  public void clearLocations() {
    this.locationList.clear();
  }
}
