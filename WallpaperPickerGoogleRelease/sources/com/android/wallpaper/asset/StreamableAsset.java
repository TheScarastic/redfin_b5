package com.android.wallpaper.asset;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;
import androidx.slice.view.R$id;
import com.android.wallpaper.asset.Asset;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes.dex */
public abstract class StreamableAsset extends Asset {
    public BitmapRegionDecoder mBitmapRegionDecoder;
    public Point mDimensions;

    /* loaded from: classes.dex */
    public class DecodeBitmapAsyncTask extends AsyncTask<Void, Void, Bitmap> {
        public Asset.BitmapReceiver mReceiver;
        public int mTargetHeight;
        public int mTargetWidth;

        public DecodeBitmapAsyncTask(int i, int i2, Asset.BitmapReceiver bitmapReceiver) {
            this.mReceiver = bitmapReceiver;
            this.mTargetWidth = i;
            this.mTargetHeight = i2;
        }

        /* Return type fixed from 'java.lang.Object' to match base method */
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object[]] */
        @Override // android.os.AsyncTask
        public Bitmap doInBackground(Void[] voidArr) {
            int exifOrientation = StreamableAsset.this.getExifOrientation();
            if (exifOrientation == 6 || exifOrientation == 8) {
                int i = this.mTargetHeight;
                this.mTargetHeight = this.mTargetWidth;
                this.mTargetWidth = i;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            Point calculateRawDimensions = StreamableAsset.this.calculateRawDimensions();
            if (calculateRawDimensions == null) {
                return null;
            }
            options.inSampleSize = R$id.calculateInSampleSize(calculateRawDimensions.x, calculateRawDimensions.y, this.mTargetWidth, this.mTargetHeight);
            options.inPreferredConfig = Bitmap.Config.HARDWARE;
            InputStream openInputStream = StreamableAsset.this.openInputStream();
            Bitmap decodeStream = BitmapFactory.decodeStream(openInputStream, null, options);
            StreamableAsset.this.closeInputStream(openInputStream, "Error closing the input stream used to decode the full bitmap");
            int access$100 = StreamableAsset.access$100(exifOrientation);
            if (access$100 <= 0) {
                return decodeStream;
            }
            Matrix matrix = new Matrix();
            matrix.setRotate((float) access$100);
            return Bitmap.createBitmap(decodeStream, 0, 0, decodeStream.getWidth(), decodeStream.getHeight(), matrix, false);
        }

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            this.mReceiver.onBitmapDecoded(bitmap);
        }
    }

    /* loaded from: classes.dex */
    public class DecodeBitmapRegionAsyncTask extends AsyncTask<Void, Void, Bitmap> {
        public Rect mCropRect;
        public final boolean mIsRtl;
        public final Asset.BitmapReceiver mReceiver;
        public int mTargetHeight;
        public int mTargetWidth;

        public DecodeBitmapRegionAsyncTask(Rect rect, int i, int i2, boolean z, Asset.BitmapReceiver bitmapReceiver) {
            this.mCropRect = rect;
            this.mReceiver = bitmapReceiver;
            this.mTargetWidth = i;
            this.mTargetHeight = i2;
            this.mIsRtl = z;
        }

        /* Return type fixed from 'java.lang.Object' to match base method */
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object[]] */
        /* JADX DEBUG: Failed to insert an additional move for type inference into block B:29:0x00d5 */
        /* JADX DEBUG: Multi-variable search result rejected for r1v5, resolved type: com.android.wallpaper.asset.StreamableAsset */
        /* JADX DEBUG: Multi-variable search result rejected for r2v5, resolved type: java.lang.String */
        /* JADX DEBUG: Multi-variable search result rejected for r6v11, resolved type: int */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r2v4, types: [android.graphics.BitmapRegionDecoder] */
        /* JADX WARN: Type inference failed for: r2v6, types: [java.lang.String] */
        /* JADX WARNING: Removed duplicated region for block: B:54:0x00ef A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARNING: Removed duplicated region for block: B:56:? A[RETURN, SYNTHETIC] */
        @Override // android.os.AsyncTask
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public android.graphics.Bitmap doInBackground(java.lang.Void[] r14) {
            /*
            // Method dump skipped, instructions count: 293
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wallpaper.asset.StreamableAsset.DecodeBitmapRegionAsyncTask.doInBackground(java.lang.Object[]):java.lang.Object");
        }

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            this.mReceiver.onBitmapDecoded(bitmap);
        }
    }

    /* loaded from: classes.dex */
    public class DecodeDimensionsAsyncTask extends AsyncTask<Void, Void, Point> {
        public Asset.DimensionsReceiver mReceiver;

        public DecodeDimensionsAsyncTask(Asset.DimensionsReceiver dimensionsReceiver) {
            this.mReceiver = dimensionsReceiver;
        }

        /* Return type fixed from 'java.lang.Object' to match base method */
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object[]] */
        @Override // android.os.AsyncTask
        public Point doInBackground(Void[] voidArr) {
            return StreamableAsset.this.calculateRawDimensions();
        }

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
        @Override // android.os.AsyncTask
        public void onPostExecute(Point point) {
            this.mReceiver.onDimensionsDecoded(point);
        }
    }

    /* loaded from: classes.dex */
    public interface StreamReceiver {
        void onInputStreamOpened(InputStream inputStream);
    }

    public static int access$100(int i) {
        if (i == 1) {
            return 0;
        }
        if (i == 3) {
            return 180;
        }
        if (i == 6) {
            return 90;
        }
        if (i == 8) {
            return 270;
        }
        Log.w("StreamableAsset", "Unsupported EXIF orientation " + i);
        return 0;
    }

    public Point calculateRawDimensions() {
        Point point = this.mDimensions;
        if (point != null) {
            return point;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream openInputStream = openInputStream();
        if (openInputStream == null) {
            return null;
        }
        BitmapFactory.decodeStream(openInputStream, null, options);
        closeInputStream(openInputStream, "There was an error closing the input stream used to calculate the image's raw dimensions");
        int exifOrientation = getExifOrientation();
        if (exifOrientation == 6 || exifOrientation == 8) {
            this.mDimensions = new Point(options.outHeight, options.outWidth);
        } else {
            this.mDimensions = new Point(options.outWidth, options.outHeight);
        }
        return this.mDimensions;
    }

    public final void closeInputStream(InputStream inputStream, String str) {
        try {
            inputStream.close();
        } catch (IOException unused) {
            Log.e("StreamableAsset", str);
        }
    }

    @Override // com.android.wallpaper.asset.Asset
    public void decodeBitmap(int i, int i2, Asset.BitmapReceiver bitmapReceiver) {
        new DecodeBitmapAsyncTask(i, i2, bitmapReceiver).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @Override // com.android.wallpaper.asset.Asset
    public void decodeBitmapRegion(Rect rect, int i, int i2, boolean z, Asset.BitmapReceiver bitmapReceiver) {
        new DecodeBitmapRegionAsyncTask(rect, i, i2, z, bitmapReceiver).execute(new Void[0]);
    }

    @Override // com.android.wallpaper.asset.Asset
    public void decodeRawDimensions(Activity activity, Asset.DimensionsReceiver dimensionsReceiver) {
        new DecodeDimensionsAsyncTask(dimensionsReceiver).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public int getExifOrientation() {
        return 1;
    }

    public abstract InputStream openInputStream();
}
