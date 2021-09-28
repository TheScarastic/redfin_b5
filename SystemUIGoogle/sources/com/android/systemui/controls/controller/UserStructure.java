package com.android.systemui.controls.controller;

import android.content.Context;
import android.os.Environment;
import android.os.UserHandle;
import java.io.File;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsControllerImpl.kt */
/* loaded from: classes.dex */
public final class UserStructure {
    private final File auxiliaryFile;
    private final File file;
    private final Context userContext;

    public UserStructure(Context context, UserHandle userHandle) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(userHandle, "user");
        Context createContextAsUser = context.createContextAsUser(userHandle, 0);
        this.userContext = createContextAsUser;
        this.file = Environment.buildPath(createContextAsUser.getFilesDir(), new String[]{"controls_favorites.xml"});
        this.auxiliaryFile = Environment.buildPath(createContextAsUser.getFilesDir(), new String[]{"aux_controls_favorites.xml"});
    }

    public final Context getUserContext() {
        return this.userContext;
    }

    public final File getFile() {
        return this.file;
    }

    public final File getAuxiliaryFile() {
        return this.auxiliaryFile;
    }
}
