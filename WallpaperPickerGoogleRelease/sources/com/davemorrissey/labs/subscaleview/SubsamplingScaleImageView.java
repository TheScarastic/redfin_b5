package com.davemorrissey.labs.subscaleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.media.ExifInterface$$ExternalSyntheticOutline0;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import androidx.constraintlayout.solver.widgets.analyzer.DependencyGraph$$ExternalSyntheticOutline0;
import com.android.wallpaper.picker.ImagePreviewFragment;
import com.android.wallpaper.picker.PreviewFragment;
import com.android.wallpaper.util.DiskBasedLogger$$ExternalSyntheticLambda1;
import com.davemorrissey.labs.subscaleview.decoder.CompatDecoderFactory;
import com.davemorrissey.labs.subscaleview.decoder.DecoderFactory;
import com.davemorrissey.labs.subscaleview.decoder.ImageDecoder;
import com.davemorrissey.labs.subscaleview.decoder.ImageRegionDecoder;
import com.davemorrissey.labs.subscaleview.decoder.SkiaImageDecoder;
import com.davemorrissey.labs.subscaleview.decoder.SkiaImageRegionDecoder;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/* loaded from: classes.dex */
public class SubsamplingScaleImageView extends View {
    public Anim anim;
    public Bitmap bitmap;
    public DecoderFactory<? extends ImageDecoder> bitmapDecoderFactory;
    public boolean bitmapIsCached;
    public boolean bitmapIsPreview;
    public Paint bitmapPaint;
    public ImageRegionDecoder decoder;
    public final ReadWriteLock decoderLock;
    public final float density;
    public GestureDetector detector;
    public int doubleTapZoomDuration;
    public float doubleTapZoomScale;
    public int doubleTapZoomStyle;
    public final float[] dstArray;
    public boolean eagerLoadingEnabled;
    public Executor executor;
    public int fullImageSampleSize;
    public final Handler handler;
    public boolean imageLoadedSent;
    public boolean isPanning;
    public boolean isQuickScaling;
    public boolean isZooming;
    public Matrix matrix;
    public float maxScale;
    public int maxTileHeight;
    public int maxTileWidth;
    public int maxTouchCount;
    public float minScale;
    public int minimumScaleType;
    public int minimumTileDpi;
    public View.OnLongClickListener onLongClickListener;
    public OnStateChangedListener onStateChangedListener;
    public int orientation;
    public boolean panEnabled;
    public int panLimit;
    public Float pendingScale;
    public boolean quickScaleEnabled;
    public float quickScaleLastDistance;
    public boolean quickScaleMoved;
    public PointF quickScaleSCenter;
    public final float quickScaleThreshold;
    public PointF quickScaleVLastPoint;
    public PointF quickScaleVStart;
    public boolean readySent;
    public DecoderFactory<? extends ImageRegionDecoder> regionDecoderFactory;
    public int sHeight;
    public int sOrientation;
    public PointF sPendingCenter;
    public RectF sRect;
    public PointF sRequestedCenter;
    public int sWidth;
    public ScaleAndTranslate satTemp;
    public float scale;
    public float scaleStart;
    public GestureDetector singleDetector;
    public final float[] srcArray;
    public Paint tileBgPaint;
    public Map<Integer, List<Tile>> tileMap;
    public Uri uri;
    public PointF vCenterStart;
    public float vDistStart;
    public PointF vTranslate;
    public PointF vTranslateBefore;
    public PointF vTranslateStart;
    public boolean zoomEnabled;
    public static final List<Integer> VALID_ORIENTATIONS = Arrays.asList(0, 90, 180, 270, -1);
    public static final List<Integer> VALID_ZOOM_STYLES = Arrays.asList(1, 2, 3);
    public static final List<Integer> VALID_EASING_STYLES = Arrays.asList(2, 1);
    public static final List<Integer> VALID_PAN_LIMITS = Arrays.asList(1, 2, 3);
    public static final List<Integer> VALID_SCALE_TYPES = Arrays.asList(2, 1, 3, 4);

    /* loaded from: classes.dex */
    public static class Anim {
        public OnAnimationEventListener listener;
        public PointF sCenterEnd;
        public PointF sCenterEndRequested;
        public PointF sCenterStart;
        public float scaleEnd;
        public float scaleStart;
        public PointF vFocusEnd;
        public PointF vFocusStart;
        public long duration = 500;
        public boolean interruptible = true;
        public int easing = 2;
        public int origin = 1;
        public long time = System.currentTimeMillis();

        public Anim(AnonymousClass1 r3) {
        }
    }

    /* loaded from: classes.dex */
    public static class BitmapLoadTask extends AsyncTask<Void, Void, Integer> {
        public Bitmap bitmap;
        public final WeakReference<Context> contextRef;
        public final WeakReference<DecoderFactory<? extends ImageDecoder>> decoderFactoryRef;
        public Exception exception;
        public final boolean preview;
        public final Uri source;
        public final WeakReference<SubsamplingScaleImageView> viewRef;

        public BitmapLoadTask(SubsamplingScaleImageView subsamplingScaleImageView, Context context, DecoderFactory<? extends ImageDecoder> decoderFactory, Uri uri, boolean z) {
            this.viewRef = new WeakReference<>(subsamplingScaleImageView);
            this.contextRef = new WeakReference<>(context);
            this.decoderFactoryRef = new WeakReference<>(decoderFactory);
            this.source = uri;
            this.preview = z;
        }

