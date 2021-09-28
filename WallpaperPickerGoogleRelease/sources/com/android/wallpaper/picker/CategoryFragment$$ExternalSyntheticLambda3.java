package com.android.wallpaper.picker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.CheckBox;
import com.android.systemui.shared.R;
import com.android.wallpaper.model.WallpaperSectionController;
import com.android.wallpaper.picker.MyPhotosStarter;
/* loaded from: classes.dex */
public final /* synthetic */ class CategoryFragment$$ExternalSyntheticLambda3 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId = 1;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ CategoryFragment$$ExternalSyntheticLambda3(WallpaperSectionController wallpaperSectionController, View view) {
        this.f$0 = wallpaperSectionController;
        this.f$1 = view;
    }

    public /* synthetic */ CategoryFragment$$ExternalSyntheticLambda3(CategoryFragment categoryFragment, View view) {
        this.f$0 = categoryFragment;
        this.f$1 = view;
    }

    public /* synthetic */ CategoryFragment$$ExternalSyntheticLambda3(StartRotationDialogFragment startRotationDialogFragment, CheckBox checkBox) {
        this.f$0 = startRotationDialogFragment;
        this.f$1 = checkBox;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                CategoryFragment categoryFragment = (CategoryFragment) this.f$0;
                int i = CategoryFragment.$r8$clinit;
                categoryFragment.getFragmentHost().requestExternalStoragePermission(new MyPhotosStarter.PermissionChangedListener((View) this.f$1) { // from class: com.android.wallpaper.picker.CategoryFragment.4
                    public final /* synthetic */ View val$rootView;

                    {
                        this.val$rootView = r2;
                    }

                    @Override // com.android.wallpaper.picker.MyPhotosStarter.PermissionChangedListener
                    public void onPermissionsDenied(boolean z) {
                        if (z) {
                            CategoryFragment categoryFragment2 = CategoryFragment.this;
                            int i2 = CategoryFragment.$r8$clinit;
                            new AlertDialog.Builder(categoryFragment2.getActivity(), R.style.LightDialogTheme).setMessage(categoryFragment2.getString(R.string.permission_needed_explanation_go_to_settings)).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).setNegativeButton(R.string.settings_button_label, new CategoryFragment$$ExternalSyntheticLambda0(categoryFragment2)).create().show();
                        }
                    }

                    @Override // com.android.wallpaper.picker.MyPhotosStarter.PermissionChangedListener
                    public void onPermissionsGranted() {
                        CategoryFragment categoryFragment2 = CategoryFragment.this;
                        View view2 = this.val$rootView;
                        int i2 = CategoryFragment.$r8$clinit;
                        categoryFragment2.showCurrentWallpaper(view2, true);
                        CategoryFragment.this.mCategorySelectorFragment.mAdapter.mObservable.notifyChanged();
                    }
                });
                return;
            case 1:
                WallpaperSectionController wallpaperSectionController = (WallpaperSectionController) this.f$0;
                wallpaperSectionController.mPermissionRequester.requestExternalStoragePermission(
                /*  JADX ERROR: Method code generation error
                    jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0015: INVOKE  
                      (wrap: com.android.wallpaper.model.PermissionRequester : 0x000e: IGET  (r0v3 com.android.wallpaper.model.PermissionRequester A[REMOVE]) = (r3v7 'wallpaperSectionController' com.android.wallpaper.model.WallpaperSectionController) com.android.wallpaper.model.WallpaperSectionController.mPermissionRequester com.android.wallpaper.model.PermissionRequester)
                      (wrap: com.android.wallpaper.model.WallpaperSectionController$1 : 0x0012: CONSTRUCTOR  (r1v1 com.android.wallpaper.model.WallpaperSectionController$1 A[REMOVE]) = 
                      (r3v7 'wallpaperSectionController' com.android.wallpaper.model.WallpaperSectionController)
                      (wrap: android.view.View : 0x000c: CHECK_CAST (r2v7 android.view.View A[REMOVE]) = (android.view.View) (wrap: java.lang.Object : 0x000a: IGET  (r2v6 java.lang.Object A[REMOVE]) = (r2v0 'this' com.android.wallpaper.picker.CategoryFragment$$ExternalSyntheticLambda3 A[IMMUTABLE_TYPE, THIS]) com.android.wallpaper.picker.CategoryFragment$$ExternalSyntheticLambda3.f$1 java.lang.Object))
                     call: com.android.wallpaper.model.WallpaperSectionController.1.<init>(com.android.wallpaper.model.WallpaperSectionController, android.view.View):void type: CONSTRUCTOR)
                     type: INTERFACE call: com.android.wallpaper.model.PermissionRequester.requestExternalStoragePermission(com.android.wallpaper.picker.MyPhotosStarter$PermissionChangedListener):void in method: com.android.wallpaper.picker.CategoryFragment$$ExternalSyntheticLambda3.onClick(android.view.View):void, file: classes.dex
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
                    Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.android.wallpaper.model.WallpaperSectionController, state: GENERATED_AND_UNLOADED
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
                    int r3 = r2.$r8$classId
                    switch(r3) {
                        case 0: goto L_0x0019;
                        case 1: goto L_0x0006;
                        default: goto L_0x0005;
                    }
                L_0x0005:
                    goto L_0x0030
                L_0x0006:
                    java.lang.Object r3 = r2.f$0
                    com.android.wallpaper.model.WallpaperSectionController r3 = (com.android.wallpaper.model.WallpaperSectionController) r3
                    java.lang.Object r2 = r2.f$1
                    android.view.View r2 = (android.view.View) r2
                    com.android.wallpaper.model.PermissionRequester r0 = r3.mPermissionRequester
                    com.android.wallpaper.model.WallpaperSectionController$1 r1 = new com.android.wallpaper.model.WallpaperSectionController$1
                    r1.<init>(r2)
                    r0.requestExternalStoragePermission(r1)
                    return
                L_0x0019:
                    java.lang.Object r3 = r2.f$0
                    com.android.wallpaper.picker.CategoryFragment r3 = (com.android.wallpaper.picker.CategoryFragment) r3
                    java.lang.Object r2 = r2.f$1
                    android.view.View r2 = (android.view.View) r2
                    int r0 = com.android.wallpaper.picker.CategoryFragment.$r8$clinit
                    com.android.wallpaper.picker.CategoryFragment$CategoryFragmentHost r0 = r3.getFragmentHost()
                    com.android.wallpaper.picker.CategoryFragment$4 r1 = new com.android.wallpaper.picker.CategoryFragment$4
                    r1.<init>(r2)
                    r0.requestExternalStoragePermission(r1)
                    return
                L_0x0030:
                    java.lang.Object r3 = r2.f$0
                    com.android.wallpaper.picker.StartRotationDialogFragment r3 = (com.android.wallpaper.picker.StartRotationDialogFragment) r3
                    java.lang.Object r2 = r2.f$1
                    android.widget.CheckBox r2 = (android.widget.CheckBox) r2
                    int r0 = com.android.wallpaper.picker.StartRotationDialogFragment.$r8$clinit
                    java.util.Objects.requireNonNull(r3)
                    boolean r2 = r2.isChecked()
                    r3.mIsWifiOnlyChecked = r2
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.wallpaper.picker.CategoryFragment$$ExternalSyntheticLambda3.onClick(android.view.View):void");
            }
        }
