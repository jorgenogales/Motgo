package com.alovola.motgo.data;

import com.google.common.collect.Lists;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by jorgenogales on 2/13/16.
 */
public class RouteSegments {

  private ArrayList<SensorDataPoint> dataPoints;

  public RouteSegments() {
    this.dataPoints = Lists.newArrayList();
  }

  public void add(SensorDataPoint dataPoint) {
    this.dataPoints.add(dataPoint);
  }

  public void writeToFile (File outputFile, String fileName) {
    try {
      File file = new File(outputFile, fileName);
      OutputStream os = new FileOutputStream(file);
      for (SensorDataPoint dataPoint : dataPoints) {
        os.write(dataPoint.toString().getBytes());
      }
      os.flush();
      os.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}
