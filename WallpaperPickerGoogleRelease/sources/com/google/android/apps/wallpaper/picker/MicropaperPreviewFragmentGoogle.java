package com.google.android.apps.wallpaper.picker;

import android.app.Activity;
import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentActivity;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.model.ImageWallpaperInfo;
import com.android.wallpaper.module.Injector;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.module.WallpaperPersister;
import com.android.wallpaper.module.WallpaperSetter;
import com.android.wallpaper.picker.LivePreviewFragment;
import com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda2;
import com.android.wallpaper.util.ScreenSizeCalculator;
import com.google.android.apps.wallpaper.model.MicropaperWallpaperInfo;
import com.google.common.io.ByteStreams;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import wireless.android.learning.acmi.micropaper.frontend.MicropaperFrontend;
/* loaded from: classes.dex */
public class MicropaperPreviewFragmentGoogle extends LivePreviewFragment {
    public static final /* synthetic */ int $r8$clinit = 0;
    public List<String> mPreviewAttributions;
    public Size mScreenSize;
    public final Executor mSetWallpaperExecutor = Executors.newCachedThreadPool(MicropaperPreviewFragmentGoogle$$ExternalSyntheticLambda3.INSTANCE);
    public WallpaperManager mWallpaperManager;
    public WallpaperPersister mWallpaperPersister;
    public WallpaperSetter mWallpaperSetter;

    public static boolean access$100(Context context, InputStream inputStream, String str) {
        try {
            FileOutputStream openFileOutput = context.openFileOutput(str, 0);
            ByteStreams.copy(inputStream, openFileOutput);
            openFileOutput.close();
            return true;
        } catch (IOException e) {
            Log.e("MicropaperPreviewFragmentGoogle", "Failed to copy input stream to local file", e);
            return false;
        }
    }

    public static void setMicropaperComponentAndReturn(Activity activity, WallpaperManager wallpaperManager, boolean z) {
        ComponentName componentName = MicropaperFrontend.MICROPAPER_SERVICE;
        Intent intent = new Intent();
        intent.setAction("ACTION_COMMIT_TO_HOME");
        intent.setComponent(MicropaperFrontend.MICROPAPER_BROADCAST_RECEIVER);
        activity.sendBroadcast(intent);
        wallpaperManager.setWallpaperComponent(MicropaperFrontend.MICROPAPER_SERVICE);
        if (z) {
            try {
                wallpaperManager.clear(2);
            } catch (IOException e) {
                Log.e("MicropaperPreviewFragmentGoogle", "Error clearing lock wallpaper", e);
            }
        }
        activity.setResult(-1);
        activity.finish();
    }

    @Override // com.android.wallpaper.picker.PreviewFragment
    public List<String> getAttributions(Context context) {
        List<String> list = this.mPreviewAttributions;
        return list == null ? this.mWallpaper.getAttributions(context) : list;
    }

    @Override // com.android.wallpaper.picker.LivePreviewFragment
    public String getDeleteAction(WallpaperInfo wallpaperInfo) {
        return null;
    }

    @Override // com.android.wallpaper.picker.LivePreviewFragment
    public String getSettingsActivity(WallpaperInfo wallpaperInfo) {
        return null;
    }

    @Override // com.android.wallpaper.picker.LivePreviewFragment
    public Uri getSettingsSliceUri(WallpaperInfo wallpaperInfo) {
        ComponentName componentName = MicropaperFrontend.MICROPAPER_SERVICE;
        return new Uri.Builder().scheme("content").authority("com.google.pixel.dynamicwallpapers.slice").appendPath("settings_slice").build();
    }

    @Override // com.android.wallpaper.picker.LivePreviewFragment
    public Intent getWallpaperIntent(WallpaperInfo wallpaperInfo) {
        ComponentName componentName = MicropaperFrontend.MICROPAPER_SERVICE;
        Intent intent = new Intent("android.service.wallpaper.WallpaperService");
        intent.setComponent(MicropaperFrontend.MICROPAPER_SERVICE);
        return intent;
    }

