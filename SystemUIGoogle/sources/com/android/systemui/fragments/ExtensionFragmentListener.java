package com.android.systemui.fragments;

import android.app.Fragment;
import android.util.Log;
import android.view.View;
import com.android.systemui.plugins.FragmentBase;
import com.android.systemui.statusbar.policy.ExtensionController;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class ExtensionFragmentListener<T extends FragmentBase> implements Consumer<T> {
    private final ExtensionController.Extension<T> mExtension;
    private final FragmentHostManager mFragmentHostManager;
    private final int mId;
    private String mOldClass;
    private final String mTag;

    /* JADX DEBUG: Multi-variable search result rejected for r0v0, resolved type: com.android.systemui.fragments.ExtensionFragmentListener<T extends com.android.systemui.plugins.FragmentBase> */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.function.Consumer
    public /* bridge */ /* synthetic */ void accept(Object obj) {
        accept((ExtensionFragmentListener<T>) ((FragmentBase) obj));
    }

    private ExtensionFragmentListener(View view, String str, int i, ExtensionController.Extension<T> extension) {
        this.mTag = str;
        FragmentHostManager fragmentHostManager = FragmentHostManager.get(view);
        this.mFragmentHostManager = fragmentHostManager;
        this.mExtension = extension;
        this.mId = i;
        fragmentHostManager.getFragmentManager().beginTransaction().replace(i, (Fragment) extension.get(), str).commit();
        extension.clearItem(false);
    }

    public void accept(T t) {
        try {
            Fragment.class.cast(t);
            this.mFragmentHostManager.getExtensionManager().setCurrentExtension(this.mId, this.mTag, this.mOldClass, t.getClass().getName(), this.mExtension.getContext());
            this.mOldClass = t.getClass().getName();
        } catch (ClassCastException e) {
            Log.e("ExtensionFragmentListener", t.getClass().getName() + " must be a Fragment", e);
        }
        this.mExtension.clearItem(true);
    }

    public static <T> void attachExtensonToFragment(View view, String str, int i, ExtensionController.Extension<T> extension) {
        extension.addCallback(new ExtensionFragmentListener(view, str, i, extension));
    }
}
