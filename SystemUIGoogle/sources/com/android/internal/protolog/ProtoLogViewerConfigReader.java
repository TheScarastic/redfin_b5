package com.android.internal.protolog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class ProtoLogViewerConfigReader {
    private Map<Integer, String> mLogMessageMap = null;

    public synchronized void loadViewerConfig(InputStream inputStream) throws IOException, JSONException {
        if (this.mLogMessageMap == null) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                sb.append(readLine);
                sb.append('\n');
            }
            bufferedReader.close();
            JSONObject jSONObject = new JSONObject(sb.toString()).getJSONObject("messages");
            this.mLogMessageMap = new TreeMap();
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                try {
                    int parseInt = Integer.parseInt(next);
                    this.mLogMessageMap.put(Integer.valueOf(parseInt), jSONObject.getJSONObject(next).getString("message"));
                } catch (NumberFormatException unused) {
                }
            }
        }
    }
}
