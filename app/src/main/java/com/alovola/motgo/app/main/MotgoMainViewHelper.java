package com.alovola.motgo.app.main;

import android.graphics.Color;
import android.hardware.Sensor;
import android.widget.TextView;
import android.widget.Toast;

import com.alovola.motgo.R;
import com.alovola.motgo.data.SensorDataPoint;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Handles reading/writing on the activity/fragment views
 */
public class MotgoMainViewHelper implements OnMapReadyCallback {

  MotgoMain main;
  TextView gpsValue;
  TextView speedValue;
  TextView maxSpeedValue;

  // Offsets
  TextView xOffset;
  TextView yOffset;
  TextView zOffset;

  // Acceleration views
  TextView xAccValue;
  TextView yAccValue;
  TextView zAccValue;

  // Acceleration current values views
  TextView xAccCurrentValue;
  TextView yAccCurrentValue;
  TextView zAccCurrentValue;

  // Orientation views
  TextView xOrientationValue;
  TextView yOrientationValue;
  TextView zOrientationValue;

  // Orientation current values views
  TextView xOrientationCurrentValue;
  TextView yOrientationCurrentValue;
  TextView zOrientationCurrentValue;

  MapFragment mapFragment;
  GoogleMap map;
  boolean mapReady = false;

  public MotgoMainViewHelper(MotgoMain main) {
    this.main = main;
    this.gpsValue = (TextView) main.findViewById(R.id.gpsValue);
    this.xAccValue = (TextView) main.findViewById(R.id.xAccValue);
    this.yAccValue = (TextView) main.findViewById(R.id.yAccValue);
    this.zAccValue = (TextView) main.findViewById(R.id.zAccValue);
    this.xAccCurrentValue = (TextView) main.findViewById(R.id.xAccCurrentValue);
    this.yAccCurrentValue = (TextView) main.findViewById(R.id.yAccCurrentValue);
    this.zAccCurrentValue = (TextView) main.findViewById(R.id.zAccCurrentValue);
    this.xOrientationValue = (TextView) main.findViewById(R.id.xOrientationValue);
    this.yOrientationValue = (TextView) main.findViewById(R.id.yOrientationValue);
    this.zOrientationValue = (TextView) main.findViewById(R.id.zOrientationValue);
    this.xOrientationCurrentValue = (TextView) main.findViewById(R.id.xOrientationCurrentValue);
    this.yOrientationCurrentValue = (TextView) main.findViewById(R.id.yOrientationCurrentValue);
    this.zOrientationCurrentValue = (TextView) main.findViewById(R.id.zOrientationCurrentValue);
    this.xOffset = (TextView) main.findViewById(R.id.xOffset);
    this.yOffset= (TextView) main.findViewById(R.id.yOffset);
    this.zOffset = (TextView) main.findViewById(R.id.zOffset);
    this.speedValue = (TextView) main.findViewById(R.id.speedValue);
    this.maxSpeedValue = (TextView) main.findViewById(R.id.maxSpeedValue);
    this.mapFragment = (MapFragment) main.getFragmentManager().findFragmentById(R.id.mapFragment);
    this.mapFragment.getMapAsync(this);
  }

  @Override
  public void onMapReady(GoogleMap map) {
    this.map = map;
    map.getUiSettings().setZoomControlsEnabled(true);
    map.getUiSettings().setMapToolbarEnabled(true);
    map.getUiSettings().setMyLocationButtonEnabled(true);
    this.mapReady = true;


  }

  public void printOffsetsValue(float xOffset, float yOffset, float zOffset) {
    this.xOffset.setText(String.format("%.1f", xOffset));
    this.yOffset.setText(String.format("%.1f", yOffset));
    this.zOffset.setText(String.format("%.1f", zOffset));
  }
  public void setGpsValue(double latitude, double longitude) {
    gpsValue.setText(String.format("Lat: %.6f # Long: %.6f", latitude, longitude));
    if (mapReady) {
      this.map.animateCamera(
          CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 17f));
    }
  }

  public void printMotionValues(float xMax, float xMin, float yMax, float yMin, float zMax,
                                float zMin, boolean isAcc) {

    (isAcc ? xAccValue : xOrientationValue).setText(
        String.format("X: %.1f / %.1f",
            xMax, xMin));

    (isAcc ? yAccValue : yOrientationValue).setText(
        String.format("Y: %.1f / %.1f",
            yMax, yMin));

    (isAcc ? zAccValue : zOrientationValue).setText(
        String.format("Z: %.1f / %.1f",
            zMax, zMin));
  }

  public void printCurrentMotionValues(float x, float y, float z, boolean isAcc) {
    (isAcc ? xAccCurrentValue : xOrientationCurrentValue).setText(String.format("X: %.1f", x));
    (isAcc ? yAccCurrentValue : yOrientationCurrentValue).setText(String.format("Y: %.1f", y));
    (isAcc ? zAccCurrentValue : zOrientationCurrentValue).setText(String.format("Z: %.1f", z));
  }

  public void printSpeedValues (float speed, float maxSpeed) {
    this.speedValue.setText(String.format("%.2f", speed));
    this.maxSpeedValue.setText(String.format("%.2f", maxSpeed));
  }

  public void clearMap() {
    if (mapReady) {
      this.map.clear();
    }
  }

  public void toast (String text) {
    Toast.makeText(this.main.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
  }

  //Draw the orientation & acceleration datapoints along with the max & min values
  public void drawOrientationAcceleration(ArrayList<SensorDataPoint> orientationDataPoints,
                      ArrayList<SensorDataPoint> accelerationDataPoints) {
    if (mapReady) {
      this.clearMap();
      PolylineOptions orientationRoute = new PolylineOptions()
          .width(15)
          .color(Color.BLUE)
          .geodesic(true);

      for (SensorDataPoint dataPoint : orientationDataPoints) {
        orientationRoute.add(new LatLng(dataPoint.getLatitude(), dataPoint.getLongitude()));
      }
      PolylineOptions accelerationRoute = new PolylineOptions()
          .width(15)
          .color(Color.RED)
          .geodesic(true);

      for (SensorDataPoint dataPoint : orientationDataPoints) {
        accelerationRoute.add(new LatLng(dataPoint.getLatitude(), dataPoint.getLongitude()));
      }
      this.map.addPolyline(orientationRoute);
      this.map.addPolyline(accelerationRoute);
    }
  }

  //Draw the orientation & acceleration datapoints along with the max & min values
  public void drawRoute(ArrayList<LatLng> route) {
    if (mapReady) {
      this.clearMap();
      PolylineOptions routeMap = new PolylineOptions()
          .width(10)
          .color(Color.GREEN)
          .geodesic(true);

      for (LatLng location : route) {
        routeMap.add(location);
      }
      this.map.addPolyline(routeMap);
    }
  }
}
