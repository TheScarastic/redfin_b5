package com.android.systemui.statusbar.notification.row;

import com.android.systemui.statusbar.notification.row.ChannelEditorDialog;
import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class ChannelEditorDialog_Builder_Factory implements Factory<ChannelEditorDialog.Builder> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final ChannelEditorDialog_Builder_Factory INSTANCE = new ChannelEditorDialog_Builder_Factory();
    }

    @Override // javax.inject.Provider
    public ChannelEditorDialog.Builder get() {
        return newInstance();
    }

    public static ChannelEditorDialog_Builder_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ChannelEditorDialog.Builder newInstance() {
        return new ChannelEditorDialog.Builder();
    }
}
