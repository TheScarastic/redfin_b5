package com.android.wallpaper.picker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.WallpaperColors;
import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ServiceInfo;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.cardview.R$color;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.slice.Slice;
import androidx.slice.SliceItem;
import androidx.slice.SliceSpec;
import androidx.slice.widget.ListContent;
import androidx.slice.widget.ShortcutView;
import androidx.slice.widget.SliceChildView;
import androidx.slice.widget.SliceLiveData;
import androidx.slice.widget.SliceView;
import androidx.slice.widget.SliceViewPolicy;
import androidx.slice.widget.TemplateView;
import com.android.systemui.shared.R;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.compat.BuildCompat;
import com.android.wallpaper.module.WallpaperPersister;
import com.android.wallpaper.picker.PreviewFragment;
import com.android.wallpaper.util.DiskBasedLogger$$ExternalSyntheticLambda1;
import com.android.wallpaper.util.FullScreenAnimation;
import com.android.wallpaper.util.ScreenSizeCalculator;
import com.android.wallpaper.util.WallpaperConnection;
import com.android.wallpaper.util.WallpaperSurfaceCallback;
import com.android.wallpaper.widget.BottomActionBar;
import com.android.wallpaper.widget.LockScreenPreviewer;
import com.android.wallpaper.widget.PreviewPager$$ExternalSyntheticLambda1;
import com.android.wallpaper.widget.WallpaperColorsLoader;
import com.google.android.material.tabs.TabLayout;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Future;
/* loaded from: classes.dex */
public class LivePreviewFragment extends PreviewFragment implements WallpaperConnection.WallpaperConnectionListener {
    public static final /* synthetic */ int $r8$clinit = 0;
    public Intent mDeleteIntent;
    public CardView mHomePreviewCard;
    public ViewGroup mLockPreviewContainer;
    public LockScreenPreviewer mLockScreenPreviewer;
    public Future<Integer> mPlaceholderColorFuture;
    public ViewGroup mPreviewContainer;
    public Point mScreenSize;
    public Intent mSettingsIntent;
    public LiveData<Slice> mSettingsLiveData;
    public SliceView mSettingsSliceView;
    public TouchForwardingLayout mTouchForwardingLayout;
    public WallpaperConnection mWallpaperConnection;
    public SurfaceView mWallpaperSurface;
    public WallpaperSurfaceCallback mWallpaperSurfaceCallback;
    public SurfaceView mWorkspaceSurface;
    public WorkspaceSurfaceHolderCallback mWorkspaceSurfaceCallback;

    /* loaded from: classes.dex */
    public final class PreviewCustomizeSettingsContent extends BottomActionBar.BottomSheetContent<View> {
        public PreviewCustomizeSettingsContent(Context context, AnonymousClass1 r3) {
            super(context);
        }

        @Override // com.android.wallpaper.widget.BottomActionBar.BottomSheetContent
        public int getViewId() {
            return R.layout.preview_customize_settings;
        }

        @Override // com.android.wallpaper.widget.BottomActionBar.BottomSheetContent
        public void onRecreateView(View view) {
            SliceView sliceView;
            LivePreviewFragment livePreviewFragment = LivePreviewFragment.this;
            LiveData<Slice> liveData = livePreviewFragment.mSettingsLiveData;
            if (liveData != null) {
                if ((liveData.mObservers.mSize > 0) && (sliceView = livePreviewFragment.mSettingsSliceView) != null) {
                    liveData.removeObserver(sliceView);
                }
            }
        }

        @Override // com.android.wallpaper.widget.BottomActionBar.BottomSheetContent
        public void onViewCreated(View view) {
            TemplateView templateView;
            ListContent listContent;
            LivePreviewFragment.this.mSettingsSliceView = (SliceView) view.findViewById(R.id.settings_slice);
            SliceView sliceView = LivePreviewFragment.this.mSettingsSliceView;
            SliceViewPolicy sliceViewPolicy = sliceView.mViewPolicy;
            int i = sliceViewPolicy.mMode;
            if (i != 2) {
                boolean z = true;
                if (i != 2) {
                    sliceViewPolicy.mMode = 2;
                    SliceViewPolicy.PolicyChangeListener policyChangeListener = sliceViewPolicy.mListener;
                    if (!(policyChangeListener == null || (listContent = (templateView = (TemplateView) policyChangeListener).mListContent) == null)) {
                        templateView.updateDisplayedItems(listContent.getHeight(templateView.mSliceStyle, templateView.mViewPolicy));
                    }
                }
                int i2 = sliceView.mViewPolicy.mMode;
                SliceChildView sliceChildView = sliceView.mCurrentView;
                boolean z2 = sliceChildView instanceof ShortcutView;
                Set<SliceItem> loadingActions = sliceChildView.getLoadingActions();
                if (i2 == 3 && !z2) {
                    sliceView.removeView(sliceView.mCurrentView);
                    ShortcutView shortcutView = new ShortcutView(sliceView.getContext());
                    sliceView.mCurrentView = shortcutView;
                    sliceView.addView(shortcutView, sliceView.getChildLp(shortcutView));
                } else if (i2 == 3 || !z2) {
                    z = false;
                } else {
                    sliceView.removeView(sliceView.mCurrentView);
                    TemplateView templateView2 = new TemplateView(sliceView.getContext());
                    sliceView.mCurrentView = templateView2;
                    sliceView.addView(templateView2, sliceView.getChildLp(templateView2));
                }
                if (z) {
                    sliceView.mCurrentView.setPolicy(sliceView.mViewPolicy);
                    sliceView.applyConfigurations();
                    ListContent listContent2 = sliceView.mListContent;
                    if (listContent2 != null && listContent2.isValid()) {
                        sliceView.mCurrentView.setSliceContent(sliceView.mListContent);
                    }
                    sliceView.mCurrentView.setLoadingActions(loadingActions);
                }
                sliceView.updateActions();
            }
            SliceViewPolicy sliceViewPolicy2 = LivePreviewFragment.this.mSettingsSliceView.mViewPolicy;
            boolean z3 = sliceViewPolicy2.mScrollable;
            if (z3 && z3) {
                sliceViewPolicy2.mScrollable = false;
                SliceViewPolicy.PolicyChangeListener policyChangeListener2 = sliceViewPolicy2.mListener;
                if (policyChangeListener2 != null) {
                    TemplateView templateView3 = (TemplateView) policyChangeListener2;
                    templateView3.mRecyclerView.setNestedScrollingEnabled(false);
                    ListContent listContent3 = templateView3.mListContent;
                    if (listContent3 != null) {
                        templateView3.updateDisplayedItems(listContent3.getHeight(templateView3.mSliceStyle, templateView3.mViewPolicy));
                    }
                }
            }
            LivePreviewFragment livePreviewFragment = LivePreviewFragment.this;
            LiveData<Slice> liveData = livePreviewFragment.mSettingsLiveData;
            if (liveData != null) {
                liveData.observeForever(livePreviewFragment.mSettingsSliceView);
            }
        }
    }

