package com.android.systemui.dump;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public class SystemUIAuxiliaryDumpService extends Service {
    private final DumpHandler mDumpHandler;

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    public SystemUIAuxiliaryDumpService(DumpHandler dumpHandler) {
        this.mDumpHandler = dumpHandler;
    }

    @Override // android.app.Service
    protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        this.mDumpHandler.dump(fileDescriptor, printWriter, new String[]{"--dump-priority", "NORMAL"});
    }
}
