package com.android.wallpaper.util;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.SurfaceControlViewHost;
import android.view.SurfaceView;
import android.widget.ImageView;
import androidx.cardview.R$attr;
import com.android.customization.model.mode.DarkModeSectionController$$ExternalSyntheticLambda1;
import com.android.customization.model.themedicon.ThemedIconSwitchProvider;
import com.android.customization.picker.WallpaperPreviewer;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.model.LiveWallpaperInfo;
import com.android.wallpaper.model.WallpaperInfo;
import com.android.wallpaper.picker.CustomizationPickerFragment;
import com.android.wallpaper.picker.ImagePreviewFragment;
import com.android.wallpaper.picker.ImagePreviewFragment$3$$ExternalSyntheticLambda0;
import com.android.wallpaper.picker.PreviewFragment;
import com.android.wallpaper.picker.WallpaperColorThemePreview;
import com.android.wallpaper.picker.WorkspaceSurfaceHolderCallback;
import com.android.wallpaper.util.PreviewUtils;
import com.android.wallpaper.widget.PreviewPager$$ExternalSyntheticLambda1;
import com.android.wallpaper.widget.WallpaperInfoView;
/* loaded from: classes.dex */
public final /* synthetic */ class PreviewUtils$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId = 1;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ PreviewUtils$$ExternalSyntheticLambda0(ThemedIconSwitchProvider themedIconSwitchProvider, ThemedIconSwitchProvider.FetchThemedIconEnabledCallback fetchThemedIconEnabledCallback) {
        this.f$0 = themedIconSwitchProvider;
        this.f$1 = fetchThemedIconEnabledCallback;
    }

    public /* synthetic */ PreviewUtils$$ExternalSyntheticLambda0(WallpaperPreviewer wallpaperPreviewer, ImageView imageView) {
        this.f$0 = wallpaperPreviewer;
        this.f$1 = imageView;
    }

    public /* synthetic */ PreviewUtils$$ExternalSyntheticLambda0(CustomizationPickerFragment customizationPickerFragment, Bundle bundle) {
        this.f$0 = customizationPickerFragment;
        this.f$1 = bundle;
    }

    public /* synthetic */ PreviewUtils$$ExternalSyntheticLambda0(ImagePreviewFragment.AnonymousClass3 r2, Bitmap bitmap) {
        this.f$0 = r2;
        this.f$1 = bitmap;
    }

    public /* synthetic */ PreviewUtils$$ExternalSyntheticLambda0(PreviewFragment.WallpaperInfoContent wallpaperInfoContent, WallpaperInfoView wallpaperInfoView) {
        this.f$0 = wallpaperInfoContent;
        this.f$1 = wallpaperInfoView;
    }

    public /* synthetic */ PreviewUtils$$ExternalSyntheticLambda0(PreviewUtils.WorkspacePreviewCallback workspacePreviewCallback, Bundle bundle) {
        this.f$0 = workspacePreviewCallback;
        this.f$1 = bundle;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ImageView imageView;
        boolean z = true;
        switch (this.$r8$classId) {
            case 0:
                Bundle bundle = (Bundle) this.f$1;
                WorkspaceSurfaceHolderCallback workspaceSurfaceHolderCallback = (WorkspaceSurfaceHolderCallback) ((PreviewPager$$ExternalSyntheticLambda1) ((PreviewUtils.WorkspacePreviewCallback) this.f$0)).f$0;
                workspaceSurfaceHolderCallback.mRequestPending.set(false);
                if (bundle != null && workspaceSurfaceHolderCallback.mLastSurface != null) {
                    workspaceSurfaceHolderCallback.mWorkspaceSurface.setChildSurfacePackage((SurfaceControlViewHost.SurfacePackage) bundle.getParcelable("surface_package"));
                    workspaceSurfaceHolderCallback.mCallback = (Message) bundle.getParcelable("callback");
                    if (workspaceSurfaceHolderCallback.mNeedsToCleanUp) {
                        workspaceSurfaceHolderCallback.cleanUp();
                        return;
                    }
                    WorkspaceSurfaceHolderCallback.WorkspaceRenderListener workspaceRenderListener = workspaceSurfaceHolderCallback.mListener;
                    if (workspaceRenderListener != null) {
                        WallpaperColorThemePreview.lambda$updateWorkspacePreview$0((SurfaceView) ((PreviewPager$$ExternalSyntheticLambda1) workspaceRenderListener).f$0);
                        return;
                    }
                    return;
                }
                return;
            case 1:
                ThemedIconSwitchProvider themedIconSwitchProvider = (ThemedIconSwitchProvider) this.f$0;
                ThemedIconSwitchProvider.FetchThemedIconEnabledCallback fetchThemedIconEnabledCallback = (ThemedIconSwitchProvider.FetchThemedIconEnabledCallback) this.f$1;
                Cursor query = themedIconSwitchProvider.mContentResolver.query(themedIconSwitchProvider.mThemedIconUtils.getUriForPath("icon_themed"), null, null, null, null);
                if (query != null) {
                    try {
                        if (query.moveToNext()) {
                            if (query.getInt(query.getColumnIndex("boolean_value")) != 1) {
                                z = false;
                            }
                            if (themedIconSwitchProvider.mCustomizationPreferences.getThemedIconEnabled() != z) {
                                themedIconSwitchProvider.mCustomizationPreferences.setThemedIconEnabled(z);
                            }
                            if (fetchThemedIconEnabledCallback != null) {
                                new Handler(Looper.getMainLooper()).post(new DarkModeSectionController$$ExternalSyntheticLambda1(fetchThemedIconEnabledCallback, z));
                            }
                            query.close();
                            return;
                        }
                    } catch (Throwable th) {
                        try {
                            query.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                }
                if (query != null) {
                    query.close();
                }
                if (fetchThemedIconEnabledCallback != null) {
                    new Handler(Looper.getMainLooper()).post(new ImagePreviewFragment$3$$ExternalSyntheticLambda0(themedIconSwitchProvider, fetchThemedIconEnabledCallback));
                    return;
                }
                return;
            case 2:
                WallpaperPreviewer wallpaperPreviewer = (WallpaperPreviewer) this.f$0;
                ImageView imageView2 = (ImageView) this.f$1;
                Activity activity = wallpaperPreviewer.mActivity;
                if (activity != null && !activity.isDestroyed()) {
                    WallpaperInfo wallpaperInfo = wallpaperPreviewer.mWallpaper;
                    boolean z2 = !(wallpaperInfo instanceof LiveWallpaperInfo);
                    Asset thumbAsset = wallpaperInfo.getThumbAsset(wallpaperPreviewer.mActivity.getApplicationContext());
                    Activity activity2 = wallpaperPreviewer.mActivity;
                    if (z2) {
                        imageView = imageView2;
                    } else {
                        imageView = wallpaperPreviewer.mHomePreview;
                    }
                    thumbAsset.loadPreviewImage(activity2, imageView, R$attr.getColorAttr(activity2, 16844080));
                    WallpaperInfo wallpaperInfo2 = wallpaperPreviewer.mWallpaper;
                    if (wallpaperInfo2 instanceof LiveWallpaperInfo) {
                        Asset thumbAsset2 = wallpaperInfo2.getThumbAsset(wallpaperPreviewer.mActivity.getApplicationContext());
                        Activity activity3 = wallpaperPreviewer.mActivity;
                        thumbAsset2.loadPreviewImage(activity3, imageView2, R$attr.getColorAttr(activity3, 16844080));
                        WallpaperInfo wallpaperInfo3 = wallpaperPreviewer.mWallpaper;
                        Activity activity4 = wallpaperPreviewer.mActivity;
                        if (activity4 != null && !activity4.isFinishing()) {
                            WallpaperConnection wallpaperConnection = wallpaperPreviewer.mWallpaperConnection;
                            if (wallpaperConnection != null) {
                                wallpaperConnection.disconnect();
                            }
                            if (WallpaperConnection.isPreviewAvailable()) {
                                wallpaperPreviewer.mHomePreview.getLocationOnScreen(wallpaperPreviewer.mLivePreviewLocation);
                                wallpaperPreviewer.mPreviewGlobalRect.set(0, 0, wallpaperPreviewer.mHomePreview.getMeasuredWidth(), wallpaperPreviewer.mHomePreview.getMeasuredHeight());
                                wallpaperPreviewer.mPreviewLocalRect.set(wallpaperPreviewer.mPreviewGlobalRect);
                                Rect rect = wallpaperPreviewer.mPreviewGlobalRect;
                                int[] iArr = wallpaperPreviewer.mLivePreviewLocation;
                                rect.offset(iArr[0], iArr[1]);
                                android.app.WallpaperInfo wallpaperComponent = wallpaperInfo3.getWallpaperComponent();
                                WallpaperConnection wallpaperConnection2 = new WallpaperConnection(new Intent("android.service.wallpaper.WallpaperService").setClassName(wallpaperComponent.getPackageName(), wallpaperComponent.getServiceName()), wallpaperPreviewer.mActivity, 
                                /*  JADX ERROR: Method code generation error
                                    jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0138: CONSTRUCTOR  (r2v9 'wallpaperConnection2' com.android.wallpaper.util.WallpaperConnection) = 
                                      (wrap: android.content.Intent : 0x012b: INVOKE  (r9v28 android.content.Intent A[REMOVE]) = 
                                      (wrap: android.content.Intent : 0x0120: CONSTRUCTOR  (r3v18 android.content.Intent A[REMOVE]) = ("android.service.wallpaper.WallpaperService") call: android.content.Intent.<init>(java.lang.String):void type: CONSTRUCTOR)
                                      (wrap: java.lang.String : 0x0123: INVOKE  (r4v20 java.lang.String A[REMOVE]) = (r9v26 'wallpaperComponent' android.app.WallpaperInfo) type: VIRTUAL call: android.app.WallpaperInfo.getPackageName():java.lang.String)
                                      (wrap: java.lang.String : 0x0127: INVOKE  (r9v27 java.lang.String A[REMOVE]) = (r9v26 'wallpaperComponent' android.app.WallpaperInfo) type: VIRTUAL call: android.app.WallpaperInfo.getServiceName():java.lang.String)
                                     type: VIRTUAL call: android.content.Intent.setClassName(java.lang.String, java.lang.String):android.content.Intent)
                                      (wrap: android.app.Activity : 0x012f: IGET  (r3v19 android.app.Activity A[REMOVE]) = (r0v15 'wallpaperPreviewer' com.android.customization.picker.WallpaperPreviewer) com.android.customization.picker.WallpaperPreviewer.mActivity android.app.Activity)
                                      (wrap: com.android.customization.picker.WallpaperPreviewer$2 : 0x0133: CONSTRUCTOR  (r4v21 com.android.customization.picker.WallpaperPreviewer$2 A[REMOVE]) = (r0v15 'wallpaperPreviewer' com.android.customization.picker.WallpaperPreviewer) call: com.android.customization.picker.WallpaperPreviewer.2.<init>(com.android.customization.picker.WallpaperPreviewer):void type: CONSTRUCTOR)
                                      (wrap: android.view.SurfaceView : 0x0136: IGET  (r5v8 android.view.SurfaceView A[REMOVE]) = (r0v15 'wallpaperPreviewer' com.android.customization.picker.WallpaperPreviewer) com.android.customization.picker.WallpaperPreviewer.mWallpaperSurface android.view.SurfaceView)
                                     call: com.android.wallpaper.util.WallpaperConnection.<init>(android.content.Intent, android.content.Context, com.android.wallpaper.util.WallpaperConnection$WallpaperConnectionListener, android.view.SurfaceView):void type: CONSTRUCTOR in method: com.android.wallpaper.util.PreviewUtils$$ExternalSyntheticLambda0.run():void, file: classes.dex
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
                                    	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:94)
                                    	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:137)
                                    	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:137)
                                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                                    	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:94)
                                    	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:137)
                                    	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:137)
                                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                                    	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:94)
                                    	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:137)
                                    	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:137)
                                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                                    	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:94)
                                    	at jadx.core.codegen.RegionGen.makeSwitch(RegionGen.java:281)
                                    	at jadx.core.dex.regions.SwitchRegion.generate(SwitchRegion.java:79)
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
                                    Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.android.customization.picker.WallpaperPreviewer, state: GENERATED_AND_UNLOADED
                                    	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:259)
                                    	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:675)
                                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:393)
                                    	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                                    	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:976)
                                    	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:711)
                                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:393)
                                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:278)
                                    	... 45 more
                                    */
                                /*
                                // Method dump skipped, instructions count: 610
                                */
                                throw new UnsupportedOperationException("Method not decompiled: com.android.wallpaper.util.PreviewUtils$$ExternalSyntheticLambda0.run():void");
                            }
                        }
