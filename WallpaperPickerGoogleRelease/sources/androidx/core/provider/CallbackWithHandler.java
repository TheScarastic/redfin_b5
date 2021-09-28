package androidx.core.provider;

import android.graphics.Typeface;
import android.os.Handler;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.TypefaceCompat;
import androidx.core.provider.FontRequestWorker;
/* loaded from: classes.dex */
public class CallbackWithHandler {
    public final FontsContractCompat$FontRequestCallback mCallback;
    public final Handler mCallbackHandler;

    public CallbackWithHandler(FontsContractCompat$FontRequestCallback fontsContractCompat$FontRequestCallback, Handler handler) {
        this.mCallback = fontsContractCompat$FontRequestCallback;
        this.mCallbackHandler = handler;
    }

    public void onTypefaceResult(FontRequestWorker.TypefaceResult typefaceResult) {
        final int i = typefaceResult.mResult;
        if (i == 0) {
            final Typeface typeface = typefaceResult.mTypeface;
            final FontsContractCompat$FontRequestCallback fontsContractCompat$FontRequestCallback = this.mCallback;
            this.mCallbackHandler.post(new Runnable(this) { // from class: androidx.core.provider.CallbackWithHandler.1
                @Override // java.lang.Runnable
                public void run() {
                    FontsContractCompat$FontRequestCallback fontsContractCompat$FontRequestCallback2 = fontsContractCompat$FontRequestCallback;
                    Typeface typeface2 = typeface;
                    ResourcesCompat.FontCallback fontCallback = ((TypefaceCompat.ResourcesCallbackAdapter) fontsContractCompat$FontRequestCallback2).mFontCallback;
                    if (fontCallback != null) {
                        fontCallback.onFontRetrieved(typeface2);
                    }
                }
            });
            return;
        }
        final FontsContractCompat$FontRequestCallback fontsContractCompat$FontRequestCallback2 = this.mCallback;
        this.mCallbackHandler.post(new Runnable(this) { // from class: androidx.core.provider.CallbackWithHandler.2
            @Override // java.lang.Runnable
            public void run() {
                FontsContractCompat$FontRequestCallback fontsContractCompat$FontRequestCallback3 = fontsContractCompat$FontRequestCallback2;
                int i2 = i;
                ResourcesCompat.FontCallback fontCallback = ((TypefaceCompat.ResourcesCallbackAdapter) fontsContractCompat$FontRequestCallback3).mFontCallback;
                if (fontCallback != null) {
                    fontCallback.onFontRetrievalFailed(i2);
                }
            }
        });
    }
}
