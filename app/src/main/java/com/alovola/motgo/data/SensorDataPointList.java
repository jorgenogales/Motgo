package com.alovola.motgo.data;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInstaller;
import android.hardware.Sensor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jorgenogales on 7/30/16.
 */
public class SensorDataPointList extends ArrayList<SensorDataPoint> {

  public SensorDataPointList() {
    super();
  }

  public void writeToFile(String fileName, Context context) throws IOException {

    File path = Environment.getExternalStoragePublicDirectory("motgo");
    if (!path.exists()) {
      path.mkdirs();
    }
    File file = new File(path, fileName);

    file.createNewFile();
    FileOutputStream outputStream = new FileOutputStream(file);
    for (SensorDataPoint dataPoint : this) {
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