    @Override // com.android.wallpaper.util.WallpaperConnection.WallpaperConnectionListener
    public void onConnected() {
        if (this.mWallpaper instanceof MicropaperWallpaperInfo) {
            Context requireContext = requireContext();
            ComponentName componentName = MicropaperFrontend.MICROPAPER_SERVICE;
            this.mPreviewAttributions = requireContext.getContentResolver().call(new Uri.Builder().scheme("content").authority("com.google.pixel.dynamicwallpapers.parameters").build(), "PROVIDER_SET_PREVIEW_FROM_HOME", (String) null, (Bundle) null).getStringArrayList("EXTRA_IMAGE_LABELS");
            return;
        }
        Context requireContext2 = requireContext();
        Uri build = new Uri.Builder().scheme("content").authority("com.android.wallpaper.picker.micropaperprovider").appendPath(this.mWallpaper.getUri().toString()).build();
        ComponentName componentName2 = MicropaperFrontend.MICROPAPER_SERVICE;
        Uri build2 = new Uri.Builder().scheme("content").authority("com.google.pixel.dynamicwallpapers.parameters").build();
        Bundle bundle = new Bundle();
        bundle.putParcelable("EXTRA_WALLPAPER_URI", build);
        this.mPreviewAttributions = requireContext2.getContentResolver().call(build2, "PROVIDER_SET_PREVIEW_URI", (String) null, bundle).getStringArrayList("EXTRA_IMAGE_LABELS");
    }