    public String getDeleteAction(WallpaperInfo wallpaperInfo) {
        ServiceInfo serviceInfo;
        Bundle bundle;
        WallpaperInfo wallpaperInfo2 = WallpaperManager.getInstance(requireContext()).getWallpaperInfo();
        ServiceInfo serviceInfo2 = wallpaperInfo.getServiceInfo();
        ApplicationInfo applicationInfo = serviceInfo2.applicationInfo;
        boolean z = true;
        if (applicationInfo == null || (applicationInfo.flags & 1) == 0) {
            z = false;
        }
        if (!z) {
            StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("This wallpaper is not pre-installed: ");
            m.append(serviceInfo2.name);
            Log.d("LivePreviewFragment", m.toString());
            return null;
        }
        if (wallpaperInfo2 == null) {
            serviceInfo = null;
        } else {
            serviceInfo = wallpaperInfo2.getServiceInfo();
        }
        if ((serviceInfo == null || !TextUtils.equals(serviceInfo2.name, serviceInfo.name)) && (bundle = serviceInfo2.metaData) != null) {
            return bundle.getString("action_delete_live_wallpaper");
        }
        return null;
    }

    @Override // com.android.wallpaper.picker.PreviewFragment
    public int getLayoutResId() {
        return R.layout.fragment_live_preview;
    }

    public String getSettingsActivity(WallpaperInfo wallpaperInfo) {
        return wallpaperInfo.getSettingsActivity();
    }

    @SuppressLint({"NewApi"})
    public Uri getSettingsSliceUri(WallpaperInfo wallpaperInfo) {
        if (BuildCompat.sSdk >= 29) {
            return wallpaperInfo.getSettingsSliceUri();
        }
        return null;
    }

    public Intent getWallpaperIntent(WallpaperInfo wallpaperInfo) {
        return new Intent("android.service.wallpaper.WallpaperService").setClassName(wallpaperInfo.getPackageName(), wallpaperInfo.getServiceName());
    }

