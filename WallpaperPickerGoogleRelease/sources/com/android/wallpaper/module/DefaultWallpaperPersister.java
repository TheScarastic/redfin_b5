package com.android.wallpaper.module;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.util.Log;
import android.view.WindowManager;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.asset.StreamableAsset;
import com.android.wallpaper.compat.WallpaperManagerCompatV16;
import com.android.wallpaper.model.WallpaperInfo;
import com.android.wallpaper.module.WallpaperPersister;
import com.android.wallpaper.picker.TopLevelPickerActivity$4$$ExternalSyntheticLambda0;
import com.android.wallpaper.util.ScreenSizeCalculator;
import com.android.wallpaper.util.WallpaperCropUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class DefaultWallpaperPersister implements WallpaperPersister {
    public final Context mAppContext;
    public final WallpaperChangedNotifier mWallpaperChangedNotifier = WallpaperChangedNotifier.getInstance();
    public WallpaperInfo mWallpaperInfoInPreview;
    public final WallpaperManager mWallpaperManager;
    public final WallpaperManagerCompatV16 mWallpaperManagerCompat;
    public final WallpaperPreferences mWallpaperPreferences;

    @SuppressLint({"ServiceCast"})
    public DefaultWallpaperPersister(Context context) {
        this.mAppContext = context.getApplicationContext();
        Injector injector = InjectorProvider.getInjector();
        this.mWallpaperManager = (WallpaperManager) context.getSystemService("wallpaper");
        this.mWallpaperManagerCompat = injector.getWallpaperManagerCompat(context);
        this.mWallpaperPreferences = injector.getPreferences(context);
    }

    public final int cropAndSetWallpaperBitmapInRotationStatic(Bitmap bitmap) {
        Point point = new Point(bitmap.getWidth(), bitmap.getHeight());
        WindowManager windowManager = (WindowManager) this.mAppContext.getSystemService("window");
        Point defaultCropSurfaceSize = WallpaperCropUtils.getDefaultCropSurfaceSize(this.mAppContext.getResources(), windowManager.getDefaultDisplay());
        Point screenSize = ScreenSizeCalculator.getInstance().getScreenSize(windowManager.getDefaultDisplay());
        float calculateMinZoom = WallpaperCropUtils.calculateMinZoom(point, screenSize);
        Context context = this.mAppContext;
        Rect calculateVisibleRect = WallpaperCropUtils.calculateVisibleRect(point, screenSize);
        int i = 1;
        WallpaperCropUtils.adjustCropRect(context, calculateVisibleRect, true);
        PointF pointF = new PointF((float) calculateVisibleRect.centerX(), (float) calculateVisibleRect.centerY());
        Point point2 = new Point((int) (pointF.x * calculateMinZoom), (int) (pointF.y * calculateMinZoom));
        Rect calculateCropRect = WallpaperCropUtils.calculateCropRect(this.mAppContext, calculateMinZoom, point, defaultCropSurfaceSize, screenSize, Math.max(0, -((screenSize.x / 2) - point2.x)), Math.max(0, -((screenSize.y / 2) - point2.y)));
        Rect rect = new Rect(Math.round(((float) calculateCropRect.left) / calculateMinZoom), Math.round(((float) calculateCropRect.top) / calculateMinZoom), Math.round(((float) calculateCropRect.right) / calculateMinZoom), Math.round(((float) calculateCropRect.bottom) / calculateMinZoom));
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height());
        if (!isSeparateLockScreenWallpaperSet()) {
            i = 3;
        }
        return setBitmapToWallpaperManagerCompat(createBitmap, false, i);
    }

    public final boolean finalizeWallpaperForRotatingComponent(List<String> list, String str, int i, int i2, String str2, int i3) {
        this.mWallpaperPreferences.clearHomeWallpaperMetadata();
        boolean isSeparateLockScreenWallpaperSet = isSeparateLockScreenWallpaperSet();
        this.mWallpaperPreferences.setHomeWallpaperManagerId(i3);
        if (!isSeparateLockScreenWallpaperSet) {
            this.mWallpaperPreferences.setLockWallpaperId(i3);
        }
        this.mWallpaperPreferences.setHomeWallpaperAttributions(list);
        this.mWallpaperPreferences.setHomeWallpaperActionUrl(str);
        this.mWallpaperPreferences.setHomeWallpaperActionLabelRes(i);
        this.mWallpaperPreferences.setHomeWallpaperActionIconRes(i2);
        this.mWallpaperPreferences.setHomeWallpaperBaseImageUrl(null);
        this.mWallpaperPreferences.setHomeWallpaperCollectionId(str2);
        if (isSeparateLockScreenWallpaperSet) {
            return true;
        }
        this.mWallpaperPreferences.setLockWallpaperAttributions(list);
        this.mWallpaperPreferences.setLockWallpaperActionUrl(str);
        this.mWallpaperPreferences.setLockWallpaperActionLabelRes(i);
        this.mWallpaperPreferences.setLockWallpaperActionIconRes(i2);
        this.mWallpaperPreferences.setLockWallpaperCollectionId(str2);
        return true;
    }

    public final boolean isSeparateLockScreenWallpaperSet() {
        ParcelFileDescriptor wallpaperFile = this.mWallpaperManagerCompat.getWallpaperFile(2);
        if (wallpaperFile == null) {
            return false;
        }
        try {
            wallpaperFile.close();
            return true;
        } catch (IOException e) {
            Log.e("WallpaperPersister", "Unable to close PFD for lock wallpaper", e);
            return true;
        }
    }

    public void onLiveWallpaperSet() {
        android.app.WallpaperInfo wallpaperInfo = this.mWallpaperManager.getWallpaperInfo();
        WallpaperInfo wallpaperInfo2 = this.mWallpaperInfoInPreview;
        android.app.WallpaperInfo wallpaperComponent = wallpaperInfo2 != null ? wallpaperInfo2.getWallpaperComponent() : null;
        if (wallpaperInfo == null || wallpaperComponent == null || !wallpaperInfo.getPackageName().equals(wallpaperComponent.getPackageName())) {
            this.mWallpaperInfoInPreview = null;
            return;
        }
        android.app.WallpaperInfo wallpaperComponent2 = this.mWallpaperInfoInPreview.getWallpaperComponent();
        this.mWallpaperPreferences.clearHomeWallpaperMetadata();
        this.mWallpaperPreferences.setHomeWallpaperAttributions(this.mWallpaperInfoInPreview.getAttributions(this.mAppContext));
        this.mWallpaperPreferences.setHomeWallpaperPackageName(wallpaperComponent2.getPackageName());
        this.mWallpaperPreferences.setHomeWallpaperServiceName(wallpaperComponent2.getServiceName());
        this.mWallpaperPreferences.setHomeWallpaperCollectionId(this.mWallpaperInfoInPreview.getCollectionId(this.mAppContext));
        this.mWallpaperPreferences.setWallpaperPresentationMode(1);
        this.mWallpaperPreferences.clearDailyRotations();
    }

    public final int setBitmapToWallpaperManagerCompat(Bitmap bitmap, boolean z, int i) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)) {
            try {
                return this.mWallpaperManagerCompat.setStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), null, z, i);
            } catch (IOException unused) {
                Log.e("WallpaperPersister", "unable to write stream to wallpaper manager");
                return 0;
            }
        } else {
            Log.e("WallpaperPersister", "unable to compress wallpaper");
            try {
                return this.mWallpaperManagerCompat.setBitmap(bitmap, null, z, i);
            } catch (IOException unused2) {
                Log.e("WallpaperPersister", "unable to set wallpaper");
                return 0;
            }
        }
    }

    public void setIndividualWallpaper(final WallpaperInfo wallpaperInfo, Asset asset, Rect rect, float f, final int i, final WallpaperPersister.SetWallpaperCallback setWallpaperCallback) {
        if (rect == null && (asset instanceof StreamableAsset)) {
            StreamableAsset streamableAsset = (StreamableAsset) asset;
            AnonymousClass1 r10 = new StreamableAsset.StreamReceiver() { // from class: com.android.wallpaper.module.DefaultWallpaperPersister.1
                @Override // com.android.wallpaper.asset.StreamableAsset.StreamReceiver
                public void onInputStreamOpened(InputStream inputStream) {
                    if (inputStream == null) {
                        setWallpaperCallback.onError(null);
                        return;
                    }
                    DefaultWallpaperPersister defaultWallpaperPersister = DefaultWallpaperPersister.this;
                    WallpaperInfo wallpaperInfo2 = wallpaperInfo;
                    int i2 = i;
                    WallpaperPersister.SetWallpaperCallback setWallpaperCallback2 = setWallpaperCallback;
                    Objects.requireNonNull(defaultWallpaperPersister);
                    new SetWallpaperTask(wallpaperInfo2, inputStream, i2, setWallpaperCallback2).execute(new Void[0]);
                }
            };
            Objects.requireNonNull(streamableAsset);
            
            /*  JADX ERROR: Method code generation error
                jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x001a: INVOKE  
                  (wrap: com.android.wallpaper.asset.StreamableAsset$1 : 0x0012: CONSTRUCTOR  (r7v1 com.android.wallpaper.asset.StreamableAsset$1 A[REMOVE]) = 
                  (r9v1 'streamableAsset' com.android.wallpaper.asset.StreamableAsset)
                  (r10v7 'r10' com.android.wallpaper.module.DefaultWallpaperPersister$1)
                 call: com.android.wallpaper.asset.StreamableAsset.1.<init>(com.android.wallpaper.asset.StreamableAsset, com.android.wallpaper.asset.StreamableAsset$StreamReceiver):void type: CONSTRUCTOR)
                  (wrap: java.util.concurrent.Executor : 0x0015: SGET  (r8v1 java.util.concurrent.Executor A[REMOVE]) =  android.os.AsyncTask.THREAD_POOL_EXECUTOR java.util.concurrent.Executor)
                  (wrap: java.lang.Void[] : 0x0018: NEW_ARRAY  (r9v3 java.lang.Void[] A[REMOVE]) = (0 int) type: java.lang.Void[])
                 type: VIRTUAL call: android.os.AsyncTask.executeOnExecutor(java.util.concurrent.Executor, java.lang.Object[]):android.os.AsyncTask in method: com.android.wallpaper.module.DefaultWallpaperPersister.setIndividualWallpaper(com.android.wallpaper.model.WallpaperInfo, com.android.wallpaper.asset.Asset, android.graphics.Rect, float, int, com.android.wallpaper.module.WallpaperPersister$SetWallpaperCallback):void, file: classes.dex
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:285)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:248)
                	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:105)
                	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                	at jadx.core.dex.regions.Region.generate(Region.java:35)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:94)
                	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:137)
                	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:137)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                	at jadx.core.dex.regions.Region.generate(Region.java:35)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:261)
                	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:254)
                	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:349)
                	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:302)
                	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:271)
                	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
                	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.android.wallpaper.asset.StreamableAsset, state: GENERATED_AND_UNLOADED
                	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:259)
                	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:675)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:393)
                	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:93)
                	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:770)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:397)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:278)
                	... 21 more
                */
            /*
                this = this;
                if (r10 != 0) goto L_0x001e
                boolean r0 = r9 instanceof com.android.wallpaper.asset.StreamableAsset
                if (r0 == 0) goto L_0x001e
                com.android.wallpaper.asset.StreamableAsset r9 = (com.android.wallpaper.asset.StreamableAsset) r9
                com.android.wallpaper.module.DefaultWallpaperPersister$1 r10 = new com.android.wallpaper.module.DefaultWallpaperPersister$1
                r10.<init>(r13, r8, r12)
                java.util.Objects.requireNonNull(r9)
                com.android.wallpaper.asset.StreamableAsset$1 r7 = new com.android.wallpaper.asset.StreamableAsset$1
                r7.<init>(r10)
                java.util.concurrent.Executor r8 = android.os.AsyncTask.THREAD_POOL_EXECUTOR
                r9 = 0
                java.lang.Void[] r9 = new java.lang.Void[r9]
                r7.executeOnExecutor(r8, r9)
                return
            L_0x001e:
                if (r10 != 0) goto L_0x0043
                android.content.Context r10 = r7.mAppContext
                java.lang.String r11 = "window"
                java.lang.Object r10 = r10.getSystemService(r11)
                android.view.WindowManager r10 = (android.view.WindowManager) r10
                android.view.Display r10 = r10.getDefaultDisplay()
                com.android.wallpaper.util.ScreenSizeCalculator r11 = com.android.wallpaper.util.ScreenSizeCalculator.getInstance()
                android.graphics.Point r10 = r11.getScreenSize(r10)
                int r11 = r10.x
                int r10 = r10.y
                com.android.wallpaper.module.DefaultWallpaperPersister$2 r0 = new com.android.wallpaper.module.DefaultWallpaperPersister$2
                r0.<init>(r13, r8, r12)
                r9.decodeBitmap(r11, r10, r0)
                return
            L_0x0043:
                com.android.wallpaper.module.Injector r0 = com.android.wallpaper.module.InjectorProvider.getInjector()
                com.android.wallpaper.module.BitmapCropper r0 = r0.getBitmapCropper()
                r5 = 0
                com.android.wallpaper.module.DefaultWallpaperPersister$3 r6 = new com.android.wallpaper.module.DefaultWallpaperPersister$3
                r6.<init>(r8, r12, r13)
                r1 = r0
                com.android.wallpaper.module.DefaultBitmapCropper r1 = (com.android.wallpaper.module.DefaultBitmapCropper) r1
                r2 = r9
                r3 = r11
                r4 = r10
                r1.cropAndScaleBitmap(r2, r3, r4, r5, r6)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wallpaper.module.DefaultWallpaperPersister.setIndividualWallpaper(com.android.wallpaper.model.WallpaperInfo, com.android.wallpaper.asset.Asset, android.graphics.Rect, float, int, com.android.wallpaper.module.WallpaperPersister$SetWallpaperCallback):void");
        }

        public void setIndividualWallpaperWithPosition(Activity activity, final WallpaperInfo wallpaperInfo, final int i, final WallpaperPersister.SetWallpaperCallback setWallpaperCallback) {
            final Point screenSize = ScreenSizeCalculator.getInstance().getScreenSize(((WindowManager) this.mAppContext.getSystemService("window")).getDefaultDisplay());
            final Asset asset = wallpaperInfo.getAsset(activity);
            asset.decodeRawDimensions(activity, new Asset.DimensionsReceiver() { // from class: com.android.wallpaper.module.DefaultWallpaperPersister.4
                @Override // com.android.wallpaper.asset.Asset.DimensionsReceiver
                public void onDimensionsDecoded(Point point) {
                    if (point == null) {
                        setWallpaperCallback.onError(null);
                        return;
                    }
                    int i2 = i;
                    if (i2 == 0) {
                        DefaultWallpaperPersister defaultWallpaperPersister = DefaultWallpaperPersister.this;
                        WallpaperInfo wallpaperInfo2 = wallpaperInfo;
                        Asset asset2 = asset;
                        Point point2 = screenSize;
                        WallpaperPersister.SetWallpaperCallback setWallpaperCallback2 = setWallpaperCallback;
                        Objects.requireNonNull(defaultWallpaperPersister);
                        int i3 = point.x;
                        if (i3 < point2.x || point.y < point2.y) {
                            asset2.decodeBitmap(i3, point.y, new Asset.BitmapReceiver(setWallpaperCallback2, wallpaperInfo2, point2) { // from class: com.android.wallpaper.module.DefaultWallpaperPersister.5
                                public final /* synthetic */ WallpaperPersister.SetWallpaperCallback val$callback;
                                public final /* synthetic */ Point val$screenSize;
                                public final /* synthetic */ WallpaperInfo val$wallpaper;

                                {
                                    this.val$callback = r2;
                                    this.val$wallpaper = r3;
                                    this.val$screenSize = r4;
                                }

                                @Override // com.android.wallpaper.asset.Asset.BitmapReceiver
                                public void onBitmapDecoded(Bitmap bitmap) {
                                    if (bitmap == null) {
                                        this.val$callback.onError(null);
                                        return;
                                    }
                                    DefaultWallpaperPersister defaultWallpaperPersister2 = DefaultWallpaperPersister.this;
                                    WallpaperInfo wallpaperInfo3 = this.val$wallpaper;
                                    Point point3 = this.val$screenSize;
                                    WallpaperPersister.SetWallpaperCallback setWallpaperCallback3 = this.val$callback;
                                    Objects.requireNonNull(defaultWallpaperPersister2);
                                    SetWallpaperTask setWallpaperTask = new SetWallpaperTask(wallpaperInfo3, bitmap, 2, setWallpaperCallback3);
                                    if (setWallpaperTask.mStretchSize == null) {
                                        setWallpaperTask.mFillSize = point3;
                                        setWallpaperTask.execute(new Void[0]);
                                        return;
                                    }
                                    throw new IllegalArgumentException("Can't pass a fill size option if a stretch size is already set.");
                                }
                            });
                            return;
                        }
                        int i4 = point.x;
                        int i5 = (i4 - point2.x) / 2;
                        int i6 = point.y;
                        int i7 = (i6 - point2.y) / 2;
                        asset2.decodeBitmapRegion(new Rect(i5, i7, i4 - i5, i6 - i7), point2.x, point2.y, false, new TopLevelPickerActivity$4$$ExternalSyntheticLambda0(defaultWallpaperPersister, wallpaperInfo2, setWallpaperCallback2));
                    } else if (i2 == 1) {
                        DefaultWallpaperPersister defaultWallpaperPersister2 = DefaultWallpaperPersister.this;
                        WallpaperInfo wallpaperInfo3 = wallpaperInfo;
                        Asset asset3 = asset;
                        Point point3 = screenSize;
                        WallpaperPersister.SetWallpaperCallback setWallpaperCallback3 = setWallpaperCallback;
                        Objects.requireNonNull(defaultWallpaperPersister2);
                        float max = Math.max(((float) point3.x) / ((float) point.x), ((float) point3.y) / ((float) point.y));
                        int i8 = (int) (((float) point.x) * max);
                        int i9 = (int) (((float) point.y) * max);
                        int i10 = (i8 - point3.x) / 2;
                        int i11 = (i9 - point3.y) / 2;
                        defaultWallpaperPersister2.setIndividualWallpaper(wallpaperInfo3, asset3, new Rect(i10, i11, i8 - i10, i9 - i11), max, 2, setWallpaperCallback3);
                    } else if (i2 != 2) {
                        StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Unsupported wallpaper position option specified: ");
                        m.append(i);
                        Log.e("WallpaperPersister", m.toString());
                        setWallpaperCallback.onError(null);
                    } else {
                        Asset asset4 = asset;
                        Point point4 = screenSize;
                        asset4.decodeBitmap(point4.x, point4.y, new Asset.BitmapReceiver() { // from class: com.android.wallpaper.module.DefaultWallpaperPersister.4.1
                            @Override // com.android.wallpaper.asset.Asset.BitmapReceiver
                            public void onBitmapDecoded(Bitmap bitmap) {
                                AnonymousClass4 r7 = AnonymousClass4.this;
                                DefaultWallpaperPersister defaultWallpaperPersister3 = DefaultWallpaperPersister.this;
                                WallpaperInfo wallpaperInfo4 = wallpaperInfo;
                                Point point5 = screenSize;
                                WallpaperPersister.SetWallpaperCallback setWallpaperCallback4 = setWallpaperCallback;
                                Objects.requireNonNull(defaultWallpaperPersister3);
                                SetWallpaperTask setWallpaperTask = new SetWallpaperTask(wallpaperInfo4, bitmap, 2, setWallpaperCallback4);
                                if (setWallpaperTask.mFillSize == null) {
                                    setWallpaperTask.mStretchSize = point5;
                                    setWallpaperTask.execute(new Void[0]);
                                    return;
                                }
                                throw new IllegalArgumentException("Can't pass a stretch size option if a fill size is already set.");
                            }
                        });
                    }
                }
            });
        }

        public boolean setWallpaperInRotation(Bitmap bitmap, List<String> list, int i, int i2, String str, String str2) {
            int cropAndSetWallpaperBitmapInRotationStatic = cropAndSetWallpaperBitmapInRotationStatic(bitmap);
            if (cropAndSetWallpaperBitmapInRotationStatic == 0) {
                return false;
            }
            finalizeWallpaperForRotatingComponent(list, str, i, i2, str2, cropAndSetWallpaperBitmapInRotationStatic);
            return true;
        }

        /* loaded from: classes.dex */
        public class SetWallpaperTask extends AsyncTask<Void, Void, Boolean> {
            public Bitmap mBitmap;
            public final WallpaperPersister.SetWallpaperCallback mCallback;
            public final int mDestination;
            public Point mFillSize;
            public InputStream mInputStream;
            public Point mStretchSize;
            public final WallpaperInfo mWallpaper;

            public SetWallpaperTask(WallpaperInfo wallpaperInfo, Bitmap bitmap, int i, WallpaperPersister.SetWallpaperCallback setWallpaperCallback) {
                this.mWallpaper = wallpaperInfo;
                this.mBitmap = bitmap;
                this.mDestination = i;
                this.mCallback = setWallpaperCallback;
            }

            /* Return type fixed from 'java.lang.Object' to match base method */
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object[]] */
            /* JADX DEBUG: Failed to insert an additional move for type inference into block B:78:0x02c0 */
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r13v2 */
            /* JADX WARN: Type inference failed for: r13v4, types: [android.graphics.Bitmap] */
            /* JADX WARN: Type inference failed for: r13v7 */
            /* JADX WARN: Type inference failed for: r13v9 */
            /* JADX WARN: Type inference failed for: r13v10, types: [android.graphics.Bitmap] */
            /* JADX WARN: Type inference failed for: r13v11 */
            /* JADX WARN: Type inference failed for: r13v12 */
            /* JADX WARNING: Removed duplicated region for block: B:29:0x0126  */
            /* JADX WARNING: Removed duplicated region for block: B:43:0x0197  */
            /* JADX WARNING: Removed duplicated region for block: B:67:0x02d8  */
            /* JADX WARNING: Removed duplicated region for block: B:75:0x02f9  */
            /* JADX WARNING: Removed duplicated region for block: B:83:0x02f1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
            /* JADX WARNING: Removed duplicated region for block: B:93:0x02a5 A[EXC_TOP_SPLITTER, SYNTHETIC] */
            /* JADX WARNING: Unknown variable types count: 2 */
            @Override // android.os.AsyncTask
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public java.lang.Boolean doInBackground(java.lang.Void[] r26) {
                /*
                // Method dump skipped, instructions count: 764
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.wallpaper.module.DefaultWallpaperPersister.SetWallpaperTask.doInBackground(java.lang.Object[]):java.lang.Object");
            }

            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
            @Override // android.os.AsyncTask
            public void onPostExecute(Boolean bool) {
                Boolean bool2 = bool;
                InputStream inputStream = this.mInputStream;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Log.e("WallpaperPersister", "Failed to close input stream " + e);
                        this.mCallback.onError(e);
                        return;
                    }
                }
                if (bool2.booleanValue()) {
                    this.mCallback.onSuccess(this.mWallpaper);
                    DefaultWallpaperPersister.this.mWallpaperChangedNotifier.notifyWallpaperChanged();
                    return;
                }
                this.mCallback.onError(null);
            }

            public SetWallpaperTask(WallpaperInfo wallpaperInfo, InputStream inputStream, int i, WallpaperPersister.SetWallpaperCallback setWallpaperCallback) {
                this.mWallpaper = wallpaperInfo;
                this.mInputStream = inputStream;
                this.mDestination = i;
                this.mCallback = setWallpaperCallback;
            }
        }

        public final void setIndividualWallpaper(WallpaperInfo wallpaperInfo, Bitmap bitmap, int i, WallpaperPersister.SetWallpaperCallback setWallpaperCallback) {
            new SetWallpaperTask(wallpaperInfo, bitmap, i, setWallpaperCallback).execute(new Void[0]);
        }
    }
