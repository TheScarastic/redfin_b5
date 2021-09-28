package com.google.android.systemui.gesture;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.util.Log;
import com.android.systemui.navigationbar.gestural.BackGestureTfClassifierProvider;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import org.tensorflow.lite.Interpreter;
/* loaded from: classes2.dex */
public class BackGestureTfClassifierProviderGoogle extends BackGestureTfClassifierProvider {
    private Interpreter mInterpreter;
    private AssetFileDescriptor mModelFileDescriptor;
    private final String mVocabFile;
    private Map<Integer, Object> mOutputMap = new HashMap();
    private float[][] mOutput = (float[][]) Array.newInstance(float.class, 1, 1);

    @Override // com.android.systemui.navigationbar.gestural.BackGestureTfClassifierProvider
    public boolean isActive() {
        return true;
    }

    public BackGestureTfClassifierProviderGoogle(AssetManager assetManager, String str) {
        this.mModelFileDescriptor = null;
        this.mInterpreter = null;
        this.mVocabFile = str + ".vocab";
        this.mOutputMap.put(0, this.mOutput);
        try {
            AssetFileDescriptor openFd = assetManager.openFd(str + ".tflite");
            this.mModelFileDescriptor = openFd;
            this.mInterpreter = new Interpreter(openFd.createInputStream().getChannel().map(FileChannel.MapMode.READ_ONLY, this.mModelFileDescriptor.getStartOffset(), this.mModelFileDescriptor.getDeclaredLength()));
        } catch (Exception e) {
            Log.e("BackGestureTfClassifier", "Load TFLite file error:", e);
        }
    }

    @Override // com.android.systemui.navigationbar.gestural.BackGestureTfClassifierProvider
    public Map<String, Integer> loadVocab(AssetManager assetManager) {
        HashMap hashMap = new HashMap();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open(this.mVocabFile)));
            int i = 0;
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                hashMap.put(readLine, Integer.valueOf(i));
                i++;
            }
            bufferedReader.close();
        } catch (Exception e) {
            Log.e("BackGestureTfClassifier", "Load vocab file error: ", e);
        }
        return hashMap;
    }

    @Override // com.android.systemui.navigationbar.gestural.BackGestureTfClassifierProvider
    public float predict(Object[] objArr) {
        Interpreter interpreter = this.mInterpreter;
        if (interpreter == null) {
            return -1.0f;
        }
        interpreter.runForMultipleInputsOutputs(objArr, this.mOutputMap);
        return this.mOutput[0][0];
    }

    @Override // com.android.systemui.navigationbar.gestural.BackGestureTfClassifierProvider
    public void release() {
        Interpreter interpreter = this.mInterpreter;
        if (interpreter != null) {
            interpreter.close();
        }
        AssetFileDescriptor assetFileDescriptor = this.mModelFileDescriptor;
        if (assetFileDescriptor != null) {
            try {
                assetFileDescriptor.close();
            } catch (Exception e) {
                Log.e("BackGestureTfClassifier", "Failed to close model file descriptor: ", e);
            }
        }
    }
}
