package com.alovola.motgo.app.main.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alovola.motgo.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Fragment showing all map activity from the rider
 */
public class MotgoMapFragment extends MapFragment implements OnMapReadyCallback {

  GoogleMap map;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.getMapAsync(this);
  }

  /*
  @Override
  public void onInflate(Activity activity, AttributeSet attrs,
                        Bundle savedInstanceState) {

    FragmentManager fm = getFragmentManager();
    if (fm != null) {
      fm.beginTransaction().remove(fm.findFragmentById(R.id.mapFragment)).commit();
    }
    super.onInflate(activity, attrs, savedInstanceState);
  }
  */

  @Override
  public void onMapReady(GoogleMap map) {
    this.map = map;
  }

}
