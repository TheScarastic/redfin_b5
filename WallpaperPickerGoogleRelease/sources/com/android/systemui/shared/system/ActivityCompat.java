package com.android.systemui.shared.system;

import android.app.Activity;
import android.view.View;
import android.view.ViewHierarchyEncoder;
import java.io.ByteArrayOutputStream;
/* loaded from: classes.dex */
public class ActivityCompat {
    private final Activity mWrapped;

    public ActivityCompat(Activity activity) {
        this.mWrapped = activity;
    }

    public boolean encodeViewHierarchy(ByteArrayOutputStream byteArrayOutputStream) {
        View view = (this.mWrapped.getWindow() == null || this.mWrapped.getWindow().peekDecorView() == null || this.mWrapped.getWindow().peekDecorView().getViewRootImpl() == null) ? null : this.mWrapped.getWindow().peekDecorView().getViewRootImpl().getView();
        if (view == null) {
            return false;
        }
        ViewHierarchyEncoder viewHierarchyEncoder = new ViewHierarchyEncoder(byteArrayOutputStream);
        int[] locationOnScreen = view.getLocationOnScreen();
        viewHierarchyEncoder.addProperty("window:left", locationOnScreen[0]);
        viewHierarchyEncoder.addProperty("window:top", locationOnScreen[1]);
        view.encode(viewHierarchyEncoder);
        viewHierarchyEncoder.endStream();
        return true;
    }

    public int getDisplayId() {
        return this.mWrapped.getDisplayId();
    }

    public void registerRemoteAnimations(RemoteAnimationDefinitionCompat remoteAnimationDefinitionCompat) {
        this.mWrapped.registerRemoteAnimations(remoteAnimationDefinitionCompat.getWrapped());
    }

    public void unregisterRemoteAnimations() {
        this.mWrapped.unregisterRemoteAnimations();
    }
}