    @Override // com.android.wallpaper.picker.LivePreviewFragment, com.android.wallpaper.picker.PreviewFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FragmentActivity requireActivity = requireActivity();
        Context applicationContext = requireActivity.getApplicationContext();
        this.mWallpaperManager = WallpaperManager.getInstance(requireActivity);
        Injector injector = InjectorProvider.getInjector();
        this.mWallpaperPersister = injector.getWallpaperPersister(requireActivity);
        Point screenSize = ScreenSizeCalculator.getInstance().getScreenSize(requireActivity.getWindowManager().getDefaultDisplay());
        this.mScreenSize = new Size(screenSize.x, screenSize.y);
        this.mWallpaperSetter = new WallpaperSetter(injector.getWallpaperPersister(applicationContext), injector.getPreferences(applicationContext), injector.getUserEventLogger(applicationContext), this.mArguments.getBoolean("testing_mode_enabled"));
        this.mArguments.getInt("preview_mode");
    }

    @Override // com.android.wallpaper.picker.LivePreviewFragment, com.android.wallpaper.picker.PreviewFragment, androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        onCreateView.setOnTouchListener(new LivePreviewFragment$$ExternalSyntheticLambda2(this));
        return onCreateView;
    }

    @Override // com.android.wallpaper.util.WallpaperConnection.WallpaperConnectionListener
    public void onDisconnected() {
        Context requireContext = requireContext();
        ComponentName componentName = MicropaperFrontend.MICROPAPER_SERVICE;
        Intent intent = new Intent();
        intent.setAction("ACTION_CLEAR_PREVIEW_URI");
        intent.setComponent(MicropaperFrontend.MICROPAPER_BROADCAST_RECEIVER);
        requireContext.sendBroadcast(intent);
    }

    @Override // com.android.wallpaper.picker.PreviewFragment
    public void onSetWallpaperClicked(View view) {
        FragmentActivity activity = getActivity();
        WallpaperManager wallpaperManager = this.mWallpaperManager;
        WallpaperPersister wallpaperPersister = this.mWallpaperPersister;
        com.android.wallpaper.model.WallpaperInfo wallpaperInfo = this.mWallpaper;
        Size size = this.mScreenSize;
        Asset asset = wallpaperInfo.getAsset(activity);
        asset.decodeRawDimensions(activity, new Asset.DimensionsReceiver(activity, size, wallpaperManager, wallpaperInfo, wallpaperPersister, asset) { // from class: com.google.android.apps.wallpaper.picker.MicropaperPreviewFragmentGoogle$$ExternalSyntheticLambda0
            public final /* synthetic */ Activity f$1;
            public final /* synthetic */ Size f$2;
            public final /* synthetic */ WallpaperManager f$3;
            public final /* synthetic */ com.android.wallpaper.model.WallpaperInfo f$4;
            public final /* synthetic */ WallpaperPersister f$5;
            public final /* synthetic */ Asset f$6;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
                this.f$5 = r6;
                this.f$6 = r7;
            }

            @Override // com.android.wallpaper.asset.Asset.DimensionsReceiver
            public final void onDimensionsDecoded(Point point) {
                MicropaperPreviewFragmentGoogle micropaperPreviewFragmentGoogle = MicropaperPreviewFragmentGoogle.this;
                Activity activity2 = this.f$1;
                Size size2 = this.f$2;
                WallpaperManager wallpaperManager2 = this.f$3;
                com.android.wallpaper.model.WallpaperInfo wallpaperInfo2 = this.f$4;
                WallpaperPersister wallpaperPersister2 = this.f$5;
                Asset asset2 = this.f$6;
                int i = MicropaperPreviewFragmentGoogle.$r8$clinit;
                Objects.requireNonNull(micropaperPreviewFragmentGoogle);
                Size size3 = new Size(point.x, point.y);
                ComponentName componentName = MicropaperFrontend.MICROPAPER_SERVICE;
                Uri build = new Uri.Builder().scheme("content").authority("com.google.pixel.dynamicwallpapers.parameters").build();
                Bundle bundle = new Bundle();
                bundle.putSize("EXTRA_IMAGE_SIZE", size3);
                bundle.putSize("EXTRA_SURFACE_SIZE", size2);
                Bundle call = activity2.getContentResolver().call(build, "PROVIDER_GET_PREVIEW_PARAMETERS", (String) null, bundle);
                Pair create = Pair.create(Boolean.valueOf(call.getBoolean("EXTRA_PREVIEW_IS_PLAYING", false)), (Rect) call.getParcelable("EXTRA_CROP_RECT"));
                boolean booleanValue = ((Boolean) create.first).booleanValue();
                Rect rect = (Rect) create.second;
                if (!booleanValue) {
                    ImageWallpaperInfo imageWallpaperInfo = new ImageWallpaperInfo(wallpaperInfo2.getUri());
                    float width = (((float) size2.getWidth()) * 1.0f) / ((float) rect.width());
                    micropaperPreviewFragmentGoogle.mWallpaperSetter.requestDestination(micropaperPreviewFragmentGoogle.getActivity(), micropaperPreviewFragmentGoogle.mFragmentManager, 
                    /*  JADX ERROR: Method code generation error
                        jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x00d8: INVOKE  
                          (wrap: com.android.wallpaper.module.WallpaperSetter : 0x00c6: IGET  (r14v17 com.android.wallpaper.module.WallpaperSetter A[REMOVE]) = (r1v0 'micropaperPreviewFragmentGoogle' com.google.android.apps.wallpaper.picker.MicropaperPreviewFragmentGoogle) com.google.android.apps.wallpaper.picker.MicropaperPreviewFragmentGoogle.mWallpaperSetter com.android.wallpaper.module.WallpaperSetter)
                          (wrap: androidx.fragment.app.FragmentActivity : 0x00c8: INVOKE  (r10v0 androidx.fragment.app.FragmentActivity A[REMOVE]) = (r1v0 'micropaperPreviewFragmentGoogle' com.google.android.apps.wallpaper.picker.MicropaperPreviewFragmentGoogle) type: VIRTUAL call: androidx.fragment.app.Fragment.getActivity():androidx.fragment.app.FragmentActivity)
                          (wrap: androidx.fragment.app.FragmentManager : 0x00cc: IGET  (r11v0 androidx.fragment.app.FragmentManager A[REMOVE]) = (r1v0 'micropaperPreviewFragmentGoogle' com.google.android.apps.wallpaper.picker.MicropaperPreviewFragmentGoogle) androidx.fragment.app.Fragment.mFragmentManager androidx.fragment.app.FragmentManager)
                          (wrap: com.google.android.apps.wallpaper.picker.MicropaperPreviewFragmentGoogle$$ExternalSyntheticLambda1 : 0x00d5: CONSTRUCTOR  (r12v0 com.google.android.apps.wallpaper.picker.MicropaperPreviewFragmentGoogle$$ExternalSyntheticLambda1 A[REMOVE]) = 
                          (r1v0 'micropaperPreviewFragmentGoogle' com.google.android.apps.wallpaper.picker.MicropaperPreviewFragmentGoogle)
                          (r2v0 'activity2' android.app.Activity)
                          (r5v0 'wallpaperPersister2' com.android.wallpaper.module.WallpaperPersister)
                          (r6v10 'imageWallpaperInfo' com.android.wallpaper.model.ImageWallpaperInfo)
                          (r13v1 'asset2' com.android.wallpaper.asset.Asset)
                          (wrap: android.graphics.Rect : 0x00c3: CONSTRUCTOR  (r9v1 android.graphics.Rect A[REMOVE]) = 
                          (wrap: int : 0x00b3: CAST (r0v7 int A[REMOVE]) = (int) (wrap: float : 0x00b2: ARITH  (r0v6 float A[REMOVE]) = (wrap: float : 0x00b1: CAST (r0v5 float A[REMOVE]) = (float) (wrap: int : 0x00af: IGET  (r0v4 int A[REMOVE]) = (r14v12 'rect' android.graphics.Rect) android.graphics.Rect.left int)) * (r7v5 'width' float)))
                          (wrap: int : 0x00b8: CAST (r3v8 int A[REMOVE]) = (int) (wrap: float : 0x00b7: ARITH  (r3v7 float A[REMOVE]) = (wrap: float : 0x00b6: CAST (r3v6 float A[REMOVE]) = (float) (wrap: int : 0x00b4: IGET  (r3v5 int A[REMOVE]) = (r14v12 'rect' android.graphics.Rect) android.graphics.Rect.top int)) * (r7v5 'width' float)))
                          (wrap: int : 0x00bd: CAST (r4v4 int A[REMOVE]) = (int) (wrap: float : 0x00bc: ARITH  (r4v3 float A[REMOVE]) = (wrap: float : 0x00bb: CAST (r4v2 float A[REMOVE]) = (float) (wrap: int : 0x00b9: IGET  (r4v1 int A[REMOVE]) = (r14v12 'rect' android.graphics.Rect) android.graphics.Rect.right int)) * (r7v5 'width' float)))
                          (wrap: int : 0x00c2: CAST (r14v16 int A[REMOVE]) = (int) (wrap: float : 0x00c1: ARITH  (r14v15 float A[REMOVE]) = (wrap: float : 0x00c0: CAST (r14v14 float A[REMOVE]) = (float) (wrap: int : 0x00be: IGET  (r14v13 int A[REMOVE]) = (r14v12 'rect' android.graphics.Rect) android.graphics.Rect.bottom int)) * (r7v5 'width' float)))
                         call: android.graphics.Rect.<init>(int, int, int, int):void type: CONSTRUCTOR)
                          (r7v5 'width' float)
                         call: com.google.android.apps.wallpaper.picker.MicropaperPreviewFragmentGoogle$$ExternalSyntheticLambda1.<init>(com.google.android.apps.wallpaper.picker.MicropaperPreviewFragmentGoogle, android.app.Activity, com.android.wallpaper.module.WallpaperPersister, com.android.wallpaper.model.WallpaperInfo, com.android.wallpaper.asset.Asset, android.graphics.Rect, float):void type: CONSTRUCTOR)
                          false
                         type: VIRTUAL call: com.android.wallpaper.module.WallpaperSetter.requestDestination(android.app.Activity, androidx.fragment.app.FragmentManager, com.android.wallpaper.picker.SetWallpaperDialogFragment$Listener, boolean):void in method: com.google.android.apps.wallpaper.picker.MicropaperPreviewFragmentGoogle$$ExternalSyntheticLambda0.onDimensionsDecoded(android.graphics.Point):void, file: classes.dex
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
                        Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.google.android.apps.wallpaper.picker.MicropaperPreviewFragmentGoogle$$ExternalSyntheticLambda1, state: NOT_LOADED
                        	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:259)
                        	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:675)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:393)
                        	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                        	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:976)
                        	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:801)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:397)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:278)
                        	... 21 more
                        */
                    /*
                        this = this;
                        com.google.android.apps.wallpaper.picker.MicropaperPreviewFragmentGoogle r1 = com.google.android.apps.wallpaper.picker.MicropaperPreviewFragmentGoogle.this
                        android.app.Activity r2 = r13.f$1
                        android.util.Size r0 = r13.f$2
                        android.app.WallpaperManager r3 = r13.f$3
                        com.android.wallpaper.model.WallpaperInfo r4 = r13.f$4
                        com.android.wallpaper.module.WallpaperPersister r5 = r13.f$5
                        com.android.wallpaper.asset.Asset r13 = r13.f$6
                        int r6 = com.google.android.apps.wallpaper.picker.MicropaperPreviewFragmentGoogle.$r8$clinit
                        java.util.Objects.requireNonNull(r1)
                        android.util.Size r6 = new android.util.Size
                        int r7 = r14.x
                        int r14 = r14.y
                        r6.<init>(r7, r14)
                        android.content.ComponentName r14 = wireless.android.learning.acmi.micropaper.frontend.MicropaperFrontend.MICROPAPER_SERVICE
                        android.net.Uri$Builder r14 = new android.net.Uri$Builder
                        r14.<init>()
                        java.lang.String r7 = "content"
                        android.net.Uri$Builder r14 = r14.scheme(r7)
                        java.lang.String r7 = "com.google.pixel.dynamicwallpapers.parameters"
                        android.net.Uri$Builder r14 = r14.authority(r7)
                        android.net.Uri r14 = r14.build()
                        android.os.Bundle r7 = new android.os.Bundle
                        r7.<init>()
                        java.lang.String r8 = "EXTRA_IMAGE_SIZE"
                        r7.putSize(r8, r6)
                        java.lang.String r6 = "EXTRA_SURFACE_SIZE"
                        r7.putSize(r6, r0)
                        android.content.ContentResolver r6 = r2.getContentResolver()
                        r8 = 0
                        java.lang.String r9 = "PROVIDER_GET_PREVIEW_PARAMETERS"
                        android.os.Bundle r14 = r6.call(r14, r9, r8, r7)
                        r8 = 0
                        java.lang.String r6 = "EXTRA_PREVIEW_IS_PLAYING"
                        boolean r6 = r14.getBoolean(r6, r8)
                        java.lang.Boolean r6 = java.lang.Boolean.valueOf(r6)
                        java.lang.String r7 = "EXTRA_CROP_RECT"
                        android.os.Parcelable r14 = r14.getParcelable(r7)
                        android.graphics.Rect r14 = (android.graphics.Rect) r14
                        android.util.Pair r14 = android.util.Pair.create(r6, r14)
                        java.lang.Object r6 = r14.first
                        java.lang.Boolean r6 = (java.lang.Boolean) r6
                        boolean r6 = r6.booleanValue()
                        java.lang.Object r14 = r14.second
                        android.graphics.Rect r14 = (android.graphics.Rect) r14
                        if (r6 == 0) goto L_0x0095
                        android.app.WallpaperInfo r13 = r3.getWallpaperInfo()
                        if (r13 == 0) goto L_0x0083
                        r13 = 2
                        int r13 = r3.getWallpaperId(r13)
                        if (r13 >= 0) goto L_0x0083
                        com.google.android.apps.wallpaper.picker.MicropaperPreviewFragmentGoogle.setMicropaperComponentAndReturn(r2, r3, r8)
                        goto L_0x00db
                    L_0x0083:
                        com.android.wallpaper.module.WallpaperSetter r13 = r1.mWallpaperSetter
                        androidx.fragment.app.FragmentActivity r14 = r1.getActivity()
                        androidx.fragment.app.FragmentManager r0 = r1.mFragmentManager
                        com.android.wallpaper.picker.TopLevelPickerActivity$4$$ExternalSyntheticLambda0 r4 = new com.android.wallpaper.picker.TopLevelPickerActivity$4$$ExternalSyntheticLambda0
                        r4.<init>(r1, r2, r3)
                        r1 = 1
                        r13.requestDestination(r14, r0, r4, r1)
                        goto L_0x00db
                    L_0x0095:
                        com.android.wallpaper.model.ImageWallpaperInfo r6 = new com.android.wallpaper.model.ImageWallpaperInfo
                        android.net.Uri r3 = r4.getUri()
                        r6.<init>(r3)
                        r3 = 1065353216(0x3f800000, float:1.0)
                        int r0 = r0.getWidth()
                        float r0 = (float) r0
                        float r0 = r0 * r3
                        int r3 = r14.width()
                        float r3 = (float) r3
                        float r7 = r0 / r3
                        android.graphics.Rect r9 = new android.graphics.Rect
                        int r0 = r14.left
                        float r0 = (float) r0
                        float r0 = r0 * r7
                        int r0 = (int) r0
                        int r3 = r14.top
                        float r3 = (float) r3
                        float r3 = r3 * r7
                        int r3 = (int) r3
                        int r4 = r14.right
                        float r4 = (float) r4
                        float r4 = r4 * r7
                        int r4 = (int) r4
                        int r14 = r14.bottom
                        float r14 = (float) r14
                        float r14 = r14 * r7
                        int r14 = (int) r14
                        r9.<init>(r0, r3, r4, r14)
                        com.android.wallpaper.module.WallpaperSetter r14 = r1.mWallpaperSetter
                        androidx.fragment.app.FragmentActivity r10 = r1.getActivity()
                        androidx.fragment.app.FragmentManager r11 = r1.mFragmentManager
                        com.google.android.apps.wallpaper.picker.MicropaperPreviewFragmentGoogle$$ExternalSyntheticLambda1 r12 = new com.google.android.apps.wallpaper.picker.MicropaperPreviewFragmentGoogle$$ExternalSyntheticLambda1
                        r0 = r12
                        r3 = r5
                        r4 = r6
                        r5 = r13
                        r6 = r9
                        r0.<init>(r1, r2, r3, r4, r5, r6, r7)
                        r14.requestDestination(r10, r11, r12, r8)
                    L_0x00db:
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.google.android.apps.wallpaper.picker.MicropaperPreviewFragmentGoogle$$ExternalSyntheticLambda0.onDimensionsDecoded(android.graphics.Point):void");
                }
            });
        }
    }