    @Override // com.android.wallpaper.picker.PreviewFragment, com.android.wallpaper.picker.AppbarFragment, com.android.wallpaper.picker.BottomActionBarFragment
    public void onBottomActionBarReady(BottomActionBar bottomActionBar) {
        super.onBottomActionBarReady(bottomActionBar);
        BottomActionBar bottomActionBar2 = ((PreviewFragment) this).mBottomActionBar;
        BottomActionBar.BottomAction bottomAction = BottomActionBar.BottomAction.INFORMATION;
        boolean z = false;
        BottomActionBar.BottomAction bottomAction2 = BottomActionBar.BottomAction.DELETE;
        BottomActionBar.BottomAction bottomAction3 = BottomActionBar.BottomAction.CUSTOMIZE;
        BottomActionBar.BottomAction bottomAction4 = BottomActionBar.BottomAction.APPLY;
        bottomActionBar2.showActionsOnly(bottomAction, bottomAction2, BottomActionBar.BottomAction.EDIT, bottomAction3, bottomAction4);
        ((PreviewFragment) this).mBottomActionBar.setActionClickListener(bottomAction4, new View.OnClickListener(this, 0) { // from class: com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda1
            public final /* synthetic */ int $r8$classId;
            public final /* synthetic */ LivePreviewFragment f$0;

            {
                this.$r8$classId = r3;
                this.f$0 = r2;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                switch (this.$r8$classId) {
                    case 0:
                        LivePreviewFragment livePreviewFragment = this.f$0;
                        int i = LivePreviewFragment.$r8$clinit;
                        livePreviewFragment.onSetWallpaperClicked(null);
                        return;
                    case 1:
                        LivePreviewFragment livePreviewFragment2 = this.f$0;
                        livePreviewFragment2.startActivity(livePreviewFragment2.mSettingsIntent);
                        return;
                    default:
                        LivePreviewFragment livePreviewFragment3 = this.f$0;
                        int i2 = LivePreviewFragment.$r8$clinit;
                        Objects.requireNonNull(livePreviewFragment3);
                        new AlertDialog.Builder(livePreviewFragment3.getContext()).setMessage(R.string.delete_wallpaper_confirmation).setOnDismissListener(
                        /*  JADX ERROR: Method code generation error
                            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x004d: INVOKE  
                              (wrap: android.app.AlertDialog : 0x0049: INVOKE  (r2v5 android.app.AlertDialog A[REMOVE]) = 
                              (wrap: android.app.AlertDialog$Builder : 0x0045: INVOKE  (r2v4 android.app.AlertDialog$Builder A[REMOVE]) = 
                              (wrap: android.app.AlertDialog$Builder : 0x003f: INVOKE  (r2v3 android.app.AlertDialog$Builder A[REMOVE]) = 
                              (wrap: android.app.AlertDialog$Builder : 0x0033: INVOKE  (r3v5 android.app.AlertDialog$Builder A[REMOVE]) = 
                              (wrap: android.app.AlertDialog$Builder : 0x002a: INVOKE  (r3v4 android.app.AlertDialog$Builder A[REMOVE]) = 
                              (wrap: android.app.AlertDialog$Builder : 0x0024: CONSTRUCTOR  (r3v3 android.app.AlertDialog$Builder A[REMOVE]) = 
                              (wrap: android.content.Context : 0x0020: INVOKE  (r1v0 android.content.Context A[REMOVE]) = (r2v1 'livePreviewFragment3' com.android.wallpaper.picker.LivePreviewFragment) type: VIRTUAL call: androidx.fragment.app.Fragment.getContext():android.content.Context)
                             call: android.app.AlertDialog.Builder.<init>(android.content.Context):void type: CONSTRUCTOR)
                              (wrap: int : ?: SGET   com.android.systemui.shared.R.string.delete_wallpaper_confirmation int)
                             type: VIRTUAL call: android.app.AlertDialog.Builder.setMessage(int):android.app.AlertDialog$Builder)
                              (wrap: com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda0 : 0x0030: CONSTRUCTOR  (r1v2 com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda0 A[REMOVE]) = (r2v1 'livePreviewFragment3' com.android.wallpaper.picker.LivePreviewFragment) call: com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda0.<init>(com.android.wallpaper.picker.LivePreviewFragment):void type: CONSTRUCTOR)
                             type: VIRTUAL call: android.app.AlertDialog.Builder.setOnDismissListener(android.content.DialogInterface$OnDismissListener):android.app.AlertDialog$Builder)
                              (wrap: int : ?: SGET   com.android.systemui.shared.R.string.delete_live_wallpaper int)
                              (wrap: com.android.wallpaper.picker.CategoryFragment$$ExternalSyntheticLambda0 : 0x0039: CONSTRUCTOR  (r1v3 com.android.wallpaper.picker.CategoryFragment$$ExternalSyntheticLambda0 A[REMOVE]) = (r2v1 'livePreviewFragment3' com.android.wallpaper.picker.LivePreviewFragment) call: com.android.wallpaper.picker.CategoryFragment$$ExternalSyntheticLambda0.<init>(com.android.wallpaper.picker.LivePreviewFragment):void type: CONSTRUCTOR)
                             type: VIRTUAL call: android.app.AlertDialog.Builder.setPositiveButton(int, android.content.DialogInterface$OnClickListener):android.app.AlertDialog$Builder)
                              (17039360 int)
                              (wrap: android.content.DialogInterface$OnClickListener : ?: CAST (android.content.DialogInterface$OnClickListener) (null android.content.DialogInterface$OnClickListener))
                             type: VIRTUAL call: android.app.AlertDialog.Builder.setNegativeButton(int, android.content.DialogInterface$OnClickListener):android.app.AlertDialog$Builder)
                             type: VIRTUAL call: android.app.AlertDialog.Builder.create():android.app.AlertDialog)
                             type: VIRTUAL call: android.app.AlertDialog.show():void in method: com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda1.onClick(android.view.View):void, file: classes.dex
                            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:285)
                            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:248)
                            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:105)
                            	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
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
                            Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda0, state: NOT_LOADED
                            	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:259)
                            	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:675)
                            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:393)
                            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                            	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:976)
                            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:801)
                            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:397)
                            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                            	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:93)
                            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:770)
                            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:397)
                            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                            	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:93)
                            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:770)
                            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:397)
                            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                            	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:93)
                            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:770)
                            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:397)
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
                            int r3 = r2.$r8$classId
                            r0 = 0
                            switch(r3) {
                                case 0: goto L_0x000f;
                                case 1: goto L_0x0007;
                                default: goto L_0x0006;
                            }
                        L_0x0006:
                            goto L_0x0017
                        L_0x0007:
                            com.android.wallpaper.picker.LivePreviewFragment r2 = r2.f$0
                            android.content.Intent r3 = r2.mSettingsIntent
                            r2.startActivity(r3)
                            return
                        L_0x000f:
                            com.android.wallpaper.picker.LivePreviewFragment r2 = r2.f$0
                            int r3 = com.android.wallpaper.picker.LivePreviewFragment.$r8$clinit
                            r2.onSetWallpaperClicked(r0)
                            return
                        L_0x0017:
                            com.android.wallpaper.picker.LivePreviewFragment r2 = r2.f$0
                            int r3 = com.android.wallpaper.picker.LivePreviewFragment.$r8$clinit
                            java.util.Objects.requireNonNull(r2)
                            android.app.AlertDialog$Builder r3 = new android.app.AlertDialog$Builder
                            android.content.Context r1 = r2.getContext()
                            r3.<init>(r1)
                            r1 = 2131820662(0x7f110076, float:1.9274045E38)
                            android.app.AlertDialog$Builder r3 = r3.setMessage(r1)
                            com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda0 r1 = new com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda0
                            r1.<init>(r2)
                            android.app.AlertDialog$Builder r3 = r3.setOnDismissListener(r1)
                            com.android.wallpaper.picker.CategoryFragment$$ExternalSyntheticLambda0 r1 = new com.android.wallpaper.picker.CategoryFragment$$ExternalSyntheticLambda0
                            r1.<init>(r2)
                            r2 = 2131820661(0x7f110075, float:1.9274043E38)
                            android.app.AlertDialog$Builder r2 = r3.setPositiveButton(r2, r1)
                            r3 = 17039360(0x1040000, float:2.424457E-38)
                            android.app.AlertDialog$Builder r2 = r2.setNegativeButton(r3, r0)
                            android.app.AlertDialog r2 = r2.create()
                            r2.show()
                            return
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda1.onClick(android.view.View):void");
                    }
                });
                ((PreviewFragment) this).mBottomActionBar.bindBottomSheetContentWithAction(new PreviewFragment.WallpaperInfoContent(getContext()), bottomAction);
                final View findViewById = this.mView.findViewById(R.id.separated_tabs_container);
                ((PreviewFragment) this).mBottomActionBar.mAccessibilityCallback = new BottomActionBar.AccessibilityCallback() { // from class: com.android.wallpaper.picker.LivePreviewFragment.2
                    @Override // com.android.wallpaper.widget.BottomActionBar.AccessibilityCallback
                    public void onBottomSheetCollapsed() {
                        LivePreviewFragment.this.mPreviewContainer.setImportantForAccessibility(1);
                        findViewById.setImportantForAccessibility(1);
                    }

                    @Override // com.android.wallpaper.widget.BottomActionBar.AccessibilityCallback
                    public void onBottomSheetExpanded() {
                        LivePreviewFragment.this.mPreviewContainer.setImportantForAccessibility(4);
                        findViewById.setImportantForAccessibility(4);
                    }
                };
                Uri settingsSliceUri = getSettingsSliceUri(this.mWallpaper.getWallpaperComponent());
                if (settingsSliceUri != null) {
                    Context requireContext = requireContext();
                    Set<SliceSpec> set = SliceLiveData.SUPPORTED_SPECS;
                    this.mSettingsLiveData = new SliceLiveData.SliceLiveDataImpl(requireContext.getApplicationContext(), settingsSliceUri, null);
                    ((PreviewFragment) this).mBottomActionBar.bindBottomSheetContentWithAction(new PreviewCustomizeSettingsContent(getContext(), null), bottomAction3);
                } else if (this.mSettingsIntent != null) {
                    ((PreviewFragment) this).mBottomActionBar.setActionClickListener(bottomAction3, new View.OnClickListener(this, 1) { // from class: com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda1
                        public final /* synthetic */ int $r8$classId;
                        public final /* synthetic */ LivePreviewFragment f$0;

                        {
                            this.$r8$classId = r3;
                            this.f$0 = r2;
                        }

                        /*  JADX ERROR: Method code generation error
                            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x004d: INVOKE  
                              (wrap: android.app.AlertDialog : 0x0049: INVOKE  (r2v5 android.app.AlertDialog A[REMOVE]) = 
                              (wrap: android.app.AlertDialog$Builder : 0x0045: INVOKE  (r2v4 android.app.AlertDialog$Builder A[REMOVE]) = 
                              (wrap: android.app.AlertDialog$Builder : 0x003f: INVOKE  (r2v3 android.app.AlertDialog$Builder A[REMOVE]) = 
                              (wrap: android.app.AlertDialog$Builder : 0x0033: INVOKE  (r3v5 android.app.AlertDialog$Builder A[REMOVE]) = 
                              (wrap: android.app.AlertDialog$Builder : 0x002a: INVOKE  (r3v4 android.app.AlertDialog$Builder A[REMOVE]) = 
                              (wrap: android.app.AlertDialog$Builder : 0x0024: CONSTRUCTOR  (r3v3 android.app.AlertDialog$Builder A[REMOVE]) = 
                              (wrap: android.content.Context : 0x0020: INVOKE  (r1v0 android.content.Context A[REMOVE]) = (r2v1 'livePreviewFragment3' com.android.wallpaper.picker.LivePreviewFragment) type: VIRTUAL call: androidx.fragment.app.Fragment.getContext():android.content.Context)
                             call: android.app.AlertDialog.Builder.<init>(android.content.Context):void type: CONSTRUCTOR)
                              (wrap: int : ?: SGET   com.android.systemui.shared.R.string.delete_wallpaper_confirmation int)
                             type: VIRTUAL call: android.app.AlertDialog.Builder.setMessage(int):android.app.AlertDialog$Builder)
                              (wrap: com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda0 : 0x0030: CONSTRUCTOR  (r1v2 com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda0 A[REMOVE]) = (r2v1 'livePreviewFragment3' com.android.wallpaper.picker.LivePreviewFragment) call: com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda0.<init>(com.android.wallpaper.picker.LivePreviewFragment):void type: CONSTRUCTOR)
                             type: VIRTUAL call: android.app.AlertDialog.Builder.setOnDismissListener(android.content.DialogInterface$OnDismissListener):android.app.AlertDialog$Builder)
                              (wrap: int : ?: SGET   com.android.systemui.shared.R.string.delete_live_wallpaper int)
                              (wrap: com.android.wallpaper.picker.CategoryFragment$$ExternalSyntheticLambda0 : 0x0039: CONSTRUCTOR  (r1v3 com.android.wallpaper.picker.CategoryFragment$$ExternalSyntheticLambda0 A[REMOVE]) = (r2v1 'livePreviewFragment3' com.android.wallpaper.picker.LivePreviewFragment) call: com.android.wallpaper.picker.CategoryFragment$$ExternalSyntheticLambda0.<init>(com.android.wallpaper.picker.LivePreviewFragment):void type: CONSTRUCTOR)
                             type: VIRTUAL call: android.app.AlertDialog.Builder.setPositiveButton(int, android.content.DialogInterface$OnClickListener):android.app.AlertDialog$Builder)
                              (17039360 int)
                              (wrap: android.content.DialogInterface$OnClickListener : ?: CAST (android.content.DialogInterface$OnClickListener) (null android.content.DialogInterface$OnClickListener))
                             type: VIRTUAL call: android.app.AlertDialog.Builder.setNegativeButton(int, android.content.DialogInterface$OnClickListener):android.app.AlertDialog$Builder)
                             type: VIRTUAL call: android.app.AlertDialog.Builder.create():android.app.AlertDialog)
                             type: VIRTUAL call: android.app.AlertDialog.show():void in method: com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda1.onClick(android.view.View):void, file: classes.dex
                            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:285)
                            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:248)
                            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:105)
                            	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
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
                            Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda0, state: NOT_LOADED
                            	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:259)
                            	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:675)
                            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:393)
                            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                            	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:976)
                            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:801)
                            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:397)
                            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                            	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:93)
                            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:770)
                            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:397)
                            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                            	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:93)
                            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:770)
                            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:397)
                            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                            	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:93)
                            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:770)
                            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:397)
                            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                            	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:93)
                            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:770)
                            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:397)
                            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:278)
                            	... 16 more
                            */
                        @Override // android.view.View.OnClickListener
                        public final void onClick(android.view.View r3) {
                            /*
                                r2 = this;
                                int r3 = r2.$r8$classId
                                r0 = 0
                                switch(r3) {
                                    case 0: goto L_0x000f;
                                    case 1: goto L_0x0007;
                                    default: goto L_0x0006;
                                }
                            L_0x0006:
                                goto L_0x0017
                            L_0x0007:
                                com.android.wallpaper.picker.LivePreviewFragment r2 = r2.f$0
                                android.content.Intent r3 = r2.mSettingsIntent
                                r2.startActivity(r3)
                                return
                            L_0x000f:
                                com.android.wallpaper.picker.LivePreviewFragment r2 = r2.f$0
                                int r3 = com.android.wallpaper.picker.LivePreviewFragment.$r8$clinit
                                r2.onSetWallpaperClicked(r0)
                                return
                            L_0x0017:
                                com.android.wallpaper.picker.LivePreviewFragment r2 = r2.f$0
                                int r3 = com.android.wallpaper.picker.LivePreviewFragment.$r8$clinit
                                java.util.Objects.requireNonNull(r2)
                                android.app.AlertDialog$Builder r3 = new android.app.AlertDialog$Builder
                                android.content.Context r1 = r2.getContext()
                                r3.<init>(r1)
                                r1 = 2131820662(0x7f110076, float:1.9274045E38)
                                android.app.AlertDialog$Builder r3 = r3.setMessage(r1)
                                com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda0 r1 = new com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda0
                                r1.<init>(r2)
                                android.app.AlertDialog$Builder r3 = r3.setOnDismissListener(r1)
                                com.android.wallpaper.picker.CategoryFragment$$ExternalSyntheticLambda0 r1 = new com.android.wallpaper.picker.CategoryFragment$$ExternalSyntheticLambda0
                                r1.<init>(r2)
                                r2 = 2131820661(0x7f110075, float:1.9274043E38)
                                android.app.AlertDialog$Builder r2 = r3.setPositiveButton(r2, r1)
                                r3 = 17039360(0x1040000, float:2.424457E-38)
                                android.app.AlertDialog$Builder r2 = r2.setNegativeButton(r3, r0)
                                android.app.AlertDialog r2 = r2.create()
                                r2.show()
                                return
                            */
                            throw new UnsupportedOperationException("Method not decompiled: com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda1.onClick(android.view.View):void");
                        }
                    });
                } else {
                    ((PreviewFragment) this).mBottomActionBar.hideActions(bottomAction3);
                }
                if (TextUtils.isEmpty(getDeleteAction(this.mWallpaper.getWallpaperComponent()))) {
                    ((PreviewFragment) this).mBottomActionBar.hideActions(bottomAction2);
                } else {
                    ((PreviewFragment) this).mBottomActionBar.setActionClickListener(bottomAction2, new View.OnClickListener(this, 2) { // from class: com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda1
                        public final /* synthetic */ int $r8$classId;
                        public final /* synthetic */ LivePreviewFragment f$0;

                        {
                            this.$r8$classId = r3;
                            this.f$0 = r2;
                        }

                        /*  JADX ERROR: Method code generation error
                            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x004d: INVOKE  
                              (wrap: android.app.AlertDialog : 0x0049: INVOKE  (r2v5 android.app.AlertDialog A[REMOVE]) = 
                              (wrap: android.app.AlertDialog$Builder : 0x0045: INVOKE  (r2v4 android.app.AlertDialog$Builder A[REMOVE]) = 
                              (wrap: android.app.AlertDialog$Builder : 0x003f: INVOKE  (r2v3 android.app.AlertDialog$Builder A[REMOVE]) = 
                              (wrap: android.app.AlertDialog$Builder : 0x0033: INVOKE  (r3v5 android.app.AlertDialog$Builder A[REMOVE]) = 
                              (wrap: android.app.AlertDialog$Builder : 0x002a: INVOKE  (r3v4 android.app.AlertDialog$Builder A[REMOVE]) = 
                              (wrap: android.app.AlertDialog$Builder : 0x0024: CONSTRUCTOR  (r3v3 android.app.AlertDialog$Builder A[REMOVE]) = 
                              (wrap: android.content.Context : 0x0020: INVOKE  (r1v0 android.content.Context A[REMOVE]) = (r2v1 'livePreviewFragment3' com.android.wallpaper.picker.LivePreviewFragment) type: VIRTUAL call: androidx.fragment.app.Fragment.getContext():android.content.Context)
                             call: android.app.AlertDialog.Builder.<init>(android.content.Context):void type: CONSTRUCTOR)
                              (wrap: int : ?: SGET   com.android.systemui.shared.R.string.delete_wallpaper_confirmation int)
                             type: VIRTUAL call: android.app.AlertDialog.Builder.setMessage(int):android.app.AlertDialog$Builder)
                              (wrap: com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda0 : 0x0030: CONSTRUCTOR  (r1v2 com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda0 A[REMOVE]) = (r2v1 'livePreviewFragment3' com.android.wallpaper.picker.LivePreviewFragment) call: com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda0.<init>(com.android.wallpaper.picker.LivePreviewFragment):void type: CONSTRUCTOR)
                             type: VIRTUAL call: android.app.AlertDialog.Builder.setOnDismissListener(android.content.DialogInterface$OnDismissListener):android.app.AlertDialog$Builder)
                              (wrap: int : ?: SGET   com.android.systemui.shared.R.string.delete_live_wallpaper int)
                              (wrap: com.android.wallpaper.picker.CategoryFragment$$ExternalSyntheticLambda0 : 0x0039: CONSTRUCTOR  (r1v3 com.android.wallpaper.picker.CategoryFragment$$ExternalSyntheticLambda0 A[REMOVE]) = (r2v1 'livePreviewFragment3' com.android.wallpaper.picker.LivePreviewFragment) call: com.android.wallpaper.picker.CategoryFragment$$ExternalSyntheticLambda0.<init>(com.android.wallpaper.picker.LivePreviewFragment):void type: CONSTRUCTOR)
                             type: VIRTUAL call: android.app.AlertDialog.Builder.setPositiveButton(int, android.content.DialogInterface$OnClickListener):android.app.AlertDialog$Builder)
                              (17039360 int)
                              (wrap: android.content.DialogInterface$OnClickListener : ?: CAST (android.content.DialogInterface$OnClickListener) (null android.content.DialogInterface$OnClickListener))
                             type: VIRTUAL call: android.app.AlertDialog.Builder.setNegativeButton(int, android.content.DialogInterface$OnClickListener):android.app.AlertDialog$Builder)
                             type: VIRTUAL call: android.app.AlertDialog.Builder.create():android.app.AlertDialog)
                             type: VIRTUAL call: android.app.AlertDialog.show():void in method: com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda1.onClick(android.view.View):void, file: classes.dex
                            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:285)
                            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:248)
                            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:105)
                            	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
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
                            Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda0, state: NOT_LOADED
                            	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:259)
                            	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:675)
                            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:393)
                            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                            	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:976)
                            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:801)
                            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:397)
                            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                            	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:93)
                            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:770)
                            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:397)
                            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                            	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:93)
                            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:770)
                            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:397)
                            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                            	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:93)
                            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:770)
                            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:397)
                            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                            	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:93)
                            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:770)
                            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:397)
                            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:278)
                            	... 15 more
                            */
                        @Override // android.view.View.OnClickListener
                        public final void onClick(android.view.View r3) {
                            /*
                                r2 = this;
                                int r3 = r2.$r8$classId
                                r0 = 0
                                switch(r3) {
                                    case 0: goto L_0x000f;
                                    case 1: goto L_0x0007;
                                    default: goto L_0x0006;
                                }
                            L_0x0006:
                                goto L_0x0017
                            L_0x0007:
                                com.android.wallpaper.picker.LivePreviewFragment r2 = r2.f$0
                                android.content.Intent r3 = r2.mSettingsIntent
                                r2.startActivity(r3)
                                return
                            L_0x000f:
                                com.android.wallpaper.picker.LivePreviewFragment r2 = r2.f$0
                                int r3 = com.android.wallpaper.picker.LivePreviewFragment.$r8$clinit
                                r2.onSetWallpaperClicked(r0)
                                return
                            L_0x0017:
                                com.android.wallpaper.picker.LivePreviewFragment r2 = r2.f$0
                                int r3 = com.android.wallpaper.picker.LivePreviewFragment.$r8$clinit
                                java.util.Objects.requireNonNull(r2)
                                android.app.AlertDialog$Builder r3 = new android.app.AlertDialog$Builder
                                android.content.Context r1 = r2.getContext()
                                r3.<init>(r1)
                                r1 = 2131820662(0x7f110076, float:1.9274045E38)
                                android.app.AlertDialog$Builder r3 = r3.setMessage(r1)
                                com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda0 r1 = new com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda0
                                r1.<init>(r2)
                                android.app.AlertDialog$Builder r3 = r3.setOnDismissListener(r1)
                                com.android.wallpaper.picker.CategoryFragment$$ExternalSyntheticLambda0 r1 = new com.android.wallpaper.picker.CategoryFragment$$ExternalSyntheticLambda0
                                r1.<init>(r2)
                                r2 = 2131820661(0x7f110075, float:1.9274043E38)
                                android.app.AlertDialog$Builder r2 = r3.setPositiveButton(r2, r1)
                                r3 = 17039360(0x1040000, float:2.424457E-38)
                                android.app.AlertDialog$Builder r2 = r2.setNegativeButton(r3, r0)
                                android.app.AlertDialog r2 = r2.create()
                                r2.show()
                                return
                            */
                            throw new UnsupportedOperationException("Method not decompiled: com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda1.onClick(android.view.View):void");
                        }
                    });
                }
                ((PreviewFragment) this).mBottomActionBar.setVisibility(0);
                ((PreviewFragment) this).mBottomActionBar.disableActions();
                WallpaperConnection wallpaperConnection = this.mWallpaperConnection;
                if (wallpaperConnection != null && wallpaperConnection.mEngineReady) {
                    z = true;
                }
                if (z) {
                    ((PreviewFragment) this).mBottomActionBar.enableActions();
                }
            }

            @Override // com.android.wallpaper.picker.PreviewFragment, androidx.fragment.app.Fragment
            public void onCreate(Bundle bundle) {
                super.onCreate(bundle);
                WallpaperInfo wallpaperComponent = this.mWallpaper.getWallpaperComponent();
                this.mPlaceholderColorFuture = this.mWallpaper.computePlaceholderColor(getContext());
                String deleteAction = getDeleteAction(wallpaperComponent);
                if (!TextUtils.isEmpty(deleteAction)) {
                    Intent intent = new Intent(deleteAction);
                    this.mDeleteIntent = intent;
                    intent.setPackage(wallpaperComponent.getPackageName());
                    this.mDeleteIntent.putExtra("android.live_wallpaper.info", wallpaperComponent);
                }
                String settingsActivity = getSettingsActivity(wallpaperComponent);
                if (settingsActivity != null) {
                    Intent intent2 = new Intent();
                    this.mSettingsIntent = intent2;
                    intent2.setComponent(new ComponentName(wallpaperComponent.getPackageName(), settingsActivity));
                    this.mSettingsIntent.putExtra("android.service.wallpaper.PREVIEW_MODE", true);
                    if (this.mSettingsIntent.resolveActivityInfo(requireContext().getPackageManager(), 0) == null) {
                        Log.i("LivePreviewFragment", "Couldn't find wallpaper settings activity: " + settingsActivity);
                        this.mSettingsIntent = null;
                    }
                }
            }

            @Override // com.android.wallpaper.picker.PreviewFragment, androidx.fragment.app.Fragment
            public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
                final View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
                final FragmentActivity requireActivity = requireActivity();
                this.mScreenSize = ScreenSizeCalculator.getInstance().getScreenSize(requireActivity.getWindowManager().getDefaultDisplay());
                this.mPreviewContainer = (ViewGroup) onCreateView.findViewById(R.id.live_wallpaper_preview);
                this.mTouchForwardingLayout = (TouchForwardingLayout) onCreateView.findViewById(R.id.touch_forwarding_layout);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone((ConstraintLayout) this.mPreviewContainer);
                constraintSet.get(this.mTouchForwardingLayout.getId()).layout.dimensionRatio = String.format(Locale.US, "%d:%d", Integer.valueOf(this.mScreenSize.x), Integer.valueOf(this.mScreenSize.y));
                constraintSet.applyTo((ConstraintLayout) this.mPreviewContainer);
                this.mHomePreviewCard = (CardView) this.mPreviewContainer.findViewById(R.id.wallpaper_full_preview_card);
                this.mLockPreviewContainer = (ViewGroup) this.mPreviewContainer.findViewById(R.id.lock_screen_preview_container);
                LockScreenPreviewer lockScreenPreviewer = new LockScreenPreviewer(this.mLifecycleRegistry, getContext(), this.mLockPreviewContainer);
                this.mLockScreenPreviewer = lockScreenPreviewer;
                lockScreenPreviewer.setDateViewVisibility(!this.mFullScreenAnimation.mIsFullScreen);
                this.mFullScreenAnimation.mFullScreenStatusListener = new PreviewPager$$ExternalSyntheticLambda1(this);
                this.mWallpaperSurface = (SurfaceView) this.mHomePreviewCard.findViewById(R.id.wallpaper_surface);
                TouchForwardingLayout touchForwardingLayout = this.mTouchForwardingLayout;
                CardView cardView = this.mHomePreviewCard;
                touchForwardingLayout.mView = cardView;
                touchForwardingLayout.mForwardingEnabled = true;
                SurfaceView surfaceView = (SurfaceView) cardView.findViewById(R.id.workspace_surface);
                this.mWorkspaceSurface = surfaceView;
                this.mWorkspaceSurfaceCallback = createWorkspaceSurfaceCallback(surfaceView);
                this.mWallpaperSurfaceCallback = new WallpaperSurfaceCallback(getContext(), this.mHomePreviewCard, this.mWallpaperSurface, this.mPlaceholderColorFuture, null);
                setUpTabs((TabLayout) onCreateView.findViewById(R.id.separated_tabs));
                onCreateView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.android.wallpaper.picker.LivePreviewFragment.1
                    @Override // android.view.View.OnLayoutChangeListener
                    public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                        CardView cardView2 = LivePreviewFragment.this.mHomePreviewCard;
                        cardView2.setRadius(R$color.getPreviewCornerRadius(requireActivity, cardView2.getMeasuredWidth()));
                        onCreateView.removeOnLayoutChangeListener(this);
                    }
                });
                return onCreateView;
            }

            @Override // androidx.fragment.app.Fragment
            public void onDestroyView() {
                SliceView sliceView;
                boolean z = true;
                this.mCalled = true;
                LiveData<Slice> liveData = this.mSettingsLiveData;
                if (liveData != null) {
                    if (liveData.mObservers.mSize <= 0) {
                        z = false;
                    }
                    if (z && (sliceView = this.mSettingsSliceView) != null) {
                        liveData.removeObserver(sliceView);
                        this.mSettingsLiveData = null;
                    }
                }
                WallpaperConnection wallpaperConnection = this.mWallpaperConnection;
                if (wallpaperConnection != null) {
                    wallpaperConnection.disconnect();
                    this.mWallpaperConnection = null;
                }
                LockScreenPreviewer lockScreenPreviewer = this.mLockScreenPreviewer;
                if (lockScreenPreviewer != null) {
                    lockScreenPreviewer.release();
                }
                this.mWorkspaceSurfaceCallback.cleanUp();
                this.mWorkspaceSurface.getHolder().removeCallback(this.mWorkspaceSurfaceCallback);
                this.mWallpaperSurfaceCallback.cleanUp();
                this.mWallpaperSurface.getHolder().removeCallback(this.mWallpaperSurfaceCallback);
            }

            @Override // com.android.wallpaper.util.WallpaperConnection.WallpaperConnectionListener
            public void onEngineShown() {
                if (getActivity() != null) {
                    this.mWallpaperSurfaceCallback.mHomeImageWallpaper.animate().setStartDelay(250).setDuration(250).alpha(0.0f).setInterpolator(PreviewFragment.ALPHA_OUT).start();
                    BottomActionBar bottomActionBar = ((PreviewFragment) this).mBottomActionBar;
                    if (bottomActionBar != null) {
                        bottomActionBar.enableActions();
                    }
                }
            }

            @Override // androidx.fragment.app.Fragment
            public void onPause() {
                this.mCalled = true;
                WallpaperConnection wallpaperConnection = this.mWallpaperConnection;
                if (wallpaperConnection != null) {
                    wallpaperConnection.mIsVisible = false;
                    wallpaperConnection.setEngineVisibility(false);
                }
            }

            @Override // com.android.wallpaper.picker.PreviewFragment, androidx.fragment.app.Fragment
            public void onResume() {
                super.onResume();
                WallpaperConnection wallpaperConnection = this.mWallpaperConnection;
                if (wallpaperConnection != null) {
                    wallpaperConnection.mIsVisible = true;
                    wallpaperConnection.setEngineVisibility(true);
                }
            }

            @Override // androidx.fragment.app.Fragment
            public void onStart() {
                this.mCalled = true;
                previewLiveWallpaper(null);
            }

            @Override // androidx.fragment.app.Fragment
            public void onStop() {
                this.mCalled = true;
                WallpaperConnection wallpaperConnection = this.mWallpaperConnection;
                if (wallpaperConnection != null) {
                    wallpaperConnection.disconnect();
                    this.mWallpaperConnection = null;
                }
            }

            @Override // com.android.wallpaper.picker.BottomActionBarFragment, androidx.fragment.app.Fragment
            public void onViewCreated(View view, Bundle bundle) {
                super.onViewCreated(view, bundle);
                this.mWallpaperSurface.getHolder().addCallback(this.mWallpaperSurfaceCallback);
                this.mWallpaperSurface.setZOrderMediaOverlay(true);
                this.mHomePreviewCard.setOnTouchListener(new LivePreviewFragment$$ExternalSyntheticLambda2(this));
                this.mWorkspaceSurface.setZOrderMediaOverlay(true);
                this.mWorkspaceSurface.getHolder().addCallback(this.mWorkspaceSurfaceCallback);
            }

            @Override // com.android.wallpaper.util.WallpaperConnection.WallpaperConnectionListener
            public void onWallpaperColorsChanged(WallpaperColors wallpaperColors, int i) {
                this.mLockScreenPreviewer.setColor(wallpaperColors);
                FullScreenAnimation fullScreenAnimation = this.mFullScreenAnimation;
                fullScreenAnimation.mFullScreenTextColor = (wallpaperColors == null || (wallpaperColors.getColorHints() & 1) == 0) ? 3 : 2;
                fullScreenAnimation.animateColor(fullScreenAnimation.mIsFullScreen);
            }

            public void previewLiveWallpaper(ImageView imageView) {
                this.mWallpaperSurface.post(new DiskBasedLogger$$ExternalSyntheticLambda1(this));
            }

            @Override // com.android.wallpaper.picker.PreviewFragment
            public void setCurrentWallpaper(final int i) {
                this.mWallpaperSetter.setCurrentWallpaper(getActivity(), this.mWallpaper, null, i, 0.0f, null, new WallpaperPersister.SetWallpaperCallback() { // from class: com.android.wallpaper.picker.LivePreviewFragment.3
                    @Override // com.android.wallpaper.module.WallpaperPersister.SetWallpaperCallback
                    public void onError(Throwable th) {
                        LivePreviewFragment.this.showSetWallpaperErrorDialog(i);
                    }

                    @Override // com.android.wallpaper.module.WallpaperPersister.SetWallpaperCallback
                    public void onSuccess(com.android.wallpaper.model.WallpaperInfo wallpaperInfo) {
                        LivePreviewFragment.this.finishActivity(true);
                    }
                });
            }

            public void setUpLiveWallpaperPreview(com.android.wallpaper.model.WallpaperInfo wallpaperInfo) {
                FragmentActivity activity = getActivity();
                if (activity != null && !activity.isFinishing()) {
                    WallpaperConnection wallpaperConnection = this.mWallpaperConnection;
                    if (wallpaperConnection != null) {
                        wallpaperConnection.disconnect();
                    }
                    if (WallpaperConnection.isPreviewAvailable()) {
                        WallpaperConnection wallpaperConnection2 = new WallpaperConnection(getWallpaperIntent(wallpaperInfo.getWallpaperComponent()), activity, this, this.mWallpaperSurface);
                        this.mWallpaperConnection = wallpaperConnection2;
                        wallpaperConnection2.mIsVisible = true;
                        wallpaperConnection2.setEngineVisibility(true);
                    } else {
                        Asset thumbAsset = wallpaperInfo.getThumbAsset(activity);
                        LockScreenPreviewer lockScreenPreviewer = this.mLockScreenPreviewer;
                        Objects.requireNonNull(lockScreenPreviewer);
                        WallpaperColorsLoader.getWallpaperColors(activity, thumbAsset, new CategoryFragment$$ExternalSyntheticLambda4(lockScreenPreviewer, 1));
                    }
                    WallpaperConnection wallpaperConnection3 = this.mWallpaperConnection;
                    if (wallpaperConnection3 != null && !wallpaperConnection3.connect()) {
                        this.mWallpaperConnection = null;
                    }
                }
            }

            @Override // com.android.wallpaper.picker.PreviewFragment
            public void updateScreenPreview(boolean z) {
                int i = 0;
                this.mWorkspaceSurface.setVisibility(z ? 0 : 4);
                ViewGroup viewGroup = this.mLockPreviewContainer;
                if (z) {
                    i = 4;
                }
                viewGroup.setVisibility(i);
                this.mFullScreenAnimation.mIsHomeSelected = z;
            }
        }
