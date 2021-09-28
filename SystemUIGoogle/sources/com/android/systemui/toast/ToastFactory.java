package com.android.systemui.toast;

import android.content.Context;
import android.view.LayoutInflater;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.plugins.ToastPlugin;
import com.android.systemui.shared.plugins.PluginManager;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes2.dex */
public class ToastFactory implements Dumpable {
    private final LayoutInflater mLayoutInflater;
    private ToastPlugin mPlugin;

    public ToastFactory(LayoutInflater layoutInflater, PluginManager pluginManager, DumpManager dumpManager) {
        this.mLayoutInflater = layoutInflater;
        dumpManager.registerDumpable("ToastFactory", this);
        pluginManager.addPluginListener((PluginListener) new PluginListener<ToastPlugin>() { // from class: com.android.systemui.toast.ToastFactory.1
            public void onPluginConnected(ToastPlugin toastPlugin, Context context) {
                ToastFactory.this.mPlugin = toastPlugin;
            }

            public void onPluginDisconnected(ToastPlugin toastPlugin) {
                if (toastPlugin.equals(ToastFactory.this.mPlugin)) {
                    ToastFactory.this.mPlugin = null;
                }
            }
        }, ToastPlugin.class, false);
    }

    public SystemUIToast createToast(Context context, CharSequence charSequence, String str, int i, int i2) {
        if (isPluginAvailable()) {
            return new SystemUIToast(this.mLayoutInflater, context, charSequence, this.mPlugin.createToast(charSequence, str, i), str, i, i2);
        }
        return new SystemUIToast(this.mLayoutInflater, context, charSequence, str, i, i2);
    }

    private boolean isPluginAvailable() {
        return this.mPlugin != null;
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("ToastFactory:");
        printWriter.println("    mAttachedPlugin=" + this.mPlugin);
    }
}
