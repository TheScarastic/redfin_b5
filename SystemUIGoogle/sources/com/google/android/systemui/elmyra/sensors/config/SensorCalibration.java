package com.google.android.systemui.elmyra.sensors.config;

import android.util.ArrayMap;
import android.util.Log;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
/* loaded from: classes2.dex */
public class SensorCalibration {
    private final Map<String, Float> mProperties = new ArrayMap();

    public static SensorCalibration getCalibration(int i) {
        try {
            return new SensorCalibration(new FileInputStream(String.format("/persist/sensors/elmyra/calibration.%d", Integer.valueOf(i))));
        } catch (FileNotFoundException e) {
            Log.e("Elmyra/SensorCalibration", "Could not find calibration file", e);
            return null;
        } catch (SecurityException e2) {
            Log.e("Elmyra/SensorCalibration", "Could not open calibration file", e2);
            return null;
        }
    }

    SensorCalibration(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    int indexOf = readLine.indexOf(58);
                    if (indexOf != -1) {
                        try {
                            this.mProperties.put(readLine.substring(0, indexOf).trim(), Float.valueOf(Float.parseFloat(readLine.substring(indexOf + 1))));
                        } catch (NumberFormatException unused) {
                        }
                    }
                } else {
                    return;
                }
            } catch (IOException e) {
                Log.e("Elmyra/SensorCalibration", "Error reading calibration file", e);
                return;
            }
        }
    }

    public boolean contains(String str) {
        return this.mProperties.containsKey(str);
    }

    public float get(String str) {
        return this.mProperties.get(str).floatValue();
    }
}
