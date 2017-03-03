package com.alovola.motgo.data;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jorgenogales on 7/30/16.
 */
public class SensorDataPointList {

  private List<SensorDataPoint> dataPoints;

  public SensorDataPointList() {
    super();
    this.dataPoints = Collections.synchronizedList(new ArrayList<SensorDataPoint>());
  }

  public void clear() {
    this.dataPoints.clear();
  }

  public void add(SensorDataPoint dataPoint)
  {
    this.dataPoints.add(dataPoint);
  }

  public void writeToFile(String fileName, Context context) throws IOException {

    File path = Environment.getExternalStoragePublicDirectory("motgo");
    if (!path.exists()) {
      path.mkdirs();
    }
    File file = new File(path, fileName);

    file.createNewFile();
    FileOutputStream outputStream = new FileOutputStream(file);
    for (SensorDataPoint dataPoint : dataPoints) {
      outputStream.write(dataPoint.toString().getBytes());
      outputStream.flush();
    }
    outputStream.close();

    MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null,
        new MediaScannerConnection.OnScanCompletedListener() {
          @Override
          public void onScanCompleted(String path, Uri uri) {
            Log.i(SensorDataPoint.class.toString(), String.format("File scan completed: %s", path));
          }
    });
  }
}