        /* Return type fixed from 'java.lang.Object' to match base method */
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object[]] */
        @Override // android.os.AsyncTask
        public Integer doInBackground(Void[] voidArr) {
            try {
                String uri = this.source.toString();
                Context context = this.contextRef.get();
                DecoderFactory<? extends ImageDecoder> decoderFactory = this.decoderFactoryRef.get();
                SubsamplingScaleImageView subsamplingScaleImageView = this.viewRef.get();
                if (!(context == null || decoderFactory == null || subsamplingScaleImageView == null)) {
                    List<Integer> list = SubsamplingScaleImageView.VALID_ORIENTATIONS;
                    this.bitmap = ((ImageDecoder) decoderFactory.make()).decode(context, this.source);
                    return Integer.valueOf(SubsamplingScaleImageView.access$5200(subsamplingScaleImageView, context, uri));
                }
            } catch (Exception e) {
                List<Integer> list2 = SubsamplingScaleImageView.VALID_ORIENTATIONS;
                Log.e("SubsamplingScaleImageView", "Failed to load bitmap", e);
                this.exception = e;
            } catch (OutOfMemoryError e2) {
                List<Integer> list3 = SubsamplingScaleImageView.VALID_ORIENTATIONS;
                Log.e("SubsamplingScaleImageView", "Failed to load bitmap - OutOfMemoryError", e2);
                this.exception = new RuntimeException(e2);
            }
            return null;
        }

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
        @Override // android.os.AsyncTask
        public void onPostExecute(Integer num) {
            Integer num2 = num;
            SubsamplingScaleImageView subsamplingScaleImageView = this.viewRef.get();
            if (subsamplingScaleImageView != null) {
                Bitmap bitmap = this.bitmap;
                if (bitmap == null || num2 == null) {
                    if (this.exception != null) {
                        List<Integer> list = SubsamplingScaleImageView.VALID_ORIENTATIONS;
                    }
                } else if (this.preview) {
                    List<Integer> list2 = SubsamplingScaleImageView.VALID_ORIENTATIONS;
                    subsamplingScaleImageView.onPreviewLoaded(bitmap);
                } else {
                    int intValue = num2.intValue();
                    List<Integer> list3 = SubsamplingScaleImageView.VALID_ORIENTATIONS;
                    subsamplingScaleImageView.onImageLoaded(bitmap, intValue, false);
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static class DefaultOnStateChangedListener implements OnStateChangedListener {
    }

    /* loaded from: classes.dex */
    public interface OnAnimationEventListener {
        void onComplete();

        void onInterruptedByNewAnim();

        void onInterruptedByUser();
    }

    /* loaded from: classes.dex */
    public interface OnStateChangedListener {
    }

    /* loaded from: classes.dex */
    public static class ScaleAndTranslate {
        public float scale;
        public final PointF vTranslate;

        public ScaleAndTranslate(float f, PointF pointF, AnonymousClass1 r3) {
            this.scale = f;
            this.vTranslate = pointF;
        }
    }

    /* loaded from: classes.dex */
    public static class Tile {
        public Bitmap bitmap;
        public Rect fileSRect;
        public boolean loading;
        public Rect sRect;
        public int sampleSize;
        public Rect vRect;
        public boolean visible;

        public Tile() {
        }

        public Tile(AnonymousClass1 r1) {
        }
    }

    /* loaded from: classes.dex */
    public static class TileLoadTask extends AsyncTask<Void, Void, Bitmap> {
        public final WeakReference<ImageRegionDecoder> decoderRef;
        public Exception exception;
        public final WeakReference<Tile> tileRef;
        public final WeakReference<SubsamplingScaleImageView> viewRef;

        public TileLoadTask(SubsamplingScaleImageView subsamplingScaleImageView, ImageRegionDecoder imageRegionDecoder, Tile tile) {
            this.viewRef = new WeakReference<>(subsamplingScaleImageView);
            this.decoderRef = new WeakReference<>(imageRegionDecoder);
            this.tileRef = new WeakReference<>(tile);
            tile.loading = true;
        }

        /* Return type fixed from 'java.lang.Object' to match base method */
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object[]] */
        @Override // android.os.AsyncTask
        public Bitmap doInBackground(Void[] voidArr) {
            try {
                SubsamplingScaleImageView subsamplingScaleImageView = this.viewRef.get();
                ImageRegionDecoder imageRegionDecoder = this.decoderRef.get();
                Tile tile = this.tileRef.get();
                if (imageRegionDecoder != null && tile != null && subsamplingScaleImageView != null && imageRegionDecoder.isReady() && tile.visible) {
                    List<Integer> list = SubsamplingScaleImageView.VALID_ORIENTATIONS;
                    subsamplingScaleImageView.decoderLock.readLock().lock();
                    if (imageRegionDecoder.isReady()) {
                        subsamplingScaleImageView.fileSRect(tile.sRect, tile.fileSRect);
                        Bitmap decodeRegion = imageRegionDecoder.decodeRegion(tile.fileSRect, tile.sampleSize);
                        subsamplingScaleImageView.decoderLock.readLock().unlock();
                        return decodeRegion;
                    }
                    tile.loading = false;
                    subsamplingScaleImageView.decoderLock.readLock().unlock();
                } else if (tile != null) {
                    tile.loading = false;
                }
            } catch (Exception e) {
                List<Integer> list2 = SubsamplingScaleImageView.VALID_ORIENTATIONS;
                Log.e("SubsamplingScaleImageView", "Failed to decode tile", e);
                this.exception = e;
            } catch (OutOfMemoryError e2) {
                List<Integer> list3 = SubsamplingScaleImageView.VALID_ORIENTATIONS;
                Log.e("SubsamplingScaleImageView", "Failed to decode tile - OutOfMemoryError", e2);
                this.exception = new RuntimeException(e2);
            }
            return null;
        }

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            Bitmap bitmap2;
            Bitmap bitmap3 = bitmap;
            SubsamplingScaleImageView subsamplingScaleImageView = this.viewRef.get();
            Tile tile = this.tileRef.get();
            if (subsamplingScaleImageView != null && tile != null) {
                if (bitmap3 != null) {
                    tile.bitmap = bitmap3;
                    tile.loading = false;
                    List<Integer> list = SubsamplingScaleImageView.VALID_ORIENTATIONS;
                    synchronized (subsamplingScaleImageView) {
                        subsamplingScaleImageView.checkReady();
                        subsamplingScaleImageView.checkImageLoaded();
                        if (subsamplingScaleImageView.isBaseLayerReady() && (bitmap2 = subsamplingScaleImageView.bitmap) != null) {
                            if (!subsamplingScaleImageView.bitmapIsCached) {
                                bitmap2.recycle();
                            }
                            subsamplingScaleImageView.bitmap = null;
                            subsamplingScaleImageView.bitmapIsPreview = false;
                            subsamplingScaleImageView.bitmapIsCached = false;
                        }
                        subsamplingScaleImageView.invalidate();
                    }
                } else if (this.exception != null) {
                    List<Integer> list2 = SubsamplingScaleImageView.VALID_ORIENTATIONS;
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static class TilesInitTask extends AsyncTask<Void, Void, int[]> {
        public final WeakReference<Context> contextRef;
        public ImageRegionDecoder decoder;
        public final WeakReference<DecoderFactory<? extends ImageRegionDecoder>> decoderFactoryRef;
        public Exception exception;
        public final Uri source;
        public final WeakReference<SubsamplingScaleImageView> viewRef;

        public TilesInitTask(SubsamplingScaleImageView subsamplingScaleImageView, Context context, DecoderFactory<? extends ImageRegionDecoder> decoderFactory, Uri uri) {
            this.viewRef = new WeakReference<>(subsamplingScaleImageView);
            this.contextRef = new WeakReference<>(context);
            this.decoderFactoryRef = new WeakReference<>(decoderFactory);
            this.source = uri;
        }

        /* Return type fixed from 'java.lang.Object' to match base method */
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object[]] */
        @Override // android.os.AsyncTask
        public int[] doInBackground(Void[] voidArr) {
            try {
                String uri = this.source.toString();
                Context context = this.contextRef.get();
                DecoderFactory<? extends ImageRegionDecoder> decoderFactory = this.decoderFactoryRef.get();
                SubsamplingScaleImageView subsamplingScaleImageView = this.viewRef.get();
                if (!(context == null || decoderFactory == null || subsamplingScaleImageView == null)) {
                    List<Integer> list = SubsamplingScaleImageView.VALID_ORIENTATIONS;
                    ImageRegionDecoder imageRegionDecoder = (ImageRegionDecoder) decoderFactory.make();
                    this.decoder = imageRegionDecoder;
                    Point init = imageRegionDecoder.init(context, this.source);
                    return new int[]{init.x, init.y, SubsamplingScaleImageView.access$5200(subsamplingScaleImageView, context, uri)};
                }
            } catch (Exception e) {
                List<Integer> list2 = SubsamplingScaleImageView.VALID_ORIENTATIONS;
                Log.e("SubsamplingScaleImageView", "Failed to initialise bitmap decoder", e);
                this.exception = e;
            }
            return null;
        }

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
        @Override // android.os.AsyncTask
        public void onPostExecute(int[] iArr) {
            int i;
            int i2;
            int i3;
            int[] iArr2 = iArr;
            SubsamplingScaleImageView subsamplingScaleImageView = this.viewRef.get();
            if (subsamplingScaleImageView != null) {
                ImageRegionDecoder imageRegionDecoder = this.decoder;
                if (imageRegionDecoder != null && iArr2 != null && iArr2.length == 3) {
                    int i4 = iArr2[0];
                    int i5 = iArr2[1];
                    int i6 = iArr2[2];
                    List<Integer> list = SubsamplingScaleImageView.VALID_ORIENTATIONS;
                    synchronized (subsamplingScaleImageView) {
                        int i7 = subsamplingScaleImageView.sWidth;
                        if (i7 > 0 && (i3 = subsamplingScaleImageView.sHeight) > 0 && !(i7 == i4 && i3 == i5)) {
                            subsamplingScaleImageView.reset(false);
                            Bitmap bitmap = subsamplingScaleImageView.bitmap;
                            if (bitmap != null) {
                                if (!subsamplingScaleImageView.bitmapIsCached) {
                                    bitmap.recycle();
                                }
                                subsamplingScaleImageView.bitmap = null;
                                subsamplingScaleImageView.bitmapIsPreview = false;
                                subsamplingScaleImageView.bitmapIsCached = false;
                            }
                        }
                        subsamplingScaleImageView.decoder = imageRegionDecoder;
                        subsamplingScaleImageView.sWidth = i4;
                        subsamplingScaleImageView.sHeight = i5;
                        subsamplingScaleImageView.sOrientation = i6;
                        subsamplingScaleImageView.checkReady();
                        if (!subsamplingScaleImageView.checkImageLoaded() && (i = subsamplingScaleImageView.maxTileWidth) > 0 && i != Integer.MAX_VALUE && (i2 = subsamplingScaleImageView.maxTileHeight) > 0 && i2 != Integer.MAX_VALUE && subsamplingScaleImageView.getWidth() > 0 && subsamplingScaleImageView.getHeight() > 0) {
                            subsamplingScaleImageView.initialiseBaseLayer(new Point(subsamplingScaleImageView.maxTileWidth, subsamplingScaleImageView.maxTileHeight));
                        }
                        subsamplingScaleImageView.invalidate();
                        subsamplingScaleImageView.requestLayout();
                    }
                } else if (this.exception != null) {
                    List<Integer> list2 = SubsamplingScaleImageView.VALID_ORIENTATIONS;
                }
            }
        }
    }

    public SubsamplingScaleImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        PointF pointF;
        int resourceId;
        String string;
        this.orientation = 0;
        this.maxScale = 2.0f;
        this.minScale = minScale();
        this.minimumTileDpi = -1;
        this.panLimit = 1;
        this.minimumScaleType = 1;
        this.maxTileWidth = Integer.MAX_VALUE;
        this.maxTileHeight = Integer.MAX_VALUE;
        this.executor = AsyncTask.THREAD_POOL_EXECUTOR;
        this.eagerLoadingEnabled = true;
        this.panEnabled = true;
        this.zoomEnabled = true;
        this.quickScaleEnabled = true;
        this.doubleTapZoomScale = 1.0f;
        this.doubleTapZoomStyle = 1;
        this.doubleTapZoomDuration = 500;
        this.decoderLock = new ReentrantReadWriteLock(true);
        this.bitmapDecoderFactory = new CompatDecoderFactory(SkiaImageDecoder.class);
        this.regionDecoderFactory = new CompatDecoderFactory(SkiaImageRegionDecoder.class);
        this.srcArray = new float[8];
        this.dstArray = new float[8];
        this.density = getResources().getDisplayMetrics().density;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float f = (float) 160;
        this.maxScale = ((displayMetrics.xdpi + displayMetrics.ydpi) / 2.0f) / f;
        DisplayMetrics displayMetrics2 = getResources().getDisplayMetrics();
        this.doubleTapZoomScale = ((displayMetrics2.xdpi + displayMetrics2.ydpi) / 2.0f) / f;
        DisplayMetrics displayMetrics3 = getResources().getDisplayMetrics();
        this.minimumTileDpi = (int) Math.min((displayMetrics3.xdpi + displayMetrics3.ydpi) / 2.0f, (float) 320);
        if (this.readySent) {
            reset(false);
            invalidate();
        }
        setGestureDetector(context);
        this.handler = new Handler(new Handler.Callback() { // from class: com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.1
            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message message) {
                SubsamplingScaleImageView subsamplingScaleImageView;
                View.OnLongClickListener onLongClickListener;
                if (message.what == 1 && (onLongClickListener = (subsamplingScaleImageView = SubsamplingScaleImageView.this).onLongClickListener) != null) {
                    subsamplingScaleImageView.maxTouchCount = 0;
                    SubsamplingScaleImageView.super.setOnLongClickListener(onLongClickListener);
                    SubsamplingScaleImageView.this.performLongClick();
                    SubsamplingScaleImageView.super.setOnLongClickListener(null);
                }
                return true;
            }
        });
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.SubsamplingScaleImageView);
            if (obtainStyledAttributes.hasValue(0) && (string = obtainStyledAttributes.getString(0)) != null && string.length() > 0) {
                String str = "file:///android_asset/" + string;
                Objects.requireNonNull(str, "Uri must not be null");
                if (!str.contains("://")) {
                    str = SupportMenuInflater$$ExternalSyntheticOutline0.m("file:///", str.startsWith("/") ? str.substring(1) : str);
                }
                ImageSource imageSource = new ImageSource(Uri.parse(str));
                imageSource.tile = true;
                setImage(imageSource);
            }
            if (obtainStyledAttributes.hasValue(3) && (resourceId = obtainStyledAttributes.getResourceId(3, 0)) > 0) {
                ImageSource imageSource2 = new ImageSource(resourceId);
                imageSource2.tile = true;
                setImage(imageSource2);
            }
            if (obtainStyledAttributes.hasValue(1)) {
                boolean z = obtainStyledAttributes.getBoolean(1, true);
                this.panEnabled = z;
                if (!z && (pointF = this.vTranslate) != null) {
                    pointF.x = ((float) (getWidth() / 2)) - (this.scale * ((float) (sWidth() / 2)));
                    this.vTranslate.y = ((float) (getHeight() / 2)) - (this.scale * ((float) (sHeight() / 2)));
                    if (this.readySent) {
                        refreshRequiredTiles(true);
                        invalidate();
                    }
                }
            }
            if (obtainStyledAttributes.hasValue(5)) {
                this.zoomEnabled = obtainStyledAttributes.getBoolean(5, true);
            }
            if (obtainStyledAttributes.hasValue(2)) {
                this.quickScaleEnabled = obtainStyledAttributes.getBoolean(2, true);
            }
            if (obtainStyledAttributes.hasValue(4)) {
                int color = obtainStyledAttributes.getColor(4, Color.argb(0, 0, 0, 0));
                if (Color.alpha(color) == 0) {
                    this.tileBgPaint = null;
                } else {
                    Paint paint = new Paint();
                    this.tileBgPaint = paint;
                    paint.setStyle(Paint.Style.FILL);
                    this.tileBgPaint.setColor(color);
                }
                invalidate();
            }
            obtainStyledAttributes.recycle();
        }
        this.quickScaleThreshold = TypedValue.applyDimension(1, 20.0f, context.getResources().getDisplayMetrics());
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:47:0x000b */
    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:50:0x000c */
    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:15:0x0052 */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r9v2, types: [boolean] */
    /* JADX WARN: Type inference failed for: r9v3, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r9v4 */
    /* JADX WARN: Type inference failed for: r9v5, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r9v6 */
    /* JADX WARN: Type inference failed for: r9v7, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r9v23 */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int access$5200(com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView r9, android.content.Context r10, java.lang.String r11) {
        /*
            java.lang.String r9 = "content"
            boolean r9 = r11.startsWith(r9)
            r0 = 0
            java.lang.String r1 = "SubsamplingScaleImageView"
            if (r9 == 0) goto L_0x0069
            r9 = 0
            java.lang.String r2 = "orientation"
            java.lang.String[] r5 = new java.lang.String[]{r2}     // Catch: Exception -> 0x0057, all -> 0x0055
            android.content.ContentResolver r3 = r10.getContentResolver()     // Catch: Exception -> 0x0057, all -> 0x0055
            android.net.Uri r4 = android.net.Uri.parse(r11)     // Catch: Exception -> 0x0057, all -> 0x0055
            r6 = 0
            r7 = 0
            r8 = 0
            android.database.Cursor r9 = r3.query(r4, r5, r6, r7, r8)     // Catch: Exception -> 0x0057, all -> 0x0055
            if (r9 == 0) goto L_0x0052
            boolean r10 = r9.moveToFirst()     // Catch: Exception -> 0x0057, all -> 0x0055
            if (r10 == 0) goto L_0x0052
            int r10 = r9.getInt(r0)     // Catch: Exception -> 0x0057, all -> 0x0055
            java.util.List<java.lang.Integer> r11 = com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.VALID_ORIENTATIONS     // Catch: Exception -> 0x0057, all -> 0x0055
            java.lang.Integer r2 = java.lang.Integer.valueOf(r10)     // Catch: Exception -> 0x0057, all -> 0x0055
            boolean r11 = r11.contains(r2)     // Catch: Exception -> 0x0057, all -> 0x0055
            if (r11 == 0) goto L_0x003e
            r11 = -1
            if (r10 == r11) goto L_0x003e
            r0 = r10
            goto L_0x0052
        L_0x003e:
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch: Exception -> 0x0057, all -> 0x0055
            r11.<init>()     // Catch: Exception -> 0x0057, all -> 0x0055
            java.lang.String r2 = "Unsupported orientation: "
            r11.append(r2)     // Catch: Exception -> 0x0057, all -> 0x0055
            r11.append(r10)     // Catch: Exception -> 0x0057, all -> 0x0055
            java.lang.String r10 = r11.toString()     // Catch: Exception -> 0x0057, all -> 0x0055
            android.util.Log.w(r1, r10)     // Catch: Exception -> 0x0057, all -> 0x0055
        L_0x0052:
            if (r9 == 0) goto L_0x00c8
            goto L_0x005e
        L_0x0055:
            r10 = move-exception
            goto L_0x0063
        L_0x0057:
            java.lang.String r10 = "Could not get orientation of image from media store"
            android.util.Log.w(r1, r10)     // Catch: all -> 0x0055
            if (r9 == 0) goto L_0x00c8
        L_0x005e:
            r9.close()
            goto L_0x00c8
        L_0x0063:
            if (r9 == 0) goto L_0x0068
            r9.close()
        L_0x0068:
            throw r10
        L_0x0069:
            java.lang.String r9 = "file:///"
            boolean r9 = r11.startsWith(r9)
            if (r9 == 0) goto L_0x00c8
            java.lang.String r9 = "file:///android_asset/"
            boolean r9 = r11.startsWith(r9)
            if (r9 != 0) goto L_0x00c8
            android.support.media.ExifInterface r9 = new android.support.media.ExifInterface     // Catch: Exception -> 0x00c3
            r10 = 7
            java.lang.String r10 = r11.substring(r10)     // Catch: Exception -> 0x00c3
            r9.<init>(r10)     // Catch: Exception -> 0x00c3
            java.lang.String r10 = "Orientation"
            android.support.media.ExifInterface$ExifAttribute r10 = r9.getExifAttribute(r10)     // Catch: Exception -> 0x00c3
            r11 = 1
            if (r10 != 0) goto L_0x008d
            goto L_0x0094
        L_0x008d:
            java.nio.ByteOrder r9 = r9.mExifByteOrder     // Catch: NumberFormatException -> 0x0094, Exception -> 0x00c3
            int r9 = r10.getIntValue(r9)     // Catch: NumberFormatException -> 0x0094, Exception -> 0x00c3
            goto L_0x0095
        L_0x0094:
            r9 = r11
        L_0x0095:
            if (r9 == r11) goto L_0x00c8
            if (r9 != 0) goto L_0x009a
            goto L_0x00c8
        L_0x009a:
            r10 = 6
            if (r9 != r10) goto L_0x00a1
            r9 = 90
        L_0x009f:
            r0 = r9
            goto L_0x00c8
        L_0x00a1:
            r10 = 3
            if (r9 != r10) goto L_0x00a7
            r9 = 180(0xb4, float:2.52E-43)
            goto L_0x009f
        L_0x00a7:
            r10 = 8
            if (r9 != r10) goto L_0x00ae
            r9 = 270(0x10e, float:3.78E-43)
            goto L_0x009f
        L_0x00ae:
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch: Exception -> 0x00c3
            r10.<init>()     // Catch: Exception -> 0x00c3
            java.lang.String r11 = "Unsupported EXIF orientation: "
            r10.append(r11)     // Catch: Exception -> 0x00c3
            r10.append(r9)     // Catch: Exception -> 0x00c3
            java.lang.String r9 = r10.toString()     // Catch: Exception -> 0x00c3
            android.util.Log.w(r1, r9)     // Catch: Exception -> 0x00c3
            goto L_0x00c8
        L_0x00c3:
            java.lang.String r9 = "Could not get EXIF orientation of image"
            android.util.Log.w(r1, r9)
        L_0x00c8:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.access$5200(com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView, android.content.Context, java.lang.String):int");
    }

    public final int calculateInSampleSize(float f) {
        int i;
        if (this.minimumTileDpi > 0) {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            f *= ((float) this.minimumTileDpi) / ((displayMetrics.xdpi + displayMetrics.ydpi) / 2.0f);
        }
        int sWidth = (int) (((float) sWidth()) * f);
        int sHeight = (int) (((float) sHeight()) * f);
        if (sWidth == 0 || sHeight == 0) {
            return 32;
        }
        int i2 = 1;
        if (sHeight() > sHeight || sWidth() > sWidth) {
            i = Math.round(((float) sHeight()) / ((float) sHeight));
            int round = Math.round(((float) sWidth()) / ((float) sWidth));
            if (i >= round) {
                i = round;
            }
        } else {
            i = 1;
        }
        while (true) {
            int i3 = i2 * 2;
            if (i3 >= i) {
                return i2;
            }
            i2 = i3;
        }
    }

    public final boolean checkImageLoaded() {
        boolean isBaseLayerReady = isBaseLayerReady();
        if (!this.imageLoadedSent && isBaseLayerReady) {
            preDraw();
            this.imageLoadedSent = true;
        }
        return isBaseLayerReady;
    }

    public final boolean checkReady() {
        boolean z = getWidth() > 0 && getHeight() > 0 && this.sWidth > 0 && this.sHeight > 0 && (this.bitmap != null || isBaseLayerReady());
        if (!this.readySent && z) {
            preDraw();
            this.readySent = true;
        }
        return z;
    }

    public final float distance(float f, float f2, float f3, float f4) {
        float f5 = f - f2;
        float f6 = f3 - f4;
        return (float) Math.sqrt((double) ((f6 * f6) + (f5 * f5)));
    }

    public final void doubleTapZoom(PointF pointF, PointF pointF2) {
        if (!this.panEnabled) {
            PointF pointF3 = this.sRequestedCenter;
            if (pointF3 != null) {
                pointF.x = pointF3.x;
                pointF.y = pointF3.y;
            } else {
                pointF.x = (float) (sWidth() / 2);
                pointF.y = (float) (sHeight() / 2);
            }
        }
        float min = Math.min(this.maxScale, this.doubleTapZoomScale);
        float f = this.scale;
        boolean z = ((double) f) <= ((double) min) * 0.9d || f == this.minScale;
        if (!z) {
            min = minScale();
        }
        int i = this.doubleTapZoomStyle;
        if (i == 3) {
            this.anim = null;
            this.pendingScale = Float.valueOf(min);
            this.sPendingCenter = pointF;
            this.sRequestedCenter = pointF;
            invalidate();
        } else if (i == 2 || !z || !this.panEnabled) {
            AnimationBuilder animationBuilder = new AnimationBuilder(min, pointF, null);
            animationBuilder.interruptible = false;
            animationBuilder.duration = (long) this.doubleTapZoomDuration;
            animationBuilder.origin = 4;
            animationBuilder.start();
        } else if (i == 1) {
            AnimationBuilder animationBuilder2 = new AnimationBuilder(min, pointF, pointF2, null);
            animationBuilder2.interruptible = false;
            animationBuilder2.duration = (long) this.doubleTapZoomDuration;
            animationBuilder2.origin = 4;
            animationBuilder2.start();
        }
        invalidate();
    }

    public final float ease(int i, long j, float f, float f2, long j2) {
        if (i == 1) {
            float f3 = ((float) j) / ((float) j2);
            return DependencyGraph$$ExternalSyntheticOutline0.m(f3, 2.0f, (-f2) * f3, f);
        } else if (i == 2) {
            float f4 = ((float) j) / (((float) j2) / 2.0f);
            if (f4 < 1.0f) {
                return ((f2 / 2.0f) * f4 * f4) + f;
            }
            float f5 = f4 - 1.0f;
            return ((((f5 - 2.0f) * f5) - 1.0f) * ((-f2) / 2.0f)) + f;
        } else {
            throw new IllegalStateException(ExifInterface$$ExternalSyntheticOutline0.m("Unexpected easing type: ", i));
        }
    }

    public final void fileSRect(Rect rect, Rect rect2) {
        if (getRequiredRotation() == 0) {
            rect2.set(rect);
        } else if (getRequiredRotation() == 90) {
            int i = rect.top;
            int i2 = this.sHeight;
            rect2.set(i, i2 - rect.right, rect.bottom, i2 - rect.left);
        } else if (getRequiredRotation() == 180) {
            int i3 = this.sWidth;
            int i4 = this.sHeight;
            rect2.set(i3 - rect.right, i4 - rect.bottom, i3 - rect.left, i4 - rect.top);
        } else {
            int i5 = this.sWidth;
            rect2.set(i5 - rect.bottom, rect.left, i5 - rect.top, rect.right);
        }
    }

    public final void fitToBounds(boolean z, ScaleAndTranslate scaleAndTranslate) {
        float f;
        float f2;
        float f3;
        int i;
        if (this.panLimit == 2 && this.readySent) {
            z = false;
        }
        PointF pointF = scaleAndTranslate.vTranslate;
        float min = Math.min(this.maxScale, Math.max(minScale(), scaleAndTranslate.scale));
        float sWidth = ((float) sWidth()) * min;
        float sHeight = ((float) sHeight()) * min;
        if (this.panLimit == 3 && this.readySent) {
            pointF.x = Math.max(pointF.x, ((float) (getWidth() / 2)) - sWidth);
            pointF.y = Math.max(pointF.y, ((float) (getHeight() / 2)) - sHeight);
        } else if (z) {
            pointF.x = Math.max(pointF.x, ((float) getWidth()) - sWidth);
            pointF.y = Math.max(pointF.y, ((float) getHeight()) - sHeight);
        } else {
            pointF.x = Math.max(pointF.x, -sWidth);
            pointF.y = Math.max(pointF.y, -sHeight);
        }
        float f4 = 0.5f;
        if (getPaddingLeft() > 0 || getPaddingRight() > 0) {
            f = ((float) getPaddingLeft()) / ((float) (getPaddingRight() + getPaddingLeft()));
        } else {
            f = 0.5f;
        }
        if (getPaddingTop() > 0 || getPaddingBottom() > 0) {
            f4 = ((float) getPaddingTop()) / ((float) (getPaddingBottom() + getPaddingTop()));
        }
        if (this.panLimit == 3 && this.readySent) {
            f2 = (float) Math.max(0, getWidth() / 2);
            i = Math.max(0, getHeight() / 2);
        } else if (z) {
            f2 = Math.max(0.0f, (((float) getWidth()) - sWidth) * f);
            f3 = Math.max(0.0f, (((float) getHeight()) - sHeight) * f4);
            pointF.x = Math.min(pointF.x, f2);
            pointF.y = Math.min(pointF.y, f3);
            scaleAndTranslate.scale = min;
        } else {
            f2 = (float) Math.max(0, getWidth());
            i = Math.max(0, getHeight());
        }
        f3 = (float) i;
        pointF.x = Math.min(pointF.x, f2);
        pointF.y = Math.min(pointF.y, f3);
        scaleAndTranslate.scale = min;
    }

    public final PointF getCenter() {
        float width = (float) (getWidth() / 2);
        float height = (float) (getHeight() / 2);
        PointF pointF = new PointF();
        if (this.vTranslate == null) {
            return null;
        }
        pointF.set(viewToSourceX(width), viewToSourceY(height));
        return pointF;
    }

    public final int getRequiredRotation() {
        int i = this.orientation;
        return i == -1 ? this.sOrientation : i;
    }

    public final synchronized void initialiseBaseLayer(Point point) {
        ScaleAndTranslate scaleAndTranslate = new ScaleAndTranslate(0.0f, new PointF(0.0f, 0.0f), null);
        this.satTemp = scaleAndTranslate;
        fitToBounds(true, scaleAndTranslate);
        int calculateInSampleSize = calculateInSampleSize(this.satTemp.scale);
        this.fullImageSampleSize = calculateInSampleSize;
        if (calculateInSampleSize > 1) {
            this.fullImageSampleSize = calculateInSampleSize / 2;
        }
        if (this.fullImageSampleSize != 1 || sWidth() >= point.x || sHeight() >= point.y) {
            initialiseTileMap(point);
            for (Tile tile : this.tileMap.get(Integer.valueOf(this.fullImageSampleSize))) {
                new TileLoadTask(this, this.decoder, tile).executeOnExecutor(this.executor, new Void[0]);
            }
            refreshRequiredTiles(true);
        } else {
            this.decoder.recycle();
            this.decoder = null;
            new BitmapLoadTask(this, getContext(), this.bitmapDecoderFactory, this.uri, false).executeOnExecutor(this.executor, new Void[0]);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for r13v2, resolved type: boolean */
    /* JADX DEBUG: Multi-variable search result rejected for r13v5, resolved type: boolean */
    /* JADX DEBUG: Multi-variable search result rejected for r13v6, resolved type: boolean */
    /* JADX WARN: Multi-variable type inference failed */
    public final void initialiseTileMap(Point point) {
        this.tileMap = new LinkedHashMap();
        int i = this.fullImageSampleSize;
        int i2 = 1;
        int i3 = 1;
        int i4 = 1;
        while (true) {
            int sWidth = sWidth() / i3;
            int sHeight = sHeight() / i4;
            int i5 = sWidth / i;
            int i6 = sHeight / i;
            while (true) {
                if (i5 + i3 + i2 > point.x || (((double) i5) > ((double) getWidth()) * 1.25d && i < this.fullImageSampleSize)) {
                    i3++;
                    sWidth = sWidth() / i3;
                    i5 = sWidth / i;
                }
            }
            while (true) {
                if (i6 + i4 + i2 > point.y || (((double) i6) > ((double) getHeight()) * 1.25d && i < this.fullImageSampleSize)) {
                    i4++;
                    sHeight = sHeight() / i4;
                    i6 = sHeight / i;
                }
            }
            ArrayList arrayList = new ArrayList(i3 * i4);
            int i7 = 0;
            while (i7 < i3) {
                int i8 = 0;
                while (i8 < i4) {
                    Tile tile = new Tile(null);
                    tile.sampleSize = i;
                    tile.visible = i == this.fullImageSampleSize ? i2 : 0;
                    tile.sRect = new Rect(i7 * sWidth, i8 * sHeight, i7 == i3 + -1 ? sWidth() : (i7 + 1) * sWidth, i8 == i4 + -1 ? sHeight() : (i8 + 1) * sHeight);
                    tile.vRect = new Rect(0, 0, 0, 0);
                    tile.fileSRect = new Rect(tile.sRect);
                    arrayList.add(tile);
                    i8++;
                    i2 = 1;
                }
                i7++;
                i2 = 1;
            }
            this.tileMap.put(Integer.valueOf(i), arrayList);
            i2 = 1;
            if (i != 1) {
                i /= 2;
            } else {
                return;
            }
        }
    }

    public final boolean isBaseLayerReady() {
        boolean z = true;
        if (!(this.bitmap == null || this.bitmapIsPreview)) {
            return true;
        }
        Map<Integer, List<Tile>> map = this.tileMap;
        if (map == null) {
            return false;
        }
        for (Map.Entry<Integer, List<Tile>> entry : map.entrySet()) {
            if (entry.getKey().intValue() == this.fullImageSampleSize) {
                for (Tile tile : entry.getValue()) {
                    if (tile.loading || tile.bitmap == null) {
                        z = false;
                    }
                }
            }
        }
        return z;
    }

    public final float minScale() {
        int paddingTop = getPaddingTop() + getPaddingBottom();
        int paddingRight = getPaddingRight() + getPaddingLeft();
        int i = this.minimumScaleType;
        if (i == 2 || i == 4) {
            return Math.max(((float) (getWidth() - paddingRight)) / ((float) sWidth()), ((float) (getHeight() - paddingTop)) / ((float) sHeight()));
        }
        if (i == 3) {
            float f = this.minScale;
            if (f > 0.0f) {
                return f;
            }
        }
        return Math.min(((float) (getWidth() - paddingRight)) / ((float) sWidth()), ((float) (getHeight() - paddingTop)) / ((float) sHeight()));
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x0121  */
    @Override // android.view.View
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onDraw(android.graphics.Canvas r29) {
        /*
        // Method dump skipped, instructions count: 990
        */
        throw new UnsupportedOperationException("Method not decompiled: com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.onDraw(android.graphics.Canvas):void");
    }

    public final synchronized void onImageLoaded(Bitmap bitmap, int i, boolean z) {
        int i2 = this.sWidth;
        if (i2 > 0 && this.sHeight > 0 && !(i2 == bitmap.getWidth() && this.sHeight == bitmap.getHeight())) {
            reset(false);
        }
        Bitmap bitmap2 = this.bitmap;
        if (bitmap2 != null && !this.bitmapIsCached) {
            bitmap2.recycle();
        }
        if (this.bitmap != null) {
            boolean z2 = this.bitmapIsCached;
        }
        this.bitmapIsPreview = false;
        this.bitmapIsCached = z;
        this.bitmap = bitmap;
        this.sWidth = bitmap.getWidth();
        this.sHeight = bitmap.getHeight();
        this.sOrientation = i;
        boolean checkReady = checkReady();
        boolean checkImageLoaded = checkImageLoaded();
        if (checkReady || checkImageLoaded) {
            invalidate();
            requestLayout();
        }
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        int mode = View.MeasureSpec.getMode(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        boolean z = true;
        boolean z2 = mode != 1073741824;
        if (mode2 == 1073741824) {
            z = false;
        }
        if (this.sWidth > 0 && this.sHeight > 0) {
            if (z2 && z) {
                size = sWidth();
                size2 = sHeight();
            } else if (z) {
                size2 = (int) ((((double) sHeight()) / ((double) sWidth())) * ((double) size));
            } else if (z2) {
                size = (int) ((((double) sWidth()) / ((double) sHeight())) * ((double) size2));
            }
        }
        setMeasuredDimension(Math.max(size, getSuggestedMinimumWidth()), Math.max(size2, getSuggestedMinimumHeight()));
    }

    public final synchronized void onPreviewLoaded(Bitmap bitmap) {
        if (this.bitmap == null && !this.imageLoadedSent) {
            this.bitmap = bitmap;
            this.bitmapIsPreview = true;
            if (checkReady()) {
                invalidate();
                requestLayout();
            }
            return;
        }
        bitmap.recycle();
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        PointF center = getCenter();
        if (this.readySent && center != null) {
            this.anim = null;
            this.pendingScale = Float.valueOf(this.scale);
            this.sPendingCenter = center;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0091, code lost:
        if (r8 != 262) goto L_0x041b;
     */
    /* JADX WARNING: Removed duplicated region for block: B:165:0x03f8  */
    /* JADX WARNING: Removed duplicated region for block: B:170:0x0411  */
    /* JADX WARNING: Removed duplicated region for block: B:214:0x051e  */
    /* JADX WARNING: Removed duplicated region for block: B:220:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    @Override // android.view.View
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(android.view.MotionEvent r17) {
        /*
        // Method dump skipped, instructions count: 1319
        */
        throw new UnsupportedOperationException("Method not decompiled: com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public final void preDraw() {
        Float f;
        if (getWidth() != 0 && getHeight() != 0 && this.sWidth > 0 && this.sHeight > 0) {
            if (!(this.sPendingCenter == null || (f = this.pendingScale) == null)) {
                this.scale = f.floatValue();
                if (this.vTranslate == null) {
                    this.vTranslate = new PointF();
                }
                this.vTranslate.x = ((float) (getWidth() / 2)) - (this.scale * this.sPendingCenter.x);
                this.vTranslate.y = ((float) (getHeight() / 2)) - (this.scale * this.sPendingCenter.y);
                this.sPendingCenter = null;
                this.pendingScale = null;
                fitToBounds(true);
                refreshRequiredTiles(true);
            }
            fitToBounds(false);
        }
    }

    public final void refreshRequiredTiles(boolean z) {
        if (!(this.decoder == null || this.tileMap == null)) {
            int min = Math.min(this.fullImageSampleSize, calculateInSampleSize(this.scale));
            for (Map.Entry<Integer, List<Tile>> entry : this.tileMap.entrySet()) {
                for (Tile tile : entry.getValue()) {
                    int i = tile.sampleSize;
                    if (i < min || (i > min && i != this.fullImageSampleSize)) {
                        tile.visible = false;
                        Bitmap bitmap = tile.bitmap;
                        if (bitmap != null) {
                            bitmap.recycle();
                            tile.bitmap = null;
                        }
                    }
                    int i2 = tile.sampleSize;
                    if (i2 == min) {
                        float viewToSourceX = viewToSourceX(0.0f);
                        float viewToSourceX2 = viewToSourceX((float) getWidth());
                        float viewToSourceY = viewToSourceY(0.0f);
                        float viewToSourceY2 = viewToSourceY((float) getHeight());
                        Rect rect = tile.sRect;
                        if (viewToSourceX <= ((float) rect.right) && ((float) rect.left) <= viewToSourceX2 && viewToSourceY <= ((float) rect.bottom) && ((float) rect.top) <= viewToSourceY2) {
                            tile.visible = true;
                            if (!tile.loading && tile.bitmap == null && z) {
                                new TileLoadTask(this, this.decoder, tile).executeOnExecutor(this.executor, new Void[0]);
                            }
                        } else if (tile.sampleSize != this.fullImageSampleSize) {
                            tile.visible = false;
                            Bitmap bitmap2 = tile.bitmap;
                            if (bitmap2 != null) {
                                bitmap2.recycle();
                                tile.bitmap = null;
                            }
                        }
                    } else if (i2 == this.fullImageSampleSize) {
                        tile.visible = true;
                    }
                }
            }
        }
    }

    public final void requestDisallowInterceptTouchEvent(boolean z) {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(z);
        }
    }

    /* JADX INFO: finally extract failed */
    public final void reset(boolean z) {
        this.scale = 0.0f;
        this.scaleStart = 0.0f;
        this.vTranslate = null;
        this.vTranslateStart = null;
        this.vTranslateBefore = null;
        this.pendingScale = Float.valueOf(0.0f);
        this.sPendingCenter = null;
        this.sRequestedCenter = null;
        this.isZooming = false;
        this.isPanning = false;
        this.isQuickScaling = false;
        this.maxTouchCount = 0;
        this.fullImageSampleSize = 0;
        this.vCenterStart = null;
        this.vDistStart = 0.0f;
        this.quickScaleLastDistance = 0.0f;
        this.quickScaleMoved = false;
        this.quickScaleSCenter = null;
        this.quickScaleVLastPoint = null;
        this.quickScaleVStart = null;
        this.anim = null;
        this.satTemp = null;
        this.matrix = null;
        this.sRect = null;
        if (z) {
            this.uri = null;
            this.decoderLock.writeLock().lock();
            try {
                ImageRegionDecoder imageRegionDecoder = this.decoder;
                if (imageRegionDecoder != null) {
                    imageRegionDecoder.recycle();
                    this.decoder = null;
                }
                this.decoderLock.writeLock().unlock();
                Bitmap bitmap = this.bitmap;
                if (bitmap != null && !this.bitmapIsCached) {
                    bitmap.recycle();
                }
                this.sWidth = 0;
                this.sHeight = 0;
                this.sOrientation = 0;
                this.readySent = false;
                this.imageLoadedSent = false;
                this.bitmap = null;
                this.bitmapIsPreview = false;
                this.bitmapIsCached = false;
            } catch (Throwable th) {
                this.decoderLock.writeLock().unlock();
                throw th;
            }
        }
        Map<Integer, List<Tile>> map = this.tileMap;
        if (map != null) {
            for (Map.Entry<Integer, List<Tile>> entry : map.entrySet()) {
                for (Tile tile : entry.getValue()) {
                    tile.visible = false;
                    Bitmap bitmap2 = tile.bitmap;
                    if (bitmap2 != null) {
                        bitmap2.recycle();
                        tile.bitmap = null;
                    }
                }
            }
            this.tileMap = null;
        }
        setGestureDetector(getContext());
    }

    public final int sHeight() {
        int requiredRotation = getRequiredRotation();
        if (requiredRotation == 90 || requiredRotation == 270) {
            return this.sWidth;
        }
        return this.sHeight;
    }

    public final int sWidth() {
        int requiredRotation = getRequiredRotation();
        if (requiredRotation == 90 || requiredRotation == 270) {
            return this.sHeight;
        }
        return this.sWidth;
    }

    public final void sendStateChanged(float f, PointF pointF, int i) {
        OnStateChangedListener onStateChangedListener = this.onStateChangedListener;
        if (!(onStateChangedListener == null || this.scale == f)) {
            Objects.requireNonNull(onStateChangedListener);
        }
        if (this.onStateChangedListener != null && !this.vTranslate.equals(pointF)) {
            OnStateChangedListener onStateChangedListener2 = this.onStateChangedListener;
            getCenter();
            ImagePreviewFragment.AnonymousClass2 r3 = (ImagePreviewFragment.AnonymousClass2) onStateChangedListener2;
            ((PreviewFragment) ImagePreviewFragment.this).mBottomActionBar.enableActionButtonsWithBottomSheet(false);
            ImagePreviewFragment.this.mImageScaleChangeCounter.incrementAndGet();
            ImagePreviewFragment.this.mFullResImageView.postDelayed(new DiskBasedLogger$$ExternalSyntheticLambda1(r3), 100);
        }
    }

    public final void setGestureDetector(final Context context) {
        this.detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() { // from class: com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.2
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
            public boolean onDoubleTap(MotionEvent motionEvent) {
                SubsamplingScaleImageView subsamplingScaleImageView = SubsamplingScaleImageView.this;
                if (!subsamplingScaleImageView.zoomEnabled || !subsamplingScaleImageView.readySent || subsamplingScaleImageView.vTranslate == null) {
                    return onDoubleTapEvent(motionEvent);
                }
                subsamplingScaleImageView.setGestureDetector(context);
                SubsamplingScaleImageView subsamplingScaleImageView2 = SubsamplingScaleImageView.this;
                if (subsamplingScaleImageView2.quickScaleEnabled) {
                    subsamplingScaleImageView2.vCenterStart = new PointF(motionEvent.getX(), motionEvent.getY());
                    SubsamplingScaleImageView subsamplingScaleImageView3 = SubsamplingScaleImageView.this;
                    PointF pointF = SubsamplingScaleImageView.this.vTranslate;
                    subsamplingScaleImageView3.vTranslateStart = new PointF(pointF.x, pointF.y);
                    SubsamplingScaleImageView subsamplingScaleImageView4 = SubsamplingScaleImageView.this;
                    subsamplingScaleImageView4.scaleStart = subsamplingScaleImageView4.scale;
                    subsamplingScaleImageView4.isQuickScaling = true;
                    subsamplingScaleImageView4.isZooming = true;
                    subsamplingScaleImageView4.quickScaleLastDistance = -1.0f;
                    subsamplingScaleImageView4.quickScaleSCenter = subsamplingScaleImageView4.viewToSourceCoord(subsamplingScaleImageView4.vCenterStart);
                    SubsamplingScaleImageView.this.quickScaleVStart = new PointF(motionEvent.getX(), motionEvent.getY());
                    SubsamplingScaleImageView subsamplingScaleImageView5 = SubsamplingScaleImageView.this;
                    PointF pointF2 = SubsamplingScaleImageView.this.quickScaleSCenter;
                    subsamplingScaleImageView5.quickScaleVLastPoint = new PointF(pointF2.x, pointF2.y);
                    SubsamplingScaleImageView.this.quickScaleMoved = false;
                    return false;
                }
                subsamplingScaleImageView2.doubleTapZoom(subsamplingScaleImageView2.viewToSourceCoord(new PointF(motionEvent.getX(), motionEvent.getY())), new PointF(motionEvent.getX(), motionEvent.getY()));
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                SubsamplingScaleImageView subsamplingScaleImageView = SubsamplingScaleImageView.this;
                if (!subsamplingScaleImageView.panEnabled || !subsamplingScaleImageView.readySent || subsamplingScaleImageView.vTranslate == null || motionEvent == null || motionEvent2 == null || ((Math.abs(motionEvent.getX() - motionEvent2.getX()) <= 50.0f && Math.abs(motionEvent.getY() - motionEvent2.getY()) <= 50.0f) || ((Math.abs(f) <= 500.0f && Math.abs(f2) <= 500.0f) || SubsamplingScaleImageView.this.isZooming))) {
                    return super.onFling(motionEvent, motionEvent2, f, f2);
                }
                PointF pointF = SubsamplingScaleImageView.this.vTranslate;
                PointF pointF2 = new PointF((f * 0.25f) + pointF.x, (f2 * 0.25f) + pointF.y);
                float width = ((float) (SubsamplingScaleImageView.this.getWidth() / 2)) - pointF2.x;
                SubsamplingScaleImageView subsamplingScaleImageView2 = SubsamplingScaleImageView.this;
                SubsamplingScaleImageView subsamplingScaleImageView3 = SubsamplingScaleImageView.this;
                AnimationBuilder animationBuilder = new AnimationBuilder(new PointF(width / subsamplingScaleImageView2.scale, (((float) (subsamplingScaleImageView2.getHeight() / 2)) - pointF2.y) / subsamplingScaleImageView3.scale), null);
                if (SubsamplingScaleImageView.VALID_EASING_STYLES.contains(1)) {
                    animationBuilder.easing = 1;
                    animationBuilder.panLimited = false;
                    animationBuilder.origin = 3;
                    animationBuilder.start();
                    return true;
                }
                throw new IllegalArgumentException("Unknown easing type: 1");
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                SubsamplingScaleImageView.this.performClick();
                return true;
            }
        });
        this.singleDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() { // from class: com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.3
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                SubsamplingScaleImageView.this.performClick();
                return true;
            }
        });
    }

    public final void setImage(ImageSource imageSource) {
        reset(true);
        Bitmap bitmap = imageSource.bitmap;
        if (bitmap != null) {
            onImageLoaded(bitmap, 0, imageSource.cached);
            return;
        }
        Uri uri = imageSource.uri;
        this.uri = uri;
        if (uri == null && imageSource.resource != null) {
            StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("android.resource://");
            m.append(getContext().getPackageName());
            m.append("/");
            m.append(imageSource.resource);
            this.uri = Uri.parse(m.toString());
        }
        if (!imageSource.tile) {
            new BitmapLoadTask(this, getContext(), this.bitmapDecoderFactory, this.uri, false).executeOnExecutor(this.executor, new Void[0]);
        } else {
            new TilesInitTask(this, getContext(), this.regionDecoderFactory, this.uri).executeOnExecutor(this.executor, new Void[0]);
        }
    }

    public final void setMatrixArray(float[] fArr, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        fArr[0] = f;
        fArr[1] = f2;
        fArr[2] = f3;
        fArr[3] = f4;
        fArr[4] = f5;
        fArr[5] = f6;
        fArr[6] = f7;
        fArr[7] = f8;
    }

    @Override // android.view.View
    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public final PointF sourceToViewCoord(PointF pointF) {
        float f = pointF.x;
        float f2 = pointF.y;
        PointF pointF2 = new PointF();
        if (this.vTranslate == null) {
            return null;
        }
        pointF2.set(sourceToViewX(f), sourceToViewY(f2));
        return pointF2;
    }

    public final float sourceToViewX(float f) {
        PointF pointF = this.vTranslate;
        if (pointF == null) {
            return Float.NaN;
        }
        return (f * this.scale) + pointF.x;
    }

    public final float sourceToViewY(float f) {
        PointF pointF = this.vTranslate;
        if (pointF == null) {
            return Float.NaN;
        }
        return (f * this.scale) + pointF.y;
    }

    public final PointF vTranslateForSCenter(float f, float f2, float f3) {
        int width = (((getWidth() - getPaddingRight()) - getPaddingLeft()) / 2) + getPaddingLeft();
        int height = (((getHeight() - getPaddingBottom()) - getPaddingTop()) / 2) + getPaddingTop();
        if (this.satTemp == null) {
            this.satTemp = new ScaleAndTranslate(0.0f, new PointF(0.0f, 0.0f), null);
        }
        ScaleAndTranslate scaleAndTranslate = this.satTemp;
        scaleAndTranslate.scale = f3;
        scaleAndTranslate.vTranslate.set(((float) width) - (f * f3), ((float) height) - (f2 * f3));
        fitToBounds(true, this.satTemp);
        return this.satTemp.vTranslate;
    }

    public final PointF viewToSourceCoord(PointF pointF) {
        float f = pointF.x;
        float f2 = pointF.y;
        PointF pointF2 = new PointF();
        if (this.vTranslate == null) {
            return null;
        }
        pointF2.set(viewToSourceX(f), viewToSourceY(f2));
        return pointF2;
    }

    public final float viewToSourceX(float f) {
        PointF pointF = this.vTranslate;
        if (pointF == null) {
            return Float.NaN;
        }
        return (f - pointF.x) / this.scale;
    }

    public final float viewToSourceY(float f) {
        PointF pointF = this.vTranslate;
        if (pointF == null) {
            return Float.NaN;
        }
        return (f - pointF.y) / this.scale;
    }

    /* loaded from: classes.dex */
    public final class AnimationBuilder {
        public long duration;
        public int easing;
        public boolean interruptible;
        public int origin;
        public boolean panLimited;
        public final PointF targetSCenter;
        public final float targetScale;
        public final PointF vFocus;

        public AnimationBuilder(PointF pointF, AnonymousClass1 r5) {
            this.duration = 500;
            this.easing = 2;
            this.origin = 1;
            this.interruptible = true;
            this.panLimited = true;
            this.targetScale = SubsamplingScaleImageView.this.scale;
            this.targetSCenter = pointF;
            this.vFocus = null;
        }

        public void start() {
            PointF pointF;
            OnAnimationEventListener onAnimationEventListener;
            Anim anim = SubsamplingScaleImageView.this.anim;
            if (!(anim == null || (onAnimationEventListener = anim.listener) == null)) {
                try {
                    onAnimationEventListener.onInterruptedByNewAnim();
                } catch (Exception e) {
                    List<Integer> list = SubsamplingScaleImageView.VALID_ORIENTATIONS;
                    Log.w("SubsamplingScaleImageView", "Error thrown by animation listener", e);
                }
            }
            int width = (((SubsamplingScaleImageView.this.getWidth() - SubsamplingScaleImageView.this.getPaddingRight()) - SubsamplingScaleImageView.this.getPaddingLeft()) / 2) + SubsamplingScaleImageView.this.getPaddingLeft();
            int height = (((SubsamplingScaleImageView.this.getHeight() - SubsamplingScaleImageView.this.getPaddingBottom()) - SubsamplingScaleImageView.this.getPaddingTop()) / 2) + SubsamplingScaleImageView.this.getPaddingTop();
            SubsamplingScaleImageView subsamplingScaleImageView = SubsamplingScaleImageView.this;
            float min = Math.min(subsamplingScaleImageView.maxScale, Math.max(subsamplingScaleImageView.minScale(), this.targetScale));
            if (this.panLimited) {
                SubsamplingScaleImageView subsamplingScaleImageView2 = SubsamplingScaleImageView.this;
                PointF pointF2 = this.targetSCenter;
                float f = pointF2.x;
                float f2 = pointF2.y;
                pointF = new PointF();
                PointF vTranslateForSCenter = subsamplingScaleImageView2.vTranslateForSCenter(f, f2, min);
                pointF.set((((float) ((((subsamplingScaleImageView2.getWidth() - subsamplingScaleImageView2.getPaddingRight()) - subsamplingScaleImageView2.getPaddingLeft()) / 2) + subsamplingScaleImageView2.getPaddingLeft())) - vTranslateForSCenter.x) / min, (((float) ((((subsamplingScaleImageView2.getHeight() - subsamplingScaleImageView2.getPaddingBottom()) - subsamplingScaleImageView2.getPaddingTop()) / 2) + subsamplingScaleImageView2.getPaddingTop())) - vTranslateForSCenter.y) / min);
            } else {
                pointF = this.targetSCenter;
            }
            SubsamplingScaleImageView.this.anim = new Anim(null);
            SubsamplingScaleImageView subsamplingScaleImageView3 = SubsamplingScaleImageView.this;
            Anim anim2 = subsamplingScaleImageView3.anim;
            anim2.scaleStart = subsamplingScaleImageView3.scale;
            anim2.scaleEnd = min;
            anim2.time = System.currentTimeMillis();
            SubsamplingScaleImageView subsamplingScaleImageView4 = SubsamplingScaleImageView.this;
            Anim anim3 = subsamplingScaleImageView4.anim;
            anim3.sCenterEndRequested = pointF;
            anim3.sCenterStart = subsamplingScaleImageView4.getCenter();
            SubsamplingScaleImageView subsamplingScaleImageView5 = SubsamplingScaleImageView.this;
            Anim anim4 = subsamplingScaleImageView5.anim;
            anim4.sCenterEnd = pointF;
            anim4.vFocusStart = subsamplingScaleImageView5.sourceToViewCoord(pointF);
            SubsamplingScaleImageView.this.anim.vFocusEnd = new PointF((float) width, (float) height);
            Anim anim5 = SubsamplingScaleImageView.this.anim;
            anim5.duration = this.duration;
            anim5.interruptible = this.interruptible;
            anim5.easing = this.easing;
            anim5.origin = this.origin;
            anim5.time = System.currentTimeMillis();
            Anim anim6 = SubsamplingScaleImageView.this.anim;
            anim6.listener = null;
            PointF pointF3 = this.vFocus;
            if (pointF3 != null) {
                float f3 = pointF3.x;
                PointF pointF4 = anim6.sCenterStart;
                float f4 = f3 - (pointF4.x * min);
                float f5 = pointF3.y - (pointF4.y * min);
                PointF pointF5 = new PointF(f4, f5);
                SubsamplingScaleImageView.this.fitToBounds(true, new ScaleAndTranslate(min, pointF5, null));
                Anim anim7 = SubsamplingScaleImageView.this.anim;
                PointF pointF6 = this.vFocus;
                anim7.vFocusEnd = new PointF((pointF5.x - f4) + pointF6.x, (pointF5.y - f5) + pointF6.y);
            }
            SubsamplingScaleImageView.this.invalidate();
        }

        public AnimationBuilder(float f, PointF pointF, AnonymousClass1 r6) {
            this.duration = 500;
            this.easing = 2;
            this.origin = 1;
            this.interruptible = true;
            this.panLimited = true;
            this.targetScale = f;
            this.targetSCenter = pointF;
            this.vFocus = null;
        }

        public AnimationBuilder(float f, PointF pointF, PointF pointF2, AnonymousClass1 r7) {
            this.duration = 500;
            this.easing = 2;
            this.origin = 1;
            this.interruptible = true;
            this.panLimited = true;
            this.targetScale = f;
            this.targetSCenter = pointF;
            this.vFocus = pointF2;
        }
    }

    public final void fitToBounds(boolean z) {
        boolean z2;
        if (this.vTranslate == null) {
            z2 = true;
            this.vTranslate = new PointF(0.0f, 0.0f);
        } else {
            z2 = false;
        }
        if (this.satTemp == null) {
            this.satTemp = new ScaleAndTranslate(0.0f, new PointF(0.0f, 0.0f), null);
        }
        ScaleAndTranslate scaleAndTranslate = this.satTemp;
        scaleAndTranslate.scale = this.scale;
        scaleAndTranslate.vTranslate.set(this.vTranslate);
        fitToBounds(z, this.satTemp);
        ScaleAndTranslate scaleAndTranslate2 = this.satTemp;
        this.scale = scaleAndTranslate2.scale;
        this.vTranslate.set(scaleAndTranslate2.vTranslate);
        if (z2 && this.minimumScaleType != 4) {
            this.vTranslate.set(vTranslateForSCenter((float) (sWidth() / 2), (float) (sHeight() / 2), this.scale));
        }
    }

    public SubsamplingScaleImageView(Context context) {
        this(context, null);
    }
}
