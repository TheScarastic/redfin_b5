package com.android.systemui.screenshot;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
/* loaded from: classes.dex */
public class ImageLoader {
    private final ContentResolver mResolver;

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Result {
        Bitmap bitmap;
        File fileName;
        Uri uri;

        Result() {
        }

        public String toString() {
            return "Result{uri=" + this.uri + ", fileName=" + this.fileName + ", bitmap=" + this.bitmap + '}';
        }
    }

    /* access modifiers changed from: package-private */
    public ImageLoader(ContentResolver contentResolver) {
        this.mResolver = contentResolver;
    }

    /* access modifiers changed from: package-private */
    public ListenableFuture<Result> load(File file) {
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver(file) { // from class: com.android.systemui.screenshot.ImageLoader$$ExternalSyntheticLambda0
            public final /* synthetic */ File f$0;

            {
                this.f$0 = r1;
            }

            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                return ImageLoader.m217$r8$lambda$h9JMGk62ggV3fhJzUwU1zdmyY(this.f$0, completer);
            }
        });
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ Object lambda$load$1(File file, CallbackToFutureAdapter.Completer completer) throws Exception {
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            Result result = new Result();
            result.fileName = file;
            result.bitmap = BitmapFactory.decodeStream(bufferedInputStream);
            completer.set(result);
            bufferedInputStream.close();
            return "BitmapFactory#decodeStream";
        } catch (IOException e) {
            completer.setException(e);
            return "BitmapFactory#decodeStream";
        }
    }
}
